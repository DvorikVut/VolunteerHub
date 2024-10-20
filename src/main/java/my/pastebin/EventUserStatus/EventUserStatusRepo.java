package my.pastebin.EventUserStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventUserStatusRepo extends JpaRepository<EventUserStatus, Long> {
    EventUserStatus findByEventIdAndUserId(Long eventId, Long userId);
    List<EventUserStatus> findAllByEventId(Long eventId);
}
