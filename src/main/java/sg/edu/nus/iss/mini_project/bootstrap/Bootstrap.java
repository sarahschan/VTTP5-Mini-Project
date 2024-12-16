package sg.edu.nus.iss.mini_project.bootstrap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.mini_project.constant.Constant;
import sg.edu.nus.iss.mini_project.model.Member;
import sg.edu.nus.iss.mini_project.repository.RedisRepo;
import sg.edu.nus.iss.mini_project.serializers.MemberSerializer;

@Component
public class Bootstrap implements CommandLineRunner{
    
    @Autowired
    RedisRepo redisRepo;
    
    @Autowired
    MemberSerializer memberSerializer;
    
    @Override
    public void run(String... args) throws Exception {
            
        // Read OG member_list.csv to redis
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/member_list.csv");

        if (is == null) {
           throw new FileNotFoundException("member_list.csv file not found");
        }
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))){

            // skip header line
            br.readLine();

            // get each member's details
            String line;
            while ((line = br.readLine()) != null){
            
                String[] details = line.split(",");

                String firstName = formatName(details[0].trim());
                String lastName = formatName(details[1].trim());
                String email = details[2].trim();
                String password = details[3].trim();

                // Create member POJO
                Member member = new Member(firstName, lastName, email, password);

                // Serialize member POJO to Json String
                String memberJson = memberSerializer.pojoToJson(member);

                // Store in redis
                redisRepo.saveValue(Constant.MEMBER_KEY, email, memberJson);
            
            }
        }

        System.out.println("Saved all members to redis");
    }


    private String formatName(String name) {

        if (name == null || name.isEmpty()) {
            return name;
        }
    
        // remove any non alphabets, replace with a space
        name = name.replaceAll("[^a-zA-Z\\s]", " ").trim();
    
        String[] parts = name.trim().toLowerCase().split("\\s+"); // Split by whitespace
        StringBuilder capitalized = new StringBuilder();
    
        for (String part : parts) {
            if (!part.isEmpty()) {
                capitalized.append(Character.toUpperCase(part.charAt(0))) // Capitalize first letter
                           .append(part.substring(1)) // Append the rest of the word
                           .append(" "); // Add a space after each word
            }
        }
    
        return capitalized.toString().trim(); // Remove the trailing space

    }

}
