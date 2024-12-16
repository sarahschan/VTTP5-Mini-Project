package sg.edu.nus.iss.mini_project.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class CommunityController {
    
    @GetMapping("/home")
    public String homePage(){
        return "community/home";
    }
}
