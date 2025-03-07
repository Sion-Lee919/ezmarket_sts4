package ezmarket;


import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
		
		//판매자 정보 수정
		void sellModify(int brand_id, String brand_name, String brandlogo_url);
		
	//관리자
		//유저 목록 가져오기
		List<MemberDTO> getAllUsers();
		
		//사용자 강퇴
		void kick(long member_id, String member_kick_comment);
		
		//사용자 복구
		void restore(long member_id, String member_kick_comment);
				
		//판매자 목록 가져오기
		List<MemberDTO> getAllBrands();
		
		//판매자 승인
		void sellAccept(long brand_id);
		
		//판매자 거절
		void sellRefuse(long brand_id, String brand_refusal_comment);

		ArrayList<MemberDTO> getBrandAddr();
}