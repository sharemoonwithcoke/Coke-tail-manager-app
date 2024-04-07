import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {
    public MainPage() {
        setTitle("Main Calendar View");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel calendarPanel = createCalendarPanel(); // This will use a custom method to create the calendar view
        JPanel sidePanel = createSidePanel(); // This panel will contain to-do list and reminders

        add(calendarPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        // Add more components and layout management according to requirements...
    }

    private JPanel createCalendarPanel() {
        // Custom method to create the calendar part of the UI
        JPanel panel = new JPanel();
        // Layout management and component creation
        return panel;
    }

    private JPanel createSidePanel() {
        // Custom method to create the side panel containing to-do list and reminders
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // Add to-do list and reminder components
        return panel;
    }

    //添加了三个按钮！！！
     // Getter methods for buttons
     public JButton getViewAppointmentsButton() {
        return getViewAppointmentsButton();
    }

    public JButton getAddAppointmentButton() {
        return getAddAppointmentButton();
    }

    public JButton getViewRemindersButton() {
        return getViewRemindersButton();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainPage mainPage = new MainPage();
            mainPage.setVisible(true);
        });
    }
}
