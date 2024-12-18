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
import sg.edu.nus.iss.mini_project.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberRestController {
    
    @Autowired
    MemberService memberService;

    @GetMapping(path="{memberID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getEvent(@PathVariable String memberID){
        
        HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

        try {

            if (memberService.memberExists(memberID)){

                String eventJson = memberService.getMemberJson(memberID);

                return ResponseEntity.status(200).headers(headers).body(eventJson);

            } else {

                JsonObject errorMsg = Json.createObjectBuilder()
                    .add("error", "Cannot find member " + memberID)
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
    public ResponseEntity<String> getAllMembers(){

        HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

        try {

            List<String> allMembersJson = memberService.getAllMembersJson();

            JsonArrayBuilder membersArrayBuilder = Json.createArrayBuilder();
            for (String memberJsonString : allMembersJson){
                JsonObject memberJson = Json.createReader(new StringReader(memberJsonString)).readObject();
                membersArrayBuilder.add(memberJson);
            }

            JsonObject membersJson = Json.createObjectBuilder()
                .add("Members", membersArrayBuilder.build())
                .build();

            return ResponseEntity.status(200).headers(headers).body(membersJson.toString());

        } catch(Exception e){

            JsonObject errorMsg = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).headers(headers).body(errorMsg.toString());
        }
    }
}
