package sg.edu.nus.iss.mini_project.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.mini_project.constant.Constant;
import sg.edu.nus.iss.mini_project.model.Member;
import sg.edu.nus.iss.mini_project.repository.RedisRepo;
import sg.edu.nus.iss.mini_project.serializers.MemberSerializer;

@Service
public class AdminService {
    
    @Autowired
    RedisRepo redisRepo;

    @Autowired
    MemberSerializer memberSerializer;

    @Autowired
    NameService nameService;

    @Value("${admin.email}")
    private String adminEmail;


    public List<Member> getMembers(){

        List<Member> members = new ArrayList<>();

        // Retrieve all members stored in redis
        List<Object> memberMap = redisRepo.getAllValues(Constant.MEMBER_KEY);

        for (Object memberObject : memberMap){
            Member m = memberSerializer.jsonToPojo(memberObject.toString());
                // don't include admin in the list of members
                if (m.getEmail().equals(adminEmail)){
                    continue;
                }
            members.add(m);
        }

        members.sort(Comparator.comparing(Member::getFirstName));

        return members;
    }

    
    public void saveNewMember(String firstName, String lastName, String email){

        String defaultPassword = "default";

        Member newMember = new Member(nameService.formatName(firstName), nameService.formatName(lastName), email, defaultPassword);

        String newMemberJson = memberSerializer.pojoToJson(newMember);

        redisRepo.saveValue(Constant.MEMBER_KEY, email, newMemberJson.toString());

    }


    public Boolean deleteMember(String email){

        Long deleted = redisRepo.delete(Constant.MEMBER_KEY, email);

        if (deleted == 1) {
            return true;
        }

        return false;
    }



}
