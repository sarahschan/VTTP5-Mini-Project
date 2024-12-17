package sg.edu.nus.iss.mini_project.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/community")
public class CommunityController {
    
    @GetMapping("/home")
    public String homePage(){
        return "community/home";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("userID");
        session.removeAttribute("userRole");
        session.invalidate();
        return "redirect:/";
    }
    
}
