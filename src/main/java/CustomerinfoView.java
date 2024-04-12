import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Customer.Address;
import Customer.CustomerManager;
import Customer.Person;
import Customer.Pet;
import Customer.Pet.Gender;
import Customer.Pet.PetType;

public class CustomerinfoView extends JDialog {

    private CustomerManager customerManager;
    private List<Person> customers;
    private List<Pet> pets;
    private JFrame frame;
    private JTable table;
    private JFrame parent;
    private JButton addButton;
    private JButton updateButton;
    private JButton removeButton;
    private JButton findButton;
    private JButton countButton;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    private void initializeTable() {
        String[] columnNames = { "First Name", "Last Name", "Phone Number", "Email" }; // Definie column names
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(customerTable); // Add table to scroll pane
        add(scrollPane, BorderLayout.CENTER); //

        refreshCustomerTable(); // Populate the table with customer data
    }

    public CustomerinfoView(JFrame parent, CustomerManager customerManager) {
        super(parent, "Customer Information", true);
        this.parent = parent;
        this.customerManager = customerManager;

        setSize(500, 300);
        setLayout(new BorderLayout());

        JPanel personInfoPanel = new JPanel(); // Add components for person info
        JPanel petInfoPanel = new JPanel(); // Add components for pet info

        JPanel buttonPanel = new JPanel();
        updateButton = new JButton("Update Customer");
        removeButton = new JButton("Remove Customer");
        findButton = new JButton("Find Customer");
        countButton = new JButton("Show All Customers Count");
        addButton = new JButton("Add Customer");

        // add action listeners
        addButton.addActionListener(e -> addCustomer());
        updateButton.addActionListener(e -> updateCustomerInfo());
        removeButton.addActionListener(e -> removeCustomer());
        findButton.addActionListener(e -> findCustomer());
        countButton.addActionListener(e -> showAllCustomersCount());
        // Add buttons to the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(findButton);
        buttonPanel.add(countButton);
        add(buttonPanel, BorderLayout.SOUTH);

        initializeTable();
    }

    // Add a new customer to the list
    // Update customer information
    private void addCustomer() {
        String firstName = JOptionPane.showInputDialog(this, "Enter customer's first name:");
        String lastName = JOptionPane.showInputDialog(this, "Enter customer's last name:");
        String phoneNumber = JOptionPane.showInputDialog(this, "Enter customer's phone number:");
        String email = JOptionPane.showInputDialog(this, "Enter customer's email:");
        // Check if any of the fields are empty
        if (firstName == null || lastName == null || phoneNumber == null || email == null ||
                firstName.trim().isEmpty() || lastName.trim().isEmpty() || phoneNumber.trim().isEmpty()
                || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Customer information is incomplete.");
            return;
        }
        // Create a new address
        Address address = createAddressDialog();
        if (address == null) {
            JOptionPane.showMessageDialog(this, "Address information is required.");
            return;
        }
        // Create a list of pets
        List<Pet> pets = new ArrayList<>();
        boolean addingPets = true;
        while (addingPets) {
            int addMore = JOptionPane.showConfirmDialog(this, "Do you want to add pet information?", "Add Pet",
                    JOptionPane.YES_NO_OPTION);
            if (addMore == JOptionPane.YES_OPTION) {
                Pet pet = createPetDialog();
                if (pet == null) { // User canceled the pet dialog
                    int continueAdding = JOptionPane.showConfirmDialog(this,
                            "Pet information is incomplete. Do you want to continue without adding pet?", "Continue?",
                            JOptionPane.YES_NO_OPTION);
                    if (continueAdding == JOptionPane.NO_OPTION) {
                        return; // Stop the whole add process
                    }
                    break; // Exit pet adding loop
                }
                pets.add(pet);
            } else {
                addingPets = false; // Stop asking for more pets
            }
        }

        // Proceed to create and add the customer
        Person newCustomer = new Person(firstName, lastName, phoneNumber, address, email, pets);
        customerManager.addCustomer(newCustomer);
        JOptionPane.showMessageDialog(this, "Customer added successfully.");
        refreshCustomerTable();
    }

    // Create a new pet
    private Pet createPetDialog() {
        JTextField nameField = new JTextField();
        JTextField birthdayField = new JTextField();
        JComboBox<Gender> genderComboBox = new JComboBox<>(Gender.values());
        JComboBox<PetType> typeComboBox = new JComboBox<>(PetType.values());
        JTextField breedField = new JTextField();
        // Add components to the dialog
        final JComponent[] inputs = new JComponent[] {
                new JLabel("Name"),
                nameField,
                new JLabel("Birthday (YYYY-MM-DD)"),
                birthdayField,
                new JLabel("Gender"),
                genderComboBox,
                new JLabel("Type"),
                typeComboBox,
                new JLabel("Breed"),
                breedField
        };
        // Show the dialog
        int result = JOptionPane.showConfirmDialog(null, inputs, "Enter Pet Details", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {// Check if the user clicked OK
            String name = nameField.getText();
            LocalDate birthday = LocalDate.parse(birthdayField.getText());
            Gender gender = (Gender) genderComboBox.getSelectedItem();
            PetType type = (PetType) typeComboBox.getSelectedItem();
            String breed = breedField.getText();

            // Create a new pet object
            return new Pet(name, birthday, gender, type, breed);
        } else {
            System.out.println("User canceled or closed the dialog.");
            return null;
        }

    }

    private Address createAddressDialog() {
        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField zipCodeField = new JTextField();

        final JComponent[] inputs = new JComponent[] {
                new JLabel("Street"),
                streetField,
                new JLabel("City"),
                cityField,
                new JLabel("State"),
                stateField,
                new JLabel("Zip Code"),
                zipCodeField
        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "Enter Address Details", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String street = streetField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String zipCode = zipCodeField.getText();

            // Create a new address object
            return new Address(street, city, state, zipCode);
        } else {
            System.out.println("User canceled or closed the dialog.");// Check if the user canceled the dialog
            return null;
        }
    }

    // Update customer information
    private void updateCustomerInfo() {
        String identifier = JOptionPane.showInputDialog(this,
                "Enter customer's email, phone number, or last name to update:");
        Person customer = customerManager.findCustomerByCriteria(identifier);// Find the customer based on the
                                                                             // identifier

        if (customer != null) {
            // Update customer information
            String firstName = JOptionPane.showInputDialog(this, "Enter the new first name:", customer.getFirstName());
            String lastName = JOptionPane.showInputDialog(this, "Enter the new last name:", customer.getLastName());
            String phoneNumber = JOptionPane.showInputDialog(this, "Enter the new phone number:",
                    customer.getPhoneNumber());
            String email = JOptionPane.showInputDialog(this, "Enter the new email:", customer.getEmail());

            // Update the address
            Address updatedAddress = updateAddressDialog(customer.getAddress()); // Update the address
            // Update the pet information
            List<Pet> updatedPets = new ArrayList<>(customer.getPets()); // Create a new list to store updated pets
            updatePetDialog(updatedPets); // Update the pet information

            // Create a new customer object with the updated information
            Person updatedCustomer = new Person(
                    firstName.isEmpty() ? customer.getFirstName() : firstName,
                    lastName.isEmpty() ? customer.getLastName() : lastName,
                    phoneNumber.isEmpty() ? customer.getPhoneNumber() : phoneNumber,
                    updatedAddress,
                    email.isEmpty() ? customer.getEmail() : email,
                    updatedPets);

            // Update the customer information
            boolean updateSuccess = customerManager.updateCustomer(identifier, updatedCustomer);
            if (updateSuccess) {
                JOptionPane.showMessageDialog(this, "Customer updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update customer.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Customer not found.");
        }
    }

    // Update address information
    private Address updateAddressDialog(Address currentAddress) {
        JTextField streetField = new JTextField(currentAddress.getStreet());
        JTextField cityField = new JTextField(currentAddress.getCity());
        JTextField stateField = new JTextField(currentAddress.getState());
        JTextField zipCodeField = new JTextField(currentAddress.getZipCode());

        final JComponent[] inputs = new JComponent[] {
                new JLabel("Street"),
                streetField,
                new JLabel("City"),
                cityField,
                new JLabel("State"),
                stateField,
                new JLabel("Zip Code"),
                zipCodeField
        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "Update Address Details", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            return new Address(
                    streetField.getText(),
                    cityField.getText(),
                    stateField.getText(),
                    zipCodeField.getText());
        } else {
            return currentAddress; // Return the current address if the user cancels the dialog
        }
    }

    // Update pet information
    private void updatePetDialog(List<Pet> pets) {
        // Convert the list of pets to an array of pet names
        String[] petNames = pets.stream().map(Pet::getName).toArray(String[]::new);
        String selectedPet = (String) JOptionPane.showInputDialog(null, "Choose a pet to update",
                "Update Pet", JOptionPane.QUESTION_MESSAGE, null, petNames, petNames[0]);

        // If a pet is selected, update the pet information
        if (selectedPet != null) {
            for (Pet pet : pets) {
                if (pet.getName().equals(selectedPet)) {
                    // Here we call createPetDialog to update the selected pet's information
                    Pet updatedPet = createPetDialog();
                    if (updatedPet != null) {
                        pets.set(pets.indexOf(pet), updatedPet);
                        JOptionPane.showMessageDialog(null, "Pet updated successfully.");
                    }
                    return; // Exit the loop after updating the pet
                }
            }
        }

        // Add a new pet
        int addNew = JOptionPane.showConfirmDialog(null, "Would you like to add a new pet?", "Add Pet",
                JOptionPane.YES_NO_OPTION);
        if (addNew == JOptionPane.YES_OPTION) {
            Pet newPet = createPetDialog();
            if (newPet != null)
                pets.add(newPet);
        }

        // Remove a pet
        int remove = JOptionPane.showConfirmDialog(null, "Would you like to remove a pet?", "Remove Pet",
                JOptionPane.YES_NO_OPTION);
        if (remove == JOptionPane.YES_OPTION) {
            String petToRemove = (String) JOptionPane.showInputDialog(null, "Choose a pet to remove",
                    "Remove Pet", JOptionPane.QUESTION_MESSAGE, null, petNames, petNames[0]);
            pets.removeIf(p -> p.getName().equals(petToRemove));
        }
    }

    // Remove a customer
    private void removeCustomer() {
        // Prompts the user which identifiers can be used for deletion operations
        String identifier = JOptionPane.showInputDialog(this,
                "Enter customer's email, phone number, last name, or pet name to remove:");
        if (identifier != null && !identifier.trim().isEmpty()) {
            if (customerManager.removeCustomer(identifier)) {
                JOptionPane.showMessageDialog(this, "Customer removed successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found or could not be removed.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Operation cancelled or invalid input.");
        }
    }

    // Find a customer
    private void findCustomer() {
        String criteria = JOptionPane.showInputDialog(this,
                "Enter search criteria (email, phone, last name, or pet name):");
        Person customer = customerManager.findCustomerByCriteria(criteria);
        if (customer != null) {
            // Convert address information to a string
            Address address = customer.getAddress();
            String addressInfo = String.format("Address: %s, %s, %s, %s",
                    address.getStreet(), address.getCity(), address.getState(), address.getZipCode());

            // Convert pet names to an array
            String[] petNames = customer.getPets().stream().map(Pet::getName).toArray(String[]::new);

            // Display client information and pet name list
            JOptionPane.showMessageDialog(this,
                    "Customer found: " + customer.getFirstName() + " " + customer.getLastName() + "\n" +
                            customer.getEmail() + "\n" + customer.getPhoneNumber() + "\n" + addressInfo,
                    "Customer Details", JOptionPane.INFORMATION_MESSAGE);

            // Prompts the user to select a pet to view details
            if (petNames.length > 0) {
                String selectedPetName = (String) JOptionPane.showInputDialog(frame,
                        "Select a pet to view details:", "Pet List",
                        JOptionPane.QUESTION_MESSAGE, null,
                        petNames, petNames[0]);

                // If the user selects a pet, show pet details
                if (selectedPetName != null && !selectedPetName.isEmpty()) {
                    showPetDetails(customer.getPets().stream().filter(p -> p.getName().equals(selectedPetName))
                            .findFirst().orElse(null));
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Customer not found.");
        }
    }

    private void showPetDetails(Pet pet) {
        if (pet != null) {
            // Withdrawal Reminder
            String remindersInfo = pet.getReminders().stream()
                    .map(reminder -> reminder.getDueDate().toString() + ": " + reminder.getNote())
                    .collect(Collectors.joining(", "));

            // Extract case record information
            String caseHistoryInfo = pet.getCaseRecords().stream()
                    .map(caseRecord -> caseRecord.getDate().toString() + ": " + caseRecord.getDescription())
                    .collect(Collectors.joining(", "));

            // Defining Table Data
            String[][] data = {
                    { "Name", pet.getName() },
                    { "Birthday", pet.getBirthday().toString() },
                    { "Gender", pet.getGender().toString() },
                    { "Type", pet.getType().toString() },
                    { "Breed", pet.getBreed() },
                    { "Reminders", remindersInfo },
                    { "Case History", caseHistoryInfo }
            };

            String[] columnNames = { "Attribute", "Value" };

            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            table.setFillsViewportHeight(true);

            // Create a dialog box to display the pet details form
            JOptionPane.showMessageDialog(null, scrollPane, "Pet Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Display the number of all customers
    private void showAllCustomersCount() {
        List<Person> allCustomers = customerManager.getAllCustomers();
        JOptionPane.showMessageDialog(this, "Total number of customers: " + allCustomers.size());
    }

    private void refreshCustomerTable() {
        tableModel.setRowCount(0); // Empty the table of existing data
        List<Person> customers = customerManager.getAllCustomers(); // Get all customers
        for (Person customer : customers) {
            Object[] row = new Object[] {
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getPhoneNumber(),
                    customer.getEmail()
            };
            tableModel.addRow(row); // Adding Rows to a Table Model
        }
    }

}
