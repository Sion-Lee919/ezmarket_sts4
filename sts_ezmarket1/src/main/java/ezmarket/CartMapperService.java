package ezmarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("cartmapperservice")
public class CartMapperService implements CartService {
    
    @Autowired
    CartMapper cartMapper;

    @Override
    public List<CartDTO> getCartByUsername(String username) {
        return cartMapper.getCartByUsername(username);
    }

    @Override
    public void addToCart(String username, Long productId, int quantity) {
        if (cartMapper.checkExistingCart(username, productId) > 0) {
            cartMapper.updateExistingCartQuantity(username, productId, quantity);
        } else {
            cartMapper.insertNewCart(username, productId, quantity);
        }
    }

    @Override
    public void removeFromCart(Long cartId) {
        cartMapper.removeFromCart(cartId);
    }

    @Override
    public void updateCartQuantity(Long cartId, int quantity) {
        cartMapper.updateCartQuantity(cartId, quantity);
    }
}