package sg.edu.nus.iss.mini_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    GeneralService generalService;

    @Autowired
    HttpServletRequest request;


    public Boolean validateLogin(String email, String password){

        // Check if member's email exists in db
        Object memberJsonObject = redisRepo.getValue(Constant.MEMBER_KEY, email);

        if (memberJsonObject == null){
            return false;
        }

        // JSON -> POJO
        Member member = memberSerializer.jsonToPojo(memberJsonObject.toString());

        return password.equals(member.getPassword());

    }


    public Boolean checkPassword(String currentPassword){

        String userEmail = request.getSession().getAttribute("userID").toString();

        Member member = generalService.getMember(userEmail);

        if (member.getPassword().equals(currentPassword)){
            return true;
        }

        return false;
    }


    public Boolean matchPassword(String newPassword, String repeatPassword){
        
        return newPassword.equals(repeatPassword);

    }


    public void changePassword(String newPassword){
        
        String userEmail = request.getSession().getAttribute("userID").toString();

        Member member = generalService.getMember(userEmail);

        member.setPassword(newPassword);

        generalService.updateMember(member);

    }

}
