package ezmarket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
	String product_id;
	String seller_id;
	String name;
	String description;
	String price;
	String stock_quantity;
	String created_at;
	String update_date;
	String image_url;
}
