import javax.swing.*;
import Appointment.AppointmentManager;
import Calendar.RemindersDialog;
import Calendar.ToDoListDialog;
import Customer.CustomerManager;
import toDo.ToDoList;
import ReminderEmail.ReminderManager;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class MainPage extends JFrame {
    private YearMonth currentMonth = YearMonth.now();
    private JPanel calendarPanel = new JPanel(new GridLayout(0, 7)); // Use a 7-column grid layout
    private JButton viewAppointmentsButton = new JButton("View Appointments");
    private JButton viewTodoListButton = new JButton("View Todo List");
    private JButton viewRemindersButton = new JButton("View Reminders");
    private JButton viewCustomerButton = new JButton("View Customer");
    private AppointmentManager appointmentManager;
    private ToDoList toDoList; // Assuming the ToDoList is of the defined type
    private ReminderManager reminderManager; // Assuming the ReminderManager is of the defined type
    private CustomerManager customerManager;

    // Modify the constructor to receive ToDoList and ReminderManager instances
    public MainPage(AppointmentManager appointmentManager, ToDoList toDoList, ReminderManager reminderManager,
            CustomerManager customerManager) {
        this.appointmentManager = appointmentManager;
        this.toDoList = toDoList; // Make sure the correct example is used here
        this.reminderManager = reminderManager; // Make sure the correct example is used here
        this.customerManager = customerManager;

        setTitle("Main Calendar View");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createCalendarPanel(), BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);

        addEventListeners();
    }

    private void addEventListeners() {
        viewAppointmentsButton.addActionListener(e -> {
            AppointmentView appointmentView = new AppointmentView(this, appointmentManager, customerManager,
                    LocalDate.now()); // Modify to pass today's date, or select other logic as needed
            appointmentView.setVisible(true);
        });

        viewTodoListButton.addActionListener(e -> {
            ToDoListDialog toDoListDialog = new ToDoListDialog(this, toDoList); // Ensure that the ToDoList instance is
                                                                                // passed correctly here
            toDoListDialog.setVisible(true);
        });

        viewRemindersButton.addActionListener(e -> {
            try {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                RemindersDialog remindersDialog = new RemindersDialog(frame, reminderManager, customerManager);
                remindersDialog.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace(); // Add proper exception handling
            }
        });

        viewCustomerButton.addActionListener(e -> {
            CustomerinfoView customerView = new CustomerinfoView(this, customerManager); // Ensure that the
                                                                                         // CustomerManager instance is
                                                                                         // passed correctly here
            customerView.setVisible(true);
        });
    }

    // Create a new method to update the calendar panel with the appointments
    private JPanel createCalendarPanel() {
        calendarPanel.removeAll();
        calendarPanel.setLayout(new GridLayout(0, 7));// Retain the original 7-column grid layout

        // Add day labels
        String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        for (String day : weekDays) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            dayLabel.setBackground(Color.GRAY); // Set background color
            dayLabel.setOpaque(true);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
            calendarPanel.add(dayLabel);
        }

        LocalDate today = LocalDate.now();
        LocalDate startDate = currentMonth.atDay(1);
        int dayOfWeekOfFirstDay = startDate.getDayOfWeek().getValue() % 7;
        dayOfWeekOfFirstDay = dayOfWeekOfFirstDay == 0 ? 6 : dayOfWeekOfFirstDay - 1;

        // Add empty labels for the days before the first day of the month
        for (int i = 0; i < dayOfWeekOfFirstDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Add buttons for each day of the month
        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            final int finalDay = day;
            JButton dayButton = new JButton(Integer.toString(day));
            dayButton.setFont(new Font("Arial", Font.PLAIN, 11));
            if (LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).equals(today)) {
                dayButton.setBackground(Color.RED);
            } else {
                dayButton.setBackground(dayOfWeekOfFirstDay >= 5 ? Color.LIGHT_GRAY : Color.WHITE); // Set different
                                                                                                    // background colors
                                                                                                    // for weekends
            }
            dayButton.setOpaque(true);
            dayButton.setBorderPainted(false);
            dayButton.addActionListener(e -> {
                LocalDate selectedDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), finalDay);
                AppointmentView appointmentView = new AppointmentView(this, appointmentManager, customerManager,
                        selectedDate);
                appointmentView.setVisible(true);
            });
            calendarPanel.add(dayButton);
        }
        calendarPanel.revalidate();
        calendarPanel.repaint();
        return calendarPanel;
    }

    private String getButtonTextForDay(LocalDate date) {
        Map<String, Integer> statusCounts = getStatusCountsForDay(date);

        String buttonText = "<html>" + date.getDayOfMonth(); // Use HTML for multi-line text
        if (statusCounts.getOrDefault("cancel", 0) > 0) {
            buttonText += "<br><font color='red'>" + statusCounts.get("cancel") + " C</font>";
        }
        if (statusCounts.getOrDefault("pending", 0) > 0) {
            buttonText += "<br><font color='orange'>" + statusCounts.get("pending") + " P</font>";
        }
        if (statusCounts.getOrDefault("complete", 0) > 0) {
            buttonText += "<br><font color='green'>" + statusCounts.get("complete") + " D</font>";
        }
        buttonText += "</html>";
        return buttonText;
    }

    // Placeholder method for getting status counts for a specific day
    private Map<String, Integer> getStatusCountsForDay(LocalDate date) {
        return new HashMap<>();
    }

    private JButton createDayButton(LocalDate date) {
        Map<String, Integer> statusCounts = appointmentManager.getStatusCountsForDay(date);
        JButton button = new JButton();
        StringBuilder buttonText = new StringBuilder("<html><b>" + date.getDayOfMonth() + "</b>");
        if (statusCounts.getOrDefault("cancelled", 0) > 0) {// Check if there are cancelled appointments
            buttonText.append("<br><font color='#FFA07A' size='2'>").append(statusCounts.get("cancelled"))// Add the
                                                                                                          // count of
                                                                                                          // cancelled
                                                                                                          // appointments
                    .append(" C</font>");
        }
        if (statusCounts.getOrDefault("completed", 0) > 0) {
            buttonText.append("<br><font color='#90EE90' size='2'>").append(statusCounts.get("completed"))// Add the
                                                                                                          // count of
                                                                                                          // completed
                                                                                                          // appointments
                    .append(" D</font>");
        }
        if (statusCounts.getOrDefault("pending", 0) > 0) {
            buttonText.append("<br><font color='#ADD8E6' size='2'>").append(statusCounts.get("pending"))// Add the count
                                                                                                        // of pending
                                                                                                        // appointments
                    .append(" P</font>");
        }
        buttonText.append("</html>");
        button.setText(buttonText.toString());
        // Add an action listener to open the appointment view for the selected date
        button.addActionListener(e -> {
            AppointmentView appointmentView = new AppointmentView(this, appointmentManager, customerManager, date);
            appointmentView.setVisible(true);
        });

        return button;
    }

    // Create a new method to update the calendar panel with the appointments
    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // Set the background color
        JButton[] buttons = { viewAppointmentsButton, viewTodoListButton, viewRemindersButton, viewCustomerButton };
        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBackground(Color.GRAY);
            button.setForeground(Color.BLACK);
            button.setOpaque(true);

            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
            panel.add(button);
        }

        panel.add(Box.createVerticalStrut(10));

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppointmentManager appointmentManager = new AppointmentManager();
            ToDoList toDoList = new ToDoList();
            CustomerManager customerManager = new CustomerManager(); // Initialize CustomerManager first
            ReminderManager reminderManager = new ReminderManager(customerManager); // Initializing ReminderManager with
                                                                                    // CustomerManager

            MainPage mainPage = new MainPage(appointmentManager, toDoList, reminderManager, customerManager);
            mainPage.setVisible(true);
        });
    }

}
