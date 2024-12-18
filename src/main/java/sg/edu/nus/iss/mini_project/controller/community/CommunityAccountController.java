package sg.edu.nus.iss.mini_project.controller.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.mini_project.model.Member;
import sg.edu.nus.iss.mini_project.service.MemberService;
import sg.edu.nus.iss.mini_project.service.LoginService;

@Controller
@RequestMapping("/community/account")
public class CommunityAccountController {
    
    @Autowired
    MemberService memberService;

    @Autowired
    LoginService loginService;



    @GetMapping()
    public String accountDetails(Model model, HttpSession session){

        String memberEmail = session.getAttribute("userID").toString();
        Member member = memberService.getMemberPojo(memberEmail);

        model.addAttribute("member", member);

        return "community/account";
    }



    @GetMapping("/changepassword")
    public String changePassword(){
        return "community/changePassword";
    }



    @PostMapping(path = "/changepassword", consumes = "application/x-www-form-urlencoded")
    public String handleChangePassword(@RequestParam String currentPassword, @RequestParam String newPassword, @RequestParam String repeatPassword, Model model){

        if (!loginService.checkPassword(currentPassword)){
            model.addAttribute("wrongPassword", "Existing password does not match");
            return "community/changePassword";
        }

        if (!loginService.checkPasswordCharacters(newPassword)){
            model.addAttribute("invalidCharacters", "New password can only contain letters, numbers, and the special characters  @  -  _  and  .");
            return "community/changePassword";
        }

        if (!loginService.matchPassword(newPassword, repeatPassword)){
            model.addAttribute("noMatch", "New password and repeat password must match");
            return "community/changePassword";
        }

        loginService.changePassword(newPassword);

        return "redirect:/community/account";
    }
}
