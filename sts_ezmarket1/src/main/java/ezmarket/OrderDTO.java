package ezmarket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
	String order_id;
	String member_id;
	String order_date;
	String quantity;
	String total_amount;
	String status;
	String shipping_address;
}
