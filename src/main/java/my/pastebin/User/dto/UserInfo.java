package my.pastebin.User.dto;

import lombok.Builder;
import my.pastebin.User.Role;

@Builder
public record UserInfo(
        Long id,
        String name,
        String surname,
        String email,
        Integer points,
        Float pointAsCreator,
        String imageURL,
        Role role
) {
}
