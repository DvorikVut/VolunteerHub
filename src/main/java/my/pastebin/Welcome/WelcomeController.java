package my.pastebin.Welcome;

import my.pastebin.User.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    @GetMapping
    public String welcome(){
        return "Welcome";
    }
}
