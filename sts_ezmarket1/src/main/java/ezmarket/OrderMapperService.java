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

            System.out.println("기본 정보 셋팅 후: " + order);
            
            orderMapper.insertOrder(order);
            System.out.println("주문 저장 완료: " + order.getMemberId());
        } catch (Exception e) {
            System.err.println("주문 생성 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<OrderDTO> getOrdersByMemberId(String memberId) {
        try {
            List<OrderDTO> orders = orderMapper.getOrdersByMemberId(memberId);
            System.out.println("조회된 주문 수: " + orders.size());

            for (OrderDTO order : orders) {
                System.out.println("주문 ID: " + order.getOrderId());
                System.out.println("주문 상태: " + order.getStatus());
                System.out.println("주문 총액: " + order.getTotalAmount());
                System.out.println("배송지: " + order.getShippingAddress());
                System.out.println("상품 정보 타입: " + (order.getProductInfo() != null ? order.getProductInfo().getClass().getName() : "null"));
                System.out.println("상품 정보 크기: " + (order.getProductInfo() != null ? order.getProductInfo().size() : 0));
            }
            
            return orders;
        } catch (Exception e) {
            System.err.println("주문 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public OrderDTO getOrderByMemberId(String memberId, Long orderId) {
        try {
            return orderMapper.getOrderByMemberId(memberId, orderId);
        } catch (Exception e) {
            System.err.println("주문 상세 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}