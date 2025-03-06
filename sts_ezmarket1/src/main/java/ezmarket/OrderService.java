package ezmarket;

import java.util.List;
import ezmarket.OrderDTO;

public interface OrderService {
    void createOrders(List<OrderDTO> orders);
    List<OrderDTO> getOrdersByMemberId(String memberId);
    OrderDTO getOrderByMemberId(String memberId, Long orderId);
}
