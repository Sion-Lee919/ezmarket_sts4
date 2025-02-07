package ezmarket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{memberId}")
    public List<CartDTO> getCart(@PathVariable Long memberId) {
        return cartService.getCartByMemberId(memberId);
    }

    @PostMapping("/add")
    public void addToCart(@RequestParam Long memberId, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.addToCart(memberId, productId, quantity);
    }

    @DeleteMapping("/remove/{cartId}")
    public void removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
    }
}
