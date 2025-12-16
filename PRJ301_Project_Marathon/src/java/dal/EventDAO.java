package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import models.Event;

/**
 *
 * @author THINKPAD
 */
public class EventDAO extends DBContext {
    
    private PreparedStatement stm;
    private ResultSet rs;
    
    public List<Event> getEventsByOrganizer(int organizerId) {
        List<Event> list = new ArrayList<>();
        try {
            String sqlStatement = "SELECT * FROM Events WHERE organizer_id = ? ORDER BY event_date DESC";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, organizerId);
            rs = stm.executeQuery();
            while (rs.next()) {
                int eventId = rs.getInt("event_id");
                int orgId = rs.getInt("organizer_id");
                String name = rs.getString("event_name");
                String description = ""; // Description column doesn't exist in new DB schema
                Date eventDate = rs.getDate("event_date");
                // Get event_start_time, fallback to event_date if null
                Timestamp eventStartTime = rs.getTimestamp("event_start_time");
                if (eventStartTime == null && eventDate != null) {
                    eventStartTime = new Timestamp(eventDate.getTime());
                }
                String location = rs.getString("location");
                int maxParticipants = rs.getInt("max_participants");
                Date registrationDeadline = rs.getDate("registration_deadline");
                String status = rs.getString("status");
                Event event = new Event(eventId, orgId, name, description, eventDate, location, maxParticipants, registrationDeadline, status);
                event.setEventStartTime(eventStartTime);
                list.add(event);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
    
    public Event getEventById(int eventId) {
        Event event = null;
        try {
            String sqlStatement = "SELECT * FROM Events WHERE event_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, eventId);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("event_id");
                int organizerId = rs.getInt("organizer_id");
                String name = rs.getString("event_name");
                String description = ""; // Description column doesn't exist in new DB schema
                Date eventDate = rs.getDate("event_date");
                // Get event_start_time, fallback to event_date if null
                Timestamp eventStartTime = rs.getTimestamp("event_start_time");
                if (eventStartTime == null && eventDate != null) {
                    eventStartTime = new Timestamp(eventDate.getTime());
                }
                String location = rs.getString("location");
                int maxParticipants = rs.getInt("max_participants");
                Date registrationDeadline = rs.getDate("registration_deadline");
                String status = rs.getString("status");
                event = new Event(id, organizerId, name, description, eventDate, location, maxParticipants, registrationDeadline, status);
                event.setEventStartTime(eventStartTime);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return event;
    }
    
    public void createEvent(Event event) {
        try {
            String sqlStatement = "INSERT INTO Events (organizer_id, event_name, event_date, event_start_time, location, max_participants, registration_deadline, status) "
                             + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, event.getOrganizerId());
            stm.setString(2, event.getName());
            stm.setDate(3, event.getEventDate());
            stm.setTimestamp(4, event.getEventStartTime());
            stm.setString(5, event.getLocation());
            stm.setInt(6, event.getMaxParticipants());
            stm.setDate(7, event.getRegistrationDeadline());
            stm.setString(8, event.getStatus());
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void updateEvent(Event event) {
        try {
            String sqlStatement = "UPDATE Events SET event_name = ?, event_date = ?, event_start_time = ?, location = ?, "
                               + "max_participants = ?, registration_deadline = ?, status = ? WHERE event_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setString(1, event.getName());
            stm.setDate(2, event.getEventDate());
            stm.setTimestamp(3, event.getEventStartTime());
            stm.setString(4, event.getLocation());
            stm.setInt(5, event.getMaxParticipants());
            stm.setDate(6, event.getRegistrationDeadline());
            stm.setString(7, event.getStatus());
            stm.setInt(8, event.getEventId());
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void pauseEvent(int eventId) {
        try {
            String sqlStatement = "UPDATE Events SET status = 'PAUSE' WHERE event_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, eventId);
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void closeEvent(int eventId) {
        try {
            String sqlStatement = "UPDATE Events SET status = 'Closed' WHERE event_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, eventId);
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
}



