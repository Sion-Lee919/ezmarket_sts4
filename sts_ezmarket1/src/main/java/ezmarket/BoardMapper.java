package ezmarket;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BoardMapper {

	BoardDTO getItemDetail(int product_id);

}
