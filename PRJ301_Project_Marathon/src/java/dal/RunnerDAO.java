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
public class RunnerDAO extends DBContext{
    private PreparedStatement stm;
    private ResultSet rs;
    public boolean insertRunner(Runner r) {
    try {
        String sql = "INSERT INTO Runners (account_id, full_name, gender, dob, email, phone) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        stm = connection.prepareCall(sql);
        stm.setInt(1, r.getAccountId());
        stm.setString(2, r.getFullName());
        stm.setString(3, r.getGender());
        stm.setDate(4, (Date) r.getDob());
        stm.setString(5, r.getEmail());
        stm.setString(6, r.getPhone());

        int rows = stm.executeUpdate();
        return rows > 0;
    } catch (Exception e) {
        System.out.println(e);
        return false;
    }
}

}
