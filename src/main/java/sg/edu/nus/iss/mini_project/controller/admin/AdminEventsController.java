package sg.edu.nus.iss.mini_project.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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


}
