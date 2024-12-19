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

import jakarta.servlet.http.HttpSession;
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

        List<Member> members = memberService.getAllMembersPojo();
        model.addAttribute("members", members);

        return "admin/members";

    }

    
    @GetMapping("/add")
    public String addMember(Model model){

        Member member = new Member();
        model.addAttribute("member", member);

        return "admin/memberAdd";
    }


    @PostMapping(path = "/add", consumes = "application/x-www-form-urlencoded")
    public String handleAddMember(@Valid @ModelAttribute Member member, BindingResult result, Model model){

        if (result.hasErrors()) {
            model.addAttribute("member", member);
            return "admin/memberAdd";
        }

        adminService.saveNewMember(member.getFirstName(), member.getLastName(), member.getEmail());

        return "redirect:/admin/members";
    }


    @GetMapping("/delete/{member-email}")
    public String confirmDelete(@PathVariable("member-email") String email, Model model){
        
        String fullName = memberService.getFullName(email);
        Member memberToDelete = memberService.getMemberPojo(email);

        model.addAttribute("memberName", fullName);
        model.addAttribute("member", memberToDelete);

        return "admin/memberDeleteConfirm";
    }


    @GetMapping("/delete/execute/{member-email}")
    public String executeDelete(@PathVariable("member-email") String email, HttpSession session){
        
        String memberName = memberService.getFullName(email);
        session.setAttribute("memberName", memberName);

        if (adminService.deleteMember(email)){
            return "redirect:/admin/members/deleted";
        }

        return "errorAdmin";
    }


    @GetMapping("/deleted")
    public String deleteConfirm(HttpSession session, Model model){

        String memberName = session.getAttribute("memberName").toString();
        model.addAttribute("memberName", memberName);

        return "admin/memberDeleted";

    }

}
