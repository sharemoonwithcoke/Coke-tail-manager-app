import javax.swing.*;

import Customer.Pet;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import ReminderEmail.Reminder;
import ReminderEmail.ReminderManager;

public class RemindersDialog extends JDialog {
    private JFrame parent;
    private JList<String> reminderList;
    private DefaultListModel<String> reminderListModel;
    private ReminderManager reminderManager;
    private List<Reminder> reminders;
    private List<Pet> pets;

    public RemindersDialog(JFrame parent, ReminderManager reminderManager, List<Pet> pets) {
        super(parent, "Reminders", true);
        this.parent = parent;
        this.reminderManager = reminderManager;
        this.pets = pets;
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

        JButton viewDetailButton = new JButton("View Detail");
        JButton dismissButton = new JButton("Dismiss");

        viewDetailButton.addActionListener(e -> viewReminderDetail());
        dismissButton.addActionListener(e -> dismissReminder());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewDetailButton);
        buttonPanel.add(dismissButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadReminders() {
        // 假设ReminderManager有一个方法返回所有提醒
        reminders = reminderManager.getAllReminders(pets);
        reminderListModel.clear();
        reminders.forEach(reminder -> reminderListModel.addElement(reminder.getNote()));
    }

    private void viewReminderDetail() {
        int selectedIndex = reminderList.getSelectedIndex();
        if (selectedIndex != -1) {
            Reminder selectedReminder = reminders.get(selectedIndex);
            new ReminderDetailView((JFrame)this.getParent(), selectedReminder).setVisible(true);

        }
    }

    private void dismissReminder() {
        int selectedIndex = reminderList.getSelectedIndex();
        if (selectedIndex != -1) {
            // 假设ReminderManager有方法根据索引解除提醒
            reminderManager.dismissReminder(reminders.get(selectedIndex));
            loadReminders(); // 重新加载提醒列表
        }
    }

    public void showDialog() {
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    
}
