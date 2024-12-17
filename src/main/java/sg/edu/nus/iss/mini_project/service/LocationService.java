package sg.edu.nus.iss.mini_project.service;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.mini_project.constant.Constant;

@Service
public class LocationService {
    
    RestTemplate restTemplate = new RestTemplate();

    @Value("${google.api.key}")
    private String googleApiKey;

    public Boolean isPostalCodeValid(String postalCode) {
        
        String url = String.format(Constant.POSTAL_CODE_URI, postalCode, googleApiKey);

        try {
            
            String response = restTemplate.getForObject(url, String.class);

            JsonReader jsonReader = Json.createReader(new StringReader(response));
            JsonObject responseObject = jsonReader.readObject();
            JsonArray resultsArray = responseObject.getJsonArray("results");

            JsonObject data = resultsArray.getJsonObject(0);

            if (data.containsKey("partial_match") && data.getBoolean("partial_match")) {
                return false;
            }

            return true;

        } catch (Exception e){
            return false;
        }
    }


    public Double[] getLatAndLng(String postalCode){

        String url = String.format(Constant.POSTAL_CODE_URI, postalCode, googleApiKey);

        String response = restTemplate.getForObject(url, String.class);

        JsonReader jsonReader = Json.createReader(new StringReader(response));
        JsonObject responseObject = jsonReader.readObject();

        JsonArray resultsArray = responseObject.getJsonArray("results");

        if (resultsArray.isEmpty()) {
            return null;
        }

        JsonObject firstResult = resultsArray.getJsonObject(0);

        JsonObject geometry = firstResult.getJsonObject("geometry");
        JsonObject location = geometry.getJsonObject("location");

        Double lat = location.getJsonNumber("lat").doubleValue();
        Double lng = location.getJsonNumber("lng").doubleValue();

        return new Double[] {lat,lng};

    }

}
