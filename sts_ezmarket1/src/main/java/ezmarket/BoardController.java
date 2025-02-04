package ezmarket;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {
	
	@Autowired
    @Qualifier("boardmapperservice")
    BoardService boardService;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/item/{itemid}")
	public BoardDTO GetItemDetail(@PathVariable("itemid") int product_id) {
		System.out.println("hwre1==>"+product_id);
		BoardDTO dto = boardService.getItemDetail(product_id);
		System.out.println("hwre1==>"+dto);
		return dto;
		
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getallitemsforsearch")
	public ArrayList<BoardDTO> GetAllItems(){
		ArrayList<BoardDTO> dtoList = boardService.getAllItems();
		System.out.println(dtoList);
		return dtoList;
		
	}

	
	
}
