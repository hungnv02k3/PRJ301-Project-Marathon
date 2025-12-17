package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.Organizer;

/**
 *
 * @author THINKPAD
 */
public class OrganizerDAO extends DBContext {

    private PreparedStatement stm;
    private ResultSet rs;

    public Organizer getOrganizerByEmail(String email) {
        Organizer organizer = null;
        try {
            String sqlStatement = "SELECT * FROM Organizers WHERE email = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setString(1, email);
            rs = stm.executeQuery();
            while (rs.next()) {
                int organizerId = rs.getInt("organizer_id");
                String name = rs.getString("name");
                String emailResult = rs.getString("email");
                String phone = rs.getString("phone");
                organizer = new Organizer(organizerId, name, emailResult, phone);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return organizer;
    }

    public Organizer getOrganizerById(int organizerId) {
        Organizer organizer = null;
        try {
            String sqlStatement = "SELECT * FROM Organizers WHERE organizer_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, organizerId);
            rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("organizer_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                organizer = new Organizer(id, name, email, phone);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return organizer;
    }

    public int getOrgIDByAccID(int accountId) {
        String sql = "SELECT organizer_id FROM Organizers WHERE account_id = ?";

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, accountId);
            rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt("organizer_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return -1; // không tìm thấy
    }

}
