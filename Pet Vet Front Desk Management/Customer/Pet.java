package Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Pet {
    private String name;
    private LocalDate birthday;
    private List<caseRecord> caseRecords = new ArrayList<>();
    private List<Reminder> reminders = new ArrayList<>();

  

    public void addCaseRecord(caseRecord record) {
        this.caseRecords.add(record);
    }

    public void addReminder(Reminder reminder) {
        this.reminders.add(reminder);
    }
}
