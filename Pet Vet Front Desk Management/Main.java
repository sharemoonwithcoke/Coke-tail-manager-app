import java.time.YearMonth;

import Appointment.AppointmentManager;
import ReminderEmail.ReminderManager;
import toDo.ToDoList;

public class Main {
    public static void main(String[] args) {
        // Create a new appointment manager
        AppointmentManager appointmentManager = new AppointmentManager();

        // Create a new reminder manager
        ReminderManager reminderManager = new ReminderManager();

        // Create a new to-do list
        ToDoList todoList = new ToDoList();

        // Create a new calendar view
        CalendarView calendarView = new CalendarView(todoList, appointmentManager, reminderManager);

        // Change the month to January 2022
        calendarView.changeMonth(YearMonth.of(2024, 4));

        // Add a new to-do item
        calendarView.interactivelyAddTodoItem();

        // Remove a to-do item
        calendarView.interactivelyRemoveTodoItem();

        // Complete a to-do item
        calendarView.interactivelyCompleteTodoItem();

        // Add a new appointment
        
    }
    
}
