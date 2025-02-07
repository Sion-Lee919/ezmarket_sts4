package ezmarket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;


@RestController
public class BoardController {
	
	@Autowired
    @Qualifier("boardmapperservice")
    BoardService boardService;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/item/{itemid}")
	public BoardDTO GetItemDetail(@PathVariable("itemid") int product_id) {
		BoardDTO dto = boardService.getItemDetail(product_id);
		return dto;
		
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getallitemsforsearch")
	public ArrayList<BoardDTO> GetAllItems(){
		ArrayList<BoardDTO> dtoList = boardService.getAllItems();
		return dtoList;
	}

	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getbranditems/{brandid}")
	public ArrayList<BoardDTO> getBrandItems(@PathVariable("brandid") int brand_id) {
		ArrayList<BoardDTO> dtoList = boardService.getBrandItems(brand_id);
		return dtoList;
		
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(value="/brand/id/registeritem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Boolean> registerItem(@ModelAttribute BoardDTO dto) throws IOException{
				
		String savePath = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
        	savePath = "c:/ezwel/ezmarketupload/";        	
        } else {
        	savePath = "/Users/minsu/Documents/ezwel/ezmarketupload/";}
		String newfilename1 = null;
		MultipartFile file1 = dto.getImage();
		if(!file1.isEmpty()) {//f1해당파일선택했다면
			//이름랜덤문자열포함
			String originalfilename1 = file1.getOriginalFilename();
			String before1 = originalfilename1.substring(0, originalfilename1.indexOf("."));
			String ext1 = originalfilename1.substring(originalfilename1.indexOf("."));
			newfilename1 = before1 + "(" + UUID.randomUUID() + ")" + ext1;
			//서버내부 지정경로에 파일내용 저장
			file1.transferTo( new java.io.File(savePath +  newfilename1));
			dto.setImage_url(newfilename1); // 업로드한 파일을 서버 저장에 이름 -- db insert
		}
		System.out.println(dto.getVolume());
		boolean result = boardService.registerItem(dto);
	    return result ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(value="/brand/id/updateitem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Boolean> updateItem(@ModelAttribute BoardDTO dto) throws IOException{
		
		 String savePath = "";
		    String osName = System.getProperty("os.name").toLowerCase();
		    if (osName.contains("win")) {
		        savePath = "c:/ezwel/ezmarketupload/";        
		    } else {
		        savePath = "/Users/minsu/Documents/ezwel/ezmarketupload/";
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
	
	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/brand/{brandid}/delete/{productid}")
	public ResponseEntity<Boolean> deleteItem(@PathVariable("brandid") int brand_id, @PathVariable("productid") int product_id) {
	    boolean result = boardService.deleteItem(product_id);
	    return result ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}

	

}



