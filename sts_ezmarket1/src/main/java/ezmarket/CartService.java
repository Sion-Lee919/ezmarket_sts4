package ezmarket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapperService cartMapperService;

    public List<CartDTO> getCartByMemberId(Long memberId) {
        return cartMapperService.getCartByMemberId(memberId);
    }

    public void addToCart(Long memberId, Long productId, int quantity) {
        cartMapperService.addToCart(memberId, productId, quantity);
    }

    public void removeFromCart(Long cartId) {
        cartMapperService.removeFromCart(cartId);
    }
}
