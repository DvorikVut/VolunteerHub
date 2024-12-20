package my.pastebin.Auth;

import lombok.RequiredArgsConstructor;
import my.pastebin.Config.JwtService;
import my.pastebin.User.Role;
import my.pastebin.User.UserRepo;
import my.pastebin.User.User;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) throws BadRequestException {

        if(userRepo.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException("User with this email already exists");
        }

        var user = User.builder()
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .role(Role.USER)
                .password(passwordEncoder.encode(request.password()))
                .points(0)
                .pointsAsCreator(0.0f)
                .build();
        userRepo.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = userRepo.findByEmail(request.email()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .name(user.getName())
                .email(user.getEmail())
                .build();

    }
}
