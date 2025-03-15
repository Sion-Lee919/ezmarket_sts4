package ezmarket.chat;

import java.util.ArrayList;

public interface ChatService {

	boolean registerChat(ChatDTO chatDto);

	ArrayList<ChatDTO> getMemberListInChatRoom(int product_id);

	ArrayList<ChatDTO> getChatHistory(String channelId);
	
	//Member Part
	ArrayList<ChatDTO> getMyChat(int member_id);
}
