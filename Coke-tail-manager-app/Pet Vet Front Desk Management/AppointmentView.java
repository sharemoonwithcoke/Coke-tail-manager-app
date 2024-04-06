import javax.swing.*;

public class AppointmentView extends JDialog {
    public AppointmentView(JFrame parent) {
        super(parent, "Appointment Management", true);
        setSize(300, 400);
        setLayout(new BorderLayout());

        JList<String> appointmentList = new JList<>(); // Fill with appointments
        JScrollPane scrollPane = new JScrollPane(appointmentList);
        add(scrollPane, BorderLayout.CENTER);

        // Add buttons for adding, completing, or cancelling appointments

        // Add more components and layout management according to requirements...
    }
}

