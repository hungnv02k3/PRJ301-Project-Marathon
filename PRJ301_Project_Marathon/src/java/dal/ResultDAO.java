/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import models.Result;

/**
 *
 * @author THINKPAD
 */
public class ResultDAO extends DBContext {
    
    private PreparedStatement stm;
    private ResultSet rs;
    
    public List<Result> getResultsByEvent(int eventId) {
        List<Result> list = new ArrayList<>();
        try {
            String sqlStatement = "SELECT res.*, r.bib_number, ru.full_name as runner_name, e.event_name as event_name "
                               + "FROM Results res "
                               + "INNER JOIN Registrations r ON res.registration_id = r.registration_id "
                               + "INNER JOIN Runners ru ON r.runner_id = ru.runner_id "
                               + "INNER JOIN Events e ON r.event_id = e.event_id "
                               + "WHERE e.event_id = ? AND r.status = 'ACCEPTED' "
                               + "ORDER BY res.ranking_overall ASC, res.net_time ASC";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, eventId);
            rs = stm.executeQuery();
            while (rs.next()) {
                int resultId = rs.getInt("result_id");
                int registrationId = rs.getInt("registration_id");
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp finishTime = rs.getTimestamp("finish_time");
                Integer netTime = rs.getInt("net_time");
                if (rs.wasNull()) {
                    netTime = null;
                }
                Integer rankingOverall = rs.getInt("ranking_overall");
                if (rs.wasNull()) {
                    rankingOverall = null;
                }
                
                Result result = new Result(resultId, registrationId, startTime, finishTime, netTime, rankingOverall);
                result.setBibNumber(rs.getString("bib_number"));
                result.setRunnerName(rs.getString("runner_name"));
                result.setEventName(rs.getString("event_name"));
                list.add(result);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
    
    public Result getResultByRegistration(int registrationId) {
        Result result = null;
        try {
            String sqlStatement = "SELECT res.*, r.bib_number, ru.full_name as runner_name, e.event_name as event_name "
                               + "FROM Results res "
                               + "INNER JOIN Registrations r ON res.registration_id = r.registration_id "
                               + "INNER JOIN Runners ru ON r.runner_id = ru.runner_id "
                               + "INNER JOIN Events e ON r.event_id = e.event_id "
                               + "WHERE res.registration_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, registrationId);
            rs = stm.executeQuery();
            if (rs.next()) {
                int resultId = rs.getInt("result_id");
                int regId = rs.getInt("registration_id");
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp finishTime = rs.getTimestamp("finish_time");
                Integer netTime = rs.getInt("net_time");
                if (rs.wasNull()) {
                    netTime = null;
                }
                Integer rankingOverall = rs.getInt("ranking_overall");
                if (rs.wasNull()) {
                    rankingOverall = null;
                }
                
                result = new Result(resultId, regId, startTime, finishTime, netTime, rankingOverall);
                result.setBibNumber(rs.getString("bib_number"));
                result.setRunnerName(rs.getString("runner_name"));
                result.setEventName(rs.getString("event_name"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
    
    public void createOrUpdateResult(int registrationId, Timestamp finishTime) {
        try {
            String getEventStartSql = "SELECT e.event_start_time, e.event_date FROM Events e "
                                    + "INNER JOIN Registrations r ON e.event_id = r.event_id "
                                    + "WHERE r.registration_id = ?";
            PreparedStatement getEventStartStm = connection.prepareStatement(getEventStartSql);
            getEventStartStm.setInt(1, registrationId);
            ResultSet eventStartRs = getEventStartStm.executeQuery();
            
            Timestamp startTime = null;
            if (eventStartRs.next()) {
                startTime = eventStartRs.getTimestamp("event_start_time");
                if (startTime == null) {
                    Date eventDate = eventStartRs.getDate("event_date");
                    if (eventDate != null) {
                        startTime = new Timestamp(eventDate.getTime());
                    }
                }
            }
            
            if (startTime == null) {
                throw new Exception("Cannot find event start time for registration");
            }
            
            Result existing = getResultByRegistration(registrationId);
            
            if (existing == null) {
                String sqlStatement = "INSERT INTO Results (registration_id, start_time, finish_time, net_time) VALUES (?, ?, ?, ?)";
                stm = connection.prepareStatement(sqlStatement);
                stm.setInt(1, registrationId);
                stm.setTimestamp(2, startTime);
                stm.setTimestamp(3, finishTime);
                
                long diffInMillis = finishTime.getTime() - startTime.getTime();
                int netTimeSeconds = (int) (diffInMillis / 1000);
                stm.setInt(4, netTimeSeconds);
                
                stm.executeUpdate();
            } else {
                String sqlStatement = "UPDATE Results SET start_time = ?, finish_time = ?, net_time = ? WHERE registration_id = ?";
                stm = connection.prepareStatement(sqlStatement);
                stm.setTimestamp(1, startTime);
                stm.setTimestamp(2, finishTime);
                
                // Calculate net time in seconds
                long diffInMillis = finishTime.getTime() - startTime.getTime();
                int netTimeSeconds = (int) (diffInMillis / 1000);
                stm.setInt(3, netTimeSeconds);
                stm.setInt(4, registrationId);
                
                stm.executeUpdate();
            }
            
            recalculateRankings(registrationId);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void recalculateRankings(int registrationId) {
        try {
            String getEventSql = "SELECT event_id FROM Registrations WHERE registration_id = ?";
            PreparedStatement getEventStm = connection.prepareStatement(getEventSql);
            getEventStm.setInt(1, registrationId);
            ResultSet eventRs = getEventStm.executeQuery();
            
            if (eventRs.next()) {
                int eventId = eventRs.getInt("event_id");              
                String resetRankSql = "UPDATE Results SET ranking_overall = NULL "
                                    + "WHERE registration_id IN ("
                                    + "    SELECT registration_id FROM Registrations WHERE event_id = ?"
                                    + ")";
                PreparedStatement resetStm = connection.prepareStatement(resetRankSql);
                resetStm.setInt(1, eventId);
                resetStm.executeUpdate();
                
                String getResultsSql = "SELECT res.result_id, res.registration_id, res.net_time "
                                     + "FROM Results res "
                                     + "INNER JOIN Registrations r ON res.registration_id = r.registration_id "
                                     + "WHERE r.event_id = ? AND r.status = 'ACCEPTED' AND res.net_time IS NOT NULL "
                                     + "ORDER BY res.net_time ASC";
                PreparedStatement getResultsStm = connection.prepareStatement(getResultsSql);
                getResultsStm.setInt(1, eventId);
                ResultSet resultsRs = getResultsStm.executeQuery();
                
                // Assign rankings
                int rank = 1;
                String updateRankSql = "UPDATE Results SET ranking_overall = ? WHERE result_id = ?";
                PreparedStatement updateRankStm = connection.prepareStatement(updateRankSql);
                
                while (resultsRs.next()) {
                    int resultId = resultsRs.getInt("result_id");
                    updateRankStm.setInt(1, rank);
                    updateRankStm.setInt(2, resultId);
                    updateRankStm.executeUpdate();
                    rank++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error recalculating rankings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Result> getResultsByRunner(int runnerId) {
        List<Result> list = new ArrayList<>();

        String sql
                = "SELECT r.result_id, r.registration_id, r.start_time, r.finish_time, "
                + "r.net_time, r.ranking_overall, e.event_name, e.event_date, e.location "
                + "FROM Results r "
                + "JOIN Registrations reg ON r.registration_id = reg.registration_id "
                + "JOIN Events e ON reg.event_id = e.event_id "
                + "WHERE reg.runner_id = ? "
                + "ORDER BY e.event_date DESC";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, runnerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Result re = new Result();
                re.setResultId(rs.getInt("result_id"));
                re.setRegistrationId(rs.getInt("registration_id"));
                re.setStartTime(rs.getTimestamp("start_time"));
                re.setFinishTime(rs.getTimestamp("finish_time"));
                re.setNetTime(rs.getInt("net_time"));
                re.setRankingOverall(rs.getInt("ranking_overall"));

                re.setEventName(rs.getString("event_name"));
                re.setEventDate(rs.getDate("event_date"));
                re.setLocation(rs.getString("location"));

                list.add(re);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

