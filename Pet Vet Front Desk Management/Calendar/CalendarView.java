import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarView extends JFrame {
    private YearMonth currentYearMonth;
    private AppointmentManager appointmentManager;
    
    public CalendarView(YearMonth currentYearMonth, AppointmentManager appointmentManager) {
        this.currentYearMonth = currentYearMonth;
        this.appointmentManager = appointmentManager;
        initUI();
    }

    private void initUI() {
        setTitle("Calendar");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    
        add(createCalendarPanel(), BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST); // 添加侧边栏
    }
    
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        
        JButton todoButton = new JButton("To-Do List");
        todoButton.addActionListener(e -> showTodoListDialog()); // 显示待办事项视图
        sidePanel.add(todoButton);
        
        JButton remindersButton = new JButton("Reminders");
        remindersButton.addActionListener(e -> showRemindersDialog()); // 显示提醒视图
        sidePanel.add(remindersButton);
        
        return sidePanel;
    }
    

    private JPanel createCalendarPanel() {
        JPanel calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5)); // 7 days in a week
        LocalDate start = currentYearMonth.atDay(1);
        LocalDate end = currentYearMonth.atEndOfMonth();
        
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            calendarPanel.add(createDayPanel(date));
        }
        
        return calendarPanel;
    }

    private JPanel createDayPanel(LocalDate date) {
        JPanel dayPanel = new JPanel();
        dayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dayPanel.setLayout(new BorderLayout());

        JLabel dateLabel = new JLabel(String.valueOf(date.getDayOfMonth()));
        dayPanel.add(dateLabel, BorderLayout.NORTH);
        
        // 根据预约状态添加小点
        addAppointmentStatusDots(dayPanel, date);
        
        return dayPanel;
    }

    private void addAppointmentStatusDots(JPanel dayPanel, LocalDate date) {
        // 获取给定日期的所有预约
        List<Appointment> appointmentsForDay = appointmentManager.getAppointmentsForDay(date);
        
        JPanel dotsPanel = new JPanel(new FlowLayout());
        for (Appointment appointment : appointmentsForDay) {
            JLabel dot = new JLabel("•");
            switch (appointment.getStatus()) {
                case PENDING:
                    dot.setForeground(Color.BLUE);
                    break;
                case CANCELLED:
                    dot.setForeground(Color.RED);
                    break;
                case COMPLETED:
                    dot.setForeground(Color.GREEN);
                    break;
            }
            dotsPanel.add(dot);
        }
        dayPanel.add(dotsPanel, BorderLayout.SOUTH);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppointmentManager appointmentManager = new AppointmentManager(); // 初始化你的AppointmentManager
            CalendarView cv = new CalendarView(YearMonth.now(), appointmentManager);
            cv.setVisible(true);
        });
    }
}
