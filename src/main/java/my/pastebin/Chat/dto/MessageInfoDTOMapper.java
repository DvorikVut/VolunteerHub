package my.pastebin.Chat.dto;

import my.pastebin.Chat.Message;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MessageInfoDTOMapper implements Function<Message,MessageInfoDTO> {
    @Override
    public MessageInfoDTO apply(Message message) {
        return new MessageInfoDTO(
                message.getId(),
                message.getSenderId(),
                message.getRecipientId(),
                message.getContent(),
                message.getSentAt(),
                message.getIsRead()
        );
    }
}
