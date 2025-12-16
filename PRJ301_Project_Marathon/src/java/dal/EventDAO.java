package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Event;

public class EventDAO extends DBContext {

    private PreparedStatement stm;
    private ResultSet rs;

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
                        rs.getString("event_name"),
                        rs.getDate("event_date"),
                        rs.getTimestamp("event_start_time"),
                        rs.getString("location"),
                        rs.getDate("registration_deadline"),
                        rs.getInt("max_participants"),
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
    public Event getEventById(int eventId) {
        try {
            String sql = "SELECT * FROM Events WHERE event_id = ?";
            stm = connection.prepareCall(sql);
            stm.setInt(1, eventId);
            rs = stm.executeQuery();

            if (rs.next()) {
                Event e = new Event();
                e.setEventId(rs.getInt("event_id"));
                e.setOrganizerId(rs.getInt("organizer_id"));
                e.setEventName(rs.getString("event_name"));
                e.setEventDate(rs.getDate("event_date"));
                e.setEventStartTime(rs.getTimestamp("event_start_time"));
                e.setLocation(rs.getString("location"));
                e.setRegistrationDeadline(rs.getDate("registration_deadline"));
                e.setMaxParticipants(rs.getInt("max_participants"));
                e.setStatus(rs.getString("status"));
                return e;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
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
}
