package lt.viko.eif.tsaviscevas.first.spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;

    public Account() {
    }

    public Account(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
