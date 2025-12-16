/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Runner;

/**
 *
 * @author User
 */
public class RunnerDAO extends DBContext {

    private PreparedStatement stm;
    private ResultSet rs;

    public boolean insertRunner(Runner r) {
        String sql = "INSERT INTO Runners (account_id, full_name, gender, dob, email, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, r.getAccountId());
            ps.setString(2, r.getFullName());
            ps.setString(3, r.getGender());
            ps.setDate(4, r.getDob());
            ps.setString(5, r.getEmail());
            ps.setString(6, r.getPhone());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM Runners WHERE email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByPhone(String phone) {
        String sql = "SELECT 1 FROM Runners WHERE phone = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int findRunnerIDByAccountID(int accountId) {
        int runnerId = -1;

        String sql = "SELECT runner_id FROM Runners WHERE account_id = ?";

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, accountId);

            rs = stm.executeQuery();
            if (rs.next()) {
                runnerId = rs.getInt("runner_id");
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

        return runnerId;
    }

    public boolean updateRunner(Runner r) {
        String sql
                = "UPDATE Runners SET full_name=?, gender=?, dob=?, email=?, phone=? "
                + "WHERE account_id=?";

        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, r.getFullName());
            stm.setString(2, r.getGender());
            stm.setDate(3, r.getDob());
            stm.setString(4, r.getEmail());
            stm.setString(5, r.getPhone());
            stm.setInt(6, r.getAccountId());

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Runner getRunnerByAccountId(int accountId) {
        String sql = "SELECT * FROM Runners WHERE account_id = ?";

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, accountId);
            rs = stm.executeQuery();

            if (rs.next()) {
                Runner r = new Runner();
                r.setRunnerId(rs.getInt("runner_id"));
                r.setAccountId(rs.getInt("account_id"));
                r.setFullName(rs.getString("full_name"));
                r.setGender(rs.getString("gender"));
                r.setDob(rs.getDate("dob"));
                r.setEmail(rs.getString("email"));
                r.setPhone(rs.getString("phone"));
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
