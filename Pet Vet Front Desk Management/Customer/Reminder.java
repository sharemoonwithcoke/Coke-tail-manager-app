package Customer;

import java.time.LocalDate;

public class Reminder {
    private LocalDate dueDate;
    private String note;

    public Reminder(LocalDate dueDate, String note) {
        this.dueDate = dueDate;
        this.note = note;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getNote() {
        return note;
    }
}
