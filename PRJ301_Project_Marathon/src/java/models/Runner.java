/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author User
 */
public class Runner {
    private int id;
    private String fullName;
    private String gender;
    private java.sql.Date dob;
    private String email;
    private String phone;
    private String password;

    public Runner() {}

    public Runner(String fullName, String gender, java.sql.Date dob, String email, String phone, String password) {
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // GETTER + SETTER
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public java.sql.Date getDob() { return dob; }
    public void setDob(java.sql.Date dob) { this.dob = dob; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

