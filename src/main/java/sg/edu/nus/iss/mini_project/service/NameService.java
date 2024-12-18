package sg.edu.nus.iss.mini_project.service;

import org.springframework.stereotype.Service;

@Service
public class NameService {
    
    public String formatName(String name) {

        if (name == null || name.isEmpty()) {
            return name;
        }
    
        name = name.replaceAll("[^a-zA-Z\\s]", " ").trim();
    
        String[] parts = name.trim().toLowerCase().split("\\s+");
        StringBuilder capitalized = new StringBuilder();
    
        for (String part : parts) {
            if (!part.isEmpty()) {
                capitalized.append(Character.toUpperCase(part.charAt(0)))
                           .append(part.substring(1))
                           .append(" ");
            }
        }
    
        return capitalized.toString().trim();

    }
    
}
