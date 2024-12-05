package my.pastebin.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import my.pastebin.User.dto.UpdateUserDTO;
import my.pastebin.User.dto.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "UserController", description = "Operations related to users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

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

    @Operation(summary = "Update the profile of the user")
    @GetMapping("/{userId}/update")
    public ResponseEntity<String> updateProfile(@ModelAttribute UpdateUserDTO updateUserDTO, @PathVariable Long userId) {
            userService.update(userId, updateUserDTO);
            return ResponseEntity.ok("Profile updated successfully");
    }
}
