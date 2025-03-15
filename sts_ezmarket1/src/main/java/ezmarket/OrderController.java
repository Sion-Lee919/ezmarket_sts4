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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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

                String userId = JWTUtil.validateAndGetUserId(token); 
                if (userId != null) {
                    System.out.println("토큰 파싱 회원 아이디: " + userId);
                    
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
                    
                    MemberDTO mem = memberService.getMember(userId);
                    System.out.println("구매 회원 정보" + mem);
                    
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
                    System.out.println("주문 처리 시작 - 주문 상품 개수: " + myPds.size());
                    
                    orderMapperService.createOrders(order, mem.getMember_id());
                    
                    System.out.println("주문 처리 완료 - 장바구니 정리 작업도 완료됨");

                    
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

                String userId = JWTUtil.validateAndGetUserId(token);
                if (userId != null) {
                    System.out.println("주문 조회 요청 받음. memberId: " + userId);
                    
                   MemberDTO mem = memberService.getMember(userId);
                   System.out.println("멤버 정보 : " + mem);
                    
                    List<OrderDTO> orders = orderMapperService.getOrdersByMemberId(mem.getMember_id());
                    System.out.println("조회된 주문 수: " + orders);
                    
                    for(var j = 0; j < orders.size(); j++) {
                    	OrderDTO order = orders.get(j);
                        List<OrderProductDTO> myPds = order.getProductInfo();
                        for(var i = 0; i < myPds.size(); i++) {
                            BoardDTO item = boardService.getItemDetail(myPds.get(i).getProductId());
                            if (item == null) {
                                return ResponseEntity.badRequest().body("상품 ID " + myPds.get(i).getProductId() + "에 해당하는 상품이 없습니다.");
                            }
                            myPds.get(i).setName(item.getName());
                            myPds.get(i).setImage_url(item.getImage_url());
                            myPds.get(i).setBrandname(item.getBrandname());
                        }
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
    
    //Member Part
    	@GetMapping("/orderFlowCount")
    	public ResponseEntity<Map<String, Integer>> orderFlowCount(@RequestHeader("Authorization") String token) {
    		int member_id = JWTUtil.validateAndGetMemberId(token.replace("Bearer ", ""));
    		
    		Map<String, Integer> counts = new HashMap<>();
    		counts.put("preparing", orderMapperService.getOrderCountByStatus("처리 중", member_id));
    		counts.put("shipping", orderMapperService.getOrderCountByStatus("배송 중", member_id));
    		counts.put("shipped", orderMapperService.getOrderCountByStatus("배송 완료", member_id));
    		counts.put("return", orderMapperService.getOrderCountByStatus("반품 중", member_id));
    		
    		return ResponseEntity.ok(counts);
    	}
}