package my.pastebin.Feedback;

import lombok.RequiredArgsConstructor;
import my.pastebin.Feedback.dto.NewFeedbackDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedBackController {

    private final FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<?> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @GetMapping("/my-written")
    public ResponseEntity<?> getAllUserFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllUserFeedbacks());
    }

    @GetMapping("/my-received")
    public ResponseEntity<?> getAllUserReceivedFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllUserReceivedFeedbacks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedback(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(feedbackService.getFeedback(id));
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createFeedback(NewFeedbackDTO newFeedbackDTO){
        try{
            return ResponseEntity.ok(feedbackService.createFeedback(newFeedbackDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeFeedback(@PathVariable Long id, NewFeedbackDTO newFeedbackDTO) {
        try {
            return ResponseEntity.ok(feedbackService.changeFeedback(id, newFeedbackDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
