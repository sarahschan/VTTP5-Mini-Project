package sg.edu.nus.iss.mini_project.restcontroller;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.mini_project.service.EventService;

@RestController
@RequestMapping("/api/events")
public class EventRestController {
    
    @Autowired
    EventService eventService;

    @GetMapping(path="{eventID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getEvent(@PathVariable String eventID){
        
        HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

        try {

            if (eventService.eventExists(eventID)){

                String eventJson = eventService.eventJsonString(eventID);

                return ResponseEntity.status(200).headers(headers).body(eventJson);

            } else {

                JsonObject errorMsg = Json.createObjectBuilder()
                    .add("error", "Cannot find event " + eventID)
                    .build();

                return ResponseEntity.status(404).headers(headers).body(errorMsg.toString());

            }

        } catch (Exception e){

            JsonObject errorMsg = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).headers(headers).body(errorMsg.toString());
        }
    }


    @GetMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllEvents(){

        HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

        try {

            List<String> allEventsJson = eventService.getAllEventsJson();

            JsonArrayBuilder eventsArrayBuilder = Json.createArrayBuilder();
            for (String eventJsonString : allEventsJson){
                JsonObject eventJson = Json.createReader(new StringReader(eventJsonString)).readObject();
                eventsArrayBuilder.add(eventJson);
            }

            JsonObject eventsJson = Json.createObjectBuilder()
                .add("events", eventsArrayBuilder.build())
                .build();

            return ResponseEntity.status(200).headers(headers).body(eventsJson.toString());

        } catch(Exception e){

            JsonObject errorMsg = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).headers(headers).body(errorMsg.toString());
        }
    }


}
