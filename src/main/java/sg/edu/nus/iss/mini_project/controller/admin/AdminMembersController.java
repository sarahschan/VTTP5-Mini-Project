package sg.edu.nus.iss.mini_project.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import sg.edu.nus.iss.mini_project.model.Member;
import sg.edu.nus.iss.mini_project.service.AdminService;
import sg.edu.nus.iss.mini_project.service.MemberService;

@Controller
@RequestMapping("/admin/members")
public class AdminMembersController {
    
    @Autowired
    AdminService adminService;

    @Autowired
    MemberService memberService;

    @GetMapping()
    public String membersPage(Model model){

        List<Member> members = adminService.getMembers();
        model.addAttribute("members", members);

        return "admin/members";

    }

    
    @GetMapping("/add")
    public String addMember(Model model){

        Member member = new Member();
        model.addAttribute("member", member);

        return "admin/memberAdd";
    }


    @PostMapping("/add")
    public String handleAddMember(@Valid @ModelAttribute("member") Member member, BindingResult result, Model model){

        // validate fields
        if (result.hasErrors()) {
            model.addAttribute("member", member);
            return "admin/memberAdd";
        }

        adminService.saveNewMember(member.getFirstName(), member.getLastName(), member.getEmail());

        return "redirect:/admin/members";
    }


    @GetMapping("/delete/{member-email}")
    public String confirmDelete(@PathVariable("member-email") String email, Model model){
        
        Member memberToDelete = memberService.getMember(email);
        model.addAttribute("member", memberToDelete);

        return "admin/confirmDelete";
    }


    @GetMapping("/delete/execute/{member-email}")
    public String executeDelete(@PathVariable("member-email") String email){
        
        if (adminService.deleteMember(email)){
            return "redirect:/admin/members";
        }

        return null;
    }
}
