package ezmarket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaDTO {
	String qna_id;
	String member_id;
	String subject;
	String content;
	String created_at;
	String update_date;
	String response;
	String response_date;
	String notifications;
}
