package ezmarket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartMapperService {
    private final CartMapper cartMapper;

    public List<CartDTO> getCartByMemberId(Long memberId) {
        return cartMapper.getCartByMemberId(memberId);
    }

    public void addToCart(Long memberId, Long productId, int quantity) {
        cartMapper.addToCart(memberId, productId, quantity);
    }

    public void removeFromCart(Long cartId) {
        cartMapper.removeFromCart(cartId);
    }
}
