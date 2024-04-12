package Customer;

import java.time.LocalDate;

public class CaseRecord {
    private LocalDate date;
    private String description;

    // Constructor
    public CaseRecord(LocalDate date, String description) {
        this.date = date;
        this.description = description;
    }

    // getter and setter
    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String toString() {
        return date.toString() + ": " + description;
    }

    public String getFullDescription() {
        return date.toString() + ": " + description;
    }
}