package ezmarket;

import java.util.List;
import ezmarket.OrderDTO;

public interface OrderService {
    void createOrders(List<OrderDTO> orders);
    List<OrderDTO> getOrdersByMemberId(int memberId);
    OrderDTO getLastOrderByMemberId(int memberId);
    OrderDTO getOrderByMemberId(int memberId, int orderId);
}
