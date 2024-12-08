package my.pastebin.Event.dto;

import lombok.RequiredArgsConstructor;
import my.pastebin.Event.Event;

import java.util.function.Function;

import my.pastebin.EventUserStatus.EventUserStatusService;
import my.pastebin.EventUserStatus.Status;
import my.pastebin.User.dto.UserInfoDTOMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class EventInfoDTOMapper implements Function<Event,EventInfoDTO> {
    private final UserInfoDTOMapper userInfoDTOMapper;
    private final EventUserStatusService eventUserStatusService;

    public EventInfoDTOMapper(@Lazy UserInfoDTOMapper userInfoDTOMapper, @Lazy EventUserStatusService eventUserStatusService) {
        this.userInfoDTOMapper = userInfoDTOMapper;
        this.eventUserStatusService = eventUserStatusService;
    }

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
                .occupiedQuantity(eventUserStatusService.getAllByUserId(event.getId()).stream().filter(e -> e.getStatus().equals(Status.CONFIRMED)).count())
                .creator(userInfoDTOMapper.apply(event.getCreator()))
                .imageURL(event.getImageURL())
                .coordinates(event.getCoordinates())
                .build();
    }
}
