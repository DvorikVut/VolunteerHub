package my.pastebin.Event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import my.pastebin.Event.dto.NewEventDTO;
import my.pastebin.Event.dto.UserEventDTO;
import my.pastebin.EventUserStatus.EventUserStatusService;
import my.pastebin.EventUserStatus.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
@Tag(name = "Event", description = "Operations related to events")
public class EventController {

    private final EventService eventService;
    private final EventUserStatusService eventUserStatusService;

    /**
     * Creates a new event.
     *
     * @param newEventDTO the DTO containing the event details
     * @return the created event
     */
    @PostMapping
    @Operation(summary = "Create a new event")
    @ApiResponse(responseCode = "200", description = "Event was created successfully")
    public ResponseEntity<?> createEvent(@RequestBody NewEventDTO newEventDTO) {
        return ResponseEntity.ok(eventService.create(newEventDTO));
    }

    /**
     * Registers a user for an event.
     *
     * @param eventId the ID of the event
     * @return response confirming the registration
     */
    @PostMapping("/register/{eventId}")
    public ResponseEntity<?> registerUserForEvent(@PathVariable Long eventId) {
        eventUserStatusService.registerUserForEvent(eventId);
        return ResponseEntity.ok().build();
    }

    /**
     * Changes the status of a user in an event to "CONFIRMED".
     *
     * @param eventId the ID of the event
     * @param userId the ID of the user
     * @return response confirming the status change
     */
    @PostMapping("/status-confirm/{eventId}/{userId}")
    public ResponseEntity<?> changeStatusConfirm(@PathVariable Long eventId, @PathVariable Long userId) {
        eventUserStatusService.changeStatus(eventId, userId, Status.CONFIRMED);
        return ResponseEntity.ok().build();
    }

    /**
     * Changes the status of a user in an event to "ATTENDED".
     *
     * @param eventId the ID of the event
     * @param userId the ID of the user
     * @return response confirming the status change
     */
    @PostMapping("/status-attended/{eventId}/{userId}")
    public ResponseEntity<?> changeStatusAttended(@PathVariable Long eventId, @PathVariable Long userId) {
        eventUserStatusService.changeStatus(eventId, userId, Status.ATTENDED);
        return ResponseEntity.ok("Status was changed successfully");
    }

    /**
     * Updates an event's details.
     *
     * @param id the ID of the event
     * @param newEventDTO the new details for the event
     * @return response confirming the update
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> changeEvent(@PathVariable Long id, @RequestBody NewEventDTO newEventDTO) {
        eventService.change(id, newEventDTO);
        return ResponseEntity.ok("Event was changed successfully");
    }

    /**
     * Deletes an event.
     *
     * @param id the ID of the event to delete
     * @return response confirming the deletion
     */
    @DeleteMapping
    public ResponseEntity<?> deleteEvent(Long id) {
        eventService.delete(id);
        return ResponseEntity.ok("Event was deleted successfully");
    }

    /**
     * Deletes a user from an event.
     *
     * @param userEventDTO DTO containing event and user IDs
     * @return response confirming the deletion
     */
    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUserFromEvent(@RequestBody UserEventDTO userEventDTO) {
        eventUserStatusService.deleteUserFromEvent(userEventDTO.eventId(), userEventDTO.userId());
        return ResponseEntity.ok("User was deleted successfully from event");
    }

    /**
     * Retrieves information about a specific event.
     *
     * @param id the ID of the event
     * @return the event details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventInfo(id));
    }

    /**
     * Retrieves all events created by the authenticated user.
     *
     * @return a list of events
     */
    @GetMapping("/my-as-creator")
    public ResponseEntity<?> getAllAsCreatorEvents() {
        return ResponseEntity.ok(eventService.getAllAsCreatorEvents());
    }

    /**
     * Retrieves all events.
     *
     * @return a list of all events
     */
    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventService.getAll());
    }

    /**
     * Retrieves the number of registered users for a specific event.
     *
     * @param eventId the ID of the event
     * @return the number of registered users
     */
    @GetMapping("/users-registered/{eventId}")
    public ResponseEntity<?> getNumberOfUsers(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventUserStatusService.getRegisteredUsers(eventId));
    }

    /**
     * Retrieves all future events.
     *
     * @return a list of future events
     */
    @GetMapping("/future")
    public ResponseEntity<?> getFutureEvents() {
        return ResponseEntity.ok(eventService.getFutureEvent());
    }

    /**
     * Retrieves all events where the authenticated user is a participant.
     *
     * @return a list of events
     */
    @GetMapping("/my-as-participant")
    public ResponseEntity<?> getAllAsParticipant(){
        return ResponseEntity.ok(eventService.getAllAsParticipant());
    }
}
