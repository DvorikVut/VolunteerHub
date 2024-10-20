package my.pastebin.Event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepo extends JpaRepository<Event, Long> {
    List<Event> findAllByCreatorId(Long id);
}
