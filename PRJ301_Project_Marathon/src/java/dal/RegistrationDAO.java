package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Registration;

/**
 *
 * @author THINKPAD
 */
public class RegistrationDAO extends DBContext {
    
    private PreparedStatement stm;
    private ResultSet rs;
    
    public List<Registration> getRegistrationsByEvent(int eventId) {
        List<Registration> list = new ArrayList<>();
        try {
            String sqlStatement = "SELECT r.*, ru.full_name as runner_name, ru.email as runner_email, "
                               + "ru.phone as runner_phone, e.name as event_name "
                               + "FROM Registrations r "
                               + "INNER JOIN Runners ru ON r.runner_id = ru.runner_id "
                               + "INNER JOIN Events e ON r.event_id = e.event_id "
                               + "WHERE r.event_id = ? ORDER BY r.registration_date DESC";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, eventId);
            rs = stm.executeQuery();
            while (rs.next()) {
                int registrationId = rs.getInt("registration_id");
                int evtId = rs.getInt("event_id");
                int runnerId = rs.getInt("runner_id");
                java.sql.Date registrationDate = rs.getDate("registration_date");
                String bibNumber = rs.getString("bib_number");
                String status = rs.getString("status");
                
                Registration reg = new Registration(registrationId, evtId, runnerId, registrationDate, bibNumber, status);
                reg.setRunnerName(rs.getString("runner_name"));
                reg.setRunnerEmail(rs.getString("runner_email"));
                reg.setRunnerPhone(rs.getString("runner_phone"));
                reg.setEventName(rs.getString("event_name"));
                list.add(reg);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
    
    public Registration getRegistrationById(int registrationId) {
        Registration registration = null;
        try {
            String sqlStatement = "SELECT r.*, ru.full_name as runner_name, ru.email as runner_email, "
                               + "ru.phone as runner_phone, e.name as event_name "
                               + "FROM Registrations r "
                               + "INNER JOIN Runners ru ON r.runner_id = ru.runner_id "
                               + "INNER JOIN Events e ON r.event_id = e.event_id "
                               + "WHERE r.registration_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, registrationId);
            rs = stm.executeQuery();
            while (rs.next()) {
                int regId = rs.getInt("registration_id");
                int eventId = rs.getInt("event_id");
                int runnerId = rs.getInt("runner_id");
                java.sql.Date registrationDate = rs.getDate("registration_date");
                String bibNumber = rs.getString("bib_number");
                String status = rs.getString("status");
                
                registration = new Registration(regId, eventId, runnerId, registrationDate, bibNumber, status);
                registration.setRunnerName(rs.getString("runner_name"));
                registration.setRunnerEmail(rs.getString("runner_email"));
                registration.setRunnerPhone(rs.getString("runner_phone"));
                registration.setEventName(rs.getString("event_name"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return registration;
    }
    
    public void approveRegistration(int registrationId) {
        try {
            String sqlStatement = "UPDATE Registrations SET status = 'Registered' WHERE registration_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, registrationId);
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void rejectRegistration(int registrationId) {
        try {
            String sqlStatement = "UPDATE Registrations SET status = 'Rejected' WHERE registration_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, registrationId);
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public int getRegistrationCountByEvent(int eventId) {
        int count = 0;
        try {
            String sqlStatement = "SELECT COUNT(*) as count FROM Registrations WHERE event_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, eventId);
            rs = stm.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }
    
    public void assignBibNumber(int registrationId, String bibNumber) {
        try {
            String sqlStatement = "UPDATE Registrations SET bib_number = ? WHERE registration_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setString(1, bibNumber);
            stm.setInt(2, registrationId);
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public boolean isBibNumberExists(int eventId, String bibNumber) {
        try {
            String sqlStatement = "SELECT COUNT(*) as count FROM Registrations WHERE event_id = ? AND bib_number = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, eventId);
            stm.setString(2, bibNumber);
            rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
    public String generateUniqueBibNumber(int eventId) {
        try {
            String getEventSql = "SELECT name FROM Events WHERE event_id = ?";
            PreparedStatement getEventStm = connection.prepareStatement(getEventSql);
            getEventStm.setInt(1, eventId);
            ResultSet eventRs = getEventStm.executeQuery();
            
            String prefix = "EVT";
            if (eventRs.next()) {
                String eventName = eventRs.getString("name");
                if (eventName.contains("Hanoi")) {
                    prefix = "HM";
                } else if (eventName.contains("Saigon")) {
                    prefix = "SGN";
                } else {
                    prefix = eventName.replaceAll("[^A-Z]", "").substring(0, Math.min(3, eventName.replaceAll("[^A-Z]", "").length()));
                }
            }
            
            // Generate random number and check uniqueness
            String bibNumber;
            int attempts = 0;
            do {
                int randomNum = 1000 + (int)(Math.random() * 9000); 
                bibNumber = prefix + randomNum;
                attempts++;
                if (attempts > 100) {
                    // Fallback: use timestamp
                    bibNumber = prefix + System.currentTimeMillis() % 10000;
                    break;
                }
            } while (isBibNumberExists(eventId, bibNumber));
            
            return bibNumber;
        } catch (Exception e) {
            System.out.println(e);
            // Fallback
            return "EVT" + (1000 + (int)(Math.random() * 9000));
        }
    }
}



