/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.Account;

/**
 *
 * @author User
 */
public class AccountDAO extends DBContext {

    private PreparedStatement stm;
    private ResultSet rs;

    public int insertAccount(Account acc) {
        String sql = "INSERT INTO Accounts(username, password, role) VALUES(?,?,?)";

        try {
            stm = connection.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            stm.setString(1, acc.getUserName());
            stm.setString(2, acc.getPassword());
            stm.setString(3, acc.getRole());
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // account_id
            }

        } catch (Exception e) {
            e.printStackTrace(); // BẮT BUỘC
        }
        return -1;
    }

    public Account getAccountByUsername(String username) {
        try {
            String sql = "SELECT account_id, username, password, role "
                    + "FROM Accounts WHERE username = ?";

            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            rs = stm.executeQuery();

            if (rs.next()) {
                Account acc = new Account();
                acc.setAccountId(rs.getInt("account_id"));
                acc.setUserName(rs.getString("username"));
                acc.setPassword(rs.getString("password"));
                acc.setRole(rs.getString("role"));
                return acc;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
