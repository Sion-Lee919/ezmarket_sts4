package ezmarket;

import lombok.Data;

@Data
public class OrderProductDTO {
    private int productId;
    private int quantity;
    private int price;
}