package ezmarket.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {
    private String channel_id;
    private int product_id;
    private int writer_id;
    private int member_id;
    private String chat;
    private String created_at;
}