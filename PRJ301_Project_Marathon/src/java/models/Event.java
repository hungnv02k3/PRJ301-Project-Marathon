package models;


import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author THINKPAD
 */
public class Event {
    private int eventId;
    private int organizerId;
    private String eventName;
    private Date eventDate;
    private Timestamp eventStartTime; 
    private String location;
    private int maxParticipants;
    private Date registrationDeadline;
    private String status;
    // getters + setters
    public Event(int eventId, int organizerId, String eventName, String location, Date eventDate, String status, int maxParticipants, Date registrationDeadline, String status1) {
        this.eventId = eventId;
        this.organizerId = organizerId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.location = location;
        this.registrationDeadline = registrationDeadline;
        this.maxParticipants = maxParticipants;
        this.status = status;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public Event() {
    }


    public Timestamp getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Timestamp eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
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
    
    public boolean hasStarted() {
        if (eventStartTime != null) {
            java.util.Date now = new java.util.Date();
            return eventStartTime.before(now) || eventStartTime.equals(now);
        }
        if (eventDate == null) {
            return false;
        }
        java.util.Date today = new java.util.Date();
        return eventDate.before(today) || eventDate.equals(today);
    }
}



