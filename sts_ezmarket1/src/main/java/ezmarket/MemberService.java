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
	 
        //Id, Pw 찾기
        MemberDTO findId(String emailOrPhone);
        MemberDTO findPw(String username, String realname, String email, String phone);
        boolean resetPw(String username, String newPassword);
        
    //내정보
        //회원정보수정
        void modify(String username, String password, String nickname, String address);
        
        //회원 탈퇴
        void resign(String member_status);
        
    //판매자
	  	//판매자 페이지
		BrandDTO getBrand(int member_id);
		
		//판매자 신청
		String sellApplication(MemberDTO dto);
		
		//중복 확인
		boolean isBrandNumberAvailable(String brand_number);
		
		//판매자 승인
		void sellAccept(long brand_id);
		
		//판매자 거절
		void sellRefuse(long brand_id, String brand_refusal_comment);
		
	//판매자
		//판매자 목록 가져오기
		List<MemberDTO> getAllBrands();
}