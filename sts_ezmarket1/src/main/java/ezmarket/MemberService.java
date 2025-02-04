package ezmarket;

import java.util.List;

public interface MemberService {
	
	//회원가입
		//회원가입
		MemberDTO getMember(String username);
		
	    String joinMember(MemberDTO dto) ;
	    
	    //중복 확인
	  	boolean isIdAvailable(String username);
	  	boolean isNicknameAvailable(String nickname);
	  	boolean isEmailAvailable(String email);
	  	boolean isPhoneAvailable(String phone);

//// 수정전
	    String updateMember(MemberDTO dto);
	
		List<MemberDTO> getAllMembers();

	

	void deleteMember(String username);
}
