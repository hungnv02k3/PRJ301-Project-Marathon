/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Timestamp;
import java.sql.Date;

/**
 *
 * @author User
 */
public class Result {

    private int resultId;
    private int registrationId;
    private Timestamp startTime;
    private Timestamp finishTime;
    private int netTime;
    private int rankingOverall;

    // thêm thông tin hiển thị
    private String eventName;
    private Date eventDate;
    private String location;

    public Result() {
    }

    // getter / setter
    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public int getNetTime() {
        return netTime;
    }

    public void setNetTime(int netTime) {
        this.netTime = netTime;
    }

    public int getRankingOverall() {
        return rankingOverall;
    }

    public void setRankingOverall(int rankingOverall) {
        this.rankingOverall = rankingOverall;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
