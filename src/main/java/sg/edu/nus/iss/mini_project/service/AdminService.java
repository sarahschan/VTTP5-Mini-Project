package sg.edu.nus.iss.mini_project.service;

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

    @Autowired
    MemberService memberService;

    @Autowired
    EventService eventService;

    @Value("${admin.email}")
    private String adminEmail;

    
    public void saveNewMember(String firstName, String lastName, String email){

        String defaultPassword = "default";

        Member newMember = new Member(nameService.formatName(firstName), nameService.formatName(lastName), email.trim().toLowerCase(), defaultPassword);

        String newMemberJson = memberSerializer.pojoToJson(newMember);

        redisRepo.saveValue(Constant.MEMBER_KEY, email.trim().toLowerCase(), newMemberJson.toString());

    }


    public Boolean deleteMember(String email){

        Member member = memberService.getMemberPojo(email);

        if (member.getHostingEvents() != null){
            for (String eventID : member.getHostingEvents()){
                String eventIDClean = eventID.replace("\"", "");
                eventService.fullDeleteEvent(eventIDClean);
            }
        }


        if (member.getAttendingEvents() != null){
            for (String eventID : member.getAttendingEvents()){
                String eventIDClean = eventID.replace("\"", "");
                eventService.removeAttendanceFromEvent(email, eventIDClean);
            }
        }


        Long deleted = redisRepo.delete(Constant.MEMBER_KEY, email);

        if (deleted == 1) {
            return true;
        }

        return false;
    }

}