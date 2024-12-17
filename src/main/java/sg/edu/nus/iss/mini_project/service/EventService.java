package sg.edu.nus.iss.mini_project.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.mini_project.constant.Constant;
import sg.edu.nus.iss.mini_project.model.Event;
import sg.edu.nus.iss.mini_project.model.Member;
import sg.edu.nus.iss.mini_project.repository.RedisRepo;
import sg.edu.nus.iss.mini_project.serializers.EventSerializer;
import sg.edu.nus.iss.mini_project.serializers.MemberSerializer;

@Service
public class EventService {
    
    @Autowired
    RedisRepo redisRepo;

    @Autowired
    EventSerializer eventSerializer;

    @Autowired
    MemberSerializer memberSerializer;

    @Autowired
    MemberService memberService;
    

    public void saveNewEvent(Event event, String hostEmail){

        String eventJson = eventSerializer.pojoToJson(event);

        redisRepo.saveValue(Constant.EVENT_KEY, event.getEventID(), eventJson);

        // add to the member's array of hosted events
        Member member = memberService.getMember(hostEmail);
        List<String> hostingEvents = member.getHostingEvents();
        hostingEvents.add(event.getEventID());
        member.setHostingEvents(hostingEvents);

        String memberJson = memberSerializer.pojoToJson(member);

        redisRepo.saveValue(Constant.MEMBER_KEY, hostEmail, memberJson);
        
    }


    public List<Event> getHostingEvents(String hostEmail){

        Member host = memberService.getMember(hostEmail);

        List<String> hostingEventsString = host.getHostingEvents();

        List<Event> hostingEvents = new ArrayList<>();

        for (String eventID : hostingEventsString){
            String eventJsonString = getEventJson(eventID.replace("\"", ""));
            Event event = eventSerializer.jsonToPojo(eventJsonString);
            hostingEvents.add(event);
        }

        hostingEvents.sort(Comparator.comparing(Event::getStartTime));

        return hostingEvents;

    }


    public String getEventJson(String eventID){
        return redisRepo.getValue(Constant.EVENT_KEY, eventID).toString();
    }

}
