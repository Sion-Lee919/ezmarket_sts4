package ezmarket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	@Override
	public boolean updateItem(BoardDTO dto) {
		// TODO Auto-generated method stub
		return mapper.updateItem(dto) > 0;
	}

	@Override
	public boolean deleteItem(int product_id) {
		// TODO Auto-generated method stub
		return mapper.deleteItem(product_id) > 0;
	}
	
	@Override
	public List<BoardDTO> getFilteredItems(BoardDTO filterCriteria) {
	    return mapper.getFilteredItems(filterCriteria);
	}

	@Override
	public int getFilteredItemsCount(BoardDTO filterCriteria) {
	    return mapper.getFilteredItemsCount(filterCriteria);
	}

	@Override
	public ArrayList<BoardDTO> getsearchItems(String searchKeyword) {
		return mapper.getsearchItems(searchKeyword);
	}

	@Override
	public List<BoardDTO> getBrand(BoardDTO filterCriteria) {
		return mapper.getBrand(filterCriteria);
	}

	@Override
	public int getBrandItemsCount(BoardDTO filterCriteria) {
		return mapper.getBrandItemsCount(filterCriteria);
	}







}

