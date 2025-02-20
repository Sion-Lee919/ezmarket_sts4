package ezmarket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ReviewController {

	@Autowired
    @Qualifier("reviewmapperservice")
    ReviewService reviewService;
	
	@GetMapping("getreview/{product_id}")
	public ArrayList<BoardDTO> getReview(@PathVariable("product_id") int product_id) {
		ArrayList<BoardDTO> dtoList = reviewService.getReview(product_id);
		return dtoList;
		
	}
	
	@PostMapping(value="review/registerreview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Boolean> registerReview(@ModelAttribute ReviewDTO dto) throws IOException{
		
		System.out.println("registerReview - dto.getReview_id : " + dto.getReview_id());
		
		String savePath = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {

        	savePath = "c:/ezwel/ezmarketupload/reviewimage";

        } else {
        	savePath = "/Users/minsu/Documents/ezwel/Desktop/downloaded_images/reviewimage";
        }
		String newfilename1 = null;
		MultipartFile file1 = dto.getImage();
		
		if (dto.getImage() != null && !file1.isEmpty()) {
			String originalfilename1 = file1.getOriginalFilename();
			String before1 = originalfilename1.substring(0, originalfilename1.indexOf("."));
			String ext1 = originalfilename1.substring(originalfilename1.indexOf("."));
			newfilename1 = before1 + "(" + UUID.randomUUID() + ")" + ext1;
			//서버내부 지정경로에 파일내용 저장
			file1.transferTo( new java.io.File(savePath +  newfilename1));
			dto.setImage_url(newfilename1);
		} else {
			dto.setImage_url(null);
		}
		boolean result = reviewService.registerReview(dto);
	    return result ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}
	
	
	
}
