package my.pastebin.Auth;

import lombok.Builder;

@Builder
public record RegisterRequest(
        String name,
        String surname,
        String email,
        String password
) {
}
