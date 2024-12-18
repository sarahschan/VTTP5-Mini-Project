package sg.edu.nus.iss.mini_project.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community/attending")
public class CommunityAttendingController {
    
    @GetMapping()
    public String attendingPage(Model model){
        return "attending";
    }
}
