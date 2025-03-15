package ezmarket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;


@RestController
public class BoardController {
	
	@Autowired
    @Qualifier("boardmapperservice")
    BoardService boardService;
	
	@GetMapping("/item/{itemid}")
	public BoardDTO GetItemDetail(@PathVariable("itemid") int product_id) {
		boardService.viewCount(product_id);
		BoardDTO dto = boardService.getItemDetail(product_id);
		return dto;
		
	}
	
	@GetMapping("/getallitemsforsearch")
	public ArrayList<BoardDTO> GetAllItems(){
		ArrayList<BoardDTO> dtoList = boardService.getAllItems();
		return dtoList;
	}

	
	@GetMapping("/getbranditems/{brandid}")
	public ArrayList<BoardDTO> getBrandItems(@PathVariable("brandid") int brand_id) {
		ArrayList<BoardDTO> dtoList = boardService.getBrandItems(brand_id);
		return dtoList;
		
	}
	
	@PostMapping(value="/brand/id/registeritem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Boolean> registerItem(@ModelAttribute BoardDTO dto) throws IOException{
				
		String savePath = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
        	savePath = "c:/ezwel/ezmarketupload/";	
        } else if (osName.contains("Mac")){
        	savePath = "/Users/minsu/Documents/ezwel/Desktop/downloaded_images/";
        } else { // linux
        	savePath = "/home/" + System.getProperty("user.name") + "/mydir/ezmarketupload/";
        }
        
		String newfilename1 = null;
		MultipartFile file1 = dto.getImage();
		if(!file1.isEmpty()) {
			String originalfilename1 = file1.getOriginalFilename();
			String before1 = originalfilename1.substring(0, originalfilename1.indexOf("."));
			String ext1 = originalfilename1.substring(originalfilename1.indexOf("."));
			newfilename1 = before1 + "(" + UUID.randomUUID() + ")" + ext1;
			//서버내부 지정경로에 파일내용 저장
			file1.transferTo( new java.io.File(savePath +  newfilename1));
			dto.setImage_url(newfilename1);
		}
		boolean result = boardService.registerItem(dto);
	    return result ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}

	
    @GetMapping("/showimage")
    public void showImg(String filename, String obj, HttpServletResponse response) throws IOException {
		//String obj = "product";
        String osName = System.getProperty("os.name").toLowerCase();
        String path = "";
        
        
        // 운영체제에 맞는 파일 경로 설정
        if (osName.contains("win")) {
        	if (obj.equals("product")) { path = "c:/ezwel/ezmarketupload/";}
        	else if (obj.equals("review")) { path = "c:/ezwel/ezmarketupload/reviewimage/";}
        	else if (obj.equals("brand")) { path = "c:/ezwel/ezmarketupload/brandlogo/";}
        }  else if (osName.contains("Mac")){
            if (obj.equals("product")) { path = "/Users/minsu/Documents/ezwel/Desktop/downloaded_images/";}
        	else if (obj.equals("review")) { path = "/Users/minsu/Documents/ezwel/Desktop/downloaded_images/reviewimage/";}
        	else if (obj.equals("brand")) { path = "/Users/minsu/Documents/ezwel/Desktop/downloaded_images/brandlogo/";}
        } else { // linux
            if (obj.equals("product")) { path = "/home/" + System.getProperty("user.name") + "/mydir/ezmarketupload/";}
        	else if (obj.equals("review")) { path = "/home/" + System.getProperty("user.name") + "/mydir/ezmarketupload/reviewimage/";}
        	else if (obj.equals("brand")) { path = "/home/" + System.getProperty("user.name") + "/mydir/ezmarketupload/brandlogo/";}
        }
        
        // 파일을 열기
        File file = new File(path + filename);
        FileInputStream fin = new FileInputStream(file);
        
        // 파일 이름 인코딩 처리
        filename = new String(filename.getBytes("utf-8"), "iso-8859-1");

        // 파일의 MIME 타입을 설정 (이미지일 경우)
        String guessedType = URLConnection.guessContentTypeFromName(filename);
        if (guessedType == null) {
        	guessedType = "application/octet-stream"; // MIME 타입이 없다면 기본 값 설정
        }
        // HTTP 응답 헤더 설정
        response.setContentType(guessedType);
        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        
        // 파일 데이터를 클라이언트에게 전송
        ServletOutputStream out = response.getOutputStream();
        FileCopyUtils.copy(fin, out);
        
        // 스트림 닫기
        fin.close();
        out.close();
    }
    


	@PostMapping(value="/brand/id/updateitem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Boolean> updateItem(@ModelAttribute BoardDTO dto) throws IOException{
		
		 String savePath = "";
		    String osName = System.getProperty("os.name").toLowerCase();
		    if (osName.contains("win")) {
		        savePath = "c:/ezwel/ezmarketupload/";
		    } else if (osName.contains("Mac")){
		        savePath = "/Users/minsu/Documents/ezwel/ezmarketupload/";
		    } else { // linux
		        savePath = "/home/" + System.getProperty("user.name") + "/mydir/ezmarketupload/";
		    }
		    
		    BoardDTO existingItem = boardService.getItemDetail(dto.getProduct_id());
		    if (existingItem == null) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
		    }
		    
		    String newFileName = null;
		    MultipartFile file = dto.getImage();
		    
		    if (file != null && !file.isEmpty()) {
		        // 기존 이미지 삭제
		        if (existingItem.getImage_url() != null) {
		            java.io.File oldFile = new java.io.File(savePath + existingItem.getImage_url());
		            if (oldFile.exists()) {
		                oldFile.delete();
		            }
		        }
		    
		        String originalFileName = file.getOriginalFilename();
		        String before = originalFileName.substring(0, originalFileName.indexOf("."));
		        String ext = originalFileName.substring(originalFileName.indexOf("."));
		        newFileName = before + "(" + UUID.randomUUID() + ")" + ext;
		        file.transferTo(new java.io.File(savePath + newFileName));
		        dto.setImage_url(newFileName);  // 새로운 이미지 URL 업데이트
		    } else {
		        // 이미지 변경이 없으면 기존 이미지 URL 유지
		        dto.setImage_url(existingItem.getImage_url());
		    }
		    boolean result = boardService.updateItem(dto);
		    return result ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}
	
	@DeleteMapping("/brand/{brandid}/delete/{productid}")
	public ResponseEntity<Boolean> deleteItem(@PathVariable("brandid") int brand_id, @PathVariable("productid") int product_id) {
	    boolean result = boardService.deleteItem(product_id);
	    return result ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}
	
	//필터
	@GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getFilteredItems(
	    @RequestParam(required = false) String searchKeyword,
	    @RequestParam(required = false) String bigcategory,
	    @RequestParam(required = false) List<String> subcategories,
	    @RequestParam(required = false) List<String> regions,
	    @RequestParam(required = false) Boolean newProduct,
	    @RequestParam(defaultValue = "latest") String sortType,
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(defaultValue = "12") int limit,
	    @RequestParam(required = false) List<Integer> sweetnesss,
	    @RequestParam(required = false) List<Integer> sournesss,
	    @RequestParam(required = false) List<Integer> carbonations,
	    @RequestParam(required = false) List<Integer> bodys
	) {
	    int offset = (page - 1) * limit;
	        
	    BoardDTO filterCriteria = new BoardDTO();
	    filterCriteria.setSearchKeyword(searchKeyword);
	    filterCriteria.setBigcategory(bigcategory);
	    filterCriteria.setSubcategories(subcategories);
	    filterCriteria.setRegions(regions);
	    filterCriteria.setNewProduct(newProduct);
	    filterCriteria.setSortType(sortType);
	    
	    filterCriteria.setOffset(offset);
	    filterCriteria.setLimit(limit);
	    
	    filterCriteria.setSweetnesss(sweetnesss);
	    filterCriteria.setSournesss(sournesss);
	    filterCriteria.setCarbonations(carbonations);
	    filterCriteria.setBodys(bodys);

	    List<BoardDTO> items = boardService.getFilteredItems(filterCriteria);
	    int totalCount = boardService.getFilteredItemsCount(filterCriteria);
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("items", items);
	    response.put("totalCount", totalCount);
	    response.put("currentPage", page);
	    response.put("totalPages", (int) Math.ceil((double) totalCount / limit));

	    return response;
	}



	@GetMapping("/getitemsforrandom")
	public Map<String, Object> GetRandomItems(){
		Map<String, List<BoardDTO>> items = boardService.getItemsByType();
        return new HashMap<>(items);
	}



	@GetMapping(value = "/searchitems", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<BoardDTO> getsearchItems(@RequestParam String searchKeyword){
		return boardService.getsearchItems(searchKeyword);
	}
	
	@GetMapping(value = "/brandItems", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getBrandFilteredItems(
	    @RequestParam int brand_id,
	    @RequestParam(required = false) String searchKeyword,
	    @RequestParam(required = false) List<String> subcategories,
	    @RequestParam(required = false) List<String> regions,
	    @RequestParam(required = false) Boolean newProduct,
	    @RequestParam(defaultValue = "latest") String sortType,
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(defaultValue = "12") int limit
	) {
	    int offset = (page - 1) * limit;

	    BoardDTO filterCriteria = new BoardDTO();
	    filterCriteria.setBrand_id(brand_id);
	    filterCriteria.setSearchKeyword(searchKeyword);
	    filterCriteria.setSubcategories(subcategories);
	    filterCriteria.setRegions(regions);
	    filterCriteria.setNewProduct(newProduct);
	    filterCriteria.setSortType(sortType);
	    
	    filterCriteria.setOffset(offset);
	    filterCriteria.setLimit(limit);

	    List<BoardDTO> items = boardService.getBrand(filterCriteria);
	    int totalCount = boardService.getBrandItemsCount(filterCriteria);
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("items", items);
	    response.put("totalCount", totalCount);
	    response.put("currentPage", page);
	    response.put("totalPages", (int) Math.ceil((double) totalCount / limit));

	    return response;
	}

	
	
}



