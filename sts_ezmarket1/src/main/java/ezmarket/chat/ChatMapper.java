package ezmarket.chat;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ChatMapper {

	boolean registerChat(ChatDTO chatDto);

	ArrayList<ChatDTO> getMemberListInChatRoom(int product_id);

	ArrayList<ChatDTO> getChatHistory(String channelId);

}
