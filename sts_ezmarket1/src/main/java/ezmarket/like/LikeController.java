 package ezmarket.like;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezmarket.cookie.JWTUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LikeController {
	
	@Autowired
	@Qualifier("likemapperservice")
	LikeService likeService;
	
	@PostMapping("/like")
    public void like(@RequestHeader("Authorization") String token, @RequestParam("product_id") int product_id) {
        int member_id = JWTUtil.validateAndGetMemberId(token.replace("Bearer ", ""));
        likeService.like(member_id, product_id);
    }
	
	@PostMapping("/unlike")
    public void unlike(@RequestHeader("Authorization") String token, @RequestParam("product_id") int product_id) {
        int member_id = JWTUtil.validateAndGetMemberId(token.replace("Bearer ", ""));
        likeService.unlike(member_id, product_id);
    }
	
	@GetMapping("/getLike")
    public ResponseEntity<List<LikeDTO>> getLike(@RequestHeader("Authorization") String token) {
        int member_id = JWTUtil.validateAndGetMemberId(token.replace("Bearer ", ""));
        List<LikeDTO> userGetLike = likeService.getLike(member_id);
        return ResponseEntity.ok(userGetLike);
    }
	
	@GetMapping("/checkLike")
	public ResponseEntity<Boolean> checkLike(@RequestHeader("Authorization") String token, @RequestParam("product_id") int product_id) {
	    int memberId = JWTUtil.validateAndGetMemberId(token.replace("Bearer ", ""));
	    List<LikeDTO> userLikes = likeService.getLike(memberId);

	    boolean isLiked = userLikes.stream().anyMatch(like -> like.getProduct_id() == product_id);
	    return ResponseEntity.ok(isLiked);
	}
}
