package my.pastebin.Event;

import my.pastebin.Event.dto.EventInfoDTO;
import my.pastebin.Event.dto.NewEventDTO;
import my.pastebin.EventUserStatus.EventUserStatus;
import my.pastebin.EventUserStatus.EventUserStatusService;
import my.pastebin.Logger.MyLogger;
import my.pastebin.User.Role;
import my.pastebin.User.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import my.pastebin.User.User;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service

public class EventService {
    private final EventRepo eventRepo;
    private final UserService userService;
    private final EventUserStatusService eventUserStatusService;
    private final MyLogger logger;

    public EventService(EventRepo eventRepo, @Lazy UserService userService, @Lazy EventUserStatusService eventUserStatusService, MyLogger logger) {
        this.eventRepo = eventRepo;
        this.userService = userService;
        this.eventUserStatusService = eventUserStatusService;
        this.logger = logger;
    }


    public Event createEvent(NewEventDTO newEventDTO) {

        System.out.println(userService.getCurrentUser());

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
                .build();
        return eventRepo.save(event);
    }

    public Event getEvent(Long id) {
        return eventRepo.findById(id).orElse(null);
    }

    public EventInfoDTO getEventInfo(Long id) {
        return EventToEventInfoDTO(Objects.requireNonNull(eventRepo.findById(id).orElse(null)));
    }

    public void deleteEvent(Long id) throws AccessDeniedException {
        var event = eventRepo.findById(id).orElseThrow();
        if(event.getCreator().getId().equals(userService.getCurrentUser().getId()) || userService.getCurrentUser().getRole().equals(Role.ADMIN)) {
            eventRepo.delete(event);
        } else {
            throw new AccessDeniedException("You are not allowed to delete this event");
        }
    }

    public void changeEvent(Long id, NewEventDTO newEventDTO) throws AccessDeniedException {
        Event event = eventRepo.findById(id).orElseThrow();
        if(!(event.getCreator().getId().equals(userService.getCurrentUser().getId()) || userService.getCurrentUser().getRole().equals(Role.ADMIN))) {
            throw new AccessDeniedException("You are not allowed to change this event");
        } else {
            event.setName(newEventDTO.name());
            event.setDescription(newEventDTO.description());
            event.setCapacity(newEventDTO.capacity());
            event.setStartDateTime(newEventDTO.startDateTime());
            event.setEndDateTime(newEventDTO.endDateTime());
            event.setCity(newEventDTO.city());
            event.setPrice(generateEventPrice(newEventDTO.startDateTime(), newEventDTO.endDateTime()));
            eventRepo.save(event);
        }
    }

    public List<EventInfoDTO> getAllEvents() {
        return eventRepo.findAll().stream().map(this::EventToEventInfoDTO).toList();
    }

    public List<Event> getAllAsCreatorEvents(){
        return eventRepo.findAllByCreatorId(userService.getCurrentUser().getId());
    }


    public Integer generateEventPrice(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        int price = 50;

        if(startDateTime.getDayOfWeek().getValue() > 5) {
            price += 10;
        }

        if(endDateTime.getHour() > 18) {
            price += 10;
        }

        if(startDateTime.isBefore(LocalDateTime.now().plusHours(2))){
            price += 10;
        }

        return price;
    }

    public EventInfoDTO EventToEventInfoDTO(Event event) {
        return EventInfoDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .city(event.getCity())
                .address(event.getAddress())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .capacity(event.getCapacity())
                .occupiedQuantity(eventUserStatusService.getNumberOfConfirmedUsers(event.getId()))
                .price(generateEventPrice(event.getStartDateTime(), event.getEndDateTime()))
                .creator(userService.UserToUserInfo(event.getCreator()))
                .build();
    }

    public List<EventInfoDTO> getFutureEvent() {
        List<Event> events = eventRepo.findAllByStartDateTimeAfter(LocalDateTime.now());
        List<EventInfoDTO> result = events.stream().map(this::EventToEventInfoDTO).toList();
        logger.logInfo(result.toString());
        return result;
    }

    public List<EventInfoDTO> getAllAsParticipant() {
        User user = userService.getCurrentUser();
        List<EventUserStatus> eventUserStatuses = eventUserStatusService.getAllByUserId(user.getId());
        return eventUserStatuses.stream().map(eventUserStatus -> getEventInfo(eventUserStatus.getEventId())).toList();
    }
}
