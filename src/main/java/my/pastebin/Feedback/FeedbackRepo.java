package my.pastebin.Feedback;

import jakarta.servlet.FilterRegistration;
import my.pastebin.Event.Event;
import my.pastebin.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByCreatorId(Long creatorId);
    List<Feedback> findAllByEventId(Long eventId);
    List<Feedback> findAllByEventIn(List<Event> events);
    List<Feedback> findAllByCreatorIdAndEventId(Long id, Long eventId);

    @Query("SELECT f FROM Feedback f WHERE f.event.creator.id = :userId")
    List<Feedback> findFeedbacksByEventCreatorId(@Param("userId") Long userId);

    List<Feedback> findAllByEventCreatorId(Long id);
}
