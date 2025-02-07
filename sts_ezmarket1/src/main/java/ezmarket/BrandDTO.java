package ezmarket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDTO {
	int brand_id;
    int member_id;
    String brandname;
    String brandlogo_url;
    String brand_number;
    String brandlicense_url;
    String brand_status;
    String brand_refusal_comment;
    String brand_join_date;
    String brand_update_date;
	
	MemberDTO memberdto;
}
