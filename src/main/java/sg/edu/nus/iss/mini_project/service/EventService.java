package sg.edu.nus.iss.mini_project.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

        List<String> hostingEventsIDs = host.getHostingEvents();

        List<Event> hostingEvents = new ArrayList<>();

        for (String s : hostingEventsIDs){
            String eventID = s.replace("\"", "");
            Event event = getEventPojo(eventID);
            hostingEvents.add(event);
        }

        hostingEvents.sort(Comparator.comparing(Event::getStartTime));

        return hostingEvents;

    }


    public String getEventJson(String eventID){
        return redisRepo.getValue(Constant.EVENT_KEY, eventID).toString();
    }


    public Event getEventPojo(String eventID){
        
        String eventJson = getEventJson(eventID);
        Event foundEvent = eventSerializer.jsonToPojo(eventJson);

        return foundEvent;
    }


    public Boolean isCapacityValid(Double capacity, Double registered){
        return capacity >= registered;
    }


    public void saveEditedEvent(Event event){

        String eventJson = eventSerializer.pojoToJson(event);

        redisRepo.saveValue(Constant.EVENT_KEY, event.getEventID(), eventJson);

    }


    public List<Event> getAllEvents(){

        Map<Object, Object> entries = redisRepo.getEntries(Constant.EVENT_KEY);

        List<Event> events = new ArrayList<>();

        for (Map.Entry<Object, Object> entry: entries.entrySet()){
            String eventJson = entry.getValue().toString();
            Event event = eventSerializer.jsonToPojo(eventJson);
            events.add(event);
        }

        events.sort(Comparator.comparing(Event::getStartTime));

        return events;

    }


    public Boolean register(String eventID, String userID){

        try {
            Event event = getEventPojo(eventID);
            List<String> attendees = event.getAttendees();
            attendees.add(userID);
            Double registered = event.getRegistered();
            event.setRegistered(registered + 1);
            String jsonEvent = eventSerializer.pojoToJson(event);
            redisRepo.saveValue(Constant.EVENT_KEY, eventID, jsonEvent);
    
            Member member = memberService.getMember(userID);
            List<String> attendingEvents = member.getAttendingEvents();
            attendingEvents.add(eventID);
            String memberJson = memberSerializer.pojoToJson(member);
            redisRepo.saveValue(Constant.MEMBER_KEY, userID, memberJson);
    
            return true;

        } catch (Exception e) {
    
            // reset event attendees in case error happened at attendingEvents
            Event event = getEventPojo(eventID);
            List<String> attendees = event.getAttendees();
            attendees.remove(userID);
            String jsonEvent = eventSerializer.pojoToJson(event);
            redisRepo.saveValue(Constant.EVENT_KEY, eventID, jsonEvent);
    
            return false;
        }
    
    }


    public List<Event> getRegisteredEvents(String userID){

        Member member = memberService.getMember(userID);

        List<String> registeredString = member.getAttendingEvents();

        List<Event> registeredEvents = new ArrayList<>();

        for (String s : registeredString){
            String eventID = s.replace("\"", "");
            Event event = getEventPojo(eventID);
            registeredEvents.add(event);
        }

        return registeredEvents;
    }

}
