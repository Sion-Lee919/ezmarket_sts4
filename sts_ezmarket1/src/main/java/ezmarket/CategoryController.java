package ezmarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class CategoryController {

	@Autowired
    @Qualifier("categorymapperservice")
    CategoryService categoryService;
}
