package ezmarket;


import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    	
	    	String hashedPassword = passwordEncoder.encode(dto.getPassword());
	    	dto.setPassword(hashedPassword);
	    	
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
	        
	        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        

	        //if (dto != null && passwordEncoder.matches(memberDTO.getPassword(), dto.getPassword())) {
	        if (dto != null && dto.getPassword().equals(memberDTO.getPassword())) {

	        	
	        	//회원 탈퇴 -> 로그인 불가
	        	if(!"정상".equals(dto.getMember_status())) {
	        		Map<String, String> errorResponse = new HashMap<>();
	                errorResponse.put("message", "로그인 할 수 없는 계정입니다. \n사유: " + dto.getMember_kick_comment());
	                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	        	}
	        	
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
        //내정보
	    @GetMapping("/userinfo")
	    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
	        String token = request.getHeader("Authorization");

	        if (token!= null && token.startsWith("Bearer")) {
	            token = token.substring(7);

	            String username = JWTUtil.validateAndGetUserId(token); 
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
		
		//회원 탈퇴 요청
		@PostMapping("/resign")
		public ResponseEntity<String> resign(@RequestParam("username") String username) {
			memberService.resign(username);
	        return ResponseEntity.ok("탈퇴가 완료되었습니다. 정보 유지기 기간은 1년이며, 회원 탈퇴 취소를 희망한다면 관리자에게 문의해주세요.");
	    }
		
	//판매자
	    //판매자 페이지
		@GetMapping("/brandinfo")
		public BrandDTO getBrandInfo(@RequestParam("memberid") Integer member_id) {
			BrandDTO dto = memberService.getBrand(member_id);
			return dto;
		}
		
		// 판매자 주소 가져오기
		@GetMapping("/brandaddress")
		public ArrayList<MemberDTO> getBrandAddr() {
			ArrayList<MemberDTO> addrList = memberService.getBrandAddr();
			return addrList;
		}
		
		//판매자 신청
		@PostMapping("/sellApplication")
		public ResponseEntity<String> sellApplication(
	            @RequestParam("brand_id") Integer brand_id,
	            @RequestParam("member_id") Integer member_id,
	            @RequestParam("brandname") String brandname,
	            @RequestParam("brand_number") String brand_number,
	            @RequestParam("brandlogo_url") MultipartFile brandLogoFile,
	            @RequestParam("brandlicense_url") MultipartFile brandLicenseFile) {

	        try {
	        	//aws 배포후 경로 재설정
	            String brandLogoFileName = brand_id + "_" + brand_number + "_" + brandLogoFile.getOriginalFilename();
	            String brandlogo_url = "C:/ezwel/ezmarketupload/brand/" + "brandlogo/" + brandLogoFileName;
	            saveFile(brandLogoFile, brandlogo_url);
	            
	            //aws 배포후 경로 재설정
	            String brandLicenseFileName = brand_id + "_" + brand_number + "_" + brandLicenseFile.getOriginalFilename();
	            String brandlicense_url = "C:/ezwel/ezmarketupload/brand/" + "brandlicense/" + brandLicenseFileName;
	            saveFile(brandLicenseFile, brandlicense_url);
	            
	            MemberDTO dto = new MemberDTO();
	            dto.setBrand_id(brand_id);
	            dto.setMember_id(member_id);
	            dto.setBrandname(brandname);
	            dto.setBrand_number(brand_number);
	            dto.setBrandlogo_url(brandlogo_url); 
	            dto.setBrandlicense_url(brandlicense_url);
	            
	            memberService.sellApplication(dto);

	            return ResponseEntity.ok("판매자 신청 완료.");
	        } catch (IOException e) {
	            return ResponseEntity.status(500).body("파일 업로드 실패.");
	        }
	    }
		    // 파일 저장
		    private void saveFile(MultipartFile file, String path) throws IOException {
		        File destinationFile = new File(path);
		        destinationFile.getParentFile().mkdirs();
		        file.transferTo(destinationFile);
		    }
		
		//중복 확인 - 사업자번호
	    @GetMapping("/checkBrandNumber")
	    @ResponseBody
	    public String checkBrandNumber(@RequestParam("brand_number") String brand_number) {
	        boolean isAvailable = memberService.isBrandNumberAvailable(brand_number);
	        return isAvailable ? "사용 가능한 사업자 번호입니다." : "중복된 사업자 번호입니다."; 
	    }
	    
	    //판매자 정보 수정
	    @PostMapping("/sellModify")
	    public ResponseEntity<?> sellModify(
	            @RequestParam("brandname") String brandname,
	            @RequestParam("brand_id") Integer brand_id, 
	            @RequestParam("brand_number") String brand_number,
	            @RequestParam(value = "brandlogo_url", required = false) MultipartFile brandLogoFile,
	            @RequestParam(value = "existing_brandlogo_url", required = false) String existBrandLogoUrl,
	            HttpServletRequest request) {

	        String token = request.getHeader("Authorization");
	        if (token != null && token.startsWith("Bearer")) {
	            token = token.substring(7);
	            String username = JWTUtil.validateAndGetUserId(token);

	            if (username != null) {
	            	String brandlogo_url = null;
	                if (brandLogoFile != null && !brandLogoFile.isEmpty()) {
	                    String brandLogoFileName = brand_id + "_" + brand_number + "_" + brandLogoFile.getOriginalFilename();
	                    brandlogo_url = "C:/ezwel/ezmarketupload/brand/brandlogo/" + brandLogoFileName;
	                    try {
	                        saveFile(brandLogoFile, brandlogo_url);
	                    } catch (IOException e) {
	                        return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
	                    }
	                } else {
	                	brandlogo_url = existBrandLogoUrl;
	                }

	                memberService.sellModify(brand_id, brandname, brandlogo_url);
	                return ResponseEntity.ok("판매자 정보가 수정되었습니다.");
	            } else {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않거나 권한이 없습니다.");
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization 헤더 오류.");
	        }
	    }
		
	//관리자
		//유저 정보 가져오기
	    @GetMapping("/getAllUsers")
	    public ResponseEntity<List<MemberDTO>> getAllUsers() {
	        List<MemberDTO> allUsers = memberService.getAllUsers();
	        return ResponseEntity.ok(allUsers);
	    }
	    
	    //사용자 강퇴
	    @PostMapping("/kick")
		public ResponseEntity<String> kick(@RequestParam("member_id") long member_id, @RequestParam("member_kick_comment") String member_kick_comment) {
			memberService.kick(member_id, member_kick_comment);
	        return ResponseEntity.ok("사용자 강퇴 완료.");
	    }
	    
	    //사용자 복구
	    @PostMapping("/restore")
		public ResponseEntity<String> restore(@RequestParam("member_id") long member_id, @RequestParam("member_kick_comment") String member_kick_comment) {
			memberService.restore(member_id, member_kick_comment);
	        return ResponseEntity.ok("사용자 복구 완료.");
	    }
	    
		//판매자 정보 가져오기
	    @GetMapping("/getAllBrands")
	    public ResponseEntity<List<MemberDTO>> getAllBrands() {
	        List<MemberDTO> allBrands = memberService.getAllBrands();
	        return ResponseEntity.ok(allBrands);
	    }
	    
		//판매자 신청 수락
		@PostMapping("/sellAccept")
		public ResponseEntity<String> sellAccept(@RequestParam("brand_id") long brand_id) {
			memberService.sellAccept(brand_id);
	        return ResponseEntity.ok("판매자 신청 승인.");
	    }
		
		//판매자 신청 거절
		@PostMapping("/sellRefuse")
		public ResponseEntity<String> sellRefuse(@RequestParam("brand_id") long brand_id, @RequestParam("brand_refusal_comment") String brand_refusal_comment) {
			memberService.sellRefuse(brand_id, brand_refusal_comment);
			return ResponseEntity.ok("판매자 신청 거절.");
		}
}
		