package sg.edu.nus.iss.mini_project.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;


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
        session.invalidate();
        return "redirect:/";
    }
    
}
