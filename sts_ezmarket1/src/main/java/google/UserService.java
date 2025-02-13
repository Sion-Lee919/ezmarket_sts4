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
    private final NaverUserMapper naverUserMapper;
    private final GithubUserMapper githubUserMapper;

    public UserService(UserMapper userMapper, NaverUserMapper naverUserMapper, GithubUserMapper githubUserMapper) {
        this.userMapper = userMapper;
        this.naverUserMapper = naverUserMapper;
        this.githubUserMapper = githubUserMapper;
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
                    logger.info("기존 사용자, 업데이트 진행");
                    int updatedRows = userMapper.updateUser(username, email, realname, picture);
                    logger.info("업데이트된 행 수: {}", updatedRows);
                } else {
                    logger.info(" 신규 사용자, 데이터 삽입");
                    int insertedRows = userMapper.saveUser(username, email, realname, picture);
                    logger.info("삽입된 행 수: {}", insertedRows);
                }
            } else if ("naver".equals(provider)) {
            	Map<String, Object> response = (Map<String, Object>) oauth2User.getAttribute("response");
            	
            	username = (String) response.get("id");
                email = (String) response.get("email");
                realname = (String) response.get("name");
                picture = (String) response.get("profile_image");
                
                if (picture == null) {
                    picture = "https://ssl.pstatic.net/static/pwe/address/img_profile.png";
                }
                
                if (naverUserMapper.countByNaverId(username) > 0) {
                    logger.info("기존 사용자, 업데이트 진행");
                    int updatedRows = naverUserMapper.updateUser(username, email, realname, picture);
                    logger.info("업데이트된 행 수: {}", updatedRows);
                } else {
                    logger.info(" 신규 사용자, 데이터 삽입");
                    int insertedRows = naverUserMapper.saveUser(username, email, realname, picture);
                    logger.info("삽입된 행 수: {}", insertedRows);
                }
            } else if ("kakao".equals(provider)){
            	return "";
            } else if ("github".equals(provider)){

            	Integer idInt = oauth2User.getAttribute("id");
            	username = String.valueOf(idInt);
                if (oauth2User.getAttribute("email") != null) {
                	email = oauth2User.getAttribute("email");                	
                } else {
                	email = username + "@github.com";
                }
                realname = oauth2User.getAttribute("login");
            	picture = oauth2User.getAttribute("avatar_url");
            	System.out.println(username + " : " + email + " : " + realname + " : " + picture);
            	
            	if (githubUserMapper.countByGithubId(username) > 0) {
                    logger.info("기존 사용자, 업데이트 진행");
                    int updatedRows = githubUserMapper.updateUser(username, email, realname, picture);
                    logger.info("업데이트된 행 수: {}", updatedRows);
                } else {
                    logger.info(" 신규 사용자, 데이터 삽입");
                    int insertedRows = githubUserMapper.saveUser(username, email, realname, picture);
                    logger.info("삽입된 행 수: {}", insertedRows);
                }

            } else {
            	return "";
            }
            
            logger.info("saveUser() 호출됨: {}, {}, {}", username, email, realname);
            
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setUsername(username);
            memberDTO.setEmail(email);
            memberDTO.setRealname(realname);
          	//memberDTO.setPicture(picture);
            
            String token = JWTUtil.generateToken(memberDTO);
            return token;
            
        } catch (Exception e) {
            logger.error("saveUser() 실행 중 오류 ", e);
            return "";
        }
    }
}
