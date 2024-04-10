package Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerManager {

    private List<Person> customers;

    public CustomerManager() {
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Person customer) {
        this.customers.add(customer);
    }

    public Person findCustomerByCriteria(String criteria) {
        return customers.stream()
            .filter(customer -> customer.getEmail().equalsIgnoreCase(criteria) ||
                               customer.getPhoneNumber().equalsIgnoreCase(criteria) ||
                               customer.getLastName().equalsIgnoreCase(criteria) ||
                               customer.getPets().stream().anyMatch(pet -> pet.getName().equalsIgnoreCase(criteria)))
            .findFirst()
            .orElse(null);
    }

    public boolean updateCustomer(String criteria, Person updatedCustomer) {
        Person customer = findCustomerByCriteria(criteria);
        if (customer != null) {
            int index = customers.indexOf(customer);
            customers.set(index, updatedCustomer);
            return true;
        }
        return false;
    }

    public boolean removeCustomer(String criteria) {
        return customers.removeIf(customer -> customer.getEmail().equalsIgnoreCase(criteria) ||
                                               customer.getPhoneNumber().equalsIgnoreCase(criteria) ||
                                               customer.getLastName().equalsIgnoreCase(criteria) ||
                                               customer.getPets().stream().anyMatch(pet -> pet.getName().equalsIgnoreCase(criteria)));
    }

   
    // It should be updated or removed to fit the actual application logic.
    public List<Person> getAllCustomers() {
        return new ArrayList<>(this.customers);
    }

    
    // 获取所有宠物的方法
    public List<Pet> getAllPets() {
        List<Pet> allPets = new ArrayList<>();
        for (Person customer : customers) {
            allPets.addAll(customer.getPets()); // 将每个客户的宠物添加到列表中
        }
        return allPets;
    }

    // 获取所有人的方法
    public List<Person> getAllPersons() {
        return new ArrayList<>(this.customers);
    }
    
    

    
}

