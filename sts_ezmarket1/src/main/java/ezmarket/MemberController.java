package ezmarket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

	@Autowired
	@Qualifier("membermapperservice")
	MemberService memberService;
	
	//회원가입
		//회원가입
	    @GetMapping("/joinN")
	    public String showJoinrForm() {
	        return "joinN"; 
	    }
	
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
	    @GetMapping("/login")
	    public ResponseEntity<String> login(HttpServletRequest request) {
	        HttpSession session = request.getSession(false);
	        
	        if (session != null && session.getAttribute("sessionid") != null) {
	            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/my").build();
	        } else {
	            return ResponseEntity.ok("");
	        }
	    }
		
	    @PostMapping("/login")
	    public ResponseEntity<String> loginform(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
	        MemberDTO dto = memberService.getMember(username);
	        
	        if (dto != null && dto.getPassword().equals(password)) {
	            HttpSession session = request.getSession();
	            session.setAttribute("sessionid", username);
	            return ResponseEntity.ok("로그인 성공"); 
	        } else if (dto == null) {
	        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디가 틀렸습니다.");
	        } else if (dto != null && !dto.getPassword().equals(password)){
	        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다."); 
	        }
	        else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패."); 
	        }
	    }
		
		//로그아웃
		@GetMapping("/logout")
		public String logout(HttpServletRequest request) {
			HttpSession session = request.getSession();
			session.removeAttribute("sessionid");
			return "redirect:/login";
		}
	
	//내정보
		//내정보
		@GetMapping("/userinfo")
		public ResponseEntity<MemberDTO> getUserInfo(HttpServletRequest request) {
		    HttpSession session = request.getSession(false);
		    if (session != null) {
		        String username = (String) session.getAttribute("sessionid"); 
		        MemberDTO dto = memberService.getMember(username);
		        return ResponseEntity.ok(dto);
		    }
		    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}

		
/////수정전
		//내정보
		@GetMapping("/mypage")
		public ModelAndView myPageForm(HttpServletRequest request) {
		    ModelAndView mv = new ModelAndView();
		    
		    HttpSession session = request.getSession();
		    String sessionid = (String) session.getAttribute("sessionid");
		    
		    if (sessionid == null) {
		        mv.setViewName("redirect:/login");
		        return mv;
		    }
		    
		    MemberDTO member = memberService.getMember(sessionid);
		    
		    mv.addObject("sessionid", sessionid);
		    mv.addObject("member", member);
		    
		    mv.setViewName("/feed/you");
		    
		    return mv;
		}
		
		//회원정보수정
		@GetMapping("/modify")
		public ModelAndView modifyForm(HttpServletRequest request) {
		    ModelAndView mv = new ModelAndView();
		    
		    HttpSession session = request.getSession();
		    String sessionid = (String) session.getAttribute("sessionid");
		    Boolean pwChecked = (Boolean) session.getAttribute("pwChecked");
		    
		    if (sessionid == null) {
		        mv.setViewName("redirect:/login");
		        return mv;
		    }
		    
		    if (pwChecked == null || !pwChecked) {
		        mv.setViewName("redirect:/mypage");
		        return mv;
		    }
		    
		    MemberDTO member = memberService.getMember(sessionid);
		    mv.addObject("member", member);
		    mv.setViewName("modify");
		    
		    return mv;
		}
		
	    // 비밀번호 재확인 처리
		@GetMapping("/modify/pwcheck")
		public String showPwCheck() {
		    return "pwcheck";  // 비밀번호 확인 화면 jsp 또는 템플릿 반환
		}
		
		@PostMapping("/modify/pwcheck")
		public ModelAndView checkPw(String password, HttpServletRequest request) {
		    ModelAndView mv = new ModelAndView();
		    
		    HttpSession session = request.getSession();
		    String sessionid = (String) session.getAttribute("sessionid");
	
		    if (sessionid == null) {
		        mv.setViewName("redirect:/login");
		        return mv;
		    }
		    
		    System.out.println(sessionid);
		    MemberDTO member = memberService.getMember(sessionid);
		    
		    if (member != null && member.getPassword().equals(password)) {
		    	mv.addObject("member",member);
		        session.setAttribute("pwchecked", true);
		        mv.setViewName("modify");
		    } else {
		        mv.setViewName("redirect:/modify/pwcheck");
		    }
	
		    return mv;
		}
		
		//계정 삭제
		@RequestMapping(value = "/deleteMember", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView deleteMember(@RequestParam("username") String username, HttpServletRequest request) {
		    ModelAndView mv = new ModelAndView();
		    String referer = request.getHeader("Referer");
		    
		    memberService.deleteMember(username);
		    
		    HttpSession session = request.getSession();
		    session.removeAttribute("username");
		    
		    if (referer != null && referer.contains("localhost:9090/admin")) {
		        mv.setViewName("redirect:" + referer);
		    } else {
		    	session.removeAttribute("sessionid");
		        mv.setViewName("redirect:/");
		    }
		    
		    return mv;
		}
	
	//관리자
		//관리자 - 계정 삭제
		@GetMapping("/admin/deleteMember")
		public ModelAndView deleteMember(@RequestParam("username") String username) {
		    ModelAndView mv = new ModelAndView();
		    memberService.deleteMember(username);
		    return mv;
		}
		
		@PostMapping("/modify")
		public ModelAndView modify(HttpServletRequest request, String username, String password, MemberDTO updateMember) {
			ModelAndView mv = new ModelAndView();
			MemberDTO dto = memberService.getMember(username);
	
				dto.setPassword(updateMember.getPassword());
				dto.setPhone(updateMember.getPhone());
				
				memberService.updateMember(dto);
				
				mv.addObject("member", dto);
			
			return mv;
		}
	
		//관리자 - 회원관리
		@GetMapping("/admin")
		public ModelAndView adminpage(HttpServletRequest request) {
			ModelAndView mv = new ModelAndView();
			HttpSession session = request.getSession(false); 
			    
			if (session != null) {
			    String sessionid = (String) session.getAttribute("sessionid");
			    mv.addObject("sessionid", sessionid);
			}
	
			List<MemberDTO> allMembers = memberService.getAllMembers();   
			mv.addObject("allMembers", allMembers);
			    
			mv.setViewName("/admin/adminpage"); 
			    
			return mv;
	}
}