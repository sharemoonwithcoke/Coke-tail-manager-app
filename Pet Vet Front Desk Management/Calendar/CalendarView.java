import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Scanner; // For demonstration of adding methods interactively

public class CalendarView {
    private YearMonth currentYearMonth;
    private DateTimeFormatter dateFormatter;
    private ToDoList todoList;
    private AppointmentManager appointmentManager;
    private ReminderManager reminderManager;

    public CalendarView(YearMonth currentYearMonth, ToDoList todoList, 
                        AppointmentManager appointmentManager, ReminderManager reminderManager) {
        this.currentYearMonth = currentYearMonth;
        this.dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        this.todoList = todoList;
        this.appointmentManager = appointmentManager;
        this.reminderManager = reminderManager;
        updateView();
    }

    // 切换月份
    public void changeMonth(YearMonth yearMonth) {
        this.currentYearMonth = yearMonth;
        updateView();
    }

    // 添加待办事项
    public void interactivelyAddTodoItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter todo item description:");
        String description = scanner.nextLine();
        todoList.addTask(description);
        updateView();
    }

    // 移除待办事项
    public void interactivelyRemoveTodoItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter todo item index to remove:");
        int index = scanner.nextInt() - 1; // User sees 1-based index
        todoList.removeTask(index);
        updateView();
    }

    // 标记待办事项为完成
    public void interactivelyCompleteTodoItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter todo item index to complete:");
        int index = scanner.nextInt() - 1; // User sees 1-based index
        todoList.completeTask(index);
        updateView();
    }

    // 更新视图
    public void updateView() {
        System.out.println("Calendar for " + currentYearMonth.format(dateFormatter));
    
        // 显示预约信息
        System.out.println("Appointments:");
        List<Appointment> appointments = appointmentManager.getAppointmentsForMonth(currentYearMonth);
        for (Appointment appointment : appointments) {
            System.out.println("- " + appointment); // 假设Appointment类重写了toString方法
        }
    
        // 显示待办事项
        System.out.println("To-Do List:");
        todoList.displayTasks();
    
        // 显示提醒信息
        System.out.println("Reminders:");
        List<Reminder> reminders = reminderManager.getRemindersForMonth(currentYearMonth);
        for (Reminder reminder : reminders) {
            System.out.println("- " + reminder); // 假设Reminder类重写了toString方法
        }
    }
}
    

