package my.pastebin.Feedback;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import my.pastebin.Feedback.dto.NewFeedbackDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
@Tag(name = "FeedbackController", description = "Operations related to feedbacks")
public class FeedBackController {

    private final FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<?> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @Operation(summary = "Get all feedbacks written by the current user")
    @GetMapping("/my-written")
    public ResponseEntity<?> getAllUserFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllUserFeedbacks());
    }

    @Operation(summary = "Get all feedbacks received by the current user")
    @GetMapping("/my-received")
    public ResponseEntity<?> getAllUserReceivedFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllUserReceivedFeedbacks());
    }

    @Operation(summary = "Get a feedback by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedback(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(feedbackService.getFeedback(id));
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Create a new feedback")
    @PostMapping
    public ResponseEntity<?> createFeedback(NewFeedbackDTO newFeedbackDTO){
        try{
            return ResponseEntity.ok(feedbackService.createFeedback(newFeedbackDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Change a feedback")
    @PutMapping("/{id}")
    public ResponseEntity<?> changeFeedback(@PathVariable Long id, NewFeedbackDTO newFeedbackDTO) {
        try {
            return ResponseEntity.ok(feedbackService.changeFeedback(id, newFeedbackDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete a feedback")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
