package ezmarket;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ReviewMapper {

	ArrayList<BoardDTO> getReview(int product_id);

	boolean registerReview(ReviewDTO dto);

	boolean deleteReview(int review_id);

	boolean updateReview(ReviewDTO dto);

	//Member Part
    List<ReviewDTO> getUserAllReviews();
}
