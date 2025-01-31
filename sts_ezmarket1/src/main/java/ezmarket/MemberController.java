package ezmarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MemberController {
	
	@Autowired
    @Qualifier("membermapperservice")
    MemberService memberService;
	
	@RestController
	public class HelloController {
	    @GetMapping("/hello")
	    public String sayHello() {
	        return "Hello, World!";
	    }
	}
}
