package google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final NaverUserMapper naverUserMapper;

    public UserService(UserMapper userMapper, NaverUserMapper naverUserMapper) {
        this.userMapper = userMapper;
        this.naverUserMapper = naverUserMapper;
    }
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void saveUser(OAuth2User oauth2User, String provider) {
        try {
            if (oauth2User == null) {
                return;
            }
            
            String id;
            String email;
            String name;
            String picture;
            
            if ("google".equals(provider)) {
            	id = oauth2User.getAttribute("sub");
            	email = oauth2User.getAttribute("email");
            	name = oauth2User.getAttribute("name");
            	picture = oauth2User.getAttribute("picture");
            	
            	if (userMapper.countByGoogleId(id) > 0) {
                    logger.info("기존 사용자, 업데이트 진행");
                    int updatedRows = userMapper.updateUser(id, email, name, picture);
                    logger.info("업데이트된 행 수: {}", updatedRows);
                } else {
                    logger.info(" 신규 사용자, 데이터 삽입");
                    int insertedRows = userMapper.saveUser(id, email, name, picture);
                    logger.info("삽입된 행 수: {}", insertedRows);
                }
            } else if ("naver".equals(provider)){
            	Map<String, Object> response = (Map<String, Object>) oauth2User.getAttribute("response");
            	
                id = (String) response.get("id");
                email = (String) response.get("email");
                name = (String) response.get("name");
                picture = (String) response.get("profile_image");
                
                if (picture == null) {
                    picture = "https://ssl.pstatic.net/static/pwe/address/img_profile.png";
                }
                
                if (naverUserMapper.countByNaverId(id) > 0) {
                    logger.info("기존 사용자, 업데이트 진행");
                    int updatedRows = naverUserMapper.updateUser(id, email, name, picture);
                    logger.info("업데이트된 행 수: {}", updatedRows);
                } else {
                    logger.info(" 신규 사용자, 데이터 삽입");
                    int insertedRows = naverUserMapper.saveUser(id, email, name, picture);
                    logger.info("삽입된 행 수: {}", insertedRows);
                }
            } else if ("kakao".equals(provider)){
            	return;
            } else if ("github".equals(provider)){
            	return;
            } else {
            	return;
            }
            
            logger.info("saveUser() 호출됨: {}, {}, {}", id, email, name);

            
        } catch (Exception e) {
            logger.error("saveUser() 실행 중 오류 ", e);
        }
    }
}
