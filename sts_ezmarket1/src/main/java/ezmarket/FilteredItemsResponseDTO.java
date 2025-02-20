package ezmarket;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class FilteredItemsResponseDTO {
    private List<BoardDTO> items;
    private int totalCount;
}