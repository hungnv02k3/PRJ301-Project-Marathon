package dal;



import java.sql.Date;

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
                               + "ru.phone as runner_phone, e.event_name as event_name "
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
                               + "ru.phone as runner_phone, e.event_name as event_name "
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
            String getEventSql = "SELECT event_name FROM Events WHERE event_id = ?";
            PreparedStatement getEventStm = connection.prepareStatement(getEventSql);
            getEventStm.setInt(1, eventId);
            ResultSet rs = getEventStm.executeQuery();
            
            String prefix = "EVT";
            if (rs.next()) {
                String eventName = rs.getString("event_name");                
                prefix = eventName.replaceAll("[^A-Z]", "").substring(0, Math.min(3, eventName.replaceAll("[^A-Z]", "").length()));                
            }
            
            // Generate random number and check unique
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
            return "EVT" + (1000 + (int)(Math.random() * 9000));
        }
    }


    public String getRegistrationStatus(int eventId, int runnerId) {
        String sql
                = "SELECT status FROM Registrations "
                + "WHERE event_id = ? AND runner_id = ? "
                + "ORDER BY registration_date DESC";

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, eventId);
            stm.setInt(2, runnerId);

            rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null; // chưa từng đăng ký
    }

    public boolean reRegister(int eventId, int runnerId, Date date) {
        String sql
                = "UPDATE Registrations "
                + "SET status = 'Registered', registration_date = ? "
                + "WHERE event_id = ? AND runner_id = ? AND status = 'CANCELLED'";

        try {
            stm = connection.prepareStatement(sql);
            stm.setDate(1, date);
            stm.setInt(2, eventId);
            stm.setInt(3, runnerId);

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 1️⃣ Kiểm tra runner đã đăng ký event chưa
    public boolean isRegistered(int eventId, int runnerId) {
        String sql = "SELECT 1 FROM Registrations WHERE event_id = ? AND runner_id = ?";

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, eventId);
            stm.setInt(2, runnerId);

            rs = stm.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    public List<RegistrationView> getRegistrationsByRunner(int runnerId) {
        List<RegistrationView> list = new ArrayList<>();

        String sql
                = "SELECT r.registration_id, e.event_id, e.event_name, e.event_date, e.location, "
                + "       r.registration_date, r.bib_number, r.status "
                + "FROM Registrations r "
                + "JOIN Events e ON r.event_id = e.event_id "
                + "WHERE r.runner_id = ? "
                + "ORDER BY r.registration_date DESC";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, runnerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RegistrationView rv = new RegistrationView();
                rv.setRegistrationId(rs.getInt("registration_id"));
                rv.setEventId(rs.getInt("event_id"));
                rv.setEventName(rs.getString("event_name"));
                rv.setEventDate(rs.getDate("event_date"));
                rv.setLocation(rs.getString("location"));
                rv.setRegistrationDate(rs.getDate("registration_date"));
                rv.setBibNumber(rs.getString("bib_number"));
                rv.setStatus(rs.getString("status"));
                list.add(rv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 2️⃣ Đăng ký event
    public boolean insertRegistration(Registration r) {
        String sql = """
            INSERT INTO Registrations
            (event_id, runner_id, registration_date, bib_number, status)
            VALUES (?, ?, ?, ?, ?)
        """;

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, r.getEventId());
            stm.setInt(2, r.getRunnerId());
            stm.setDate(3, (Date) r.getRegistrationDate());
            stm.setString(4, r.getBibNumber());
            stm.setString(5, r.getStatus());

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // 3️⃣ Lấy danh sách đăng ký của 1 runner
    public List<Registration> getRegistrationsByRunnerr(int runnerId) {
        List<Registration> list = new ArrayList<>();

        String sql = "SELECT * FROM Registrations WHERE runner_id = ? ORDER BY registration_date DESC";

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, runnerId);
            rs = stm.executeQuery();

            while (rs.next()) {
                Registration r = new Registration();
                r.setRegistrationId(rs.getInt("registration_id"));
                r.setEventId(rs.getInt("event_id"));
                r.setRunnerId(rs.getInt("runner_id"));
                r.setRegistrationDate(rs.getDate("registration_date"));
                r.setBibNumber(rs.getString("bib_number"));
                r.setStatus(rs.getString("status"));

                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    // 4️⃣ Đếm số runner đã đăng ký 1 event (để check full)
    public int countRegisteredByEvent(int eventId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Registrations WHERE event_id = ?";

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, eventId);
            rs = stm.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    // 5️⃣ Hủy đăng ký
    public boolean cancelRegistration(int registrationId, int runnerId) {
        String sql
                = "UPDATE Registrations "
                + "SET status = 'CANCELLED' "
                + "WHERE registration_id = ? "
                + "AND runner_id = ? "
                + "AND status = 'Registered'";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, registrationId);
            ps.setInt(2, runnerId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔒 Đóng tài nguyên (dùng chung)
    private void closeResources() {
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
}

