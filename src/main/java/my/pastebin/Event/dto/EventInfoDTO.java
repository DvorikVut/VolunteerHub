package my.pastebin.Event.dto;

import my.pastebin.User.User;

import java.time.LocalDateTime;

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
        User creator
) {
}
