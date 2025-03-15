package ezmarket;


import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        
    //내정보
        //회원정보수정
        @Override
        public void modify(String username, String password, String nickname, String phone, String address) {
        	mapper.modifyInfo(username, password, nickname, phone, address);
        }
        
        //회원 탈퇴
        @Override
        public void resign(String member_status) {
        	mapper.resignMember(member_status);
        }
        
    //판매자
		//판매자 페이지
		@Override
		public BrandDTO getBrand(int member_id) {
			return mapper.getBrand(member_id);
		}
		
		//판매자 신청
		@Override
		 public String sellApplication(MemberDTO dto) {
	        int result = mapper.sellApplicationSubmit(dto);
	        return result == 1 ? "success" : "fail";
	    }
		
		//중복 확인
		@Override
		public boolean isBrandNumberAvailable(String brand_number) {
			return mapper.checkBrandNumber(brand_number) == 0;
		}
		
		//판매자 정보 수정
		@Override
		public void sellModify(int brand_id, String brand_name, String brandlogo_url) {
			mapper.modifySeller(brand_id, brand_name, brandlogo_url);
		}
		
		
	//관리자
		//유저 정보 가져오기
		@Override
		public List<MemberDTO> getAllUsers() {
			List<MemberDTO> allUsers = mapper.getAllUsersMember(); 
			return allUsers;
		}
		
		//사용자 강퇴
		@Override
		public void kick(long member_id, String member_kick_comment) {
			mapper.kickMember(member_id, member_kick_comment);
		}
		
		//사용자 복구
		@Override
		public void restore(long member_id, String member_kick_comment) {
			mapper.restoreMember(member_id, member_kick_comment);
		}
				
		//판매자 정보 가져오기
		@Override
		public List<MemberDTO> getAllBrands() {
	        List<MemberDTO> allBrands = mapper.getAllBrandsMember(); 
	        return allBrands;
	    }
		
		//판매자 수락
		@Override
		public void sellAccept(long brand_id) {
			mapper.sellApplicationAccept(brand_id);
			mapper.sellApplicationAcceptAuthor(brand_id);
		}
		
		//판매자 거절
		@Override
		public void sellRefuse(long brand_id, String brand_refusal_comment) {
			mapper.sellApplicationRefuse(brand_id, brand_refusal_comment);
			mapper.sellApplicationRefuseAuthor(brand_id, brand_refusal_comment);
		}
		
		//브랜드주소가져오기
		@Override
		public ArrayList<MemberDTO> getBrandAddr() {
			
			return mapper.getBrandAddr();
		}
}