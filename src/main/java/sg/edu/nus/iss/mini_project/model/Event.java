package sg.edu.nus.iss.mini_project.model;

import java.time.Duration;
import java.time.LocalDateTime;
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
    
        @NotBlank(message = "Please enter an event name")
    private String eventName;

        @NotBlank(message = "Please write a short description of your event")
        @Size(max = 250, message = "Description cannot be Doubleer than 250 characters")
    private String description;


    private String hostName;

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

        @NotBlank(message = "Please provide a valid location for your event")
    private String location;

        @NotNull(message = "Please set a capacity for your event")
        @Min(value = 3, message = "Minimum capacity for an event is 3 participants")
        @Max(value = 999, message = "Maximum capacity for an event is 999 participants")
        @Digits(integer = 3, fraction = 0, message = "Capacity must be a whole number")
    private Double capacity;

    private List<String> attendees;
    
    
    public Event() {
    }


    public Event(String eventName, String description, String hostName, String hostContact, LocalDateTime startTime, Double durationHours, Double durationMinutes, String location, Double capacity) {
        this.eventID = UUID.randomUUID().toString();
        this.eventName = eventName;
        this.description = description;
        this.hostName = hostName;
        this.hostContact = hostContact;
        this.startTime = startTime;
        this.durationHours = durationHours;
        this.durationMinutes = durationMinutes;
        this.endTime = calculateEndTime(startTime, durationHours, durationMinutes);
        this.location = location;
        this.capacity = capacity;
        this.attendees = null;
    }


    private LocalDateTime calculateEndTime(LocalDateTime startTime, Double durationHours, Double durationMinutes) {
        long hours = durationHours.longValue();
        long minutes = durationMinutes.longValue();
        Duration duration = Duration.ofHours(hours).plusMinutes(minutes);
        return startTime.plus(duration);
    }


    @Override
    public String toString() {
        return eventID + "," + eventName + "," + description + "," + hostName + "," + hostContact + "," + startTime + "," + durationHours + "," + durationMinutes + "," + endTime + "," + location + "," + capacity + "," + attendees;
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


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
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

    
}
