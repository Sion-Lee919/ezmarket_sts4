package ezmarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class OrderMapperService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    @Qualifier("cartmapperservice")
    private CartService cartService;

    @Transactional
    public void createOrders(OrderDTO order, int memberId) {
        try {
            order.setMemberId(memberId);

            List<OrderProductDTO> myPds = order.getProductInfo();
            int totalAmount = 0;
            for (var i = 0; i < myPds.size(); i++) {
                totalAmount += myPds.get(i).getPrice() * myPds.get(i).getQuantity();
            }
            order.setTotalAmount(totalAmount);
            order.setStatus("처리 중");

            // MEMBER 정보 조회
            Long currentPoints = jdbcTemplate.queryForObject(
                "SELECT points FROM member WHERE member_id = ?",
                Long.class, memberId
            ) != null ? jdbcTemplate.queryForObject("SELECT points FROM member WHERE member_id = ?", Long.class, memberId) : 0L;
            Long currentEzpay = jdbcTemplate.queryForObject(
                "SELECT ezpay FROM member WHERE member_id = ?",
                Long.class, memberId
            ) != null ? jdbcTemplate.queryForObject("SELECT ezpay FROM member WHERE member_id = ?", Long.class, memberId) : 0L;

            System.out.println("현재 적립금: " + currentPoints + ", EZPAY: " + currentEzpay);

            Long usedPoints = order.getUsedPoints() != null ? order.getUsedPoints() : 0L;
            Long newPoints = currentPoints - usedPoints;

            Long remainingAmount = totalAmount - usedPoints;
            Long newEzpay = currentEzpay - remainingAmount;

            // POINTS , EZPAY 잔액 검증
            if (newPoints < 0) {
                throw new IllegalArgumentException("보유 적립금이 부족합니다.");
            }
            if (newEzpay < 0) {
                throw new IllegalArgumentException("EZPAY 잔액이 부족합니다.");
            }

            String updateSql = "UPDATE member SET points = ?, ezpay = ?, update_date = SYSDATE WHERE member_id = ?";
            jdbcTemplate.update(updateSql, newPoints, newEzpay, memberId);

            // 적립금을 사용하지 않았으면 5% 지급
            if (usedPoints == 0L) {
                Long awardPoints = (long) (totalAmount * 0.05);
                jdbcTemplate.update(updateSql, newPoints + awardPoints, newEzpay, memberId);
            }

            orderMapper.insertOrder(order);
            System.out.println("주문 저장 완료: " + order.getMemberId());
            
            // 주문이 완료된 후 장바구니에서 해당 상품 제거
            removeOrderedItemsFromCart(myPds, memberId);
            
        } catch (Exception e) {
            System.err.println("주문 생성 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }



    public OrderDTO getLastOrderByMemberId(int memberId) {
        try {
            OrderDTO order = orderMapper.getLastOrderByMemberId(memberId);
            System.out.println("조회된 주문 수: " + order);
            

    private void removeOrderedItemsFromCart(List<OrderProductDTO> orderedProducts, int memberId) {
        try {
            String username = jdbcTemplate.queryForObject(
                "SELECT username FROM member WHERE member_id = ?",
                String.class, memberId
            );
            
            if (username == null) {
                System.err.println("회원 정보를 찾을 수 없습니다. memberId: " + memberId);
                return;
            }
            
            System.out.println("주문 상품 장바구니 삭제 시작 - 회원: " + username);

            List<CartDTO> cartItems = cartService.getCartByUsername(username);
            
            if (cartItems == null || cartItems.isEmpty()) {
                System.out.println("장바구니가 비어있습니다.");
                return;
            }

            for (OrderProductDTO orderedProduct : orderedProducts) {
                int productId = orderedProduct.getProductId();
                int orderedQty = orderedProduct.getQuantity();
                
                System.out.println("주문 상품 처리: 상품ID=" + productId + ", 주문수량=" + orderedQty);

                for (CartDTO cartItem : cartItems) {
                    if (cartItem.getProductId() == productId) {
                        int cartQty = cartItem.getQuantity();
                        
                        if (cartQty <= orderedQty) {
                            // 장바구니 수량이 주문 수량보다 작거나 같으면 완전히 삭제
                            cartService.removeFromCart(cartItem.getCartId());
                            System.out.println("장바구니 상품 삭제 완료: cartId=" + cartItem.getCartId());
                        } else {
                            // 장바구니 수량이 주문 수량보다 많으면 수량 차감
                            cartService.updateCartQuantity(cartItem.getCartId(), cartQty - orderedQty);
                            System.out.println("장바구니 상품 수량 업데이트: cartId=" + cartItem.getCartId() + 
                                             ", 이전 수량=" + cartQty + ", 새 수량=" + (cartQty - orderedQty));
                        }
                        break;
                    }
                }
            }
            
            System.out.println("주문 상품 장바구니 삭제 완료");
            
        } catch (Exception e) {
            System.err.println("장바구니 상품 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
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


   

    public OrderDTO getOrderByMemberIdAndOrderId(int memberId, int orderId) {

        try {
            return orderMapper.getOrderByMemberIdAndOrderId(memberId, orderId);
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