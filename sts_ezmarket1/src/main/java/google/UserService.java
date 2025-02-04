package google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void saveUser(OAuth2User oauth2User) {
        try {
            if (oauth2User == null) {
              
                return;
            }

            String googleId = oauth2User.getAttribute("sub");
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String picture = oauth2User.getAttribute("picture");

            logger.info("saveUser() 호출됨: {}, {}, {}", googleId, email, name);

            if (userMapper.countByGoogleId(googleId) > 0) {
                logger.info("기존 사용자, 업데이트 진행");
                int updatedRows = userMapper.updateUser(googleId, email, name, picture);
                logger.info("업데이트된 행 수: {}", updatedRows);
            } else {
                logger.info(" 신규 사용자, 데이터 삽입");
                int insertedRows = userMapper.saveUser(googleId, email, name, picture);
                logger.info("삽입된 행 수: {}", insertedRows);
            }
        } catch (Exception e) {
            logger.error("saveUser() 실행 중 오류 ", e);
        }
    }
}
