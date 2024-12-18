package sg.edu.nus.iss.mini_project.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import sg.edu.nus.iss.mini_project.constant.Constant;
import sg.edu.nus.iss.mini_project.model.Member;
import sg.edu.nus.iss.mini_project.repository.RedisRepo;
import sg.edu.nus.iss.mini_project.serializers.MemberSerializer;

@Service
public class LoginService {
    
    @Autowired
    RedisRepo redisRepo;

    @Autowired
    MemberSerializer memberSerializer;

    @Autowired
    MemberService memberService;

    @Autowired
    HttpServletRequest request;


    public Boolean validateLogin(String email, String password){

        Object memberJsonObject = redisRepo.getValue(Constant.MEMBER_KEY, email);

        if (memberJsonObject == null){
            return false;
        }

        Member member = memberSerializer.jsonToPojo(memberJsonObject.toString());

        return password.equals(member.getPassword());

    }


    public Boolean checkPassword(String currentPassword){

        String userEmail = request.getSession().getAttribute("userID").toString();

        Member member = memberService.getMemberPojo(userEmail);

        if (member.getPassword().equals(currentPassword)){
            return true;
        }

        return false;
    }


    public Boolean checkPasswordCharacters(String newPassword){
        
        String regex = "^[a-zA-Z0-9@._-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newPassword);

        return matcher.matches();
    }


    public Boolean matchPassword(String newPassword, String repeatPassword){
        
        return newPassword.equals(repeatPassword);

    }


    public void changePassword(String newPassword){
        
        String userEmail = request.getSession().getAttribute("userID").toString();

        Member member = memberService.getMemberPojo(userEmail);

        member.setPassword(newPassword);

        memberService.updateMember(member);

    }

}
