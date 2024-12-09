package my.pastebin.EventUserStatus;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import my.pastebin.Event.Event;
import my.pastebin.Event.EventService;
import my.pastebin.Exceptions.BadRequestException;
import my.pastebin.Exceptions.NotAuthorizedException;
import my.pastebin.Exceptions.ResourceNotFoundException;
import my.pastebin.Logger.MyLogger;
import my.pastebin.User.Role;
import my.pastebin.User.User;
import my.pastebin.User.UserService;
import my.pastebin.User.dto.UserInfo;
import my.pastebin.User.dto.UserInfoDTOMapper;
import my.pastebin.User.dto.UserOnEvent;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventUserStatusService {

    private final EventUserStatusRepo eventUserStatusRepo;
    private final EventService eventService;
    private final UserService userService;
    private final UserInfoDTOMapper userInfoDTOMapper;

    /**
     * Changes the status of a user for a specific event.
     *
     * @param eventId the ID of the event
     * @param userToChangeId the ID of the user
     * @param status the new status to set
     * @throws EntityNotFoundException if the user is not registered for the event
     * @throws AccessDeniedException if the current user is not authorized to change the status
     */
    public void changeStatus(Long eventId, Long userToChangeId, Status status) throws EntityNotFoundException {
        User user = userService.getCurrentUser();
        if (!(checkIfUserRoleIs(user,Role.ADMIN) || isUserIsCreatorOfEvent(eventId, user))) {
            MyLogger.logInfo("EXCEPTION: User " + user.getUsername() + " is not authorized to change status of user " +
                    userService.getUserById(userToChangeId).getUsername() + " in event " + eventService.getById(eventId).getName());
            throw new AccessDeniedException("Only creator can change status");
        }

        EventUserStatus eventUserStatus = eventUserStatusRepo.findByEventIdAndUserId(eventId, userToChangeId);
        if (eventUserStatus == null) {
            MyLogger.logInfo("EXCEPTION: User " + userService.getUserById(userToChangeId).getUsername() +
                    " is not registered for event " + eventService.getById(eventId).getName());
            throw new EntityNotFoundException("User is not registered for this event");
        }

        eventUserStatus.setStatus(status);
        eventUserStatusRepo.save(eventUserStatus);
    }

    /**
     * Registers the current user for a specific event.
     *
     * @param eventId the ID of the event
     * @throws ResourceNotFoundException if the event does not exist
     * @throws BadRequestException if the event is already full
     */
    public void registerUserForEvent(Long eventId) {
        Event event = eventService.getById(eventId);

        if (event == null) {
            MyLogger.logInfo("EXCEPTION: Event with ID " + eventId + " not found");
            throw new ResourceNotFoundException("Event not found");
        }

        if (event.getCapacity() <= getNumberOfConfirmedUsers(eventId)) {
            MyLogger.logInfo("EXCEPTION: Event " + event.getName() + " is full");
            throw new BadRequestException("Event is full");
        }

        EventUserStatus eventUserStatus = new EventUserStatus();
        eventUserStatus.setEventId(eventId);
        eventUserStatus.setUserId(userService.getCurrentUser().getId());
        eventUserStatus.setStatus(Status.REGISTERED);
        eventUserStatusRepo.save(eventUserStatus);
    }

    public void registerUserForEventByUserId(Long eventId, Long userId) {
        Event event = eventService.getById(eventId);

        if (event == null) {
            MyLogger.logInfo("EXCEPTION: Event with ID " + eventId + " not found");
            throw new ResourceNotFoundException("Event not found");
        }

        if (event.getCapacity() <= getNumberOfConfirmedUsers(eventId)) {
            MyLogger.logInfo("EXCEPTION: Event " + event.getName() + " is full");
            throw new BadRequestException("Event is full");
        }

        EventUserStatus eventUserStatus = new EventUserStatus();
        eventUserStatus.setEventId(eventId);
        eventUserStatus.setUserId(userId);
        eventUserStatus.setStatus(Status.REGISTERED);
        eventUserStatusRepo.save(eventUserStatus);
    }

    /**
     * Deletes a user from a specific event.
     *
     * @param eventId the ID of the event
     * @param userId the ID of the user
     * @throws NotAuthorizedException if the current user is not authorized to delete the user
     * @throws ResourceNotFoundException if the user is not registered for the event
     */
    public void deleteUserFromEvent(Long eventId, Long userId) {
        Event event = eventService.getById(eventId);
        User currentUser = userService.getCurrentUser();

        if (!(isUserIsCreatorOfEvent(eventId,currentUser) || checkIfUserRoleIs(currentUser,Role.ADMIN) || userService.getUserById(userId).equals(currentUser))) {
            MyLogger.logInfo("EXCEPTION: User " + userService.getCurrentUser().getUsername() + " is not authorized to delete user " + userService.getUserById(userId).getUsername() +  " from event " + event.getName());
            throw new NotAuthorizedException("Only creator, admin or user itself can delete users from event");
        }

        EventUserStatus eventUserStatus = eventUserStatusRepo.findByEventIdAndUserId(eventId, userId);

        if (eventUserStatus == null) {
            MyLogger.logInfo("EXCEPTION: User " + userService.getUserById(userId).getUsername() +
                    " is not registered for event " + event.getName());
            throw new ResourceNotFoundException("User is not registered for this event");
        }
        eventUserStatusRepo.delete(eventUserStatus);
    }

    /**
     * Retrieves the number of users confirmed for a specific event.
     *
     * @param eventId the ID of the event
     * @return the number of confirmed users
     */
    public Integer getNumberOfConfirmedUsers(Long eventId) {
        return eventUserStatusRepo.findAllByEventIdAndStatus(eventId, Status.CONFIRMED).size();
    }

    /**
     * Retrieves the status of a specific user in an event.
     *
     * @param eventId the ID of the event
     * @param userId the ID of the user
     * @return the status of the user
     */
    public Status getEventUserStatus(Long eventId, Long userId) {
        EventUserStatus eventUserStatus = eventUserStatusRepo.findByEventIdAndUserId(eventId, userId);
        return eventUserStatus.getStatus();
    }

    /**
     * Retrieves all users registered for a specific event as DTOs.
     *
     * @param eventId the ID of the event
     * @return users registered for the event
     */
    public List<UserOnEvent> getRegisteredUsers(Long eventId) {
        return eventUserStatusRepo.findAllByEventId(eventId).stream()
                .map(eventUserStatus -> UserOnEvent.builder()
                        .id(eventUserStatus.getUserId())
                        .name(userService.getUserById(eventUserStatus.getUserId()).getName())
                        .email(userService.getUserById(eventUserStatus.getUserId()).getEmail())
                        .surname(userService.getUserById(eventUserStatus.getUserId()).getSurname())
                        .pointsAsCreator(userService.getUserById(eventUserStatus.getUserId()).getPointsAsCreator())
                        .points(userService.getUserById(eventUserStatus.getUserId()).getPoints())
                        .imageURL(userService.getUserById(eventUserStatus.getUserId()).getImageURL())
                        .status(eventUserStatus.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all event statuses for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of event statuses
     */
    public List<EventUserStatus> getAllByUserId(Long userId) {
        return eventUserStatusRepo.findAllByUserId(userId);
    }

    public boolean isUserIsCreatorOfEvent(Long eventId, User user) {
        return eventService.getById(eventId).getCreator().equals(user);
    }

    public boolean checkIfUserRoleIs(User user, Role role){
        return user.getRole().equals(role);
    }
}
