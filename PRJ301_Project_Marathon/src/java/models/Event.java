/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.security.Timestamp;
import java.util.Date;

/**
 *
 * @author User
 */
public class Event {

    private int eventId;
    private String eventName;
    private Date eventDate;
    private Timestamp eventStartTime;
    private String location;
    private Date registrationDeadline;
    private int maxParticipants;
    private String status;

    // getters + setters
    public Event(int eventId, String eventName, Date eventDate, Timestamp eventStartTime, String location, Date registrationDeadline, int maxParticipants, String status) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.location = location;
        this.registrationDeadline = registrationDeadline;
        this.maxParticipants = maxParticipants;
        this.status = status;
    }

    public Event() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Timestamp getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Timestamp eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
