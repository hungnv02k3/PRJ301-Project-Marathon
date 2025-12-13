package models;

/**
 *
 * @author THINKPAD
 */
public class Organizer {
    private int organizerId;
    private String name;
    private String email;
    private String phone;

    public Organizer() {
    }

    public Organizer(int organizerId, String name, String email, String phone) {
        this.organizerId = organizerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

