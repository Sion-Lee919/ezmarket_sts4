package ezmarket;

import java.util.ArrayList;
import java.util.List;

public interface ReviewService {

	ArrayList<BoardDTO> getReview(int product_id);

	boolean registerReview(ReviewDTO dto);

	boolean deleteReview(int review_id);

	boolean updateReview(ReviewDTO dto);

	//Member Part
	List<ReviewDTO> getUserReviews(int member_id);
}
