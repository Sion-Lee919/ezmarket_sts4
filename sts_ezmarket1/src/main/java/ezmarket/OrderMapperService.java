package ezmarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderMapperService {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void createOrders(OrderDTO order, String memberId) {
        try {
            order.setMemberId(memberId);
            
            List<OrderProductDTO> myPds = order.getProductInfo();
            int totalAmount = 0;
            for(var i = 0; i < myPds.size(); i++) {
                totalAmount += myPds.get(i).getPrice() * myPds.get(i).getQuantity();
            }
            order.setTotalAmount(totalAmount);
            order.setStatus("처리 중");
            
            orderMapper.insertOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<OrderDTO> getOrdersByMemberId(String memberId) {
        try {
            List<OrderDTO> orders = orderMapper.getOrdersByMemberId(memberId);
            System.out.println("조회된 주문 수: " + orders.size());
            
            // 각 주문의 상품 정보 확인
            for (OrderDTO order : orders) {
                System.out.println("주문 ID: " + order.getOrderId());
                System.out.println("주문 상태: " + order.getStatus());
                System.out.println("주문 총액: " + order.getTotalAmount());
                System.out.println("배송지: " + order.getShippingAddress());
            }
            
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public OrderDTO getOrderByMemberId(String memberId, Long orderId) {
        try {
            return orderMapper.getOrderByMemberId(memberId, orderId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}