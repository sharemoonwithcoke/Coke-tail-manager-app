import javax.swing.*;
import Appointment.Appointment;
import Appointment.AppointmentManager;
import Customer.CustomerManager;
import Customer.Person;
import Customer.Pet;
import java.awt.*;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AppointmentView extends JDialog {
    private JFrame parent;
    private JList<Appointment> appointmentList;
    private DefaultListModel<Appointment> appointmentListModel;
    private JButton addButton, cancelButton, completeButton, modifyButton;
    private AppointmentManager appointmentManager;
    private CustomerManager customerManager; // Added to access customer data

    // Modify the constructor to include the CustomerManager instance
    public AppointmentView(JFrame parent, AppointmentManager appointmentManager, CustomerManager customerManager,
            LocalDate date) {
        super(parent, "Appointment Management", true);
        this.parent = parent;
        this.appointmentManager = appointmentManager;
        this.customerManager = customerManager;

        setSize(500, 400);
        setLayout(new BorderLayout());

        // Create the list model and list
        appointmentListModel = new DefaultListModel<>();
        appointmentList = new JList<>(appointmentListModel);
        // Update the list based on the provided date
        updateAppointmentListForDate(date);

        JScrollPane scrollPane = new JScrollPane(appointmentList);
        add(scrollPane, BorderLayout.CENTER);
        // Create the button panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        completeButton = new JButton("Complete");
        modifyButton = new JButton("Modify");

        // Add action listeners to the buttons
        addButton.addActionListener(e -> addAppointment());
        cancelButton.addActionListener(e -> {
            Appointment selectedAppointment = appointmentList.getSelectedValue();
            if (selectedAppointment != null) {
                cancelAppointment(selectedAppointment.getAppointmentId());
            } else {
                JOptionPane.showMessageDialog(this, "Please select an appointment first.");
            }
        });
        // Add an action listener to the Complete button
        completeButton.addActionListener(e -> {
            Appointment selectedAppointment = appointmentList.getSelectedValue();
            if (selectedAppointment != null) {
                completeAppointment(selectedAppointment.getAppointmentId());
            } else {
                JOptionPane.showMessageDialog(this, "Please select an appointment first.");
            }
        });
        // Add an action listener to the Modify button
        modifyButton.addActionListener(e -> {
            Appointment selectedAppointment = appointmentList.getSelectedValue();
            if (selectedAppointment != null) {
                modifyAppointment(selectedAppointment.getAppointmentId());
            } else {
                JOptionPane.showMessageDialog(this, "Please select an appointment first.");
            }
        });
        // Add buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(modifyButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Add a new method to update the appointment list based on the selected date
    private void addAppointment() {
        Person person = selectPerson();
        if (person == null) {
            JOptionPane.showMessageDialog(this, "No customer selected.");
            return;
        }
        // Select a pet for the appointment
        Pet pet = selectPet(person);
        if (pet == null) {
            JOptionPane.showMessageDialog(this, "No pet selected.");
            return;
        }
        // Enter the date, time, and reason for the appointment
        String dateStr = JOptionPane.showInputDialog(this, "Please enter the date (YYYY-MM-DD):");
        String timeStr = JOptionPane.showInputDialog(this, "Please enter the time (HH:MM):");
        String reason = JOptionPane.showInputDialog(this, "Please enter the reason:");
        LocalDate date;
        LocalTime time;

        try {
            date = LocalDate.parse(dateStr);
            time = LocalTime.parse(timeStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "The date or time is formatted incorrectly.");
            return;
        }

        // Select a doctor for the appointment
        Appointment.Doctor[] doctors = Appointment.Doctor.values();
        Appointment.Doctor doctor = (Appointment.Doctor) JOptionPane.showInputDialog(
                this,
                "Select Doctor:",
                "Select Doctor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                doctors,
                doctors[0]);

        if (doctor == null) { // Check if a doctor is selected
            JOptionPane.showMessageDialog(this, "No doctor selected.");
            return;
        }
        // Check if a reason is provided
        if (!reason.isEmpty()) {
            Appointment appointment = new Appointment(date, person, pet, time, reason, doctor);
            appointmentManager.addAppointment(appointment);
            JOptionPane.showMessageDialog(this, "Appointment added successfully.");
            // Update the list based on the selected date
            updateAppointmentListForDate(date); // Update the list based on the selected date
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a reason for the appointment.");
        }
    }
    // Add a new method to select a person from the list of customers
    private Person selectPerson() {
        List<Person> persons = this.customerManager.getAllCustomers(); // Get all customers
        Person[] personArray = persons.toArray(new Person[0]);

        if (personArray.length == 0) {
            JOptionPane.showMessageDialog(this, "No customers available.");
            return null; // Return null if no customers are available
        }
        // Display a dialog to select a person
        Person selectedPerson = (Person) JOptionPane.showInputDialog(
                this,
                "Select Customer:",
                "Select Customer",
                JOptionPane.QUESTION_MESSAGE,
                null,
                personArray,
                personArray[0]);

        JOptionPane.showMessageDialog(this,
                "Selected Customers: " + (selectedPerson != null ? selectedPerson.getLastName() : "None"));// Display the selected customer
        return selectedPerson;
    }
    // Add a new method to select a pet from the list of pets
    private Pet selectPet(Person person) {
        List<Pet> pets = person.getPets();
        Pet[] petArray = pets.toArray(new Pet[0]);

        Pet selectedPet = (Pet) JOptionPane.showInputDialog(
                this,
                "Select Pet",
                "Select Pet",
                JOptionPane.QUESTION_MESSAGE,
                null,
                petArray,
                petArray[0]);

        JOptionPane.showMessageDialog(this, "Selected Pet " + (selectedPet != null ? selectedPet.getName() : "None"));
        return selectedPet;
    }

    private void cancelAppointment(long appointmentId) {
        if (appointmentManager.cancelAppointment(appointmentId)) {
            JOptionPane.showMessageDialog(this, "Appointment has been canceled.", "The operation was successful.",
                    JOptionPane.INFORMATION_MESSAGE);
            updateAppointmentList(); // Refresh the appointment list
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to cancel the appointment or the appointment is no longer in the pending status.",
                    "Failure of an operation", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Add a new method to complete the appointment
    private void completeAppointment(long appointmentId) {
        if (appointmentManager.completeAppointment(appointmentId)) {
            JOptionPane.showMessageDialog(this, "Appointments have been completed.", "The operation was successful.",
                    JOptionPane.INFORMATION_MESSAGE);
            updateAppointmentList(); // Refresh the appointment list
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to complete the reservation or the reservation is no longer in the pending status。",
                    "Failure of an operation", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifyAppointment(long appointmentId) {
        // Find the appointment by ID
        Appointment appointment = appointmentManager.getAppointmentsByStatus(Appointment.Status.PENDING)
                .stream()
                .filter(a -> a.getAppointmentId() == appointmentId)
                .findFirst()
                .orElse(null);

        if (appointment == null) {
            JOptionPane.showMessageDialog(this, "The appointment was not found or is not in the pending status.");
            return;
        }

        // Edit the appointment details
        String newDateStr = JOptionPane.showInputDialog(this, "Modified Date (YYYY-MM-DD), skip and press Enter:",
                appointment.getDate().toString());
        LocalDate newDate = newDateStr.isEmpty() ? appointment.getDate() : LocalDate.parse(newDateStr);

        // Modify time
        String newTimeStr = JOptionPane.showInputDialog(this, "Modify Time (HH:MM), skip and press Enter:",
                appointment.getTime().toString());
        LocalTime newTime = newTimeStr.isEmpty() ? appointment.getTime() : LocalTime.parse(newTimeStr);

        // Modify reason
        String newReason = JOptionPane.showInputDialog(this, "Reason for modification, To skip, simply press Enter:",
                appointment.getReason());
        newReason = newReason.isEmpty() ? appointment.getReason() : newReason;

        // Select a doctor for the appointment
        Appointment.Doctor[] doctors = Appointment.Doctor.values();
        Appointment.Doctor newDoctor = (Appointment.Doctor) JOptionPane.showInputDialog(
                this,
                "Select the doctor, to skip please just press Enter:",
                "Select Doctor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                doctors,
                appointment.getDoctor());
        newDoctor = newDoctor == null ? appointment.getDoctor() : newDoctor;

        // Update the appointment
        appointment.setDate(newDate);
        appointment.setTime(newTime);
        appointment.setReason(newReason);
        appointment.setDoctor(newDoctor);

        JOptionPane.showMessageDialog(this, "The appointment has been successfully modified.");
        // Refresh the appointment list
        updateAppointmentList();
    }

    private void updateAppointmentList() {
        appointmentListModel.clear();
        List<Appointment> appointments = appointmentManager.getAllAppointments();
        for (Appointment appointment : appointments) {
            appointmentListModel.addElement(appointment);
        }
    }

    // Add a new method to update the appointment list based on the selected date
    private void updateAppointmentListForDate(LocalDate date) {
        appointmentListModel.clear();
        List<Appointment> appointments = appointmentManager.getAppointmentsForDate(date);
        for (Appointment appointment : appointments) {
            appointmentListModel.addElement(appointment);
        }
    }

}