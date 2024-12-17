package sg.edu.nus.iss.mini_project.controller.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.mini_project.model.Event;
import sg.edu.nus.iss.mini_project.service.EventService;
import sg.edu.nus.iss.mini_project.service.LocationService;
import sg.edu.nus.iss.mini_project.service.MemberService;

@Controller
@RequestMapping("/community/hosting")
public class CommunityHostingController {
    
    @Value("${google.api.key}")
    String googleApiKey;

    @Autowired
    LocationService locationService;

    @Autowired
    MemberService memberService;

    @Autowired
    EventService eventService;

    
    @GetMapping()
    public String hostingPage(HttpSession session, Model model){

        List<Event> events = eventService.getHostingEvents(session.getAttribute("userID").toString());
        model.addAttribute("events", events);
        return "community/hosting";
    }



    @GetMapping("/new")
    public String hostNewEvent(Model model){
        Event event = new Event();
        model.addAttribute("event", event);
        return "community/newEvent";
    }



    @PostMapping("/new")
    public String handleNewEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, Model model, HttpSession session){
        
        if (!locationService.isPostalCodeValid(event.getPostalCode())){
            ObjectError postalCodeError = new ObjectError("globalError", "Postal code entered is invalid");
            result.addError(postalCodeError);
        }

        if (result.hasErrors()){
            model.addAttribute("event", event);
            return "community/newEvent";
        }

        String hostEmail = session.getAttribute("userID").toString();
        String host = memberService.getFullName(hostEmail);
        Double[] latLng = locationService.getLatAndLng(event.getPostalCode());
        Double lat = latLng[0];
        Double lng = latLng[1];

        Event newEvent = new Event(event.getEventName(), event.getDescription(), host, hostEmail, event.getHostContact(), event.getStartTime(), event.getDurationHours(), event.getDurationMinutes(), event.getPostalCode(), lat, lng, event.getCapacity());

        eventService.saveNewEvent(newEvent, hostEmail);

        return "redirect:/community/hosting/confirm";
    }



    @GetMapping("/confirm")
    public String confirmEvent(Model model){

        return "community/confirmEvent";
    }


}
