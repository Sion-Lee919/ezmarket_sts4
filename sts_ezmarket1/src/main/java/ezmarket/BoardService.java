package ezmarket;

import java.util.ArrayList;

public interface BoardService {

	BoardDTO getItemDetail(int product_id);

	ArrayList<BoardDTO> getAllItems();

	ArrayList<BoardDTO> getBrandItems(int member_id);

	boolean registerItem(BoardDTO dto);
	
	boolean updateItem(BoardDTO dto);
	
	boolean deleteItem(int product_id);


}

