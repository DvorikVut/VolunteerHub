package my.pastebin.Feedback;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import my.pastebin.Feedback.dto.FeedbackInfoDTO;
import my.pastebin.Feedback.dto.NewFeedbackDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
@Tag(name = "FeedbackController", description = "Operations related to feedbacks")
public class FeedBackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "Get all feedbacks")
    @GetMapping
    public ResponseEntity<List<FeedbackInfoDTO>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAll());
    }

    @Operation(summary = "Get all feedbacks written by the current user")
    @GetMapping("/my-written")
    public ResponseEntity<List<FeedbackInfoDTO>> getAllUserFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllByCurrentUserWrite());
    }

    @Operation(summary = "Get all feedbacks received by the current user")
    @GetMapping("/my-received")
    public ResponseEntity<?> getAllUserReceivedFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllByCurrentUserReceive());
    }

    @Operation(summary = "Get all feedbacks by event ID")
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<FeedbackInfoDTO>> getAllFeedbacksByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(feedbackService.getAllByEventId(eventId));
    }

    @Operation(summary = "Get a feedback by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackInfoDTO> getFeedback(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getInfoById(id));
    }

    @Operation(summary = "Create a new feedback")
    @PostMapping
    public ResponseEntity<?> createFeedback(@RequestBody NewFeedbackDTO newFeedbackDTO){
        return ResponseEntity.ok(feedbackService.createFeedback(newFeedbackDTO));
    }

    @Operation(summary = "Change a feedback")
    @PutMapping("/{id}")
    public ResponseEntity<?> changeFeedback(@PathVariable Long id, @RequestBody NewFeedbackDTO newFeedbackDTO) {
        feedbackService.changeFeedback(id, newFeedbackDTO);
        return ResponseEntity.ok("Feedback changed");
    }

    @Operation(summary = "Delete a feedback")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Feedback deleted");
    }

    @Operation(summary = "Get a feedback by current user and feedback ID")
    @GetMapping("/my/{id}")
    public ResponseEntity<?> getFeedbackByCurrentUserAndFeedbackId(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getInfoByCurrentUserAndEventId(id));
    }

}
