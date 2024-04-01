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
