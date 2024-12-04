package my.pastebin.User;

import lombok.RequiredArgsConstructor;
import my.pastebin.Feedback.Feedback;
import my.pastebin.Feedback.FeedbackService;
import my.pastebin.User.dto.UserInfo;
import my.pastebin.User.dto.UserInfoDTOMapper;
import my.pastebin.User.dto.UserOnEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final FeedbackService feedbackService;
    private final UserMapper userMapper;
    private final UserInfoDTOMapper userInfoDTOMapper;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the user entity
     */
    public User getUserById(Long id) {
        return userRepo.getReferenceById(id);
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the currently authenticated user
     * @throws RuntimeException if no authenticated user is found
     */
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return getUserById(((User) principal).getId());
        } else {
            throw new RuntimeException("User not authenticated");
        }
    }

    /**
     * Adds points to a user's score.
     *
     * @param user the user entity
     * @param points the number of points to add
     */
    public void addPoints(User user, int points) {
        user.setPoints(user.getPoints() + points);
        userRepo.save(user);
    }

    /**
     * Calculates and updates the average points as a creator for a user based on feedback.
     *
     * @param feedback the feedback given to the user's creation
     */
    public void calculatePointsAsCreator(Feedback feedback) {
        User user = feedback.getCreator();
        int feedbackCount = feedbackService.getQuantityOfFeedbacks(user);
        user.setPointAsCreator((user.getPointAsCreator() * feedbackCount + feedback.getRating()) / (feedbackCount + 1));
        userRepo.save(user);
    }

    /**
     * Maps a User entity to a UserOnEvent DTO.
     *
     * @param user the user entity
     * @param eventId the ID of the event
     * @return the mapped UserOnEvent DTO
     */
    public UserOnEvent UserToUserOnEvent(User user, Long eventId) {
        return userMapper.mapToUserOnEvent(user, eventId);
    }

    /**
     * Retrieves information about the currently authenticated user.
     *
     * @return the UserInfo DTO of the currently authenticated user
     */
    public ResponseEntity<UserInfo> getCurrentUserInfo() {
        return ResponseEntity.ok(userMapper.mapToUserInfo(getCurrentUser()));
    }

    /**
     * Retrieves the top 100 users ranked by their points.
     *
     * @return a list of UserTopInfo DTOs for the top 100 users
     */
    public List<UserInfo> getTop100ByPoints() {
        List<User> users = userRepo.findTop100ByOrderByPointsDesc();
        return users.stream()
                .map(userInfoDTOMapper)
                .toList();
    }
}
