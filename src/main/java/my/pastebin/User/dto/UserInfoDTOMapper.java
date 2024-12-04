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
                .surname(user.getSurname())
                .email(user.getEmail())
                .imageURL(user.getImageURL())
                .build();
    }
}
