package my.pastebin.EventUserStatus;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import my.pastebin.Event.Event;
import my.pastebin.Event.EventService;
import my.pastebin.User.Role;
import my.pastebin.User.User;
import my.pastebin.User.UserService;
import my.pastebin.User.dto.UserInfo;
import my.pastebin.User.dto.UserOnEvent;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventUserStatusService {

    private final EventUserStatusRepo eventUserStatusRepo;
    private final EventService eventService;
    private final UserService userService;


    public void changeStatus(Long eventId, Long userId, Status status) throws EntityNotFoundException  {
            if(     !userService.getCurrentUser().getRole().equals(Role.ADMIN) ||
                    !userService.getCurrentUser().equals(eventService.getEvent(eventId).getCreator())) {
            throw new AccessDeniedException("Only admin or creator can change status");
        }

        EventUserStatus eventUserStatus = eventUserStatusRepo.findByEventIdAndUserId(eventId, userId);
        if(eventUserStatus == null) {
            throw new EntityNotFoundException("User is not registered for this event");
        }

        eventUserStatus.setStatus(status);
        eventUserStatusRepo.save(eventUserStatus);
    }

    public void registerUserForEvent(Long eventId) throws BadRequestException, EntityNotFoundException {

        Event event = eventService.getEvent(eventId);

        if(event == null) {
            throw new EntityNotFoundException("Event not found");
        }

        if(event.getCapacity() <= getNumberOfUsers(eventId)) {
            throw new BadRequestException("Event is full");
        }

        EventUserStatus eventUserStatus = new EventUserStatus();
        eventUserStatus.setEventId(eventId);
        eventUserStatus.setUserId(userService.getCurrentUser().getId());
        eventUserStatus.setStatus(Status.REGISTERED);
        eventUserStatusRepo.save(eventUserStatus);
    }

    public void deleteUserFromEvent(Long eventId, Long userId) throws EntityNotFoundException, ChangeSetPersister.NotFoundException {

        Event event = eventService.getEvent(eventId);
        User creator = event.getCreator();

        if(!(creator.equals(userService.getCurrentUser()) || userService.getCurrentUser().getRole().equals(Role.ADMIN) || userService.getUserById(userId).equals(userService.getCurrentUser()))) {
            throw new AccessDeniedException("Only creator, admin or user itself can delete users from event");
        }

        EventUserStatus eventUserStatus = eventUserStatusRepo.findByEventIdAndUserId(eventId, userId);


        if(eventUserStatus == null) {
            throw new EntityNotFoundException("User is not registered for this event");
        }

        eventUserStatusRepo.delete(eventUserStatus);
    }


    public Integer getNumberOfUsers(Long eventId) {
        return eventUserStatusRepo.findAllByEventId(eventId).size();
    }

    public Integer getNumberOfConfirmedUsers(Long eventId){
         return eventUserStatusRepo.findAllByEventIdAndStatus(eventId, Status.CONFIRMED).size();
    }

    public Status getEventUserStatus(Long eventId, Long userId) {
        EventUserStatus eventUserStatus = eventUserStatusRepo.findByEventIdAndUserId(eventId, userId);
        return eventUserStatus.getStatus();
    }

    public List<UserOnEvent> getRegisteredUsers(Long eventId) {
        return eventUserStatusRepo.findAllByEventId(eventId).stream().map(eventUserStatus -> {
            try {
                return userService.UserToUserOnEvent(userService.getUserById(eventUserStatus.getUserId()), eventId);
            } catch (ChangeSetPersister.NotFoundException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<EventUserStatus> getAllByUserId(Long userId){
        return eventUserStatusRepo.findAllByUserId(userId);
    }
}
