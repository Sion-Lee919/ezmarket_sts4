package ezmarket;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
	String review_id;
	String product_id;
	String member_id;
	String comments;
	String review_date;
	String update_date;
	String image_url;
	MultipartFile image;
	int rating;
	
	String nickname; //from member
}
