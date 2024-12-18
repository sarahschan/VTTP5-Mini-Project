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
@RequestMapping("/community/events")
public class CommunityEventsController {
    
    @Value("${google.api.key}")
    String googleApiKey;

    @Autowired
    EventService eventService;


    @GetMapping()
    public String eventsPage(Model model){

        List<Event> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        model.addAttribute("googleApiKey", googleApiKey);
        return "community/events";

    }


    @GetMapping("/register/{eventID}")
    public String register(@PathVariable("eventID") String eventID, Model model, HttpSession session){
        
        Event event = eventService.getEventPojo(eventID);

        // check if already registered or you are hosting
        String userID = session.getAttribute("userID").toString();
        if (event.getAttendees().contains(userID) || event.getHostEmail().equals(userID)){
            return "redirect:/community/events/registered/" + eventID;
        }

        model.addAttribute("event", event);

        return "community/register";
    }


    @GetMapping("/register/confirm/{eventID}")
    public String confirmRegister(@PathVariable("eventID") String eventID, Model model, HttpSession session){
        
        String userID = session.getAttribute("userID").toString();
        
        if (eventService.register(eventID, userID)){
            return "redirect:/community/events/register/success/" + eventID;
        }

        return "errorCommunity";
    }


    @GetMapping("/register/success/{eventID}")
    public String registerSuccess(@PathVariable("eventID") String eventID, Model model){

        Event event = eventService.getEventPojo(eventID);
        model.addAttribute("event", event);

        return "community/registerConfirm";
    }


    @GetMapping("/registered/{eventID}")
    public String alreadyRegistered(@PathVariable("eventID") String eventID, Model model){

        Event event = eventService.getEventPojo(eventID);
        model.addAttribute("event", event);

        return "community/registered";
    }
}
