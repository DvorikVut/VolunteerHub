package my.pastebin.User.dto;

import lombok.Builder;

@Builder
public record UserTopInfo(
        String name,
        String email,
        Integer points
) {
}
