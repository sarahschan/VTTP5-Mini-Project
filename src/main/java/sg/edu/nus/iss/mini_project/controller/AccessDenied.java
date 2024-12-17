package sg.edu.nus.iss.mini_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("access-denied")
public class AccessDenied {
    
    @GetMapping
    public String accessDenied(HttpSession session, Model model){
        
        Object userRole = session.getAttribute("userRole");

        if (userRole == null){
            return "redirect:/";
        }

        if (userRole.equals("admin")){
            return "admin/denied";
        }

        return "community/denied";

    }

}
