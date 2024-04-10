package Customer;

import java.util.List;

public class Person {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Address address;
    private String email;
    private List<Pet> pets;

    public Person(String firstName, String lastName, String phoneNumber, Address address, String email, List<Pet> pets) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.pets = pets;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public List<Pet> getPets() {
        return pets;
    }
    //to string
    @Override
public String toString() {
    return firstName + " " + lastName + " (" + email + ")";
}

}
