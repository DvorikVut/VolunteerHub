package my.pastebin.Event.dto;

import lombok.Builder;
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
        Long occupiedQuantity,
        Integer price,
        UserInfo creator,
        String imageURL,
        String coordinates
) {
}
