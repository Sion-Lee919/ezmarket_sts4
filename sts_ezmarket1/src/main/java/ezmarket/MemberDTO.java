package ezmarket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
	Long member_id;
	String username;
	String realname;
	String nickname;
	String password;
	String email;
	String phone;
	String address;
	String join_date;
	String update_date;
	String userauthor;
	String points;
	String member_status;
	String member_kick_comment;
	
	//판매자
	Integer brand_id;
	Long brandname;
	String brandlogo_url;
	String brand_number;
	String brandlicense_url;
	String brand_status;
	String brand_refusal_comment;
	String brand_join_date;
	String brand_update_date;
}
