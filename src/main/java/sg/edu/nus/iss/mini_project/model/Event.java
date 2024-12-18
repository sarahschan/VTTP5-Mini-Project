package sg.edu.nus.iss.mini_project.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Event {
    
    private String eventID;
    
        @Size(min = 5, max = 250, message = "Event name must be between 5 and 100 characters")
    private String eventName;

        @NotBlank(message = "Please write a short description of your event")
        @Size(max = 250, message = "Description cannot be more than than 250 characters")
    private String description;


    private String hostName;

    private String hostEmail;

        @NotBlank(message = "Please provide your phone number or Telegram handle")
        @Pattern(regexp = "^(9\\d{7}|8\\d{7}|@.*)$", message = "Please provide a valid phone number (9xxxxxxx or 8xxxxxxx) or a Telegram handle (starting with '@')")
    private String hostContact;

        @NotNull(message = "Please set a start date and time for your event")
        @Future(message = "Event start time must be in the future")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

        @NotNull(message = "Please set a duration for your event")
        @Min(value = 1, message = "Event duration must be at least 1 hour")
        @Max(value = 12, message = "Event duration cannot exceed 12 hours")
        @Digits(integer = 2, fraction = 0, message = "Hours must be a whole number")
    private Double durationHours;

        @NotNull(message = "Please set the minutes duration for your event")
        @Min(value = 0, message = "Duration minutes cannot be negative")
        @Max(value = 59, message = "Duration minutes cannot exceed 59")
        @Digits(integer = 2, fraction = 0, message = "Minutes must be a whole number")
    private Double durationMinutes;

    private LocalDateTime endTime;

        @Pattern(regexp = "^\\d{6}$", message = "Please provide a valid Singapore postal code for your event")
    private String postalCode;

    private Double latitude;

    private Double longitude;

        @NotNull(message = "Please set a capacity for your event")
        @Min(value = 3, message = "Minimum capacity for an event is 3 participants")
        @Max(value = 999, message = "Maximum capacity for an event is 999 participants")
        @Digits(integer = 3, fraction = 0, message = "Capacity must be a whole number")
    private Double capacity;

    private Double registered;

    private List<String> attendees;

    private String formattedStartTime;
    
    private String formattedEndTime;
    
    
    public Event() {
    }


    public Event(String eventName, String description, String hostName, String hostEmail, String hostContact, LocalDateTime startTime, Double durationHours, Double durationMinutes, String postalCode, Double latitude, Double longitude, Double capacity) {
        this.eventID = UUID.randomUUID().toString();
        this.eventName = eventName;
        this.description = description;
        this.hostName = hostName;
        this.hostEmail = hostEmail;
        this.hostContact = hostContact;
        this.startTime = startTime;
        this.durationHours = durationHours;
        this.durationMinutes = durationMinutes;
        this.endTime = calculateEndTime(startTime, durationHours, durationMinutes);
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacity = capacity;
        this.registered = 0.0;
        this.attendees = null;
        this.formattedStartTime = formatTime(startTime);
        this.formattedEndTime = formatTime(endTime);
    }


    


    public Event(String eventID, String eventName, String description, String hostName, String hostEmail, String hostContact, LocalDateTime startTime, Double durationHours, Double durationMinutes, LocalDateTime endTime, String postalCode, Double latitude, Double longitude, Double capacity, Double registered, List<String> attendees, String formattedStartTime, String formattedEndTime) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.description = description;
        this.hostName = hostName;
        this.hostEmail = hostEmail;
        this.hostContact = hostContact;
        this.startTime = startTime;
        this.durationHours = durationHours;
        this.durationMinutes = durationMinutes;
        this.endTime = endTime;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacity = capacity;
        this.registered = registered;
        this.attendees = attendees;
        this.formattedStartTime = formattedStartTime;
        this.formattedEndTime = formattedEndTime;
    }


    private LocalDateTime calculateEndTime(LocalDateTime startTime, Double durationHours, Double durationMinutes) {
        long hours = durationHours.longValue();
        long minutes = durationMinutes.longValue();
        Duration duration = Duration.ofHours(hours).plusMinutes(minutes);
        return startTime.plus(duration);
    }

    private String formatTime(LocalDateTime localDateTime){
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy (hh:mm a)");

        return localDateTime.format(formatter);
    }


    @Override
    public String toString() {
        return eventID + "," + eventName + "," + description + "," + hostName + "," + hostEmail + "," + hostContact + "," + startTime + "," + durationHours + "," + durationMinutes + "," + endTime + "," + postalCode + "," + latitude + "," + longitude + "," + capacity + "," + registered + "," + attendees+ "," + formattedStartTime + "," + formattedEndTime;
    }


    public String getEventID() {
        return eventID;
    }


    public void setEventID(String eventID) {
        this.eventID = eventID;
    }


    public String getEventName() {
        return eventName;
    }


    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getHostName() {
        return hostName;
    }


    public void setHostName(String hostName) {
        this.hostName = hostName;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }


    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


    public Double getDurationHours() {
        return durationHours;
    }


    public void setDurationHours(Double durationHours) {
        this.durationHours = durationHours;
    }


    public Double getDurationMinutes() {
        return durationMinutes;
    }


    public void setDurationMinutes(Double durationMinutes) {
        this.durationMinutes = durationMinutes;
    }


    public LocalDateTime getEndTime() {
        return endTime;
    }


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    public Double getCapacity() {
        return capacity;
    }


    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }


    public List<String> getAttendees() {
        return attendees;
    }


    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }


    public String getHostContact() {
        return hostContact;
    }


    public void setHostContact(String hostContact) {
        this.hostContact = hostContact;
    }


    public String getPostalCode() {
        return postalCode;
    }


    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    public Double getLatitude() {
        return latitude;
    }


    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }


    public Double getLongitude() {
        return longitude;
    }


    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public String getHostEmail() {
        return hostEmail;
    }


    public void setHostEmail(String hostEmail) {
        this.hostEmail = hostEmail;
    }


    public String getFormattedStartTime() {
        return formattedStartTime;
    }


    public void setFormattedStartTime(String formattedStartTime) {
        this.formattedStartTime = formattedStartTime;
    }


    public String getFormattedEndTime() {
        return formattedEndTime;
    }


    public void setFormattedEndTime(String formattedEndTime) {
        this.formattedEndTime = formattedEndTime;
    }


    public Double getRegistered() {
        return registered;
    }


    public void setRegistered(Double registered) {
        this.registered = registered;
    }
    
    
}
