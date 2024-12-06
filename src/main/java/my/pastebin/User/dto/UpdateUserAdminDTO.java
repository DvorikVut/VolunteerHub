package my.pastebin.User.dto;

public record UpdateUserAdminDTO(
        String name,
        String surname,
        Integer points,
        Float pointsAsCreator
) {
}
