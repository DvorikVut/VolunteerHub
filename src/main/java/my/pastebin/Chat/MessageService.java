package my.pastebin.Chat;

import lombok.RequiredArgsConstructor;
import my.pastebin.Chat.dto.MessageInfoDTO;
import my.pastebin.Chat.dto.MessageInfoDTOMapper;
import my.pastebin.Chat.dto.NewMessageDTO;
import my.pastebin.Exceptions.ResourceNotFoundException;
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
                .read(false)
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
}
