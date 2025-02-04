package ezmarket;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BoardMapper {

	BoardDTO getItemDetail(int product_id);

	ArrayList<BoardDTO> getAllItems();

}
