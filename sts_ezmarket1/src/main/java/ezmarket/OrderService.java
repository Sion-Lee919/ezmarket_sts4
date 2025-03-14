package ezmarket;

import java.util.List;
import ezmarket.OrderDTO;
import java.util.Map;

public interface OrderService {
    void createOrders(List<OrderDTO> orders);
    List<OrderDTO> getOrdersByMemberId(int memberId);
    OrderDTO getLastOrderByMemberId(int memberId);
    
    //Member Part
    Map<String, Integer> getOrderFlowCount();

    OrderDTO getOrderByMemberIdAndOrderId(int memberId, int orderId);

}
