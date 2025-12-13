package models;

import java.sql.Date;

/**
 *
 * @author THINKPAD
 */
public class Event {
    private int eventId;
    private int organizerId;
    private String name;
    private String description;
    private Date eventDate;
    private String location;
    private int maxParticipants;
    private Date registrationDeadline;
    private String status;

    public Event() {
    }

    public Event(int eventId, int organizerId, String name, String description, 
                 Date eventDate, String location, int maxParticipants, 
                 Date registrationDeadline, String status) {
        this.eventId = eventId;
        this.organizerId = organizerId;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.maxParticipants = maxParticipants;
        this.registrationDeadline = registrationDeadline;
        this.status = status;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    // Helper method to check if event has started
    public boolean hasStarted() {
        if (eventDate == null) {
            return false;
        }
        java.util.Date today = new java.util.Date();
        return eventDate.before(today) || eventDate.equals(today);
    }
}

