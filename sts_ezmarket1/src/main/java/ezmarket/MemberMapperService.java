package ezmarket;

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
		
		//판매자 페이지
		@Override
		public BrandDTO getBrand(int member_id) {
			return mapper.getBrand(member_id);
		}

        //Id, Pw 찾기
        @Override
        public MemberDTO findId(String emailOrPhone) {
            return mapper.findIdByEmailOrPhone(emailOrPhone);
        }
        
        @Override
        public MemberDTO findPw(String username, String realname, String email, String phone) {
            return mapper.findPwByUsernameAndRealnameAndEmailAndPhone(username, realname, email, phone);
        }
        
        @Override
        public boolean resetPw(String username, String newPassword) {
            int result = mapper.resetPwOnly(username, newPassword);
            return result > 0;
        }
        
        //회원정보수정
        @Override
        public void modify(String username, String password, String nickname, String address) {
        	mapper.modifyInfo(username, password, nickname, address);
        }
        
        //회원 탈퇴
        @Override
        public void resign(String member_status) {
        	mapper.resignMember(member_status);
        }
}