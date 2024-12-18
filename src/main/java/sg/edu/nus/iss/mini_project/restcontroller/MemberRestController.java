package sg.edu.nus.iss.mini_project.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.mini_project.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberRestController {
    
    @Autowired
    MemberService memberService;
    
}
