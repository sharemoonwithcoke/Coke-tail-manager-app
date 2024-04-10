package Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ReminderEmail.Reminder;
import Customer.CaseRecord;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pet {
    public enum Gender {
        MALE, FEMALE, UNKNOWN
    }

    
    public enum PetType {
        CAT, DOG, OTHER
    }

    private String name;
    private LocalDate birthday;
    private Gender gender; 
    private PetType type; 
    private String breed; 
    private List<CaseRecord> caseRecords = new ArrayList<>();
    private List<Reminder> reminders = new ArrayList<>();


    // 构造函数
    public Pet(String name, LocalDate birthday, Gender gender, PetType type, String breed) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.type = type;
        this.breed = breed;
    }

    
    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public LocalDate getBirthday() {
        return birthday;
    }



    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }



    public Gender getGender() {
        return gender;
    }



    public void setGender(Gender gender) {
        this.gender = gender;
    }



    public PetType getType() {
        return type;
    }



    public void setType(PetType type) {
        this.type = type;
    }



    public String getBreed() {
        return breed;
    }



    public void setBreed(String breed) {
        this.breed = breed;
    }



    public List<CaseRecord> getCaseRecords() {
        return caseRecords;
    }



    public void setCaseRecords(List<CaseRecord> caseRecords) {
        this.caseRecords = caseRecords;
    }



    public List<Reminder> getReminders() {
        return reminders;
    }



    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }



    public void addCaseRecord(CaseRecord record) {
        this.caseRecords.add(record);
    }
    public boolean removeCaseRecord(CaseRecord record) {
        return this.caseRecords.remove(record);
    }

    // 获取所有病例记录
    public List<CaseRecord> getAllCaseRecords() {
        return new ArrayList<>(this.caseRecords);
    }

    public void addReminder(Reminder reminder) {
        this.reminders.add(reminder);
    }
    public boolean removeReminder(Reminder reminder) {
        return this.reminders.remove(reminder);
    }

    // 获取所有提醒
    public List<Reminder> getAllReminders() {
        return new ArrayList<>(this.reminders);
    }
    @Override
public String toString() {
    return name + " [" + type.toString() + "]";
}

}
    

