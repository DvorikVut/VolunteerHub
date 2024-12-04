package my.pastebin.Chat.dto;

import java.time.LocalDateTime;

public record MessageInfoDTO(
        Long id,
        Long senderId,
        Long recipientId,
        String content,
        LocalDateTime sentAt,
        Boolean read
) {
}
