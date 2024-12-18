package sg.edu.nus.iss.mini_project.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${admin.email}")
    private String adminEmail;

    
    public String getMemberJson(String email){
        return redisRepo.getValue(Constant.MEMBER_KEY, email).toString();
    }


    public Member getMemberPojo(String email){

        String foundMemberJson = getMemberJson(email);

        Member foundMember = memberSerializer.jsonToPojo(foundMemberJson);

        return foundMember;
    }


    public List<Member> getAllMembersPojo(){

        List<Member> members = new ArrayList<>();

        List<Object> memberMap = redisRepo.getAllValues(Constant.MEMBER_KEY);

        for (Object memberObject : memberMap){
            Member m = memberSerializer.jsonToPojo(memberObject.toString());
                if (m.getEmail().equals(adminEmail)){
                    continue;
                }
            members.add(m);
        }

        members.sort(Comparator.comparing(Member::getFirstName));

        return members;
    }


    public List<String> getAllMembersJson(){

        Map<Object, Object> entries = redisRepo.getEntries(Constant.MEMBER_KEY);

        List<String> membersJson = new ArrayList<>();

        for (Map.Entry<Object, Object> entry: entries.entrySet()){
            String memberJson = entry.getValue().toString();
            membersJson.add(memberJson);
        }

        return membersJson;

    }


    public void updateMember(Member member){

        String memberJson = memberSerializer.pojoToJson(member);

        redisRepo.saveValue(Constant.MEMBER_KEY, member.getEmail(), memberJson);
        
    }


    public String getFullName(String email){

        Member member = getMemberPojo(email);

        String fullName = member.getFirstName() + " " + member.getLastName();

        return fullName;
    }


    public Boolean memberExists(String memberID){
        return redisRepo.valueExists(Constant.MEMBER_KEY, memberID);
    }

}
