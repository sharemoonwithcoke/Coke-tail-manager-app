import javax.swing.*;

import Appointment.AppointmentManager;

import java.awt.*;

public class MainPage extends JFrame {
    private JButton viewAppointmentsButton = new JButton("View Appointments");
    private JButton viewTodoListButton = new JButton("View Todo List");
    private JButton viewRemindersButton = new JButton("View Reminders");
    private AppointmentManager appointmentManager;

    public MainPage(AppointmentManager appointmentManager) {
        this.appointmentManager = appointmentManager;
        setTitle("Main Calendar View");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel calendarPanel = createCalendarPanel();
        JPanel sidePanel = createSidePanel();

        add(calendarPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
    }


    private JPanel createCalendarPanel() {
        // 实现日历面板
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createSidePanel() {
        // 实现包含按钮的侧边栏面板
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(viewAppointmentsButton);
        panel.add(viewTodoListButton); // 将待办事项列表按钮添加到侧边栏
        panel.add(viewRemindersButton);

        return panel;
    }

    // 获取按钮的方法
    public JButton getViewAppointmentsButton() {
        return viewAppointmentsButton;
    }

    public JButton getViewTodoListButton() { // 更改这里
        return viewTodoListButton;
    }

    public JButton getViewRemindersButton() {
        return viewRemindersButton;
    }

   
}
