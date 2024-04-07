import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.*;
import Appointment.AppointmentManager;
import ReminderEmail.EmailService;
import ReminderEmail.Reminder;
import ReminderEmail.ReminderManager;
import toDo.ToDoList;
import Customer.CustomerManager;
import Customer.Person;
import Customer.Address;
import Customer.Pet;
import Customer.Pet.Gender;
import Customer.Pet.PetType;

import java.util.List;
import java.util.stream.Collectors;



public class AppGUI {
    // 主框架和其他GUI组件
    private JFrame frame;
    private MainPage mainPage;
    private AppointmentView appointmentView;
    private CustomerinfoView customerInfoView;
    private CalendarView calendarView;
    private ToDoListDialog toDoListDialog;
    private RemindersDialog reminderDialog;
    private RemaiderDetailsDialog reminderDetailsDialog;

    
    // 管理类实例
    private AppointmentManager appointmentManager;
    private ReminderManager reminderManager;
    private ToDoList toDoList;
    private EmailService emailService;
    private CustomerManager customerManager;

    public AppGUI() {
        // 初始化管理器
        this.appointmentManager = new AppointmentManager();
        this.reminderManager = new ReminderManager();
        this.toDoList = new ToDoList();
        this.emailService = new EmailService();
        this.customerManager = new CustomerManager();
        

        // 初始化视图
        initializeViews();
    }

    private void initializeViews() {
        // 创建主窗口和其他视图
        mainPage = new MainPage();
        appointmentView = new AppointmentView(frame); // 假设它是一个对话框
        customerInfoView = new CustomerinfoView(frame); // 假设它是一个对话框

        // 配置主窗口
        frame = new JFrame("Calendar Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.add(mainPage, BorderLayout.CENTER); // MainPage应该是一个JPanel

        // 添加事件监听器
        addListeners();
    }

    private void addListeners() {
        // 示例: 为主窗口添加按钮监听器
        mainPage.getViewAppointmentsButton().addActionListener(e -> showAppointments());
        mainPage.getAddAppointmentButton().addActionListener(e -> addAppointment());
        mainPage.getViewRemindersButton().addActionListener(e -> showReminders());

        // 更多的监听器可以在这里添加
    }

// 显示预约视图
private void showAppointments() {
    if (appointmentView == null) {
        appointmentView = new AppointmentView(frame);//更改！！
    }
    appointmentView.setVisible(true);
}

// 显示客户信息视图
private void showCustomerInfo() {
    if (customerInfoView == null) {
        customerInfoView = new CustomerinfoView(frame); // 更改！！
    }
    customerInfoView.setVisible(true);
}   

// 显示待办列表对话框
private void showToDoListDialog() {
    if (toDoListDialog == null) {
        toDoListDialog = new ToDoListDialog(frame, toDoList);
    }
    toDoListDialog.setVisible(true);
}

// 显示提醒对话框
private void showReminderDialog() {
    if (reminderDialog == null) {
        reminderDialog = new RemindersDialog(frame, reminderManager, null);//更改！！
    }
    reminderDialog.setVisible(true);
}

// 显示提醒详情视图
private void showReminderDetails(Reminder reminder) {
    reminderDetailsDialog = new ReminderDetailsDialog(frame, reminder);
    reminderDetailsDialog.setVisible(true);
}

   
     // Display the total number of appointments
     private void showAppointmentCount() {
        int totalAppointments = appointmentManager.getAppointmentsCount();
        JOptionPane.showMessageDialog(frame, "Total Appointments: " + totalAppointments);
    }

    ///display to-do list
    private void showTodoListDialog() {
        ToDoListDialog dialog = new ToDoListDialog(frame, toDoList);
        dialog.setVisible(true);
    }
    

    // Add a new to-do item
    private void addTodoItem() {
        String description = JOptionPane.showInputDialog(frame, "Enter the description for the new to-do item:");
        if (description != null && !description.trim().isEmpty()) {
            toDoList.addTask(description.trim());
        }
    }

    // Edit a to-do item
    private void editTodoItem(int index) {
        String newDescription = JOptionPane.showInputDialog(frame, "Enter the new description for the to-do item:");
        if (newDescription != null && !newDescription.trim().isEmpty()) {
            toDoList.editTask(index, newDescription.trim());
        }
    }

    // Remove a to-do item
    private void removeTodoItem(int index) {
        toDoList.removeTask(index);
    }

    // Display reminders
    private void showReminders() {
        // Assuming getReminders returns a List<Reminder>
        List<Reminder> reminders = reminderManager.getReminders();
        reminders.forEach(reminder -> {
            // Assuming Reminder has a toString that formats the reminder for display
            JOptionPane.showMessageDialog(frame, reminder.toString());
        });
    }
///add reminder
    private void addReminder() {
        // 提示用户输入提醒详情
        String note = JOptionPane.showInputDialog(frame, "Enter reminder note:");
        // 提示用户输入日期
        String dueDateString = JOptionPane.showInputDialog(frame, "Enter due date (YYYY-MM-DD):");
        LocalDate dueDate = LocalDate.parse(dueDateString);
        // 提供选择宠物的方式
        Pet selectedPet = selectPetForReminder();
    
        if (note != null && dueDate != null && selectedPet != null) {
            reminderManager.addGeneralReminder(selectedPet, dueDate, note, 0); // 最后一个参数为提前天数，根据需要调整
            JOptionPane.showMessageDialog(frame, "Reminder added successfully.");
        }
    }
    

    // Remove a reminder
    private void removeReminder(int reminderId) {
        reminderManager.removeReminder(reminderId);
    }


    ///////////////////////////////////////////////////////////////客户信息管理

   // 增加新客户信息
   private void addCustomer() {
    // 从用户获取客户信息
    String firstName = JOptionPane.showInputDialog(frame, "Enter customer's first name:");
    String lastName = JOptionPane.showInputDialog(frame, "Enter customer's last name:");
    String phoneNumber = JOptionPane.showInputDialog(frame, "Enter customer's phone number:");
    String email = JOptionPane.showInputDialog(frame, "Enter customer's email:");
    
    // 假设Address类有一个合适的构造器或者一个输入对话框来创建Address对象
    Address address = createAddressDialog();
    
    // 收集宠物信息
    List<Pet> pets = new ArrayList<>();
    int addMore = JOptionPane.showConfirmDialog(frame, "Do you want to add pet information?", "Add Pet", JOptionPane.YES_NO_OPTION);
    while (addMore == JOptionPane.YES_OPTION) {
        Pet pet = createPetDialog();
        if (pet != null) {
            pets.add(pet);
        }
        addMore = JOptionPane.showConfirmDialog(frame, "Do you want to add more pet information?", "Add More Pets", JOptionPane.YES_NO_OPTION);
    }
    
    // 创建并添加新的Person实例到CustomerManager
    if (email != null && !email.trim().isEmpty()) {
        Person newCustomer = new Person(firstName, lastName, phoneNumber, address, email, pets);
        customerManager.addCustomer(newCustomer);
        JOptionPane.showMessageDialog(frame, "Customer added successfully.");
    } else {
        JOptionPane.showMessageDialog(frame, "Customer addition cancelled or invalid input.");
    }
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
    String identifier = JOptionPane.showInputDialog(frame, "Enter customer's email, phone number, or last name to update:");
    Person customer = customerManager.findCustomerByCriteria(identifier);
    
    if (customer != null) {
        // 更新基本信息
        String firstName = JOptionPane.showInputDialog(frame, "Enter the new first name:", customer.getFirstName());
        String lastName = JOptionPane.showInputDialog(frame, "Enter the new last name:", customer.getLastName());
        String phoneNumber = JOptionPane.showInputDialog(frame, "Enter the new phone number:", customer.getPhoneNumber());
        String email = JOptionPane.showInputDialog(frame, "Enter the new email:", customer.getEmail());
        
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
            JOptionPane.showMessageDialog(frame, "Customer updated successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to update customer.");
        }
    } else {
        JOptionPane.showMessageDialog(frame, "Customer not found.");
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
    String identifier = JOptionPane.showInputDialog(frame, "Enter customer's email, phone number, last name, or pet name to remove:");
    if (identifier != null && !identifier.trim().isEmpty()) {
        if (customerManager.removeCustomer(identifier)) {
            JOptionPane.showMessageDialog(frame, "Customer removed successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Customer not found or could not be removed.");
        }
    } else {
        JOptionPane.showMessageDialog(frame, "Operation cancelled or invalid input.");
    }
}


// 查找客户
private void findCustomer() {
    String criteria = JOptionPane.showInputDialog(frame, "Enter search criteria (email, phone, last name, or pet name):");
    Person customer = customerManager.findCustomerByCriteria(criteria);
    if (customer != null) {
        // 将地址信息转换为字符串
        Address address = customer.getAddress();
        String addressInfo = String.format("Address: %s, %s, %s, %s", 
                                address.getStreet(), address.getCity(), address.getState(), address.getZipCode());
        
        // 将宠物名字转换为字符串数组
        String[] petNames = customer.getPets().stream().map(Pet::getName).toArray(String[]::new);

        // 显示客户信息和宠物名字列表
        JOptionPane.showMessageDialog(frame, 
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
        JOptionPane.showMessageDialog(frame, "Customer not found.");
    }
}
private void showPetDetails(Pet pet) {
    if (pet != null) {
        // 定义表格数据
        String[][] data = {
            {"Name", pet.getName()},
            {"Birthday", pet.getBirthday().toString()},
            {"Gender", pet.getGender().toString()},
            {"Type", pet.getType().toString()},
            {"Breed", pet.getBreed()},
            // 结合提醒的到期日期和注释
            {"Reminders", pet.getReminders().stream()
                .map(reminder -> reminder.getDueDate().toString() + ": " + reminder.getNote())
                .collect(Collectors.joining(", "))},
            // 结合病例记录的日期和描述
            {"Case History", pet.getCaseRecords().stream()
                .map(caseRecord -> caseRecord.getDate().toString() + ": " + caseRecord.getDescription())
                .collect(Collectors.joining(", "))}
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
    JOptionPane.showMessageDialog(frame, "Total number of customers: " + allCustomers.size());
}
}