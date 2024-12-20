package my.pastebin.Admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import my.pastebin.Event.EventService;
import my.pastebin.Event.dto.EventInfoDTO;
import my.pastebin.Event.dto.NewEventDTO;
import my.pastebin.EventUserStatus.EventUserStatusService;
import my.pastebin.Feedback.FeedbackService;
import my.pastebin.Feedback.dto.FeedbackInfoDTO;
import my.pastebin.Feedback.dto.NewFeedbackDTO;
import my.pastebin.User.UserService;
import my.pastebin.User.dto.UpdateUserAdminDTO;
import my.pastebin.User.dto.UserInfo;
import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final EventService eventService;
    private final EventUserStatusService eventUserStatusService;
    private final FeedbackService feedbackService;

    //EVENTS
    @Operation(summary = "Get all events")
    @GetMapping("/events")
    public ResponseEntity<List<EventInfoDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @Operation(summary = "register a user for an event")
    @PostMapping("/event/{eventId}/add-participant/{userId}")
    public ResponseEntity<String> addParticipant(@PathVariable Long eventId, @PathVariable Long userId) {
        eventUserStatusService.registerUserForEventByUserId(eventId, userId);
        return ResponseEntity.ok("Participant added");
    }

    @Operation(summary = "remove a user from an event")
    @DeleteMapping("/event/{eventId}/remove-participant/{userId}")
    public ResponseEntity<String> removeParticipant(@PathVariable Long eventId, @PathVariable Long userId) {
        eventUserStatusService.deleteUserFromEvent(eventId, userId);
        return ResponseEntity.ok("Participant removed");
    }

    @Operation(summary = "Change an event")
    @PutMapping("/event/{eventId}")
    public ResponseEntity<String> changeEvent(@PathVariable Long eventId, @RequestBody  NewEventDTO newEventDTO) {
        eventService.change(eventId, newEventDTO);
        return ResponseEntity.ok("Event updated");
    }
    @Operation(summary = "Get all created events by a user")
    @GetMapping("/user/created-events/{userId}")
    public ResponseEntity<List<EventInfoDTO>> getCreatedEventsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(eventService.getCreatedEventsByUserId(userId));
    }

    @Operation(summary = "Get all attended events by a user")
    @GetMapping("/user/attended-events/{userId}")
    public ResponseEntity<List<EventInfoDTO>> getParticipatedEventsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(eventService.getParticipatedEventsByUserId(userId));
    }

    @Operation(summary = "Delete an event")
    @DeleteMapping("/event/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        eventService.delete(eventId);
        return ResponseEntity.ok("Event deleted");
    }

    //USERS
    @Operation(summary = "Get all users")
    @GetMapping("/users")
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(summary = "Get a user by its ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getInfoById(userId));
    }

    @Operation(summary = "Change a user profile")
    @PutMapping("/user/{userId}")
    public ResponseEntity<?> changeUserProfile(@PathVariable Long userId, @RequestBody UpdateUserAdminDTO updateUserAdminDTO) {
        userService.changeUserProfile(userId, updateUserAdminDTO);
        return ResponseEntity.ok("User profile updated");
    }

    @Operation(summary = "Get all feedbacks")
    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedbackInfoDTO>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAll());
    }

    @Operation(summary = "Get a feedback by its ID")
    @GetMapping("/feedback/{id}")
    public ResponseEntity<FeedbackInfoDTO> getFeedback(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getInfoById(id));
    }

    @Operation(summary = "Change a feedback")
    @PutMapping("/feedback/{id}")
    public ResponseEntity<?> changeFeedback(@PathVariable Long id, @RequestBody NewFeedbackDTO changeFeedbackDTO) {
        feedbackService.changeFeedback(id, changeFeedbackDTO);
        return ResponseEntity.ok("Feedback changed");
    }

    @Operation(summary = "Delete a feedback")
    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Feedback deleted");
    }

}
