package ezmarket;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class FilterRequestDTO {
    private String searchKeyword;
    private String bigcategory;
    private List<String> subcategories;
    private List<Map<String, Integer>> alcoholRanges;
    private List<String> regions;
    private List<Map<String, Integer>> priceRanges;
    private Boolean newProduct;
    private String sortType;
}
