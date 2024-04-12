package Calendar;

import javax.swing.*;
import Customer.CustomerManager;
import Customer.Person;
import Customer.Pet;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import ReminderEmail.Reminder;
import ReminderEmail.ReminderManager;

public class RemindersDialog extends JDialog {
    private JFrame parent;
    private JList<String> reminderList;
    private DefaultListModel<String> reminderListModel;
    private ReminderManager reminderManager;
    private List<Reminder> reminders;
    private CustomerManager customerManager; // Added to access customer data

    // Constructor modified to remove the pets parameter, since we're now showing
    // reminders for all pets
    public RemindersDialog(JFrame parent, ReminderManager reminderManager, CustomerManager customerManager) {
        super(parent, "Reminders", true);
        this.parent = parent;
        this.reminderManager = reminderManager;
        this.customerManager = customerManager; // Initialize customerManager
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

        JButton dismissButton = new JButton("Dismiss");
        JButton addReminderButton = new JButton("Add");
        // Removed the Add Reminder button, as it was not fully implemented
        // and its functionality was not specified for all pets

        dismissButton.addActionListener(e -> removeReminder());
        addReminderButton.addActionListener(e -> {
            System.out.println("Add Reminder Button Clicked");
            addReminder();
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(dismissButton);
        buttonPanel.add(addReminderButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadReminders() {
        // Loading reminders for all pets now
        reminders = reminderManager.getAllReminders(); // Assuming getAllReminders method is updated to require no
                                                       // parameters
        reminderListModel.clear();
        reminders.forEach(reminder -> reminderListModel.addElement(reminder.getNote()));
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
        // Choose a pet for the reminder
        List<Pet> pets = customerManager.getAllPets();
        System.out.println("Pets size: " + pets.size());
        if (pets.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pets found. Please add a pet first.");
            return;
        }
        // Convert the list of pets to an array for the dialog
        Pet[] petArray = pets.toArray(new Pet[0]);
        Pet pet = (Pet) JOptionPane.showInputDialog(
                this,
                "Choose Pets:",
                "Add reminder for pet",
                JOptionPane.QUESTION_MESSAGE,
                null,
                petArray,
                petArray[0]);

        if (pet == null) {
            JOptionPane.showMessageDialog(this, "Please select a pet.");// Check if a pet is selected
            return;
        }

        // Ask for reminder details
        String dateStr = JOptionPane.showInputDialog(this, "Please enter the date (YYYY-MM-DD):");
        LocalDate date;

        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Date formatting error. Please use the format YYYY-MM-DD.");
            return;
        }

        String note = JOptionPane.showInputDialog(this, "Please enter a note:");
        if (note.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid note.");
            return;
        }

        // Ask for days before reminder
        String daysBeforeStr = JOptionPane.showInputDialog(this, "Please enter the number of days before the reminder:");
        int daysBefore;
        try {
            daysBefore = Integer.parseInt(daysBeforeStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number of days. Please enter a valid number.");
            return;
        }

        // Add the reminder
        reminderManager.addGeneralReminder(pet, date, note, daysBefore);

        JOptionPane.showMessageDialog(this, "Reminder added successfully.");
        loadReminders(); // Reload reminders after adding
    }

    // Removed addReminder method as it was unclear how it should function without
    // specific pet context

    public void showDialog() {
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
