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

        }
        return event;
    }

    public int countOpenEvents(String keyword) {
        int count = 0;

        String sql = "SELECT COUNT(*) FROM Events "
                + "WHERE status = 'OPEN' "
                + (keyword != null && !keyword.trim().isEmpty()
                ? "AND event_name LIKE ? " : "");

        try {
            stm = connection.prepareStatement(sql);

            if (keyword != null && !keyword.trim().isEmpty()) {
                stm.setString(1, "%" + keyword + "%");
            }

            rs = stm.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
            } catch (Exception e) {
            }
        }

        return count;
    }

    // Lấy danh sách event còn mở đăng ký (hiện homepage)
    public List<Event> getAvailableEvents(String keyword, int page, int pageSize) {
        List<Event> list = new ArrayList<>();
        String sql
                = "SELECT * FROM Events "
                + "WHERE status = 'OPEN' "
                + (keyword != null && !keyword.isEmpty()
                ? "AND event_name LIKE ? " : "")
                + "ORDER BY event_date "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            int index = 1;

            if (keyword != null && !keyword.isEmpty()) {
                ps.setString(index++, "%" + keyword + "%");
            }

            ps.setInt(index++, (page - 1) * pageSize);
            ps.setInt(index, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Event e = new Event(
                        rs.getInt("event_id"),
                        rs.getInt("organizer_id"),
                        rs.getString("event_name"), rs.getString("location"),
                        rs.getDate("event_date"),
                        rs.getString("status"),
                        rs.getInt("max_participants"),
                        rs.getDate("registration_deadline"),
                        rs.getString("status")
                );
                list.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy event theo id (dùng khi đăng ký)
    public void createEvent(Event event) {
        try {
            String sqlStatement = "INSERT INTO Events (organizer_id, event_name, event_date, event_start_time, location, max_participants, registration_deadline, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, event.getOrganizerId());
            stm.setString(2, event.getEventName());
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
            stm.setString(1, event.getEventName());
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

    // Organizer tạo event
    public boolean insertEvent(Event e) {
        try {
            String sql = "INSERT INTO Events "
                    + "(organizer_id, event_name, event_date, event_start_time, location, "
                    + "registration_deadline, max_participants, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            stm = connection.prepareCall(sql);
            stm.setInt(1, e.getOrganizerId());
            stm.setString(2, e.getEventName());
            stm.setDate(3, (Date) e.getEventDate());
            stm.setTimestamp(4, e.getEventStartTime());
            stm.setString(5, e.getLocation());
            stm.setDate(6, (Date) e.getRegistrationDeadline());
            stm.setInt(7, e.getMaxParticipants());
            stm.setString(8, e.getStatus());

            int rows = stm.executeUpdate();
            return rows > 0;

        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    // Đóng event (hết hạn hoặc organizer đóng)
    public void closeEvent(int eventId) {
        try {
            String sql = "UPDATE Events SET status = 'CLOSED' WHERE event_id = ?";
            stm = connection.prepareCall(sql);
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
