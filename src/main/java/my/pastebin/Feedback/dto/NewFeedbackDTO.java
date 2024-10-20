package my.pastebin.Feedback.dto;

public record NewFeedbackDTO(
        Long userToRateId,
        String text,
        Integer rating
) {
}
