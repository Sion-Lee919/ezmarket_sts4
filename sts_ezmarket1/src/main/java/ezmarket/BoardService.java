package ezmarket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface BoardService {

	BoardDTO getItemDetail(int product_id);
	
	boolean viewCount(int product_id);

	ArrayList<BoardDTO> getAllItems();

	ArrayList<BoardDTO> getBrandItems(int member_id);

	boolean registerItem(BoardDTO dto);
	
	boolean updateItem(BoardDTO dto);
	
	boolean deleteItem(int product_id);
	
	List<BoardDTO> getFilteredItems(BoardDTO filterCriteria);
	
	int getFilteredItemsCount(BoardDTO filterCriteria);

	ArrayList<BoardDTO> getsearchItems(String searchKeyword);
	
	List<BoardDTO> getBrand(BoardDTO filterCriteria);
	
	int getBrandItemsCount(BoardDTO filterCriteria);
}

