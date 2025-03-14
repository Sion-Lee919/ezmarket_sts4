package ezmarket;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OrderDTO {
	private int orderId;
	private int memberId;
	private List<OrderProductDTO> productInfo;
	private int totalAmount;
	private String status;
	private String shippingAddress;
	private String shippingMessage;
	private String paymentMethod;
	private String receiverName;
	private String receiverPhone;
	private String receiverEmail;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Date orderDate;
	private Long usedPoints;
}