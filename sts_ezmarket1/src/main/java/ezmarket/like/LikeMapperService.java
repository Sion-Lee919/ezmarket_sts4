package ezmarket.like;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("likemapperservice")
public class LikeMapperService implements LikeService {
	
	@Autowired
	LikeMapper mapper;
	
	@Override
    public void like(int member_id, int product_id) {
    	mapper.like(member_id, product_id);
    }
	
	@Override
    public void unlike(int member_id, int product_id) {
    	mapper.unlike(member_id, product_id);
    }
	
	@Override
	public List<LikeDTO> getLike(int member_id) {
		List<LikeDTO> getLike = mapper.getLike(member_id); 
		return getLike;
	}
}
