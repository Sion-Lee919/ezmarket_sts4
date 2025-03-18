package ezmarket.like;

import java.util.List;

public interface LikeService {
	
	//찜 추가, 제거
	void like(int member_id, int product_id);
	void unlike(int member_id, int product_id);
	
	//찜 목록 가져오기
	List<LikeDTO> getLike(int member_id);
}
