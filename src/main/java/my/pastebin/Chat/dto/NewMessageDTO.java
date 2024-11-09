package my.pastebin.Chat.dto;

public record NewMessageDTO(
        Long senderId,
        Long recipientId,
        String content
) {
}
