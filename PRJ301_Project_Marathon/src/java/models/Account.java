/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author User
 */

public class Account {

    private int accountId;
    private String userName;
    private String password;
    private String role;

    // Constructor đầy đủ
    public Account(int accountId, String userName, String password, String role) {
        this.accountId = accountId;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    // Constructor dùng khi insert (chưa có id)
    public Account(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    // Constructor rỗng
    public Account() {
    }

    // Getter & Setter
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

