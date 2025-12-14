package models;

import java.sql.Date;

/**
 *
 * @author THINKPAD
 */
public class Registration {
    private int registrationId;
    private int eventId;
    private int runnerId;
    private Date registrationDate;
    private String bibNumber;
    private String status;
    
    // Additional fields for display
    private String runnerName;
    private String runnerEmail;
    private String runnerPhone;
    private String eventName;

    public Registration() {
    }

    public Registration(int registrationId, int eventId, int runnerId, 
                       Date registrationDate, String bibNumber, String status) {
        this.registrationId = registrationId;
        this.eventId = eventId;
        this.runnerId = runnerId;
        this.registrationDate = registrationDate;
        this.bibNumber = bibNumber;
        this.status = status;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
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

    public String getRunnerName() {
        return runnerName;
    }

    public void setRunnerName(String runnerName) {
        this.runnerName = runnerName;
    }

    public String getRunnerEmail() {
        return runnerEmail;
    }

    public void setRunnerEmail(String runnerEmail) {
        this.runnerEmail = runnerEmail;
    }

    public String getRunnerPhone() {
        return runnerPhone;
    }

    public void setRunnerPhone(String runnerPhone) {
        this.runnerPhone = runnerPhone;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}




