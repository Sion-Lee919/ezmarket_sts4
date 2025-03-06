package ezmarket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BoardMapper {

	BoardDTO getItemDetail(int product_id);
	
	boolean viewCount(int product_id);

	ArrayList<BoardDTO> getAllItems();

	ArrayList<BoardDTO> getBrandItems(int brand_id);

	boolean registerItem(BoardDTO dto);
	
	int updateItem(BoardDTO dto);
	
	int deleteItem(int product_id);
	
	List<BoardDTO> getFilteredItems(BoardDTO filterCriteria);
	
	int getFilteredItemsCount(BoardDTO filterCriteria);
	
	ArrayList<BoardDTO> getsearchItems(String searchKeyword);
	
	List<BoardDTO> getBrand(BoardDTO filterCriteria);
	
	int getBrandItemsCount(BoardDTO filterCriteria);
}



