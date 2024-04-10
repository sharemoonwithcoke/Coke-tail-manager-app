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
    String[] columnNames = {"First Name", "Last Name", "Phone Number", "Email"}; // 定义列名
    tableModel = new DefaultTableModel(columnNames, 0);
    customerTable = new JTable(tableModel);

    JScrollPane scrollPane = new JScrollPane(customerTable); // 使表格可滚动
    add(scrollPane, BorderLayout.CENTER); // 添加到中央布局

    refreshCustomerTable(); // 初始刷新表格
}



    public CustomerinfoView(JFrame parent, CustomerManager customerManager) {
        super(parent, "Customer Information", true);
        this.parent = parent;
        this.customerManager = customerManager; 
    
        setSize(500, 300);
        setLayout(new BorderLayout());
    
        JPanel personInfoPanel = new JPanel(); // Add components for person info
        JPanel petInfoPanel = new JPanel();    // Add components for pet info
    
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
    
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(findButton);
        buttonPanel.add(countButton);
        add(buttonPanel, BorderLayout.SOUTH);

        initializeTable();
    }
    

    
   
    //添加方法

   // 增加新客户信息
   private void addCustomer() {
    String firstName = JOptionPane.showInputDialog(this, "Enter customer's first name:");
    String lastName = JOptionPane.showInputDialog(this, "Enter customer's last name:");
    String phoneNumber = JOptionPane.showInputDialog(this, "Enter customer's phone number:");
    String email = JOptionPane.showInputDialog(this, "Enter customer's email:");

    if (firstName == null || lastName == null || phoneNumber == null || email == null ||
        firstName.trim().isEmpty() || lastName.trim().isEmpty() || phoneNumber.trim().isEmpty() || email.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Customer information is incomplete.");
        return;
    }
    
    Address address = createAddressDialog();
    if (address == null) {
        JOptionPane.showMessageDialog(this, "Address information is required.");
        return;
    }

    List<Pet> pets = new ArrayList<>();
    boolean addingPets = true;
    while (addingPets) {
        int addMore = JOptionPane.showConfirmDialog(this, "Do you want to add pet information?", "Add Pet", JOptionPane.YES_NO_OPTION);
        if (addMore == JOptionPane.YES_OPTION) {
            Pet pet = createPetDialog();
            if (pet == null) { // User canceled the pet dialog
                int continueAdding = JOptionPane.showConfirmDialog(this, "Pet information is incomplete. Do you want to continue without adding pet?", "Continue?", JOptionPane.YES_NO_OPTION);
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

private Pet createPetDialog() {
    JTextField nameField = new JTextField();
    JTextField birthdayField = new JTextField();
    JComboBox<Gender> genderComboBox = new JComboBox<>(Gender.values());
    JComboBox<PetType> typeComboBox = new JComboBox<>(PetType.values());
    JTextField breedField = new JTextField();

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
    
    int result = JOptionPane.showConfirmDialog(null, inputs, "Enter Pet Details", JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        String name = nameField.getText();
        LocalDate birthday = LocalDate.parse(birthdayField.getText());
        Gender gender = (Gender)genderComboBox.getSelectedItem();
        PetType type = (PetType)typeComboBox.getSelectedItem();
        String breed = breedField.getText();

        // 根据收集的信息创建Pet对象
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

        // 根据收集的信息创建Address对象
        return new Address(street, city, state, zipCode);
    } else {
        System.out.println("User canceled or closed the dialog.");
        return null;
    }
}


private void updateCustomerInfo() {
    String identifier = JOptionPane.showInputDialog(this, "Enter customer's email, phone number, or last name to update:");
    Person customer = customerManager.findCustomerByCriteria(identifier);
    
    if (customer != null) {
        // 更新基本信息
        String firstName = JOptionPane.showInputDialog(this, "Enter the new first name:", customer.getFirstName());
        String lastName = JOptionPane.showInputDialog(this, "Enter the new last name:", customer.getLastName());
        String phoneNumber = JOptionPane.showInputDialog(this, "Enter the new phone number:", customer.getPhoneNumber());
        String email = JOptionPane.showInputDialog(this, "Enter the new email:", customer.getEmail());
        
        // 更新地址信息
        Address updatedAddress = updateAddressDialog(customer.getAddress()); // 使用当前地址作为输入
        
        // 更新宠物信息，可能包括添加、更新和删除宠物
        List<Pet> updatedPets = new ArrayList<>(customer.getPets()); // 创建宠物列表的副本以便更新
        updatePetDialog(updatedPets); // 更新宠物信息
        
        // 构造新的Person对象来反映更新
        Person updatedCustomer = new Person(
            firstName.isEmpty() ? customer.getFirstName() : firstName,
            lastName.isEmpty() ? customer.getLastName() : lastName,
            phoneNumber.isEmpty() ? customer.getPhoneNumber() : phoneNumber,
            updatedAddress,
            email.isEmpty() ? customer.getEmail() : email,
            updatedPets
        );

        // 使用更新后的客户信息替换原有信息
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


//更新地址

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
            zipCodeField.getText()
        );
    } else {
        return currentAddress; // 如果用户取消，则返回原地址
    }
}

// 更新宠物信息
private void updatePetDialog(List<Pet> pets) {
    // 列出所有宠物让用户选择
    String[] petNames = pets.stream().map(Pet::getName).toArray(String[]::new);
    String selectedPet = (String) JOptionPane.showInputDialog(null, "Choose a pet to update", 
        "Update Pet", JOptionPane.QUESTION_MESSAGE, null, petNames, petNames[0]);

    // 根据选择执行不同的操作
    if (selectedPet != null) {
        for (Pet pet : pets) {
            if (pet.getName().equals(selectedPet)) {
                // 这里我们调用 createPetDialog 来更新选定的宠物信息
                Pet updatedPet = createPetDialog();
                if (updatedPet != null) {
                    pets.set(pets.indexOf(pet), updatedPet);
                    JOptionPane.showMessageDialog(null, "Pet updated successfully.");
                }
                return; // 完成更新后退出
            }
        }
    }
    
    // 添加宠物的逻辑
    int addNew = JOptionPane.showConfirmDialog(null, "Would you like to add a new pet?", "Add Pet", JOptionPane.YES_NO_OPTION);
    if (addNew == JOptionPane.YES_OPTION) {
        Pet newPet = createPetDialog();
        if (newPet != null) pets.add(newPet);
    }

    // 删除宠物的逻辑
    int remove = JOptionPane.showConfirmDialog(null, "Would you like to remove a pet?", "Remove Pet", JOptionPane.YES_NO_OPTION);
    if (remove == JOptionPane.YES_OPTION) {
        String petToRemove = (String) JOptionPane.showInputDialog(null, "Choose a pet to remove", 
            "Remove Pet", JOptionPane.QUESTION_MESSAGE, null, petNames, petNames[0]);
        pets.removeIf(p -> p.getName().equals(petToRemove));
    }
}


// 删除客户信息
private void removeCustomer() {
    // 提示用户可以使用哪些标识符进行删除操作
    String identifier = JOptionPane.showInputDialog(this, "Enter customer's email, phone number, last name, or pet name to remove:");
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


// 查找客户
private void findCustomer() {
    String criteria = JOptionPane.showInputDialog(this, "Enter search criteria (email, phone, last name, or pet name):");
    Person customer = customerManager.findCustomerByCriteria(criteria);
    if (customer != null) {
        // 将地址信息转换为字符串
        Address address = customer.getAddress();
        String addressInfo = String.format("Address: %s, %s, %s, %s", 
                                address.getStreet(), address.getCity(), address.getState(), address.getZipCode());
        
        // 将宠物名字转换为字符串数组
        String[] petNames = customer.getPets().stream().map(Pet::getName).toArray(String[]::new);

        // 显示客户信息和宠物名字列表
        JOptionPane.showMessageDialog(this, 
            "Customer found: " + customer.getFirstName() + " " + customer.getLastName() + "\n" +
            customer.getEmail() + "\n" + customer.getPhoneNumber() + "\n" + addressInfo,
            "Customer Details", JOptionPane.INFORMATION_MESSAGE);

        // 提示用户选择一个宠物来查看详情
        if (petNames.length > 0) {
            String selectedPetName = (String) JOptionPane.showInputDialog(frame, 
                    "Select a pet to view details:", "Pet List",
                    JOptionPane.QUESTION_MESSAGE, null, 
                    petNames, petNames[0]);

            // 如果用户选择了一个宠物，展示宠物详情
            if (selectedPetName != null && !selectedPetName.isEmpty()) {
                showPetDetails(customer.getPets().stream().filter(p -> p.getName().equals(selectedPetName)).findFirst().orElse(null));
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Customer not found.");
    }
}
private void showPetDetails(Pet pet) {
    if (pet != null) {
        // 提取提醒信息
        String remindersInfo = pet.getReminders().stream()
            .map(reminder -> reminder.getDueDate().toString() + ": " + reminder.getNote())
            .collect(Collectors.joining(", "));

        // 提取病例记录信息
        String caseHistoryInfo = pet.getCaseRecords().stream()
            .map(caseRecord -> caseRecord.getDate().toString() + ": " + caseRecord.getDescription())
            .collect(Collectors.joining(", "));

        // 定义表格数据
        String[][] data = {
            {"Name", pet.getName()},
            {"Birthday", pet.getBirthday().toString()},
            {"Gender", pet.getGender().toString()},
            {"Type", pet.getType().toString()},
            {"Breed", pet.getBreed()},
            {"Reminders", remindersInfo},
            {"Case History", caseHistoryInfo}
        };

        String[] columnNames = {"Attribute", "Value"};

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // 创建一个对话框来展示宠物详情表格
        JOptionPane.showMessageDialog(null, scrollPane, "Pet Details", JOptionPane.INFORMATION_MESSAGE);
    }
}



// 显示所有客户的数量
private void showAllCustomersCount() {
    List<Person> allCustomers = customerManager.getAllCustomers();
    JOptionPane.showMessageDialog(this, "Total number of customers: " + allCustomers.size());
}

private void refreshCustomerTable() {
    tableModel.setRowCount(0); // 清空表格现有数据
    List<Person> customers = customerManager.getAllCustomers(); // 获取所有客户
    for (Person customer : customers) {
        Object[] row = new Object[]{
            customer.getFirstName(),
            customer.getLastName(),
            customer.getPhoneNumber(),
            customer.getEmail()
        };
        tableModel.addRow(row); // 向表格模型中添加行
    }
}

}


