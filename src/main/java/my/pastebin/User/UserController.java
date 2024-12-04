package my.pastebin.User;

import lombok.RequiredArgsConstructor;
import my.pastebin.User.dto.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @GetMapping("/me")
    public ResponseEntity<UserInfo> getProfile() {
        return userService.getCurrentUserInfo();
    }

    @GetMapping("/top100")
    public ResponseEntity<List<UserInfo>> getTop100UserByPoints(){
        return ResponseEntity.ok(userService.getTop100ByPoints());
    }
}
