/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

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
            String sqlStatement = "INSERT INTO Runners (full_name, gender, dob, email, phone, password) "
                                + "VALUES (?, ?, ?, ?, ?, ?)";

            stm = connection.prepareCall(sqlStatement);
            stm.setString(1, r.getFullName());
            stm.setString(2, r.getGender());
            stm.setDate(3, r.getDob());
            stm.setString(4, r.getEmail());
            stm.setString(5, r.getPhone());
            stm.setString(6, r.getPassword());

            int rows = stm.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
