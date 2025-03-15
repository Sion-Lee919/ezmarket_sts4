package ezmarket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reviewmapperservice")
public class ReviewMapperService implements ReviewService {
	
	@Autowired
	ReviewMapper mapper;

	@Override
	public ArrayList<BoardDTO> getReview(int product_id) {
		return mapper.getReview(product_id);
	}

	@Override
	public boolean registerReview(ReviewDTO dto) {
		return mapper.registerReview(dto);
	}

	@Override
	public boolean deleteReview(int review_id) {
		
		return mapper.deleteReview(review_id);
	}

	@Override
	public boolean updateReview(ReviewDTO dto) {
		// TODO Auto-generated method stub
		return mapper.updateReview(dto);
	}
	
	//Member Part
	@Override
	public List<ReviewDTO> getUserReviews(int member_id) {
		List<ReviewDTO> userReviews = mapper.getUserAllReviews(member_id); 
		return userReviews;
	}

}
