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
		
//// 수정전	
	int updateMember(MemberDTO dto);

	void deleteMember(String username);

	List<MemberDTO> getAllMembers();
}
