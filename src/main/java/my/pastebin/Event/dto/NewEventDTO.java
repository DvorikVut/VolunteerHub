package my.pastebin.Event.dto;

import java.time.LocalDateTime;

public record NewEventDTO(
        String name,
        String description,
        String city,
        String address,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Integer capacity
) {
}
