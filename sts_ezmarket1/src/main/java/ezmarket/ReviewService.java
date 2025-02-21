package ezmarket;

import java.util.ArrayList;

public interface ReviewService {

	ArrayList<BoardDTO> getReview(int product_id);

	boolean registerReview(ReviewDTO dto);

}
