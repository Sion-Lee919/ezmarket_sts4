package ezmarket.like;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LikeMapper {
	
	void like(int member_id, int product_id);
	void unlike(int member_id, int product_id);
	
	List<LikeDTO> getLike(int member_id);

}
