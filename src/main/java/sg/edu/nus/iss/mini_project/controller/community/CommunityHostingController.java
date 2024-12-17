package sg.edu.nus.iss.mini_project.controller.community;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import sg.edu.nus.iss.mini_project.model.Event;

@Controller
@RequestMapping("/community/hosting")
public class CommunityHostingController {
    
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    
    @GetMapping()
    public String hostingPage(){
        return "community/hosting";
    }



    @GetMapping("/new")
    public String hostNewEvent(Model model){
        Event event = new Event();
        model.addAttribute("event", event);
        model.addAttribute("apiKey", googleMapsApiKey);
        return "community/newEvent";
    }



    @PostMapping("/new")
    public String handleNewEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, Model model){
        
        if (result.hasErrors()){
            model.addAttribute("event", event);
            return "community/newEvent";
        }

        return null;
    }

}
