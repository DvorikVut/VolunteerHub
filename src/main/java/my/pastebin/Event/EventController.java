package my.pastebin.Event;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;
import my.pastebin.Event.dto.NewEventDTO;
import my.pastebin.Event.dto.UserEventDTO;
import my.pastebin.EventUserStatus.EventUserStatusService;
import my.pastebin.EventUserStatus.Status;
import my.pastebin.Logger.MyLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import my.pastebin.User.User;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;
    private final EventUserStatusService eventUserStatusService;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody NewEventDTO newEventDTO) {
        return ResponseEntity.ok(eventService.create(newEventDTO));
    }

    @PostMapping("/register/{eventId}")
    public ResponseEntity<?> registerUserForEvent(@PathVariable Long eventId) {
        eventUserStatusService.registerUserForEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/status-confirm/{eventId}/{userId}")
    public ResponseEntity<?> changeStatusConfirm(@PathVariable Long eventId, @PathVariable Long userId) {
        eventUserStatusService.changeStatus(eventId, userId, Status.CONFIRMED);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/status-attended/{eventId}/{userId}")
    public ResponseEntity<?> changeStatusAttended(@PathVariable Long eventId, @PathVariable Long userId) {
        eventUserStatusService.changeStatus(eventId, userId, Status.ATTENDED);
        return ResponseEntity.ok("Status was changed successfully");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> changeEvent(@PathVariable Long id, @RequestBody NewEventDTO newEventDTO) {
        try {
            eventService.change(id, newEventDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }


    @DeleteMapping
    public ResponseEntity<?> deleteEvent(Long id) {
        try {
            eventService.delete(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUserFromEvent(@RequestBody UserEventDTO userEventDTO) {
        eventUserStatusService.deleteUserFromEvent(userEventDTO.eventId(), userEventDTO.userId());
        return ResponseEntity.ok("User was deleted successfully from event");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventInfo(id));
    }

    @GetMapping("/my-as-creator")
    public ResponseEntity<?> getAllAsCreatorEvents() {
        return ResponseEntity.ok(eventService.getAllAsCreatorEvents());
    }

    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @GetMapping("/users-registered/{eventId}")
    public ResponseEntity<?> getNumberOfUsers(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventUserStatusService.getRegisteredUsers(eventId));
    }
    @GetMapping("/future")
    public ResponseEntity<?> getFutureEvents() {
        return ResponseEntity.ok(eventService.getFutureEvent());
    }

    @GetMapping("/my-as-participant")
    public ResponseEntity<?> getAllAsParticipant(){
        return ResponseEntity.ok(eventService.getAllAsParticipant());
    }
}
