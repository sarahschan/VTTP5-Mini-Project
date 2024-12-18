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
import sg.edu.nus.iss.mini_project.service.MemberService;
import sg.edu.nus.iss.mini_project.service.LoginService;


@Controller
@RequestMapping("/admin/account")
public class AdminAccountController {
    
    @Autowired
    MemberService memberService;

    @Autowired
    LoginService loginService;

    @Value("${admin.email}")
    private String adminEmail;
    

    
    @GetMapping()
    public String accountDetails(Model model){

        Member member = memberService.getMemberPojo(adminEmail);
        model.addAttribute("member", member);
        
        return "admin/account";
    }
    


    @GetMapping("/changepassword")
    public String changePassword(){
        return "admin/changePassword";
    }



    @PostMapping(path = "/changepassword", consumes = "application/x-www-form-urlencoded")
    public String handleChangePassword(@RequestParam String currentPassword, @RequestParam String newPassword, @RequestParam String repeatPassword, Model model){

        if (!loginService.checkPassword(currentPassword)){
            model.addAttribute("wrongPassword", "Existing password does not match");
            return "admin/changePassword";
        }

        if (!loginService.checkPasswordCharacters(newPassword)){
            model.addAttribute("invalidCharacters", "New password can only contain letters, numbers, and the special characters  @  -  _  and  .");
            return "community/changePassword";
        }

        if (!loginService.matchPassword(newPassword, repeatPassword)){
            model.addAttribute("noMatch", "New password and repeat password must match");
            return "admin/changePassword";
        }

        loginService.changePassword(newPassword);

        return "redirect:/admin/account";
    }
}
