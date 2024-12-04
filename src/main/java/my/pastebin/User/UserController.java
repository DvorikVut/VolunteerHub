package my.pastebin.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "UserController", description = "Operations related to users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get the profile of the current user")
    @GetMapping("/me")
    public ResponseEntity<UserInfo> getProfile() {
        return userService.getCurrentUserInfo();
    }

    @Operation(summary = "Get top 100 users by points")
    @GetMapping("/top100")
    public ResponseEntity<List<UserInfo>> getTop100UserByPoints(){
        return ResponseEntity.ok(userService.getTop100ByPoints());
    }
}
