package ezmarket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ezmarket.cookie.JWTUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class MemberController {

	@Autowired
	@Qualifier("membermapperservice")
	MemberService memberService;
	
	//회원가입
		//회원가입
	    @PostMapping("/joinN")
	    public String join(@RequestBody MemberDTO dto) {
	        String result = memberService.joinMember(dto);
	        System.out.println(result);
	        return "redirect:login";
	    }
	    
	    //중복 확인
	    @GetMapping("/checkId")
	    @ResponseBody
	    public String checkDuplicatedId(@RequestParam("username") String username) {
	        boolean isAvailable = memberService.isIdAvailable(username);
	        return isAvailable ? "사용 가능한 아이디입니다." : "중복된 아이디입니다."; 
	    }
	    
	    @GetMapping("/checkNickname")
	    @ResponseBody
	    public String checkDuplicatedNickname(@RequestParam("nickname") String nickname) {
	        boolean isAvailable = memberService.isNicknameAvailable(nickname);
	        return isAvailable ? "사용 가능한 닉네임입니다." : "중복된 닉네임입니다."; 
	    }
	    
	    @GetMapping("/checkEmail")
	    @ResponseBody
	    public String checkDuplicatedEmail(@RequestParam("email") String email) {
	        boolean isAvailable = memberService.isEmailAvailable(email);
	        return isAvailable ? "사용 가능한 이메일입니다." : "중복된 이메일입니다."; 
	    }
	    
	    @GetMapping("/checkPhone")
	    @ResponseBody
	    public String checkDuplicatedPhone(@RequestParam("phone") String phone) {
	        boolean isAvailable = memberService.isPhoneAvailable(phone);
	        return isAvailable ? "사용 가능한 전화번호입니다." : "중복된 전화번호입니다."; 
	    }
    
	//로그인
		//로그인
	    @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO, HttpServletResponse response) {
	        MemberDTO dto = memberService.getMember(memberDTO.getUsername());

	        if (dto != null && dto.getPassword().equals(memberDTO.getPassword())) {
	        	String token = JWTUtil.generateToken(dto);
	        	
	            Cookie cookie = new Cookie("jwt_token", token);
	            cookie.setPath("/"); 
	            cookie.setMaxAge(24 * 60 * 60); 
	            response.addCookie(cookie);
	            cookie.setAttribute("SameSite", "None");

	            Map<String, String> responseBody = new HashMap<>();
	            responseBody.put("message", "로그인 성공"); 
	            return ResponseEntity.ok(responseBody);
	            
	        } else {
	            Map<String, String> errorResponse = new HashMap<>();

	            if (dto == null) {
	                errorResponse.put("message", "아이디가 틀렸습니다.");
	            } else {
	                errorResponse.put("message", "비밀번호가 틀렸습니다.");
	            }

	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	        }
	    }
		
		//로그아웃
	    @PostMapping("/logout")
	    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
	    	System.out.println("로그아웃");
	        Cookie jwtCookie = new Cookie("jwt_token", null); 
	        jwtCookie.setMaxAge(0);
	        jwtCookie.setPath("/");
	        
	        return ResponseEntity.ok("로그아웃 성공");
	    }
	
	//내정보
		//내정보
	    @GetMapping("/userinfo")
	    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
	        String token = request.getHeader("Authorization");

	        if (token!= null && token.startsWith("Bearer")) {
	            token = token.substring(7);

	            String username = JWTUtil.validateAndGetUserId(token); 

	            if (username!= null) { 
	                MemberDTO dto = memberService.getMember(username);
	                return ResponseEntity.ok(dto);
	            } else { 
	                Map<String, String> errorResponse = new HashMap<>();
	                errorResponse.put("message", "토큰이 유효하지 않습니다.");
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	            }
	        } else { 
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("message", "Authorization 헤더 오류.");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	        }
	    }
}