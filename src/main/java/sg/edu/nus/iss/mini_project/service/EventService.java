package sg.edu.nus.iss.mini_project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

        Member member = memberService.getMemberPojo(hostEmail);
        List<String> hostingEvents = member.getHostingEvents();
        hostingEvents.add(event.getEventID());
        member.setHostingEvents(hostingEvents);

        String memberJson = memberSerializer.pojoToJson(member);

        redisRepo.saveValue(Constant.MEMBER_KEY, hostEmail, memberJson);
        
    }


    public List<Event> getHostingEvents(String hostEmail){

        Member host = memberService.getMemberPojo(hostEmail);

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
    
            Member member = memberService.getMemberPojo(userID);
            List<String> attendingEvents = member.getAttendingEvents();
            attendingEvents.add(eventID);
            String memberJson = memberSerializer.pojoToJson(member);
            redisRepo.saveValue(Constant.MEMBER_KEY, userID, memberJson);
    
            return true;

        } catch (Exception e) {
    
            Event event = getEventPojo(eventID);
            List<String> attendees = event.getAttendees();
            attendees.remove(userID);
            String jsonEvent = eventSerializer.pojoToJson(event);
            redisRepo.saveValue(Constant.EVENT_KEY, eventID, jsonEvent);
    
            return false;
        }
    
    }


    public List<Event> getRegisteredEvents(String userID){

        Member member = memberService.getMemberPojo(userID);

        List<String> registeredString = member.getAttendingEvents();

        List<Event> registeredEvents = new ArrayList<>();

        for (String s : registeredString){
            String eventID = s.replace("\"", "");
            Event event = getEventPojo(eventID);
            registeredEvents.add(event);
        }

        registeredEvents.sort(Comparator.comparing(Event::getStartTime));

        return registeredEvents;
    }


    public void removeAttendanceFromUser(String userID, String eventID) {

        Member member = memberService.getMemberPojo(userID);
        List<String> attendingEvents = member.getAttendingEvents();

        attendingEvents.removeIf(id -> id.replace("\"", "").equals(eventID));
    
        member.setAttendingEvents(attendingEvents);
        String memberJson = memberSerializer.pojoToJson(member);
        redisRepo.saveValue(Constant.MEMBER_KEY, userID, memberJson);

    }


    public void removeAttendanceFromEvent(String userID, String eventID){

        Event event = getEventPojo(eventID);
        List<String> attendees = event.getAttendees();
        attendees.remove(userID);
        Double registered = event.getRegistered();

        event.setAttendees(attendees);
        event.setRegistered(registered - 1);

        String eventJson = eventSerializer.pojoToJson(event);
        redisRepo.saveValue(Constant.EVENT_KEY, eventID, eventJson);

    }


    public void removeHostEvent(String eventID){

        Event event = getEventPojo(eventID);
        String hostID = event.getHostEmail();
        Member host = memberService.getMemberPojo(hostID);

        List<String> hostingEvents = host.getHostingEvents();

        hostingEvents.removeIf(id -> id.replace("\"", "").equals(eventID));

        host.setHostingEvents(hostingEvents);
        String memberJson = memberSerializer.pojoToJson(host);
        redisRepo.saveValue(Constant.MEMBER_KEY, hostID, memberJson);

    }


    public void fullDeleteEvent(String eventID){

        Event event = getEventPojo(eventID);
        
        List<String> attendees = event.getAttendees();
        for (String attendee : attendees){
            removeAttendanceFromUser(attendee, eventID);
        }

        removeHostEvent(eventID);

        redisRepo.delete(Constant.EVENT_KEY, eventID);

    }


    // @Scheduled(fixedRate = 120000)  // 2mins
    @Scheduled(fixedRate = 3600000) // 60mins
    public void scheduledCleanUp(){

        System.out.println("Running scheduled clean up.....");

        Set<Object> eventIDs = redisRepo.getAllKeys(Constant.EVENT_KEY);
        
        for (Object obj : eventIDs){
            
            String eventID = obj.toString();
            Event event = getEventPojo(eventID);

            if (event.getEndTime().isBefore(LocalDateTime.now())){
                fullDeleteEvent(eventID);
                System.out.println("Cleaned up event: " + eventID);
            }

        }

    }


    public Boolean eventExists(String eventID){
        return redisRepo.valueExists(Constant.EVENT_KEY, eventID);
    }


    public String eventJsonString(String eventID){
        
        Event event = getEventPojo(eventID);
        String eventJson = eventSerializer.pojoToJson(event);
        
        return eventJson;

    }


    public List<String> getAllEventsJson(){
        
        Map<Object, Object> entries = redisRepo.getEntries(Constant.EVENT_KEY);

        List<String> eventsJson = new ArrayList<>();

        for (Map.Entry<Object, Object> entry: entries.entrySet()){
            String eventJson = entry.getValue().toString();
            eventsJson.add(eventJson);
        }

        return eventsJson;

    }

}
