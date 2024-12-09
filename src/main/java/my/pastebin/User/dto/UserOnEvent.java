package my.pastebin.User.dto;

import lombok.Builder;
import my.pastebin.EventUserStatus.Status;

@Builder
public record UserOnEvent(Long id,
                          String name,
                          String email,
                          String surname,
                          Integer points,
                          Float pointsAsCreator,
                          String imageURL,
                          Status status
) {
}
