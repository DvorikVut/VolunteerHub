package my.pastebin.Feedback;

import my.pastebin.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByUserId(Long id);
    List<Feedback> findAllByCreator(User currentUser);
}
