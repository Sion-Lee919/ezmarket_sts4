package ezmarket;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("boardmapperservice")
public class BoardMapperService implements BoardService {

	@Autowired
	BoardMapper mapper;
	
	@Override
	public BoardDTO getItemDetail(int product_id) {
		return mapper.getItemDetail(product_id);
	}

	@Override
	public ArrayList<BoardDTO> getAllItems() {
		return mapper.getAllItems();
	}

	@Override
	public ArrayList<BoardDTO> getBrandItems(int brand_id) {
		// TODO Auto-generated method stub
		return mapper.getBrandItems(brand_id);
	}

	@Override
	public boolean registerItem(BoardDTO dto) {
		// TODO Auto-generated method stub
		return mapper.registerItem(dto);
	}


}

