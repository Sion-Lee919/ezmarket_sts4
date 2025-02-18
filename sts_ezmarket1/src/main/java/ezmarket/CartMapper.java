package ezmarket;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface CartMapper {
    List<CartDTO> getCartByUsername(String username);
    int checkExistingCart(String username, Long productId);
    void insertNewCart(String username, Long productId, int quantity);
    void updateExistingCartQuantity(String username, Long productId, int quantity);
    void removeFromCart(Long cartId);
    void updateCartQuantity(Long cartId, int quantity);
}