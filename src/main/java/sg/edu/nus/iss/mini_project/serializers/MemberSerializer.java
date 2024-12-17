package sg.edu.nus.iss.mini_project.serializers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import sg.edu.nus.iss.mini_project.model.Member;

@Component
public class MemberSerializer {
    
    public String pojoToJson(Member member){

        List<String> hostingEvents = (member.getHostingEvents() != null) ? member.getHostingEvents() : new ArrayList<>();
        List<String> attendingEvents = (member.getAttendingEvents() != null) ? member.getAttendingEvents() : new ArrayList<>();

        JsonArrayBuilder hostingEventsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder attendingEventsArrayBuilder = Json.createArrayBuilder();

        for (String eventId : hostingEvents) {
            hostingEventsArrayBuilder.add(Json.createValue(eventId.replace("\"", "")));
        }
    
        for (String eventId : attendingEvents) {
            attendingEventsArrayBuilder.add(Json.createValue(eventId.replace("\"", "")));
        }
        
        JsonObjectBuilder memberJsonObjectBuilder = Json.createObjectBuilder()
            .add("firstName", member.getFirstName())
            .add("lastName", member.getLastName())
            .add("email", member.getEmail())
            .add("password", member.getPassword())
            .add("hostingEvents", hostingEventsArrayBuilder)
            .add("attendingEvents", attendingEventsArrayBuilder);

        JsonObject memberJsonObject = memberJsonObjectBuilder.build();

        return memberJsonObject.toString();

    }


    public Member jsonToPojo(String memberJson){
        
        JsonObject jsonObject = Json.createReader(new StringReader(memberJson)).readObject();

        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");

        // handle hostingEvents and attending Events, which may or may not be filled
        List<String> hostingEvents = new ArrayList<>();
        JsonArray hostingEventsArray = jsonObject.getJsonArray("hostingEvents");
        for (JsonValue value : hostingEventsArray) {
            hostingEvents.add(value.toString()); // Convert each element to a String
        }
        
        List<String> attendingEvents = new ArrayList<>();
        JsonArray attendingEventsArray = jsonObject.getJsonArray("attendingEvents");
        for (JsonValue value : attendingEventsArray) {
            attendingEvents.add(value.toString()); // Convert each element to a String
        }

        Member member = new Member(firstName, lastName, email, password, hostingEvents, attendingEvents);

        return member;
    }

}
