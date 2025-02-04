package ezmarket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("membermapperservice")
public class MemberMapperService implements MemberService {

	@Autowired
	MemberMapper mapper;

	@Override
	public MemberDTO getMember(String username) {
		return mapper.getMember(username);
	}
	//회원가입
		//회원가입
		@Override
		@Transactional
	    public String joinMember(MemberDTO dto) {
	        int result = mapper.insertMember(dto);
	        return result == 1 ? "success" : "fail";
	    }
		
		//중복 확인
		@Override
		public boolean isIdAvailable(String username) {
			return mapper.checkId(username) == 0;
		}
		@Override
		public boolean isNicknameAvailable(String nickname) {
			return mapper.checkNickname(nickname) == 0;
		}
		@Override
		public boolean isEmailAvailable(String email) {
			return mapper.checkEmail(email) == 0;
		}
		@Override
		public boolean isPhoneAvailable(String phone) {
			return mapper.checkPhone(phone) == 0;
		}
	
	@Override
	@Transactional
	public String updateMember(MemberDTO dto) {
		mapper.updateMember(dto);
		return "success";
	}

//// 수정전	
	
	//계정 삭제
	@Override
	public void deleteMember(String username) {
		mapper.deleteMember(username);
	}
	
	//관리자 - 회원 관리
	@Override
	public List<MemberDTO> getAllMembers() {
		return mapper.getAllMembers();
	}
	
}
