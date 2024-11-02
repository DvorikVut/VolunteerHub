package my.pastebin.User;

import lombok.RequiredArgsConstructor;
import my.pastebin.Event.EventService;
import my.pastebin.EventUserStatus.EventUserStatusService;
import my.pastebin.EventUserStatus.Status;
import my.pastebin.User.dto.UserInfo;
import my.pastebin.User.dto.UserOnEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    private final EventUserStatusService eventUserStatusService;
    public UserMapper(@Lazy EventUserStatusService eventUserStatusService) {
        this.eventUserStatusService = eventUserStatusService;
    }

    public User mapToUserInfo(UserInfo userInfo) {
        return User.builder()
                .id(userInfo.id())
                .name(userInfo.name())
                .surname(userInfo.surname())
                .email(userInfo.email())
                .points(userInfo.points())
                .pointAsCreator(userInfo.pointAsCreator())
                .build();
    }

    public UserInfo mapToUserInfo(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .points(user.getPoints())
                .pointAsCreator(user.getPointAsCreator())
                .build();
    }

    public UserOnEvent mapToUserOnEvent(User user, Long eventId) {
        Status status = eventUserStatusService.getEventUserStatus(eventId, user.getId());
        return UserOnEvent.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .status(status)
                .build();
    }
}
