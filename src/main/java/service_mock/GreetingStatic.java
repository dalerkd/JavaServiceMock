package service_mock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class GreetingStatic {
    @RequestMapping(value= "/")
    public String indexUrl(){
        return "/index.html";
    }
}
