package my.pastebin.Event.dto;

import lombok.Builder;
import my.pastebin.User.User;
import my.pastebin.User.dto.UserInfo;

import java.time.LocalDateTime;

@Builder
public record EventInfoDTO(
        Long id,
        String name,
        String description,
        String city,
        String address,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Integer capacity,
        Integer occupiedQuantity,
        Integer price,
        UserInfo creator
) {
}
