package jdbcsuperstore;

import java.sql.*;

public class Customer implements SQLData {
    private String firstName;
    private String lastName;
    private String email;
    private String streetAddress;
    private String city;
    private String province;
    private String country;

    public Customer(String firstName, String lastName, String email, String streetAddress, String city, String province,
            String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.streetAddress = streetAddress;
        this.city = city;
        this.province = province;
        this.country = country;

        Map map = 
    }

    public String toString() {
        return "Name: " + this.firstName + " " + this.lastName + ", Email: " + this.email +
                ", Address: " + this.streetAddress + ", " + this.city + ", " + this.province + ", " + this.country;
    }
}
