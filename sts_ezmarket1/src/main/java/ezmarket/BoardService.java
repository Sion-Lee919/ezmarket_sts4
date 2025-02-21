package ezmarket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface BoardService {

	BoardDTO getItemDetail(int product_id);

	ArrayList<BoardDTO> getAllItems();

	ArrayList<BoardDTO> getBrandItems(int member_id);

	boolean registerItem(BoardDTO dto);
	
	boolean updateItem(BoardDTO dto);
	
	boolean deleteItem(int product_id);
	
	List<BoardDTO> getFilteredItems(FilterRequestDTO filters);

	int getFilteredItemsCount(FilterRequestDTO filters);


}

