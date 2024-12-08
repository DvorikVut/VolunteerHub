package my.pastebin.Feedback.dto;

import lombok.Builder;
import my.pastebin.User.dto.UserInfo;

import java.time.LocalDateTime;

@Builder
public record FeedbackInfoDTO(
        Long id,
        String text,
        Integer rating,
        UserInfo creator,
        Long eventId,
        UserInfo target,
        LocalDateTime createdAt
) {
}
