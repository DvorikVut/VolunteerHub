package my.pastebin.Feedback.dto;

import lombok.RequiredArgsConstructor;
import my.pastebin.Feedback.Feedback;
import my.pastebin.User.dto.UserInfoDTOMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class FeedbackInfoDTOMapper implements Function<Feedback,FeedbackInfoDTO> {
    private final UserInfoDTOMapper userInfoDTOMapper;
    @Override
    public FeedbackInfoDTO apply(Feedback feedback) {
        return FeedbackInfoDTO.builder()
                .id(feedback.getId())
                .text(feedback.getText())
                .rating(feedback.getRating())
                .creator(userInfoDTOMapper.apply(feedback.getCreator()))
                .eventId(feedback.getEvent().getId())
                .target(userInfoDTOMapper.apply(feedback.getEvent().getCreator()))
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
