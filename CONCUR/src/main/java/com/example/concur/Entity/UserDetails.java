package com.example.concur.Entity;
import com.example.concur.embeddable.*;
import jakarta.persistence.*;

@Entity
@Table(name = "user_details", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "username")
    private  String username;

    @Column(name = "last_name")
    private String lastName;
//    private String additionalDetails; // This can be JSON or another relevant format
    @Embedded
    private Address address;
    @Column(name = "phone")
    private String phone;

    @Column(name = "website")
    private String website;

    @Embedded
    private Company company;
    // Default constructor
    public UserDetails() {}

    // Parameterized constructor
    public UserDetails(String email, String firstName, String lastName, String username, Address address,
                       String phone, String website, Company company) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.company = company;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
