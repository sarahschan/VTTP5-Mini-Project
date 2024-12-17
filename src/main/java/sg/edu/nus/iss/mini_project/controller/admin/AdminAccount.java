package sg.edu.nus.iss.mini_project.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.mini_project.model.Member;
import sg.edu.nus.iss.mini_project.service.GeneralService;
import sg.edu.nus.iss.mini_project.service.LoginService;


@Controller
@RequestMapping("/admin/account")
public class AdminAccount {
    
    @Autowired
    GeneralService generalService;

    @Autowired
    LoginService loginService;

    @Value("${admin.email}")
    private String adminEmail;
    

    @GetMapping()
    public String accountDetails(Model model){

        Member member = generalService.getMember(adminEmail);
        model.addAttribute("member", member);
        
        return "admin/account";
    }
    

    @GetMapping("/changepassword")
    public String changePassword(){
        return "admin/changePassword";
    }

    @PostMapping("/changepassword")
    public String handleChangePassword(@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword, @RequestParam("repeatPassword") String repeatPassword, Model model){

        if (!loginService.checkPassword(currentPassword)){
            model.addAttribute("wrongPassword", "Existing password does not match");
            return "admin/changePassword";
        }

        if (!loginService.matchPassword(newPassword, repeatPassword)){
            model.addAttribute("noMatch", "New password and repeat password must match");
            return "admin/changePassword";
        }

        loginService.changePassword(newPassword);

        return "redirect:/admin/account";
    }
}
