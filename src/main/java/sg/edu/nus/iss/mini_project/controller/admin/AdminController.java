package sg.edu.nus.iss.mini_project.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("/home")
    public String adminHome() {
        return "admin/home";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("userID");
        session.removeAttribute("userRole");
        session.invalidate();
        return "redirect:/";
    }
    
}
