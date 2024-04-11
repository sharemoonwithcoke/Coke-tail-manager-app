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
                RemindersDialog remindersDialog = new RemindersDialog(frame, reminderManager, customerManager);
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
        calendarPanel.setLayout(new GridLayout(0, 7)); // 保留原有的7列网格布局
    
        // 设置日历头部的样式
        String[] weekDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : weekDays) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            dayLabel.setBackground(Color.GRAY); // 设置背景颜色
            dayLabel.setOpaque(true);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
            calendarPanel.add(dayLabel);
        }
    
        LocalDate today = LocalDate.now();
        LocalDate startDate = currentMonth.atDay(1);
        int dayOfWeekOfFirstDay = startDate.getDayOfWeek().getValue() % 7;
        dayOfWeekOfFirstDay = dayOfWeekOfFirstDay == 0 ? 6 : dayOfWeekOfFirstDay - 1;
    
        // 在第一天之前填充空标签以对齐日期
        for (int i = 0; i < dayOfWeekOfFirstDay; i++) {
            calendarPanel.add(new JLabel(""));
        }
    
        // 添加每一天的按钮
        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            final int finalDay = day;
            JButton dayButton = new JButton(Integer.toString(day));
            dayButton.setFont(new Font("Arial", Font.PLAIN, 11));
            if (LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).equals(today)) {
                dayButton.setBackground(Color.CYAN); // 当天日期突出显示
            } else {
                dayButton.setBackground(dayOfWeekOfFirstDay >= 5 ? Color.LIGHT_GRAY : Color.WHITE); // 区分周末
            }
            dayButton.setOpaque(true);
            dayButton.setBorderPainted(false);
            dayButton.addActionListener(e -> {
                LocalDate selectedDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), finalDay);
                AppointmentView appointmentView = new AppointmentView(this, appointmentManager, customerManager, selectedDate);
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
    
        
        if (statusCounts.getOrDefault("cancelled", 0) > 0) {
            buttonText.append("<br><font color='#FFA07A' size='2'>").append(statusCounts.get("cancelled")).append(" C</font>");
        }
        if (statusCounts.getOrDefault("completed", 0) > 0) {
            buttonText.append("<br><font color='#90EE90' size='2'>").append(statusCounts.get("completed")).append(" D</font>");
        }
        if (statusCounts.getOrDefault("pending", 0) > 0) {
            buttonText.append("<br><font color='#ADD8E6' size='2'>").append(statusCounts.get("pending")).append(" P</font>");
        }
        buttonText.append("</html>");
        button.setText(buttonText.toString());
    
        button.addActionListener(e -> {
            AppointmentView appointmentView = new AppointmentView(this, appointmentManager, customerManager, date);
            appointmentView.setVisible(true);
        });
    
        return button;
    }
    

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        
        JButton[] buttons = {viewAppointmentsButton, viewTodoListButton, viewRemindersButton, viewCustomerButton};
        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.BOLD, 14)); 
            button.setBackground(Color.LIGHT_GRAY); 
            button.setForeground(Color.WHITE); 
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
            CustomerManager customerManager = new CustomerManager(); // 先初始化CustomerManager
            ReminderManager reminderManager = new ReminderManager(customerManager); // 使用CustomerManager初始化ReminderManager
    
            MainPage mainPage = new MainPage(appointmentManager, toDoList, reminderManager, customerManager);
            mainPage.setVisible(true);
        });
    }
    
    
    }
    

