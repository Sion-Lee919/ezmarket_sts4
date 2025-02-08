package ezmarket;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    @Select("SELECT c.cart_id, c.member_id, c.product_id, p.name as product_name, c.quantity " +
            "FROM cart c JOIN product p ON c.product_id = p.product_id " +
            "WHERE c.member_id = #{memberId}")
    List<CartDTO> getCartByMemberId(Long memberId);

    @Insert("INSERT INTO cart (member_id, product_id, quantity) VALUES (#{memberId}, #{productId}, #{quantity})")
    void addToCart(@Param("memberId") Long memberId, @Param("productId") Long productId, @Param("quantity") int quantity);

    @Delete("DELETE FROM cart WHERE cart_id = #{cartId}")
    void removeFromCart(Long cartId);
}
