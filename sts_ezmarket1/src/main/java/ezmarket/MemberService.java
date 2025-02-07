package ezmarket;

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

		BrandDTO getBrand(int member_id);
}