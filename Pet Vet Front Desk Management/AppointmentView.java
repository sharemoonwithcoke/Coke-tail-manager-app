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
import java.time.format.DateTimeParseException;

public class AppointmentView extends JDialog {
    private JFrame parent;
    private JList<Appointment> appointmentList;
    private DefaultListModel<Appointment> appointmentListModel;
    private JButton addButton, cancelButton, completeButton, modifyButton;
    private AppointmentManager appointmentManager;
    private CustomerManager customerManager; // 新增

    public AppointmentView(JFrame parent, AppointmentManager appointmentManager, CustomerManager customerManager, LocalDate date) {
        super(parent, "Appointment Management", true);
        this.parent = parent;
        this.appointmentManager = appointmentManager;
        this.customerManager = customerManager; 

        setSize(500, 400);
        setLayout(new BorderLayout());

        // 初始化组件
        appointmentListModel = new DefaultListModel<>();
        appointmentList = new JList<>(appointmentListModel);
        // 根据提供的日期更新预约列表
        updateAppointmentListForDate(date);

        JScrollPane scrollPane = new JScrollPane(appointmentList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        completeButton = new JButton("Complete");
        modifyButton = new JButton("Modify");
        


        // 添加按钮事件监听器

       
        

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

private void addAppointment() {
    Person person = selectPerson();
    if (person == null) {
        JOptionPane.showMessageDialog(this, "没有选择人物。");
        return;
    }

    Pet pet = selectPet(person);
    if (pet == null) {
        JOptionPane.showMessageDialog(this, "没有选择宠物。");
        return;
    }

    String dateStr = JOptionPane.showInputDialog(this, "请输入日期 (YYYY-MM-DD):");
    String timeStr = JOptionPane.showInputDialog(this, "请输入时间 (HH:MM):");
    String reason = JOptionPane.showInputDialog(this, "请输入预约原因:");
    LocalDate date;
    LocalTime time;

    try {
        date = LocalDate.parse(dateStr);
        time = LocalTime.parse(timeStr);
    } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(this, "日期或时间格式错误。");
        return;
    }

    // 用户选择医生
    Appointment.Doctor[] doctors = Appointment.Doctor.values();
    Appointment.Doctor doctor = (Appointment.Doctor) JOptionPane.showInputDialog(
            this,
            "选择医生:",
            "选择医生",
            JOptionPane.QUESTION_MESSAGE,
            null,
            doctors,
            doctors[0]);

    if (doctor == null) { // 如果用户没有选择医生（例如，他们点击了取消）
        JOptionPane.showMessageDialog(this, "没有选择医生。");
        return;
    }

    if (!reason.isEmpty()) {
        Appointment appointment = new Appointment(date, person, pet, time, reason, doctor);
        appointmentManager.addAppointment(appointment);
        JOptionPane.showMessageDialog(this, "预约已添加。");
        // 在这里调用更新列表的方法
        updateAppointmentListForDate(date); // 或 updateAppointmentList();
    } else {
        JOptionPane.showMessageDialog(this, "输入信息不完整。");
    }
}






private Person selectPerson() {
    List<Person> persons = this.customerManager.getAllCustomers(); // 修改调用方式
    Person[] personArray = persons.toArray(new Person[0]);

    if (personArray.length == 0) {
        JOptionPane.showMessageDialog(this, "没有可选的客户。");
        return null; // 如果没有客户可选，提前返回
    }

    Person selectedPerson = (Person)JOptionPane.showInputDialog(
            this,
            "请选择客户:",
            "选择客户",
            JOptionPane.QUESTION_MESSAGE,
            null,
            personArray,
            personArray[0]);

    JOptionPane.showMessageDialog(this, "选择的客户: " + (selectedPerson != null ? selectedPerson.getLastName() : "无"));
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
               
                JOptionPane.showMessageDialog(this, "选择的宠物: " + (selectedPet != null ? selectedPet.getName() : "无"));
        return selectedPet;
    }
    




   

    private void cancelAppointment(long appointmentId) {
        if (appointmentManager.cancelAppointment(appointmentId)) {
            JOptionPane.showMessageDialog(this, "预约已取消。", "操作成功", JOptionPane.INFORMATION_MESSAGE);
            updateAppointmentList(); // 刷新预约列表以显示状态更新
        } else {
            JOptionPane.showMessageDialog(this, "取消预约失败或预约已经不处于待处理状态。", "操作失败", JOptionPane.ERROR_MESSAGE);
        }
    }
    



private void completeAppointment(long appointmentId) {
    if (appointmentManager.completeAppointment(appointmentId)) {
        JOptionPane.showMessageDialog(this, "预约已完成。", "操作成功", JOptionPane.INFORMATION_MESSAGE);
        updateAppointmentList(); // 同样，刷新预约列表
    } else {
        JOptionPane.showMessageDialog(this, "完成预约失败或预约已经不处于待处理状态。", "操作失败", JOptionPane.ERROR_MESSAGE);
    }
}


private void modifyAppointment(long appointmentId) {
    // 查找并获取预约
    Appointment appointment = appointmentManager.getAppointmentsByStatus(Appointment.Status.PENDING)
            .stream()
            .filter(a -> a.getAppointmentId() == appointmentId)
            .findFirst()
            .orElse(null);

    if (appointment == null) {
        JOptionPane.showMessageDialog(this, "找不到指定的预约。");
        return;
    }

    // 修改日期
    String newDateStr = JOptionPane.showInputDialog(this, "修改日期 (YYYY-MM-DD)，跳过请直接按Enter：", appointment.getDate().toString());
    LocalDate newDate = newDateStr.isEmpty() ? appointment.getDate() : LocalDate.parse(newDateStr);

    // 修改时间
    String newTimeStr = JOptionPane.showInputDialog(this, "修改时间 (HH:MM)，跳过请直接按Enter：", appointment.getTime().toString());
    LocalTime newTime = newTimeStr.isEmpty() ? appointment.getTime() : LocalTime.parse(newTimeStr);

    // 修改原因
    String newReason = JOptionPane.showInputDialog(this, "修改原因，跳过请直接按Enter：", appointment.getReason());
    newReason = newReason.isEmpty() ? appointment.getReason() : newReason;

    // 修改医生
    Appointment.Doctor[] doctors = Appointment.Doctor.values();
    Appointment.Doctor newDoctor = (Appointment.Doctor) JOptionPane.showInputDialog(
            this,
            "选择医生，跳过请直接按Enter：",
            "选择医生",
            JOptionPane.QUESTION_MESSAGE,
            null,
            doctors,
            appointment.getDoctor());
    newDoctor = newDoctor == null ? appointment.getDoctor() : newDoctor;

    // 更新预约信息
    appointment.setDate(newDate);
    appointment.setTime(newTime);
    appointment.setReason(newReason);
    appointment.setDoctor(newDoctor);

    JOptionPane.showMessageDialog(this, "预约信息已更新。");
    // 可能需要刷新视图
    updateAppointmentList();
}



   
    private void updateAppointmentList() {
        appointmentListModel.clear();
        List<Appointment> appointments = appointmentManager.getAllAppointments();
        for (Appointment appointment : appointments) {
            appointmentListModel.addElement(appointment);
        }
    }

        // 根据日期更新预约列表的新方法
        private void updateAppointmentListForDate(LocalDate date) {
            appointmentListModel.clear();
            List<Appointment> appointments = appointmentManager.getAppointmentsForDate(date);
            for (Appointment appointment : appointments) {
                appointmentListModel.addElement(appointment);
            }
        }
    
    
    }