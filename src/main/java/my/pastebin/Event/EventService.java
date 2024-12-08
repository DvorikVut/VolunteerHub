package my.pastebin.Event;

import my.pastebin.Event.dto.EventInfoDTO;
import my.pastebin.Event.dto.EventInfoDTOMapper;
import my.pastebin.Event.dto.NewEventDTO;
import my.pastebin.EventUserStatus.EventUserStatus;
import my.pastebin.EventUserStatus.EventUserStatusService;
import my.pastebin.Exceptions.NotAuthorizedException;
import my.pastebin.Logger.MyLogger;
import my.pastebin.S3.S3Service;
import my.pastebin.User.Role;
import my.pastebin.User.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import my.pastebin.User.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepo eventRepo;
    private final UserService userService;
    private final EventUserStatusService eventUserStatusService;
    private final EventInfoDTOMapper eventInfoDTOMapper;
    private final S3Service s3Service;

    public EventService(EventRepo eventRepo, @Lazy UserService userService, @Lazy EventUserStatusService eventUserStatusService, EventInfoDTOMapper eventInfoDTOMapper, S3Service s3Service) {
        this.eventRepo = eventRepo;
        this.userService = userService;
        this.eventUserStatusService = eventUserStatusService;
        this.eventInfoDTOMapper = eventInfoDTOMapper;
        this.s3Service = s3Service;
    }

    /**
     * Creates a new event based on the provided DTO.
     *
     * @param newEventDTO the DTO containing event details
     * @return the created event
     */
    public Event create(NewEventDTO newEventDTO) {
        var event = Event.builder()
                .name(newEventDTO.name())
                .description(newEventDTO.description())
                .capacity(newEventDTO.capacity())
                .startDateTime(newEventDTO.startDateTime())
                .endDateTime(newEventDTO.endDateTime())
                .city(newEventDTO.city())
                .address(newEventDTO.address())
                .creator(userService.getCurrentUser())
                .price(generateEventPrice(newEventDTO.startDateTime(), newEventDTO.endDateTime()))
                .coordinates(newEventDTO.coordinates())
                .build();
        MyLogger.logInfo("Event with name " + event.getName() + " created");
        return eventRepo.save(event);
    }


    public void addImage(Long id, MultipartFile image) {
        Event event = eventRepo.findById(id).orElseThrow();
        if (!event.getCreator().getId().equals(userService.getCurrentUser().getId()) && !userService.getCurrentUser().getRole().equals(Role.ADMIN))
            throw new NotAuthorizedException("You are not allowed to add image to this event");

        String key = s3Service.uploadImage(image);
        event.setS3ImageKey(key);
        event.setImageURL(s3Service.getPublicUrl(key));
        eventRepo.save(event);
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param id the ID of the event
     * @return the event if found, otherwise null
     */
    public Event getById(Long id) {
        return eventRepo.findById(id).orElse(null);
    }

    /**
     * Retrieves detailed information about an event.
     *
     * @param id the ID of the event
     * @return a DTO containing the event details
     */
    public EventInfoDTO getEventInfo(Long id) {
        return eventInfoDTOMapper.apply(Objects.requireNonNull(eventRepo.findById(id).orElse(null)));
    }

    /**
     * Deletes an event if the current user is authorized.
     *
     * @param id the ID of the event to delete
     * @throws NotAuthorizedException if the user is not authorized to delete the event
     */
    public void delete(Long id) {
        var event = eventRepo.findById(id).orElseThrow();

        if (!event.getCreator().getId().equals(userService.getCurrentUser().getId()) && !userService.getCurrentUser().getRole().equals(Role.ADMIN))
            throw new NotAuthorizedException("You are not allowed to delete this event");

        if(event.getS3ImageKey() != null)
            s3Service.delete(event.getS3ImageKey());

        eventRepo.delete(event);
    }

    /**
     * Updates an existing event with new details.
     *
     * @param id the ID of the event to update
     * @param newEventDTO the new details for the event
     * @throws NotAuthorizedException if the user is not authorized to modify the event
     */
    public void change(Long id, NewEventDTO newEventDTO) {
        Event event = eventRepo.findById(id).orElseThrow();
        if (!(event.getCreator().getId().equals(userService.getCurrentUser().getId()) ||
                userService.getCurrentUser().getRole().equals(Role.ADMIN))){

            MyLogger.logInfo("User with ID " + userService.getCurrentUser().getId() + " is not allowed to change event with ID " + id);
            throw new NotAuthorizedException("You are not allowed to change this event");
        }

        event.setName(newEventDTO.name());
        event.setDescription(newEventDTO.description());
        event.setCapacity(newEventDTO.capacity());
        event.setStartDateTime(newEventDTO.startDateTime());
        event.setEndDateTime(newEventDTO.endDateTime());
        event.setCity(newEventDTO.city());
        event.setPrice(generateEventPrice(newEventDTO.startDateTime(), newEventDTO.endDateTime()));
        eventRepo.save(event);

    }

    /**
     * Retrieves all events.
     *
     * @return a list of all events as DTOs
     */
    public List<EventInfoDTO> getAll() {
        return eventRepo.findAll().stream().map(eventInfoDTOMapper).toList();
    }

    /**
     * Retrieves all events created by the current user.
     *
     * @return a list of events created by the user
     */
    public List<EventInfoDTO> getAllAsCreatorEvents() {
        return eventRepo.findAllByCreatorId(userService.getCurrentUser().getId())
                .stream()
                .map(eventInfoDTOMapper)
                .collect(Collectors.toList());
    }

    /**
     * Generates the price for an event based on its timing.
     *
     * @param startDateTime the start time of the event
     * @param endDateTime the end time of the event
     * @return the calculated price
     */
    public Integer generateEventPrice(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        int price = 50;

        if (startDateTime.getDayOfWeek().getValue() > 5) {
            price += 10;
        }

        if (endDateTime.getHour() > 18) {
            price += 10;
        }

        if (startDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            price += 10;
        }

        return price;
    }
    /**
     * Retrieves all future events.
     *
     * @return a list of future events as DTOs
     */
    public List<EventInfoDTO> getFutureEvent() {
        List<Event> events = eventRepo.findAllByStartDateTimeAfter(LocalDateTime.now());
        return events.stream().map(eventInfoDTOMapper).toList();
    }

    /**
     * Retrieves all events where the current user is a participant.
     *
     * @return a list of events as DTOs
     */
    public List<EventInfoDTO> getAllAsParticipant() {
        User user = userService.getCurrentUser();
        List<EventUserStatus> eventUserStatuses = eventUserStatusService.getAllByUserId(user.getId());
        return eventUserStatuses.stream().map(eventUserStatus -> getEventInfo(eventUserStatus.getEventId())).toList();
    }

    public List<EventInfoDTO> getCreatedEventsByUserId(Long userId) {
        return eventRepo.findAllByCreatorId(userId)
                .stream()
                .map(eventInfoDTOMapper)
                .collect(Collectors.toList());
    }

    public List<EventInfoDTO> getParticipatedEventsByUserId(Long userId) {
        List<EventUserStatus> eventUserStatuses = eventUserStatusService.getAllByUserId(userId);
        return eventUserStatuses.stream().map(eventUserStatus -> getEventInfo(eventUserStatus.getEventId())).toList();
    }

    public List<Event> getAllByCreatorId(Long id) {
        return eventRepo.findAllByCreatorId(id);
    }
}
