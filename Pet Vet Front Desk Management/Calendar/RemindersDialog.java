import javax.swing.*;

import Customer.CustomerManager;
import Customer.Person;
import Customer.Pet;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import ReminderEmail.Reminder;
import ReminderEmail.ReminderManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RemindersDialog extends JDialog {
    private JFrame parent;
    private JList<String> reminderList;
    private DefaultListModel<String> reminderListModel;
    private ReminderManager reminderManager;
    private List<Reminder> reminders;
    private CustomerManager customerManager; // Added to access customer data

    // Constructor modified to remove the pets parameter, since we're now showing reminders for all pets
    public RemindersDialog(JFrame parent, ReminderManager reminderManager, CustomerManager customerManager) {
        super(parent, "Reminders", true);
        this.parent = parent;
        this.reminderManager = reminderManager;
        this.customerManager = customerManager; // 确保传递了customerManager的实例
        initializeUI();
        loadReminders();
    }
    

    private void initializeUI() {
        setSize(300, 400);
        setLayout(new BorderLayout());

        reminderListModel = new DefaultListModel<>();
        reminderList = new JList<>(reminderListModel);
        JScrollPane scrollPane = new JScrollPane(reminderList);
        add(scrollPane, BorderLayout.CENTER);

        JButton viewDetailButton = new JButton("View");
        JButton dismissButton = new JButton("Dismiss");
        JButton addReminderButton = new JButton("Add");
        // Removed the Add Reminder button, as it was not fully implemented
        // and its functionality was not specified for all pets

        viewDetailButton.addActionListener(e -> viewReminderDetail());
        dismissButton.addActionListener(e -> removeReminder());
        addReminderButton.addActionListener(e -> {
            System.out.println("Add Reminder Button Clicked");
            addReminder();
        });
        

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewDetailButton);
        buttonPanel.add(dismissButton);
        buttonPanel.add(addReminderButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadReminders() {
        // Loading reminders for all pets now
        reminders = reminderManager.getAllReminders(); // Assuming getAllReminders method is updated to require no parameters
        reminderListModel.clear();
        reminders.forEach(reminder -> reminderListModel.addElement(reminder.getNote()));
    }


    /////

    private void viewReminderDetail() {
        int selectedIndex = reminderList.getSelectedIndex();
        if (selectedIndex != -1) {
            Reminder selectedReminder = reminders.get(selectedIndex);
            Pet associatedPet = findPetForReminder(selectedReminder); // 需要实现这个方法
            if (associatedPet != null) {
                // 这里假设每个Pet都可以获取到其Owner的信息
                Person owner = findOwnerForPet(associatedPet); // 同样，需要实现这个方法
                if (owner != null) {
                    new ReminderDetailView((JFrame)this.getParent(), selectedReminder, associatedPet, owner).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Unable to find the owner for the selected pet.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Unable to find the pet for the selected reminder.");
            }
        }
    }
    

    private Person findOwnerForPet(Pet associatedPet) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOwnerForPet'");
    }


    private Pet findPetForReminder(Reminder selectedReminder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findPetForReminder'");
    }


    private void removeReminder() {
        int selectedIndex = reminderList.getSelectedIndex();
        if (selectedIndex != -1) {
            Reminder selectedReminder = reminders.get(selectedIndex);
            reminderManager.removeReminder(selectedReminder);
            loadReminders(); // Reload reminders after removal
        }
    }

    private void addReminder() {
        // 选择宠物
        List<Pet> pets = customerManager.getAllPets();
        System.out.println("Pets size: " + pets.size());
        if (pets.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有宠物。");
            return;
        }
        Pet[] petArray = pets.toArray(new Pet[0]);
        Pet pet = (Pet) JOptionPane.showInputDialog(
                this,
                "选择宠物:",
                "添加提醒",
                JOptionPane.QUESTION_MESSAGE,
                null,
                petArray,
                petArray[0]);
    
        if (pet == null) {
            JOptionPane.showMessageDialog(this, "没有选择宠物。");
            return;
        }
    
        // 收集提醒信息
        String dateStr = JOptionPane.showInputDialog(this, "请输入日期 (YYYY-MM-DD):");
        LocalDate date;
    
        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "日期格式错误。");
            return;
        }
    
        String note = JOptionPane.showInputDialog(this, "请输入备注:");
        if (note.isEmpty()) {
            JOptionPane.showMessageDialog(this, "输入信息不完整。");
            return;
        }
    
        // 询问提前多少天提醒
        String daysBeforeStr = JOptionPane.showInputDialog(this, "提前多少天提醒:");
        int daysBefore;
        try {
            daysBefore = Integer.parseInt(daysBeforeStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "天数输入不正确。");
            return;
        }
    
        // 添加通用提醒
        reminderManager.addGeneralReminder(pet, date, note, daysBefore);
    
        JOptionPane.showMessageDialog(this, "提醒已添加。");
        loadReminders(); // 重新加载提醒列表
    }
    
    
    // Removed addReminder method as it was unclear how it should function without specific pet context

    public void showDialog() {
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
