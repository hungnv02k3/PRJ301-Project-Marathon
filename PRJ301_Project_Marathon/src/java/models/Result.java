package models;

import java.sql.Timestamp;

/**
 *
 * @author THINKPAD
 */
public class Result {
    private int resultId;
    private int registrationId;
    private Timestamp startTime;
    private Timestamp finishTime;
    private Integer netTime; // in seconds
    private Integer rankingOverall;
    
    // Additional fields for display
    private String bibNumber;
    private String runnerName;
    private String eventName;

    public Result() {
    }

    public Result(int resultId, int registrationId, Timestamp startTime, 
                 Timestamp finishTime, Integer netTime, Integer rankingOverall) {
        this.resultId = resultId;
        this.registrationId = registrationId;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.netTime = netTime;
        this.rankingOverall = rankingOverall;
    }

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

    public Integer getNetTime() {
        return netTime;
    }

    public void setNetTime(Integer netTime) {
        this.netTime = netTime;
    }

    public Integer getRankingOverall() {
        return rankingOverall;
    }

    public void setRankingOverall(Integer rankingOverall) {
        this.rankingOverall = rankingOverall;
    }

    public String getBibNumber() {
        return bibNumber;
    }

    public void setBibNumber(String bibNumber) {
        this.bibNumber = bibNumber;
    }

    public String getRunnerName() {
        return runnerName;
    }

    public void setRunnerName(String runnerName) {
        this.runnerName = runnerName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    // Helper method to format net time as HH:MM:SS
    public String getFormattedNetTime() {
        if (netTime == null) {
            return "N/A";
        }
        int hours = netTime / 3600;
        int minutes = (netTime % 3600) / 60;
        int seconds = netTime % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

