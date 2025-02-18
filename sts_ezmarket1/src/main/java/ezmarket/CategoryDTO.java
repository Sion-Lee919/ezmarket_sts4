package ezmarket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    String product_id;
    String seller_id;
    String name;
    String description;
    int alcohol;
    int volume;
    int price;
    int stock_quantity;
    String created_at;
    String update_date;
    String product_ingredient;
    String product_region;
    String bigcategory;
    String subcategory;
    String image_url;
}
