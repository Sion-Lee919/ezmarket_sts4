package ezmarket.chat;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {
	
	@Autowired
	@Qualifier("chatmapperservice")
	ChatService chatservice;
	
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(ChatDTO chatDto, SimpMessageHeaderAccessor accessor) {
    	boolean result = chatservice.registerChat(chatDto);
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatDto.getChannel_id(), chatDto);

    }
    
    @GetMapping("chatroom/getmemberlistinchatroom")
    public ArrayList<ChatDTO> getMemberListInChatRoom(@RequestParam("productId") int product_id){
    	ArrayList<ChatDTO> dtoList = chatservice.getMemberListInChatRoom(product_id);
    	return dtoList;
    }
    
    @GetMapping("/chat/records")
    public ResponseEntity<ArrayList<ChatDTO>> getChatHistory(@RequestParam String channelId) {
    	ArrayList<ChatDTO> chatHistory = chatservice.getChatHistory(channelId);
        return ResponseEntity.ok(chatHistory);
    }

    
}
