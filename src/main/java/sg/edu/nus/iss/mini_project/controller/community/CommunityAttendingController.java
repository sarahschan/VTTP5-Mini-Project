package sg.edu.nus.iss.mini_project.controller.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.mini_project.model.Event;
import sg.edu.nus.iss.mini_project.service.EventService;

@Controller
@RequestMapping("/community/attending")
public class CommunityAttendingController {
        
    @Value("${google.api.key}")
    String googleApiKey;
    
    @Autowired
    EventService eventService;


    @GetMapping()
    public String attendingPage(Model model, HttpSession session){

        String userID = session.getAttribute("userID").toString();
        List<Event> attendingEvents = eventService.getRegisteredEvents(userID);
        model.addAttribute("attendingEvents", attendingEvents);
        model.addAttribute("googleApiKey", googleApiKey);
        return "community/attending";

    }



    @GetMapping("/remove/{eventID}")
    public String removeAttendance(@PathVariable("eventID") String eventID, Model model){

        Event event = eventService.getEventPojo(eventID);
        model.addAttribute("event", event);

        return "community/removeAttendance";
    }


    @GetMapping("/remove/confirm/{eventID}")
    public String removeAttendance(@PathVariable("eventID") String eventID, Model model, HttpSession session){

        try {

            String userID = session.getAttribute("userID").toString();
            eventService.removeAttendanceFromEvent(userID, eventID);
            eventService.removeAttendanceFromUser(userID, eventID);
            return "redirect:/community/attending/removed/" + eventID;

        } catch (Exception e){
            e.printStackTrace();
            return "redirect:/error";
        }
    }


    @GetMapping("/removed/{eventID}")
    public String removed(@PathVariable("eventID") String eventID, Model model){

        Event event = eventService.getEventPojo(eventID);
        model.addAttribute("event", event);
        return "community/removeAttendanceConfirm";
    }

}
