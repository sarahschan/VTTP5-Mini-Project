package sg.edu.nus.iss.mini_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
