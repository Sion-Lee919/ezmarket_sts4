package ezmarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ezmarket.cookie.JWTUtil;
import java.util.List;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    @Qualifier("cartmapperservice")
    CartService cartService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/me")
    public ResponseEntity<?> getCart(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || token.isEmpty()) {
                System.out.println("토큰이 없습니다.");
                return ResponseEntity.badRequest().body("토큰이 없습니다.");
            }

            System.out.println("받은 토큰: " + token);

            String username = extractUsernameFromToken(token);
            System.out.println("추출된 username: " + username);

            List<CartDTO> cartItems = cartService.getCartByUsername(username);
            System.out.println("조회된 장바구니 아이템 수: " + (cartItems != null ? cartItems.size() : 0));
            System.out.println("장바구니 내용: " + cartItems);

            if (cartItems == null || cartItems.isEmpty()) {
                System.out.println("장바구니가 비어있습니다.");
                return ResponseEntity.ok(Collections.emptyList());
            }

            return ResponseEntity.ok(cartItems);
        } catch (RuntimeException e) {
            System.out.println("장바구니 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("장바구니 불러오기 실패: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String token, 
                                     @RequestParam Long productId, 
                                     @RequestParam int quantity) {
        try {
            System.out.println("장바구니 추가 시도 - productId: " + productId + ", quantity: " + quantity);
            
            String username = extractUsernameFromToken(token);
            System.out.println("추출된 username: " + username);
            
            cartService.addToCart(username, productId, quantity);
            System.out.println("장바구니 추가 성공");
            
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("장바구니 추가 실패: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("장바구니 추가 실패: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long cartId) {
        try {
            System.out.println("장바구니 삭제 시도 - cartId: " + cartId);
            
            cartService.removeFromCart(cartId);
            System.out.println("장바구니 삭제 성공");
            
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("장바구니 삭제 실패: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("장바구니 삭제 실패: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/update/{cartId}")
    public ResponseEntity<?> updateCartQuantity(@PathVariable Long cartId, 
                                              @RequestBody Map<String, Integer> request) {
        try {
            int newQuantity = request.get("quantity");
            System.out.println("장바구니 수량 업데이트 시도 - cartId: " + cartId + ", newQuantity: " + newQuantity);
            
            cartService.updateCartQuantity(cartId, newQuantity);
            System.out.println("장바구니 수량 업데이트 성공");
            
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("수량 업데이트 실패: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("수량 업데이트 실패: " + e.getMessage());
        }
    }

    private String extractUsernameFromToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String username = JWTUtil.validateAndGetUserId(token);
            System.out.println("토큰에서 추출된 username: " + username);
            return username;
        } catch (Exception e) {
            System.out.println("Token parsing error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("토큰 파싱 중 오류 발생: " + e.getMessage());
        }
    }
}