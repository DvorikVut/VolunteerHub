package my.pastebin.Feedback;

import lombok.RequiredArgsConstructor;
import my.pastebin.Feedback.dto.NewFeedbackDTO;
import my.pastebin.User.Role;
import my.pastebin.User.User;
import my.pastebin.User.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepo feedbackRepo;
    private final UserService userService;

    public FeedbackService(FeedbackRepo feedbackRepo, @Lazy UserService userService) {
        this.feedbackRepo = feedbackRepo;
        this.userService = userService;
    }

    public Feedback createFeedback(NewFeedbackDTO newFeedbackDTO) throws ChangeSetPersister.NotFoundException {
        Feedback feedback = new Feedback();
        feedback.setUser(userService.getUserById(newFeedbackDTO.userToRateId()));
        feedback.setText(newFeedbackDTO.text());
        feedback.setRating(newFeedbackDTO.rating());
        feedback.setCreator(userService.getCurrentUser());
        return feedbackRepo.save(feedback);
    }

    public void deleteFeedback(Long id) throws ChangeSetPersister.NotFoundException, AccessDeniedException {
        Feedback feedback = feedbackRepo.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        if(feedback.getCreator().getId().equals(userService.getCurrentUser().getId()) || userService.getCurrentUser().getRole().equals(Role.ADMIN)) {
            feedbackRepo.delete(feedback);
        } else {
            throw new AccessDeniedException("You are not allowed to delete this feedback");
        }
    }

    public Feedback changeFeedback(Long feedback_id, NewFeedbackDTO newFeedbackDTO) throws ChangeSetPersister.NotFoundException, AccessDeniedException {
        Feedback feedback = feedbackRepo.findById(feedback_id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        if(!(feedback.getCreator().getId().equals(userService.getCurrentUser().getId()) || userService.getCurrentUser().getRole().equals(Role.ADMIN))) {
            throw new AccessDeniedException("You are not allowed to change this feedback");
        } else {
            feedback.setText(newFeedbackDTO.text());
            feedback.setRating(newFeedbackDTO.rating());
            userService.calculatePointsAsCreator(feedback);
            return feedbackRepo.save(feedback);
        }
    }

    public Feedback getFeedback(Long id) throws ChangeSetPersister.NotFoundException {
        return feedbackRepo.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepo.findAll();
    }

    public List<Feedback> getAllUserFeedbacks() {
        return feedbackRepo.findAllByCreator(userService.getCurrentUser());

    }

    public List<Feedback> getAllUserReceivedFeedbacks() {
        return feedbackRepo.findAllByUserId(userService.getCurrentUser().getId());
    }

    public Integer getQuantityOfFeedbacks(User user) {
        return feedbackRepo.findAllByUserId(user.getId()).size();
    }
}
