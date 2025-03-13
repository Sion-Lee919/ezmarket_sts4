package ezmarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class OrderMapperService {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void createOrders(OrderDTO order, int memberId) {
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

    public List<OrderDTO> getOrdersByMemberId(int memberId) {
        try {
            List<OrderDTO> order = orderMapper.getOrdersByMemberId(memberId);
            System.out.println("조회된 주문 수: " + order);
            
            return order;
        } catch (Exception e) {
            System.err.println("주문 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public OrderDTO getLastOrderByMemberId(int memberId) {
        try {
            OrderDTO order = orderMapper.getLastOrderByMemberId(memberId);
            System.out.println("조회된 주문 수: " + order);
            
            return order;
        } catch (Exception e) {
            System.err.println("주문 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public OrderDTO getOrderByMemberId(int memberId, int orderId) {
        try {
            return orderMapper.getOrderByMemberId(memberId, orderId);
        } catch (Exception e) {
            System.err.println("주문 상세 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    //Member Part
    public int getOrderCountByStatus(String status) {
        return orderMapper.getOrderCountByStatus(status);
    }

    public Map<String, Integer> getOrderFlowCount() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("pay", orderMapper.getOrderCountByStatus("결제 완료"));
        counts.put("preparing", orderMapper.getOrderCountByStatus("상품 준비중"));
        counts.put("shipping", orderMapper.getOrderCountByStatus("배송중"));
        counts.put("shipped", orderMapper.getOrderCountByStatus("배송 완료"));
        return counts;
    }
}