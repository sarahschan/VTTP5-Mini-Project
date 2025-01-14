package sg.edu.nus.iss.mini_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.mini_project.model.Login;
import sg.edu.nus.iss.mini_project.service.LoginService;

@Controller
@RequestMapping()
public class LoginController {
    
    @Autowired
    LoginService loginService;

    @Value("${admin.email}")
    private String adminEmail;
    

    @GetMapping()
    public String loginPage(Model model){
        
        Login login = new Login();
        model.addAttribute("login", login);
        
        return "login";

    }


    @PostMapping(path = "login", consumes = "application/x-www-form-urlencoded")
    public String handleLogin(@Valid @ModelAttribute Login login, BindingResult result, Model model, HttpSession session){
        
        if (result.hasErrors()) {
            model.addAttribute("login", login);
            return "login";
        }


        String email = login.getEmail().toLowerCase();
        String password = login.getPassword();
        Boolean isValid = loginService.validateLogin(email, password);

        
        if (!isValid) {
            model.addAttribute("loginError", "Invalid email and/or password");
            return "login";
        

        } else {
            
            session.setAttribute("userID", login.getEmail().toLowerCase());

            if (session.getAttribute("userID").equals(adminEmail)){
                session.setAttribute("userRole", "admin");
                return "redirect:/admin/home";


            } else {

                session.setAttribute("userRole", "community");
                return "redirect:/community/home";
            }
            
        }
        
    }

}
