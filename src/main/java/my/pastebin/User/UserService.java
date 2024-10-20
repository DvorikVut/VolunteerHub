package my.pastebin.User;


import lombok.RequiredArgsConstructor;
import my.pastebin.Event.EventService;
import my.pastebin.Feedback.Feedback;
import my.pastebin.Feedback.FeedbackService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final FeedbackService feedbackService;

    public User getUserById(Long id) throws ChangeSetPersister.NotFoundException {
        return userRepo.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void addPoints(User user, int points) {
        user.setPoints(user.getPoints() + points);
        userRepo.save(user);
    }

    public void calculatePointsAsCreator(Feedback feedback) {
        User user = feedback.getCreator();
        user.setPointAsCreator((user.getPointAsCreator() * feedbackService.getQuantityOfFeedbacks(user) + feedback.getRating() ) / (feedbackService.getQuantityOfFeedbacks(user) + 1));
        userRepo.save(user);
    }
}
