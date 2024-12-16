package sg.edu.nus.iss.mini_project.service;

import org.springframework.stereotype.Service;

@Service
public class NameService {
    
    public String formatName(String name) {

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
