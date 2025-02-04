package ezmarket;

import java.util.ArrayList;

public interface BoardService {

	BoardDTO getItemDetail(int product_id);

	ArrayList<BoardDTO> getAllItems();

}