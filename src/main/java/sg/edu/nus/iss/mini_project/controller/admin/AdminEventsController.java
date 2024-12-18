package sg.edu.nus.iss.mini_project.controller.admin;

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
@RequestMapping("/admin/events")
public class AdminEventsController {
    
    @Value("${google.api.key}")
    String googleApiKey;

    @Autowired
    EventService eventService;

    

    @GetMapping()
    public String eventsPage(Model model){

        List<Event> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        model.addAttribute("googleApiKey", googleApiKey);
        return "admin/events";

    }


    @GetMapping("/cancel/{eventID}")
    public String eventCancel(@PathVariable String eventID, Model model){

        Event event = eventService.getEventPojo(eventID);
        model.addAttribute("event", event);
        
        return "admin/eventCancel";
    }


    @GetMapping("/cancelled/{eventID}")
    public String eventCancelled(@PathVariable String eventID, Model model, HttpSession session){

        try{

            session.setAttribute("eventName", eventService.getEventPojo(eventID).getEventName().toString());
            model.addAttribute("eventName", session.getAttribute("eventName"));
            eventService.fullDeleteEvent(eventID);
            return "admin/eventCancelled";

        } catch (Exception e){
            e.printStackTrace();
            return "redirect:/error";
        }
    }


}
