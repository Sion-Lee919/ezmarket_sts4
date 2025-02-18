package ezmarket;

import java.util.List;

public interface CartService {
    List<CartDTO> getCartByUsername(String username);
    void addToCart(String username, Long productId, int quantity);
    void removeFromCart(Long cartId);
    void updateCartQuantity(Long cartId, int quantity);
}