package my.pastebin.User.dto;

import my.pastebin.User.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserInfoDTOMapper implements Function<User, UserInfo> {
    @Override
    public UserInfo apply(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .points(user.getPoints())
                .pointsAsCreator(user.getPointsAsCreator())
                .imageURL(user.getImageURL())
                .role(user.getRole())
                .build();
    }
}
