package ezmarket;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
	int product_id;
	int brand_id;
	String name;
	String description;
	int alcohol;
	String volume;
	int price;
	int stock_quantity;
	String created_at;
	String update_date;
	String product_ingredient;
	String product_region;
	String bigcategory;
	String subcategory;
	String image_url;
	MultipartFile image;
	int view_count;
	String brandname;
	int sweetness;
    int sourness;
    int carbonation;
    int body;

	String searchKeyword; 
    int offset;            
    int limit;
    
    List<Integer> sweetnesss;
    List<Integer> sournesss;
    List<Integer> carbonations;
    List<Integer> bodys;
	
	List<String> subcategories;
    List<String> regions;
    Boolean newProduct;
    String sortType;
    
    String category;
    
    int calculated_money;
    int calculating_money;
    int calculate_possible;
}
