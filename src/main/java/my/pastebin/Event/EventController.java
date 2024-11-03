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
        return ResponseEntity.ok(eventService.createEvent(newEventDTO));
    }

    @PostMapping("/test")
    public String test(){
        return "hui";
    }

    @PostMapping("/register/{eventId}")
    public ResponseEntity<?> registerUserForEvent(@PathVariable Long eventId) {
        try {
            eventUserStatusService.registerUserForEvent(eventId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/status-confirm")
    public ResponseEntity<?> changeStatusConfirm(@RequestBody Long eventId, @RequestBody Long userId) {
        try {
            eventUserStatusService.changeStatus(eventId, userId, Status.CONFIRMED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/status-attended")
    public ResponseEntity<?> changeStatusAttended(@RequestBody Long eventId, @RequestBody Long userId) {
        try {
            eventUserStatusService.changeStatus(eventId, userId, Status.ATTENDED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> changeEvent(@PathVariable Long id, NewEventDTO newEventDTO) {
        try {
            eventService.changeEvent(id, newEventDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }


    @DeleteMapping
    public ResponseEntity<?> deleteEvent(Long id) {
        try {
            eventService.deleteEvent(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUserFromEvent(@RequestBody UserEventDTO userEventDTO) {
        try {
            eventUserStatusService.deleteUserFromEvent(userEventDTO.eventId(), userEventDTO.userId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
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
        return ResponseEntity.ok(eventService.getAllEvents());
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
