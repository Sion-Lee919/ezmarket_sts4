package ezmarket;

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

		BrandDTO getBrand(int member_id);

		
        //Id, Pw 찾기
        MemberDTO findIdByEmailOrPhone(String emailOrphone);
        MemberDTO findPwByUsernameAndRealnameAndEmailAndPhone(String username, String realname, String email, String phone);
        int resetPwOnly(String username, String newPassword);
        
        //회원정보수정
        void modifyInfo(String username, String password, String nickname, String address);
}