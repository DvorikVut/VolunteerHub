package my.pastebin.Chat;

import lombok.RequiredArgsConstructor;
import my.pastebin.Chat.dto.MessageInfoDTO;
import my.pastebin.Chat.dto.MessageInfoDTOMapper;
import my.pastebin.Chat.dto.NewMessageDTO;
import my.pastebin.Exceptions.NotAuthorizedException;
import my.pastebin.Exceptions.ResourceNotFoundException;
import my.pastebin.User.Role;
import my.pastebin.User.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageInfoDTOMapper messageInfoDTOMapper;
    private final UserService userService;

    public void createMessage(NewMessageDTO newMessageDTO){
        Message message = Message.builder()
                .senderId(newMessageDTO.senderId())
                .recipientId(newMessageDTO.recipientId())
                .content(newMessageDTO.content())
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();
        save(message);
    }


    public Message getById(Long messageId){
        return messageRepository.getReferenceById(messageId);
    }

    public MessageInfoDTO getInfoById(Long messageId){
        return messageInfoDTOMapper.apply(getById(messageId));
    }

    public void save(Message message){
        messageRepository.save(message);
    }

    public void delete(Message message){
        checkIfExists(message.getId());
        if(!message.getSenderId().equals(userService.getCurrentUser().getId()) && !userService.getCurrentUser().getRole().equals(Role.ADMIN)){
            throw new NotAuthorizedException("You are not allowed to delete this message");
        }
        messageRepository.delete(message);
    }

    public void checkIfExists(Long messageId){
        if(!messageRepository.existsById(messageId)){
            throw new ResourceNotFoundException("Message with ID " + messageId + " does not exist");
        }
    }

    public List<Message> getAllBySenderId(Long senderId) {
        return messageRepository.findAllBySenderId(senderId);
    }

    public List<Message> getAllByReceiverId(Long receiverId) {
        return messageRepository.findAllByRecipientId(receiverId);
    }

    public List<Message> getAllBySenderIdAndReceiverId(Long senderId, Long receiverId) {
        return messageRepository.findAllBySenderIdAndRecipientId(senderId, receiverId);
    }

    public void markAsReadById(Long messageId) {
        Message message = getById(messageId);
        if(!message.getRecipientId().equals(userService.getCurrentUser().getId())){
            throw new NotAuthorizedException("You are not allowed to mark this message as read");
        }
        message.setIsRead(true);
        save(message);
    }

    public List<Long> getWriters() {
        return messageRepository.findDistinctByRecipientId(userService.getCurrentUser().getId())
                .stream()
                .map(Message::getSenderId)
                .toList();
    }
}
