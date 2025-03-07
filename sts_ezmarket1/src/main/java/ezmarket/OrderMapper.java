package ezmarket;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderMapper {
    void insertOrder(OrderDTO order);

    List<OrderDTO> getOrdersByMemberId(@Param("memberId") String memberId);

    OrderDTO getOrderByMemberId(@Param("memberId") String memberId, @Param("orderId") Long orderId);
}