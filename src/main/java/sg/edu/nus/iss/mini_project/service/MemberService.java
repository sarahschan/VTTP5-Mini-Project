package sg.edu.nus.iss.mini_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.mini_project.constant.Constant;
import sg.edu.nus.iss.mini_project.model.Member;
import sg.edu.nus.iss.mini_project.repository.RedisRepo;
import sg.edu.nus.iss.mini_project.serializers.MemberSerializer;

@Service
public class MemberService {
    
    @Autowired
    RedisRepo redisRepo;

    @Autowired
    MemberSerializer memberSerializer;
    
    
    public Member getMember(String email){

        String foundMemberJson = redisRepo.getValue(Constant.MEMBER_KEY, email).toString();

        Member foundMember = memberSerializer.jsonToPojo(foundMemberJson);

        return foundMember;
    }


    public void updateMember(Member member){

        String memberJson = memberSerializer.pojoToJson(member);

        redisRepo.saveValue(Constant.MEMBER_KEY, member.getEmail(), memberJson);
        
    }


    public String getFullName(String email){

        Member member = getMember(email);

        String fullName = member.getFirstName() + " " + member.getLastName();

        return fullName;
    }

}
