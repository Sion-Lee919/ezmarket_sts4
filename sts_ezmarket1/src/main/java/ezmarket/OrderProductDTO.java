package ezmarket;

import lombok.Data;

@Data
public class OrderProductDTO {
    private int productId;
    private int quantity;
    private int price;
    private String name;
    private String image_url;
    private String brandname;
} 