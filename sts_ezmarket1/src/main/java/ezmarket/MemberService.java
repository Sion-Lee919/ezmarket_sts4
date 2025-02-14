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

	  	//판매자 페이지
		BrandDTO getBrand(int member_id);
	  	
        //Id, Pw 찾기
        MemberDTO findId(String emailOrPhone);
        MemberDTO findPw(String username, String realname, String email, String phone);
        boolean resetPw(String username, String newPassword);
        
        //회원정보수정
        void modify(String username, String password, String nickname, String address);
        
        //회원 탈퇴
        void resign(String member_status);
}