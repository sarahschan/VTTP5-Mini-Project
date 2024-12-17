package sg.edu.nus.iss.mini_project.serializers;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import sg.edu.nus.iss.mini_project.model.Event;

@Component
public class EventSerializer {
    
    public String pojoToJson(Event event) {
        
        JsonArrayBuilder attendeesArrayBuilder = Json.createArrayBuilder();
        
        if (event.getAttendees() != null) {
            for (String attendee : event.getAttendees()) {
                attendeesArrayBuilder.add(attendee);
            }
        }
        
        JsonObjectBuilder eventJsonObjectBuilder = Json.createObjectBuilder()
            .add("eventID", event.getEventID())
            .add("eventName", event.getEventName())
            .add("description", event.getDescription())
            .add("hostName", event.getHostName())
            .add("hostEmail", event.getHostEmail())
            .add("hostContact", event.getHostContact())
            .add("startTime", event.getStartTime().toString())
            .add("durationHours", event.getDurationHours())
            .add("durationMinutes", event.getDurationMinutes())
            .add("endTime", event.getEndTime().toString())
            .add("postalCode", event.getPostalCode())
            .add("latitude", event.getLatitude())
            .add("longitude", event.getLongitude())
            .add("capacity", event.getCapacity())
            .add("attendees", attendeesArrayBuilder);
        

        JsonObject eventJsonObject = eventJsonObjectBuilder.build();

        return eventJsonObject.toString();
    }


    public Event jsonToPojo(String eventJson) {

        JsonObject jsonObject = Json.createReader(new StringReader(eventJson)).readObject();

        String eventID = jsonObject.getString("eventID");
        String eventName = jsonObject.getString("eventName");
        String description = jsonObject.getString("description");
        String hostName = jsonObject.getString("hostName");
        String hostEmail = jsonObject.getString("hostEmail");
        String hostContact = jsonObject.getString("hostContact");
        LocalDateTime startTime = LocalDateTime.parse(jsonObject.getString("startTime"));
        Double durationHours = jsonObject.getJsonNumber("durationHours").doubleValue();
        Double durationMinutes = jsonObject.getJsonNumber("durationMinutes").doubleValue();
        LocalDateTime endTime = LocalDateTime.parse(jsonObject.getString("endTime"));
        String postalCode = jsonObject.getString("postalCode");
        Double latitude = jsonObject.getJsonNumber("latitude").doubleValue();
        Double longitude = jsonObject.getJsonNumber("longitude").doubleValue();
        Double capacity = jsonObject.getJsonNumber("capacity").doubleValue();
        
        JsonArray attendeesJsonArray = jsonObject.getJsonArray("attendees");
        List<String> attendees = new ArrayList<>();
        
        for (JsonValue attendee : attendeesJsonArray) {
            attendees.add(attendee.toString().replace("\"", ""));
        }

        return new Event(eventID, eventName, description, hostName, hostEmail, hostContact, startTime, durationHours, durationMinutes, endTime, postalCode, latitude, longitude, capacity, attendees);
    }
}
