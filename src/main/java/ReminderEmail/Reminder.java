package ReminderEmail;

import java.time.LocalDate;

public class Reminder {
    private LocalDate dueDate;
    private String note;

    // Constructor
    public Reminder(LocalDate dueDate, String note) {
        this.dueDate = dueDate;
        this.note = note;
    }
    //getter
    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getNote() {
        return note;
    }
}
