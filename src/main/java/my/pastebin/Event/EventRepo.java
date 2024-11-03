package my.pastebin.Event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepo extends JpaRepository<Event, Long> {
    List<Event> findAllByStartDateTimeAfter(LocalDateTime dateTime);
    List<Event> findAllByCreatorId(Long id);
}
