package ezmarket;

import java.util.ArrayList;

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

}
