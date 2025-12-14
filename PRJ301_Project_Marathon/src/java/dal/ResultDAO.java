package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
            String sqlStatement = "SELECT res.*, r.bib_number, ru.full_name as runner_name, e.name as event_name "
                               + "FROM Results res "
                               + "INNER JOIN Registrations r ON res.registration_id = r.registration_id "
                               + "INNER JOIN Runners ru ON r.runner_id = ru.runner_id "
                               + "INNER JOIN Events e ON r.event_id = e.event_id "
                               + "WHERE e.event_id = ? ORDER BY res.ranking_overall ASC, res.net_time ASC";
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
            String sqlStatement = "SELECT res.*, r.bib_number, ru.full_name as runner_name, e.name as event_name "
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
                try {
                    java.sql.ResultSetMetaData metaData = eventStartRs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    boolean hasEventStartTime = false;
                    for (int i = 1; i <= columnCount; i++) {
                        if ("event_start_time".equalsIgnoreCase(metaData.getColumnName(i))) {
                            hasEventStartTime = true;
                            break;
                        }
                    }
                    if (hasEventStartTime) {
                        startTime = eventStartRs.getTimestamp("event_start_time");
                    }
                } catch (Exception e) {
                    // Column doesn't exist
                }
                // If event_start_time is null or column doesn't exist, use event_date at 00:00:00
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
                
                String getResultsSql = "SELECT registration_id FROM Results res "
                                     + "INNER JOIN Registrations r ON res.registration_id = r.registration_id "
                                     + "WHERE r.event_id = ? AND res.net_time IS NOT NULL "
                                     + "ORDER BY res.net_time ASC";
                PreparedStatement getResultsStm = connection.prepareStatement(getResultsSql);
                getResultsStm.setInt(1, eventId);
                ResultSet resultsRs = getResultsStm.executeQuery();
                
                int rank = 1;
                String updateRankSql = "UPDATE Results SET ranking_overall = ? WHERE registration_id = ?";
                PreparedStatement updateRankStm = connection.prepareStatement(updateRankSql);
                
                while (resultsRs.next()) {
                    int regId = resultsRs.getInt("registration_id");
                    updateRankStm.setInt(1, rank);
                    updateRankStm.setInt(2, regId);
                    updateRankStm.executeUpdate();
                    rank++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

