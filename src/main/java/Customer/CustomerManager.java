package Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerManager {

    private List<Person> customers;
    // Constructor
    public CustomerManager() {
        this.customers = new ArrayList<>();
    }
    // Method to add a customer
    public void addCustomer(Person customer) {
        this.customers.add(customer);
    }
    // Methods for finding customers based on conditions
    public Person findCustomerByCriteria(String criteria) {
        return customers.stream()//Find the first customer whose email, phone number, last name, or pet name matches the criteria
                .filter(customer -> customer.getEmail().equalsIgnoreCase(criteria) ||//Check if the email, phone number, last name, or pet name matches the criteria
                        customer.getPhoneNumber().equalsIgnoreCase(criteria) ||
                        customer.getLastName().equalsIgnoreCase(criteria) ||
                        customer.getPets().stream().anyMatch(pet -> pet.getName().equalsIgnoreCase(criteria)))
                .findFirst()
                .orElse(null);
    }
    // Method to update a customer based on the criteria
    public boolean updateCustomer(String criteria, Person updatedCustomer) {
        Person customer = findCustomerByCriteria(criteria);
        if (customer != null) {//If the customer is found, update the customer
            int index = customers.indexOf(customer);
            customers.set(index, updatedCustomer);
            return true;
        }
        return false;
    }
    // Method to remove a customer based on the criteria
    public boolean removeCustomer(String criteria) {
        return customers.removeIf(customer -> customer.getEmail().equalsIgnoreCase(criteria) ||//Remove the customer if the criteria matches the email, phone number, last name, or pet name
                customer.getPhoneNumber().equalsIgnoreCase(criteria) ||
                customer.getLastName().equalsIgnoreCase(criteria) ||
                customer.getPets().stream().anyMatch(pet -> pet.getName().equalsIgnoreCase(criteria)));
    }

    // It should be updated or removed to fit the actual application logic.
    public List<Person> getAllCustomers() {
        return new ArrayList<>(this.customers);
    }

    // Get all pets from all customers
    public List<Pet> getAllPets() {
        List<Pet> allPets = new ArrayList<>();
        for (Person customer : customers) {
            allPets.addAll(customer.getPets()); // Add all pets of each customer to the list
        }
        return allPets;
    }

    // Get all pets from all customers
    public List<Person> getAllPersons() {
        return new ArrayList<>(this.customers);
    }

}
