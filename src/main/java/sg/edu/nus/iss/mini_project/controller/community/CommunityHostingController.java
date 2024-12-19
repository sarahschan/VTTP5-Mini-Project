package sg.edu.nus.iss.mini_project.controller.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
        model.addAttribute("googleApiKey", googleApiKey);
        model.addAttribute("events", events);
        return "community/hosting";
    }



    @GetMapping("/new")
    public String hostNewEvent(Model model){
        Event event = new Event();
        model.addAttribute("event", event);
        return "community/eventNew";
    }



    @PostMapping(path = "/new", consumes = "application/x-www-form-urlencoded")
    public String handleNewEvent(@Valid @ModelAttribute Event event, BindingResult result, Model model, HttpSession session){
        
        if (result.hasErrors() || !locationService.isPostalCodeValid(event.getPostalCode())){

            if (!locationService.isPostalCodeValid(event.getPostalCode())){
                model.addAttribute("postalCodeErrorMsg", "Postal code entered cannot be found");
            }

            model.addAttribute("event", event);
            return "community/eventNew";
        }

        String hostEmail = session.getAttribute("userID").toString();
        String host = memberService.getFullName(hostEmail);
        Double[] latLng = locationService.getLatAndLng(event.getPostalCode());
        Double lat = latLng[0];
        Double lng = latLng[1];

        Event newEvent = new Event(event.getEventName(), event.getDescription(), host, hostEmail, event.getHostContact(), event.getStartTime(), event.getDurationHours(), event.getDurationMinutes(), event.getLocation(), event.getPostalCode(), lat, lng, event.getCapacity());

        eventService.saveNewEvent(newEvent, hostEmail);

        return "redirect:/community/hosting/confirm/" + newEvent.getEventID();
    }



    @GetMapping("/confirm/{eventID}")
    public String confirmEvent(@PathVariable String eventID, Model model){
        Event event = eventService.getEventPojo(eventID);
        model.addAttribute("event", event);
        return "community/eventNewConfirm";
    }



    @GetMapping("/edit/{eventID}")
    public String editEvent(@PathVariable String eventID, Model model){
        Event event = eventService.getEventPojo(eventID);
        model.addAttribute("event", event);
        return "community/eventEdit";
    }


    @PostMapping(path = "/edit/{eventID}", consumes = "application/x-www-form-urlencoded")
    public String handleEditEvent(@Valid @ModelAttribute Event event, BindingResult result, Model model, HttpSession session){

        if (result.hasErrors() || !locationService.isPostalCodeValid(event.getPostalCode()) || !eventService.isCapacityValid(event.getCapacity(), event.getRegistered())){

            if (!locationService.isPostalCodeValid(event.getPostalCode())){
                model.addAttribute("postalCodeErrorMsg", "Postal code entered cannot be found");
            }

            if (!eventService.isCapacityValid(event.getCapacity(), event.getRegistered())){
                model.addAttribute("capacityErrorMsg", "Cannot lower capacity - There are more people already signed up");
            }

            model.addAttribute("event", event);
            return "community/eventNew";
        }

        Double[] latLng = locationService.getLatAndLng(event.getPostalCode());
        Double lat = latLng[0];
        Double lng = latLng[1];
        Event editedEvent = new Event(event.getEventID(), event.getEventName(), event.getDescription(), event.getHostName(), event.getHostEmail(), event.getHostContact(), event.getStartTime(), event.getDurationHours(), event.getDurationMinutes(), event.getLocation(), event.getPostalCode(), lat, lng, event.getCapacity(), event.getRegistered(), event.getAttendees());
        eventService.saveEditedEvent(editedEvent);

        return "redirect:/community/hosting/edit/saved/" + editedEvent.getEventID();
    }


    @GetMapping("/edit/saved/{eventID}")
    public String savedEdit(@PathVariable String eventID, Model model){
        Event event = eventService.getEventPojo(eventID);
        model.addAttribute("event", event);
        return "community/eventEditSaved";
    }



    @GetMapping("/delete/{eventID}")
    public String deleteEvent(@PathVariable String eventID, Model model){

        Event event = eventService.getEventPojo(eventID);
        model.addAttribute("event", event);

        return "community/eventDelete";
    }


    @GetMapping("/delete/confirm/{eventID}")
    public String deleteEventConfirm(@PathVariable String eventID, Model model, HttpSession session){

        try{

            session.setAttribute("eventName", eventService.getEventPojo(eventID).getEventName().toString());
            eventService.fullDeleteEvent(eventID);
            return "redirect:/community/hosting/deleted";

        } catch (Exception e){
            e.printStackTrace();
            return "redirect:/error";
        }
    }


    @GetMapping("/deleted")
    public String deleted(Model model, HttpSession session){

        String eventName = session.getAttribute("eventName").toString();
        model.addAttribute("eventName", eventName);
        return "community/eventDeleted";

    }



}