package ezmarket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	        return "redirect:login";
	    }
	    
        //중복 확인 - 아이디 확인
	    @GetMapping("/checkId")
	    @ResponseBody
	    public String checkDuplicatedId(@RequestParam("username") String username) {
	        boolean isAvailable = memberService.isIdAvailable(username);
	        return isAvailable ? "사용 가능한 아이디입니다." : "중복된 아이디입니다."; 
	    }
	    
        //중복 확인 - 닉네임 확인
	    @GetMapping("/checkNickname")
	    @ResponseBody
	    public String checkDuplicatedNickname(@RequestParam("nickname") String nickname) {
	        boolean isAvailable = memberService.isNicknameAvailable(nickname);
	        return isAvailable ? "사용 가능한 닉네임입니다." : "중복된 닉네임입니다."; 
	    }
	    
        //중복 확인 - 이메일 확인
	    @GetMapping("/checkEmail")
	    @ResponseBody
	    public String checkDuplicatedEmail(@RequestParam("email") String email) {
	        boolean isAvailable = memberService.isEmailAvailable(email);
	        return isAvailable ? "사용 가능한 이메일입니다." : "중복된 이메일입니다."; 
	    }
	    
        //중복 확인 - 전화번호 확인
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
	            cookie.setMaxAge(60 * 60); 
	            response.addCookie(cookie);

	            Map<String, String> responseBody = new HashMap<>();
	            responseBody.put("message", "로그인에 성공했습니다."); 
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
	    
        
        //아이디 찾기
        @PostMapping("/findId")
        public ResponseEntity<?> findId(@RequestBody Map<String, String> requestBody) {
            String emailOrPhone = requestBody.get("emailOrPhone");
            MemberDTO dto = memberService.findId(emailOrPhone);

            if (dto != null) {
                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("username", dto.getUsername()); 
                return ResponseEntity.ok(responseBody);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "해당 이메일 또는 전화번호로 가입된 계정이 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        }
        
        //비밀번호 찾기 -> 일단은 정보들 다 확인되면 재설정되게 -> 가능하면 이메일 인증 API 넣기[넣는다면 회원가입도](아이디, 이메일 동일한 경우)
        @PostMapping("/findPw")
        public ResponseEntity<?> findPw(@RequestBody Map<String, String> requestBody) {
            String username = requestBody.get("username");
            String realname = requestBody.get("realname");
            String email = requestBody.get("email");
            String phone = requestBody.get("phone");
            
            MemberDTO dto = memberService.findPw(username, realname, email, phone);

            if (dto != null) {
                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("message", "계정 확인 완료. 비밀번호를 재설정해주세요.");
                return ResponseEntity.ok(responseBody);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "해당 정보로 가입된 계정이 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        }
        
        //비밀번호 재설정
        @PostMapping("/resetPw")
        public ResponseEntity<?> resetPw(@RequestBody Map<String, String> requestBody) {
            String username = requestBody.get("username");
            String newPassword = requestBody.get("newPassword");
            
            boolean isUpdated = memberService.resetPw(username, newPassword);
            
            if (isUpdated) {
                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("message", "비밀번호가 변경되었습니다. 로그인 창으로 이동합니다.");
                return ResponseEntity.ok(responseBody);
            } else { 
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); 
            }
        }
        
	//내정보
	    @GetMapping("/userinfo")
	    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
	        String token = request.getHeader("Authorization");

	        if (token!= null && token.startsWith("Bearer")) {
	            token = token.substring(7);

	            String username = JWTUtil.validateAndGetUserId(token); 
	            System.out.println(username);
	            if (username != null) { 
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

	    //판매자 페이지
		@GetMapping("/brandinfo")
		public BrandDTO getBrandInfo(@RequestParam("memberid") Integer member_id) {
			BrandDTO dto = memberService.getBrand(member_id);
			return dto;
		}
		
		//회원 정보 수정
		@PostMapping("/modify")
		public ResponseEntity<?> modify(@RequestBody MemberDTO memberDTO, HttpServletRequest request) {
			String token = request.getHeader("Authorization");
		    if (token != null && token.startsWith("Bearer")) {
		        token = token.substring(7);
		        String username = JWTUtil.validateAndGetUserId(token);

		        if (username != null && memberDTO.getUsername().equals(username)) {
		            memberService.modify(memberDTO.getUsername(), memberDTO.getPassword(), memberDTO.getNickname(), memberDTO.getAddress());
		            return ResponseEntity.ok("회원 정보가 수정되었습니다.");
		        } else {
		            Map<String, String> errorResponse = new HashMap<>();
		            errorResponse.put("message", "토큰이 유효하지 않거나 권한이 없습니다.");
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		        }
		    } else {
		        Map<String, String> errorResponse = new HashMap<>();
		        errorResponse.put("message", "Authorization 헤더 오류.");
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		    }
		}
}
		