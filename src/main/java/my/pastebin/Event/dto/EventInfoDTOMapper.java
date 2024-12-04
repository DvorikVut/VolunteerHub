package my.pastebin.Event.dto;

import lombok.RequiredArgsConstructor;
import my.pastebin.Event.Event;

import java.util.function.Function;
import my.pastebin.User.dto.UserInfoDTOMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventInfoDTOMapper implements Function<Event,EventInfoDTO> {
    private final UserInfoDTOMapper userInfoDTOMapper;
    @Override
    public EventInfoDTO apply(Event event) {
        return EventInfoDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .capacity(event.getCapacity())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .city(event.getCity())
                .address(event.getAddress())
                .price(event.getPrice())
                .creator(userInfoDTOMapper.apply(event.getCreator()))
                .ImageURL(event.getImageURL())
                .build();
    }
}
