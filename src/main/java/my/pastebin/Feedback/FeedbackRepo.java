package my.pastebin.Feedback;

import jakarta.servlet.FilterRegistration;
import my.pastebin.Event.Event;
import my.pastebin.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByUserId(Long id);
    List<Feedback> findAllByEventId(Long eventId);
    List<Feedback> findAllByEventIn(List<Event> events);
}
