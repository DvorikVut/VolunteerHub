package my.pastebin.Auth;

import lombok.Builder;
import lombok.Data;

@Builder
public record AuthRequest(
        String email,
        String password) {
}
