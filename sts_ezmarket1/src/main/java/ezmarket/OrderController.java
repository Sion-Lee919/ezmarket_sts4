package ezmarket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezmarket.cookie.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/buy")
public class OrderController {

    @Autowired
    private OrderMapperService orderMapperService;
    
    @Autowired
    @Qualifier("membermapperservice")
    MemberService memberService;
    
    @Autowired
    @Qualifier("boardmapperservice")
    BoardService boardService;

    @PostMapping("/orderid")
    public ResponseEntity<?> createOrders(HttpServletRequest request, @RequestBody OrderDTO order) {
        try {
            String token = request.getHeader("Authorization");
            System.out.println("token: " + token);
            System.out.println("주문 요청 받음: " + order);

            if (token != null && token.startsWith("Bearer")) {
                token = token.substring(7);

                String memberId = JWTUtil.validateAndGetUserId(token); 
                if (memberId != null) {
                    System.out.println("토큰 파싱 회원 아이디: " + memberId);
                    
                    // 상품 정보 검증
                    if (order.getProductInfo() == null || order.getProductInfo().isEmpty()) {
                        return ResponseEntity.badRequest().body("상품 정보가 없습니다.");
                    }
                    
                    // 배송 정보 검증
                    if (order.getShippingAddress() == null || order.getShippingAddress().isEmpty()) {
                        return ResponseEntity.badRequest().body("배송지 정보가 없습니다.");
                    }
                    
                    if (order.getReceiverName() == null || order.getReceiverName().isEmpty()) {
                        return ResponseEntity.badRequest().body("받는 사람 정보가 없습니다.");
                    }
                    
                    // Product info 불러와서 가격 정보 order 안에 orderProduct에 가격 정보 저장
                    List<OrderProductDTO> myPds = order.getProductInfo();
                    for(var i = 0; i < myPds.size(); i++) {
                        BoardDTO item = boardService.getItemDetail(myPds.get(i).getProductId());
                        if (item == null) {
                            return ResponseEntity.badRequest().body("상품 ID " + myPds.get(i).getProductId() + "에 해당하는 상품이 없습니다.");
                        }
                        myPds.get(i).setPrice(item.getPrice());
                    }
                    System.out.println("가격 저장 후: " + order);
                    
                    orderMapperService.createOrders(order, memberId);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("result", "success");
                    response.put("message", "주문이 성공적으로 처리되었습니다.");
                    return ResponseEntity.ok().body(response);
                } else { 
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "토큰이 유효하지 않습니다.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
                }
            } else { 
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Authorization 헤더 오류.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } catch (Exception e) {
            System.err.println("주문 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("result", "fail");
            errorResponse.put("message", "주문 처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/my/order")
    public ResponseEntity<?> getMyOrders(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            System.out.println("token: " + token);

            if (token != null && token.startsWith("Bearer")) {
                token = token.substring(7);

                String memberId = JWTUtil.validateAndGetUserId(token);
                if (memberId != null) {
                    System.out.println("주문 조회 요청 받음. memberId: " + memberId);
                    List<OrderDTO> orders = orderMapperService.getOrdersByMemberId(memberId);
                    System.out.println("조회된 주문 수: " + orders.size());
                    
                    // 각 주문의 상품 정보 로깅
                    for (OrderDTO order : orders) {
                        System.out.println("주문 ID: " + order.getOrderId() + ", 상품 정보: " + order.getProductInfo());
                    }
                    
                    return ResponseEntity.ok().body(orders);
                } else {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "토큰이 유효하지 않습니다.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
                }
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Authorization 헤더 오류.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } catch (Exception e) {
            System.err.println("주문 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("result", "fail");
            errorResponse.put("message", "주문 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}