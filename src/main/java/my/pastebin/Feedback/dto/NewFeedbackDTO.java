package my.pastebin.Feedback.dto;

public record NewFeedbackDTO(
        Long eventId,
        String text,
        Integer rating
) {
}
