package my.pastebin.Event;

import lombok.RequiredArgsConstructor;
import my.pastebin.Event.dto.EventInfoDTO;
import my.pastebin.Event.dto.NewEventDTO;
import my.pastebin.EventUserStatus.EventUserStatusService;
import my.pastebin.User.Role;
import my.pastebin.User.UserService;
import org.springframework.context.annotation.Lazy;
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

    public EventService(EventRepo eventRepo, @Lazy UserService userService, @Lazy EventUserStatusService eventUserStatusService) {
        this.eventRepo = eventRepo;
        this.userService = userService;
        this.eventUserStatusService = eventUserStatusService;
    }

    public Event createEvent(NewEventDTO newEventDTO) {
        var event = Event.builder()
                .name(newEventDTO.name())
                .description(newEventDTO.description())
                .capacity(newEventDTO.capacity())
                .startDateTime(newEventDTO.startDateTime())
                .endDateTime(newEventDTO.endDateTime())
                .city(newEventDTO.city())
                .address(newEventDTO.address())
                .creator(userService.getCurrentUser())
                .price(generateEventPrice(newEventDTO))
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
            event.setPrice(generateEventPrice(newEventDTO));
            eventRepo.save(event);
        }
    }


    public List<EventInfoDTO> getAllEvents() {
        return eventRepo.findAll().stream().map(this::EventToEventInfoDTO).toList();
    }

    public List<Event> getAllUserAsCreatorEvents(User creator){
        return eventRepo.findAllByCreatorId(creator.getId());
    }


    public Integer generateEventPrice(NewEventDTO newEventDTO) {
        int price = 50;

        if(newEventDTO.startDateTime().getDayOfWeek().getValue() > 5) {
            price += 10;
        }

        if(newEventDTO.endDateTime().getHour() > 18) {
            price += 10;
        }

        if(newEventDTO.startDateTime().isBefore(LocalDateTime.now().plusHours(2))){
            price += 10;
        }

        return price;
    }

    public EventInfoDTO EventToEventInfoDTO(Event event) {
        return new EventInfoDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getCity(),
                event.getAddress(),
                event.getStartDateTime(),
                event.getEndDateTime(),
                event.getCapacity(),
                eventUserStatusService.getNumberOfUsers(event.getId()),
                event.getPrice(),
                event.getCreator()
        );
    }
}
