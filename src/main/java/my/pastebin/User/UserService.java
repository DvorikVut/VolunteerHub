package my.pastebin.User;


import lombok.RequiredArgsConstructor;
import my.pastebin.Event.EventService;
import my.pastebin.Feedback.Feedback;
import my.pastebin.Feedback.FeedbackService;
import my.pastebin.User.dto.UserInfo;
import my.pastebin.User.dto.UserOnEvent;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final FeedbackService feedbackService;
    private final UserMapper userMapper;

    public User getUserById(Long id){
        return userRepo.getReferenceById(id);
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        } else {
            throw new RuntimeException("User ne user nah");
        }
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

    public UserInfo UserToUserInfo(User user){
        return userMapper.mapToUserInfo(user);
    }

    public UserOnEvent UserToUserOnEvent(User user, Long eventId){
        return userMapper.mapToUserOnEvent(user, eventId);
    }

    public ResponseEntity<UserInfo> getCurrentUserInfo() {
        return ResponseEntity.ok(userMapper.mapToUserInfo(getCurrentUser()));
    }
}
