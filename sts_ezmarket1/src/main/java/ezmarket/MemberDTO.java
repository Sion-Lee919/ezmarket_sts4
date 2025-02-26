package ezmarket;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
	int member_id;
	String username;
	String realname;
	String nickname;
	String password;
	String email;
	String phone;
	String address;
	String join_date;
	String update_date;
	Integer userauthor;
	Long points;
	Long ezpay;
	String member_status;
	String member_kick_comment;
	Integer social;
	String kick_date;
	
	//판매자
	int brand_id;
	String brandname;
	String brand_number;
	String brandlogo_url;
	String brandlicense_url;
	String brand_status;
	String brand_refusal_comment;
	String brand_join_date;
	String brand_update_date;
}
