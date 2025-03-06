package ezmarket.chat;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("chatmapperservice")
public class ChatMapperService implements ChatService {
	
	@Autowired
	ChatMapper mapper;

	@Override
	public boolean registerChat(ChatDTO chatDto) {
		return mapper.registerChat(chatDto);
	}

	@Override
	public ArrayList<ChatDTO> getMemberListInChatRoom(int product_id) {
		return mapper.getMemberListInChatRoom(product_id);
	}

	@Override
	public ArrayList<ChatDTO> getChatHistory(String channelId) {
		return mapper.getChatHistory(channelId);
	}

}
