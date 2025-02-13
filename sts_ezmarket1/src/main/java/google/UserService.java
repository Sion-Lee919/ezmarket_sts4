package google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ezmarket.cookie.JWTUtil;

import ezmarket.MemberDTO;

import java.util.Map;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public String saveUser(OAuth2User oauth2User, String provider) {
        try {
            if (oauth2User == null) {
                return "";
            }
            
            String username;
            String email;
            String realname;
            String picture;
            
            if ("google".equals(provider)) {
                username = oauth2User.getAttribute("sub");
                email = oauth2User.getAttribute("email");
                realname = oauth2User.getAttribute("name");
                picture = oauth2User.getAttribute("picture");

                if (userMapper.countByGoogleId(username) > 0) {
                    logger.info("기존 Google 사용자, 업데이트 진행");
                    userMapper.updateUser(username, email, realname, picture);
                } else {
                    logger.info("신규 Google 사용자, 데이터 삽입");
                    userMapper.saveUser(username, email, realname, picture);
                }
            } else if ("kakao".equals(provider)) {
                Map<String, Object> kakaoAccount = (Map<String, Object>) oauth2User.getAttribute("kakao_account");
                Map<String, Object> properties = (Map<String, Object>) oauth2User.getAttribute("properties");

                username = oauth2User.getAttribute("id").toString();
                email = kakaoAccount.get("email") != null ? kakaoAccount.get("email").toString() : username + "@kakao.com";
                realname = properties.get("nickname") != null ? properties.get("nickname").toString() : "카카오 사용자";
                picture = properties.get("profile_image") != null ? properties.get("profile_image").toString() : "";

                if (userMapper.countByKakaoId(username) > 0) {
                    logger.info("기존 Kakao 사용자, 업데이트 진행");
                    userMapper.updateKakaoUser(username, email, realname, picture);
                } else {
                    logger.info("신규 Kakao 사용자, 데이터 삽입");
                    userMapper.saveKakaoUser(username, email, realname, picture);
                }
            } else {
                return "";
            }
            
            logger.info("saveUser() 호출됨: {}, {}, {}", username, email, realname);
            
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setUsername(username);
            memberDTO.setEmail(email);
            memberDTO.setRealname(realname);

            String token = JWTUtil.generateToken(memberDTO);
            return token;

        } catch (Exception e) {
            logger.error("saveUser() 실행 중 오류 ", e);
            return "";
        }
    }
}
