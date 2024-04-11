import javax.swing.*;

import Appointment.AppointmentManager;
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
    private JPanel calendarPanel = new JPanel(new GridLayout(0, 7)); // 7天一周
    private JButton viewAppointmentsButton = new JButton("View Appointments");
    private JButton viewTodoListButton = new JButton("View Todo List");
    private JButton viewRemindersButton = new JButton("View Reminders");
    private JButton viewCustomerButton = new JButton("View Customer");
    private AppointmentManager appointmentManager;
    private ToDoList toDoList; // 假设ToDoList是已定义的类型
    private ReminderManager reminderManager; // 同上
    private CustomerManager customerManager;

    // 修改构造函数以接收ToDoList和ReminderManager实例
    public MainPage(AppointmentManager appointmentManager, ToDoList toDoList, ReminderManager reminderManager, CustomerManager customerManager) {
        this.appointmentManager = appointmentManager;
        this.toDoList = toDoList; // 确保这里使用了正确的实例
        this.reminderManager = reminderManager; // 同上
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
            AppointmentView appointmentView = new AppointmentView(this, appointmentManager, customerManager, LocalDate.now()); // 修改为传递今天的日期，或根据需要选择其他逻辑
            appointmentView.setVisible(true);
        });

        viewTodoListButton.addActionListener(e -> {
            ToDoListDialog toDoListDialog = new ToDoListDialog(this, toDoList); // 确保这里正确传递ToDoList实例
            toDoListDialog.setVisible(true);
        });

        viewRemindersButton.addActionListener(e -> {
            try {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                RemindersDialog remindersDialog = new RemindersDialog(frame, reminderManager, null);
                remindersDialog.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace(); // 打印异常堆栈，便于调试
            }
        });
        
        

        viewCustomerButton.addActionListener(e -> {
            CustomerinfoView customerView = new CustomerinfoView(this, customerManager); // 确保这里正确传递CustomerManager实例
            customerView.setVisible(true);
        });
    }

    
    private JPanel createCalendarPanel() {
        calendarPanel.removeAll();
        calendarPanel.setLayout(new GridLayout(0, 7)); // Resetting the layout for the calendar panel

        LocalDate startDate = currentMonth.atDay(1);
        int dayOfWeekOfFirstDay = startDate.getDayOfWeek().getValue() % 7;

        // Fill empty for days before the first day of the month
        for (int i = 0; i < dayOfWeekOfFirstDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Add buttons for each day
        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            final int finalDay = day; // 使变量在内部类/lambda表达式中可以被使用
            JButton dayButton = new JButton(Integer.toString(day));
            dayButton.addActionListener(e -> {
                LocalDate selectedDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), finalDay);
                // 假设AppointmentView构造器接受一个LocalDate参数
                AppointmentView appointmentView = new AppointmentView(this, appointmentManager, customerManager, selectedDate);
                appointmentView.setVisible(true); // 显示预约详情视图
            });
            calendarPanel.add(dayButton);
        }
        

        calendarPanel.revalidate();
        calendarPanel.repaint();

        return calendarPanel;
    }

    private String getButtonTextForDay(LocalDate date) {
        // Here, you'd fetch the appointment status counts for the given date
        // This is a placeholder implementation
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
        // This should be replaced with actual logic to fetch data from AppointmentManager
        return new HashMap<>(); // Empty map as a placeholder
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(viewAppointmentsButton);
        panel.add(viewTodoListButton);
        panel.add(viewRemindersButton);
        panel.add(viewCustomerButton);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppointmentManager appointmentManager = new AppointmentManager();
            ToDoList toDoList = new ToDoList();
            ReminderManager reminderManager = new ReminderManager();
            CustomerManager customerManager = new CustomerManager(); // 初始化CustomerManager
    
            MainPage mainPage = new MainPage(appointmentManager, toDoList, reminderManager, customerManager);
            mainPage.setVisible(true);
        });
    }
    
    }
    

