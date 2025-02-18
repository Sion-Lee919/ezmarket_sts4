package ezmarket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long cartId;
    private String username;
    private Long productId;
    private int quantity;
    
    private String productName;
    private int price;
    private String image;
}