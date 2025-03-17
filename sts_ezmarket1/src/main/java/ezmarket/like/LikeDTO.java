package ezmarket.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    int member_id;
    int product_id;
    String like_date;
    
    String name;
    String price;
    String description;
    String image_url;
    String alcohol;
    String volume;
}