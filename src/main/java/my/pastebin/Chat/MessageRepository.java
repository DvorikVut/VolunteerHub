package my.pastebin.Chat;

import my.pastebin.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySenderId(Long senderId);
    List<Message> findAllByRecipientId(Long receiverId);
    List<Message> findAllBySenderIdAndRecipientId(Long senderId, Long receiverId);
}
