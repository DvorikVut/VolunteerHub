package my.pastebin.Event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import my.pastebin.Event.dto.EventInfoDTO;
import my.pastebin.Event.dto.NewEventDTO;
import my.pastebin.Event.dto.UserEventDTO;
import my.pastebin.EventUserStatus.EventUserStatusService;
import my.pastebin.EventUserStatus.Status;
import my.pastebin.Logger.MyLogger;
import my.pastebin.User.dto.UserInfo;
import my.pastebin.User.dto.UserOnEvent;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
@Tag(name = "EventController", description = "Operations related to events")
public class EventController {

    private final EventService eventService;
    private final EventUserStatusService eventUserStatusService;

    /**
     * Creates a new event.
     *
     * @param newEventDTO the DTO containing the event details
     * @return the created event
     */
    @Operation(summary = "Create a new event")
    @PostMapping
    public ResponseEntity<Long> createEvent(@RequestBody NewEventDTO newEventDTO) {
        Event event = eventService.create(newEventDTO);
        return ResponseEntity.ok(event.getId());
    }

    @Operation(summary = "upload image for event")
    @PostMapping(value = "/{eventId}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestPart("image") MultipartFile image, @PathVariable Long eventId) {
        eventService.addImage(eventId, image);
        return ResponseEntity.ok("Image was uploaded successfully");
    }

    /**
     * Registers a user for an event.
     *
     * @param eventId the ID of the event
     * @return response confirming the registration
     */


    @Operation(summary = "Register a user for an event")
    @PostMapping("/register/{eventId}")
    public ResponseEntity<String> registerUserForEvent(@PathVariable Long eventId) {
        eventUserStatusService.registerUserForEvent(eventId);
        return ResponseEntity.ok("User was registered successfully for event");
    }

    /**
     * Changes the status of a user in an event to "CONFIRMED".
     *
     * @param eventId the ID of the event
     * @param userId the ID of the user
     * @return response confirming the status change
     */
    @Operation(summary = "Change the status of a user in an event to CONFIRMED")
    @PostMapping("/status-confirm/{eventId}/{userId}")
    public ResponseEntity<String> changeStatusConfirm(@PathVariable Long eventId, @PathVariable Long userId) {
        eventUserStatusService.changeStatus(eventId, userId, Status.CONFIRMED);
        return ResponseEntity.ok("Status was changed successfully");
    }

    /**
     * Changes the status of a user in an event to "ATTENDED".
     *
     * @param eventId the ID of the event
     * @param userId the ID of the user
     * @return response confirming the status change
     */
    @Operation(summary = "Change the status of a user in an event to ATTENDED")
    @PostMapping("/status-attended/{eventId}/{userId}")
    public ResponseEntity<String> changeStatusAttended(@PathVariable Long eventId, @PathVariable Long userId) {
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
    @Operation(summary = "Update an event's details")
    @PutMapping("/{id}")
    public ResponseEntity<String> changeEvent(@PathVariable Long id, @RequestBody NewEventDTO newEventDTO) {
        eventService.change(id, newEventDTO);
        return ResponseEntity.ok("Event was changed successfully");
    }

    /**
     * Deletes an event.
     *
     * @param eventId the ID of the event to delete
     * @return response confirming the deletion
     */
    @Operation(summary = "Delete an event")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        eventService.delete(eventId);
        return ResponseEntity.ok("Event was deleted successfully");
    }

    /**
     * Deletes a user from an event.
     *
     * @param userEventDTO DTO containing event and user IDs
     * @return response confirming the deletion
     */
    @Operation(summary = "Delete a user from an event")
    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUserFromEvent(@RequestBody UserEventDTO userEventDTO) {
        eventUserStatusService.deleteUserFromEvent(userEventDTO.eventId(), userEventDTO.userId());
        return ResponseEntity.ok("User was deleted successfully from event");
    }

    /**
     * Retrieves information about a specific event.
     *
     * @param id the ID of the event
     * @return the event details
     */
    @Operation(summary = "Get information about an event")
    @GetMapping("/{id}")
    public ResponseEntity<EventInfoDTO> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventInfo(id));
    }

    /**
     * Retrieves all events created by the authenticated user.
     *
     * @return a list of events
     */
    @Operation(summary = "Get all events created by the authenticated user")
    @GetMapping("/my-as-creator")
    public ResponseEntity<List<EventInfoDTO>> getAllAsCreatorEvents() {
        return ResponseEntity.ok(eventService.getAllAsCreatorEvents());
    }

    /**
     * Retrieves all events.
     *
     * @return a list of all events
     */
    @Operation(summary = "Get all events")
    @GetMapping
    public ResponseEntity<List<EventInfoDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAll());
    }

    /**
     * Retrieves the number of registered users for a specific event.
     *
     * @param eventId the ID of the event
     * @return the number of registered users
     */
    @Operation(summary = "Get the number of registered users for an event")
    @GetMapping("/users-registered/{eventId}")
    public ResponseEntity<List<UserOnEvent>> getNumberOfUsers(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventUserStatusService.getRegisteredUsers(eventId));
    }

    /**
     * Retrieves all future events.
     *
     * @return a list of future events
     */
    @Operation(summary = "Get all future events")
    @GetMapping("/future")
    public ResponseEntity<List<EventInfoDTO>> getFutureEvents() {
        return ResponseEntity.ok(eventService.getFutureEvent());
    }

    /**
     * Retrieves all events where the authenticated user is a participant.
     *
     * @return a list of events
     */
    @Operation(summary = "Get all events where the authenticated user is a participant")
    @GetMapping("/my-as-participant")
    public ResponseEntity<List<EventInfoDTO>> getAllAsParticipant(){
        return ResponseEntity.ok(eventService.getAllAsParticipant());
    }
}
