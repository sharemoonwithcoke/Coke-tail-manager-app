package Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pet {
    private String name;
    private LocalDate birthday;
    private List<CaseRecord> caseRecords = new ArrayList<>();
    private List<Reminder> reminders = new ArrayList<>();

  

    public void addCaseRecord(CaseRecord record) {
        this.caseRecords.add(record);
    }

    public void addReminder(Reminder reminder) {
        this.reminders.add(reminder);
    }
}

class CaseRecord {
    private LocalDate date;
    private String description;



    
}

class Reminder {
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
