/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.Date;

/**
 *
 * @author User
 */
public class Registration {

    private int eventId;
    private int runnerId;
    private Date registrationDate;
    private String bibNumber;
    private String status;

    public Registration() {
    }

    public Registration(int eventId, int runnerId, Date registrationDate, String bibNumber, String status) {
        this.eventId = eventId;
        this.runnerId = runnerId;
        this.registrationDate = registrationDate;
        this.bibNumber = bibNumber;
        this.status = status;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getRunnerId() {
        return runnerId;
    }

    public void setRunnerId(int runnerId) {
        this.runnerId = runnerId;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getBibNumber() {
        return bibNumber;
    }

    public void setBibNumber(String bibNumber) {
        this.bibNumber = bibNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
