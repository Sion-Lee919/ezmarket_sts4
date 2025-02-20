package ezmarket;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ReviewMapper {

	ArrayList<BoardDTO> getReview(int product_id);

	boolean registerReview(ReviewDTO dto);

}
