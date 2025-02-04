package google;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public RedirectView home(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            logger.info("User is null, redirecting to login page.");
            return new RedirectView("http://localhost:4000/login");
        }
        System.out.println(" HomeController에서 saveUser() 호출");
        userService.saveUser(user);
        return new RedirectView("http://localhost:3000/home"); // 로그인 후 리디렉트
    }
    
    @GetMapping("/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
        }
        System.out.println(" HomeController에서 saveUser() 호출");
        userService.saveUser(user);
        Map<String, Object> userInfo = Map.of(
            "authenticated", true,
            "name", user.getAttribute("name"),
            "email", user.getAttribute("email"),
            "picture", user.getAttribute("picture")
        );
        return ResponseEntity.ok(userInfo);
    }

}

