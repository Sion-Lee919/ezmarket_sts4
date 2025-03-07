package ezmarket;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MemberMapper {

	MemberDTO getMember(String username);
	
	//회원가입
		//회원가입
		int insertMember(MemberDTO dto);
		
		//중복확인
		int checkId(String username);
		int checkNickname(String nickname);
		int checkEmail(String email);
		int checkPhone(String phone);
	
        //Id, Pw 찾기
        MemberDTO findIdByEmailOrPhone(String emailOrphone);
        MemberDTO findPwByUsernameAndRealnameAndEmailAndPhone(String username, String realname, String email, String phone);
        int resetPwOnly(String username, String newPassword);
        
    //내정보
        //회원정보수정
        void modifyInfo(String username, String password, String nickname, String address);
        
        //회원 탈퇴
        void resignMember(String member_status);
        
    //판매자
        //판매자 페이지
		BrandDTO getBrand(int member_id);
		
        //판매자 신청
        int sellApplicationSubmit(MemberDTO dto);
        
        //중복확인
        int checkBrandNumber(String brand_number);
        
        //판매자 정보 수정
        void modifySeller(int brand_id, String brandname, String brandlogo_url);
        
        
    //관리자
        //유저 목록 가져오기
        List<MemberDTO> getAllUsersMember();
        
        //사용자 강퇴
        void kickMember(long member_id, String member_kick_comment);
        
        //사용자 복구
        void restoreMember(long member_id, String member_kick_comment);
        
        //판매자 목록 가져오기
        List<MemberDTO> getAllBrandsMember();
        
        //판매자 승인
        void sellApplicationAccept(long brand_id);
        void sellApplicationAcceptAuthor(long brand_id);
        
        //판매자 거절
        void sellApplicationRefuse(long brand_id, String brand_refusal_comment);
        void sellApplicationRefuseAuthor(long brand_id, String brand_refusal_comment);
        
        //판매자 주소 가져오기
		ArrayList<MemberDTO> getBrandAddr();
}