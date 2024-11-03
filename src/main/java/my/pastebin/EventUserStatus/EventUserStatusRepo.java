package my.pastebin.EventUserStatus;

import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventUserStatusRepo extends JpaRepository<EventUserStatus, Long> {
    EventUserStatus findByEventIdAndUserId(Long eventId, Long userId);
    List<EventUserStatus> findAllByEventId(Long eventId);
    List<EventUserStatus> findAllByEventIdAndStatus(Long eventId, Status status);
    List<EventUserStatus> findAllByUserId(Long userId);
    boolean existsByUserIdAndEventId(Long userId ,Long eventId);
}
