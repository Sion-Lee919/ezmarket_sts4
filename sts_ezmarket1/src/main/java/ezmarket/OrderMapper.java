package ezmarket;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderMapper {
    void insertOrder(OrderDTO order);

    List<OrderDTO> getOrdersByMemberId(@Param("memberId") int memberId);

    OrderDTO getLastOrderByMemberId(@Param("memberId") int memberId);

    OrderDTO getOrderByMemberIdAndOrderId(@Param("memberId") int memberId, @Param("orderId") int orderId);
    
    void updateOrderStatus(@Param("orderId") int orderId, @Param("status") String status);
}