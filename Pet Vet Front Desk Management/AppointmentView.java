import javax.swing.*;

import Appointment.Appointment;
import Appointment.AppointmentManager;
import Customer.CustomerManager;
import Customer.Person;
import Customer.Pet;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AppointmentView extends JDialog {
    private JFrame parent;
    private JList<Appointment> appointmentList;
    private DefaultListModel<Appointment> appointmentListModel;
    private JButton addButton;
    private JButton cancelButton;
    private JButton completeButton;
    private JButton modifyButton;

    private AppointmentManager appointmentManager;

    public AppointmentView(JFrame parent, AppointmentManager appointmentManager) {
        super(parent, "Appointment Management", true);
        this.parent = parent;
        this.appointmentManager = appointmentManager;
        
        setSize(500, 400);
        setLayout(new BorderLayout());

        appointmentListModel = new DefaultListModel<>();
        appointmentList = new JList<>(appointmentListModel);
        updateAppointmentList();

        JScrollPane scrollPane = new JScrollPane(appointmentList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        completeButton = new JButton("Complete");
        modifyButton = new JButton("Modify");


        // 添加按钮事件监听器

        appointmentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Appointment selectedAppointment = appointmentList.getSelectedValue();
                if (selectedAppointment != null) {
                    Person client = selectedAppointment.getPerson();
                    openClientView(client);
                }
            }
        });
        

        addButton.addActionListener(e -> addAppointment());
        cancelButton.addActionListener(e -> {
            Appointment selectedAppointment = appointmentList.getSelectedValue();
            if (selectedAppointment != null) {
                cancelAppointment(selectedAppointment.getAppointmentId());
            } else {
                JOptionPane.showMessageDialog(this, "请先选择一个预约。");
            }
        });
        
        completeButton.addActionListener(e -> {
            Appointment selectedAppointment = appointmentList.getSelectedValue();
            if (selectedAppointment != null) {
                completeAppointment(selectedAppointment.getAppointmentId());
            } else {
                JOptionPane.showMessageDialog(this, "请先选择一个预约。");
            }
        });
        
        modifyButton.addActionListener(e -> {
            Appointment selectedAppointment = appointmentList.getSelectedValue();
            if (selectedAppointment != null) {
                modifyAppointment(selectedAppointment.getAppointmentId());
            } else {
                JOptionPane.showMessageDialog(this, "请先选择一个预约。");
            }
        });
        

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(modifyButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    



// 添加以下方法

private void openClientView(Person person) {
    CustomerinfoView clientView = new CustomerinfoView(this.parent, person);
    clientView.setVisible(true);
}



    private Person selectPerson() {
        List<Person> persons = CustomerManager.getAllPersons();
        Person[] personArray = persons.toArray(new Person[0]);
        
        Person selectedPerson = (Person)JOptionPane.showInputDialog(
                this,
                "请选择客户:",
                "选择客户",
                JOptionPane.QUESTION_MESSAGE,
                null,
                personArray,
                personArray[0]);
        
        return selectedPerson;
    }
    private Pet selectPet(Person person) {
        List<Pet> pets = person.getPets();
        Pet[] petArray = pets.toArray(new Pet[0]);
        
        Pet selectedPet = (Pet)JOptionPane.showInputDialog(
                this,
                "请选择宠物:",
                "选择宠物",
                JOptionPane.QUESTION_MESSAGE,
                null,
                petArray,
                petArray[0]);
        
        return selectedPet;
    }
    
    private void addAppointment() {
    // 假设您有一个获取Person和Pet对象的方法
    Person person = selectPerson(); // 选择Person
    if (person == null) {
        JOptionPane.showMessageDialog(this, "没有选择人物。");
        return;
    }
    Pet pet = selectPet(person); // 基于选中的Person选择Pet
    if (pet == null) {
        JOptionPane.showMessageDialog(this, "没有选择宠物。");
        return;
    }
    String dateStr = JOptionPane.showInputDialog(this, "请输入日期 (YYYY-MM-DD):");
    String timeStr = JOptionPane.showInputDialog(this, "请输入时间 (HH:MM):");
    String reason = JOptionPane.showInputDialog(this, "请输入预约原因:");
    LocalDate date = LocalDate.parse(dateStr);
    LocalTime time = LocalTime.parse(timeStr);
    Appointment.Doctor doctor = Appointment.Doctor.DR_A; // 这里简化处理，实际可以让用户选择

    if (person != null && pet != null && date != null && time != null && reason != null && !reason.isEmpty()) {
        Appointment appointment = new Appointment(date, person, pet, time, reason, doctor);
        appointmentManager.addAppointment(appointment);
        JOptionPane.showMessageDialog(this, "预约已添加。");
    } else {
        JOptionPane.showMessageDialog(this, "输入信息不完整。");
    }
}


private void cancelAppointment(long appointmentId) {
    if (appointmentManager.cancelAppointment(appointmentId)) {
        JOptionPane.showMessageDialog(this, "预约已取消。");
    } else {
        JOptionPane.showMessageDialog(this, "取消预约失败。");
    }
    // 可能需要刷新视图
}
private void completeAppointment(long appointmentId) {
    if (appointmentManager.completeAppointment(appointmentId)) {
        JOptionPane.showMessageDialog(this, "预约已完成。");
    } else {
        JOptionPane.showMessageDialog(this, "完成预约失败。");
    }
    // 可能需要刷新视图
}
private void modifyAppointment(long appointmentId) {
    // 查找并获取预约
    Appointment appointment = appointmentManager.getAppointmentsByStatus(Appointment.Status.PENDING)
            .stream()
            .filter(a -> a.getAppointmentId() == appointmentId)
            .findFirst()
            .orElse(null);
    if (appointment != null) {
        // 示例：修改预约时间，实际应从用户输入获取新的时间
        LocalTime newTime = LocalTime.of(14, 0);
        appointment.setTime(newTime);
        JOptionPane.showMessageDialog(this, "预约时间已更新。");
        // 可能需要刷新视图
    } else {
        JOptionPane.showMessageDialog(this, "找不到指定的预约。");
    }
}


   
    private void updateAppointmentList() {
        appointmentListModel.clear();
        List<Appointment> appointments = appointmentManager.getAllAppointments();
        for (Appointment appointment : appointments) {
            appointmentListModel.addElement(appointment);
        }
    }
}
