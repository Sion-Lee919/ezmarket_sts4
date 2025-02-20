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
    
    private Integer page; 
    private Integer limit;
    private Integer offset; 

    public void calculateOffset() {
        if (page != null && limit != null) {
            this.offset = (page - 1) * limit;
        } else {
            this.offset = 0;
        }
        System.out.println("offset테스트중 : "+offset);
    }
}
