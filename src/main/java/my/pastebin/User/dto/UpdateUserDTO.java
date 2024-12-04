package my.pastebin.User.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserDTO(
        String name,
        String surname,
        MultipartFile image
) {
}
