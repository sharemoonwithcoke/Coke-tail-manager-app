
package ReminderEmail;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Customer.Pet;


public class ReminderManager {
    
    // 获取接下来一周内的提醒
    public List<Reminder> getUpcomingReminders(List<Pet> pets) {
        LocalDate nextWeek = LocalDate.now().plusWeeks(1);
        return pets.stream()
                .flatMap(pet -> pet.getReminders().stream())
                .filter(reminder -> reminder.getDueDate().isBefore(nextWeek))
                .collect(Collectors.toList());
    }

    // 为宠物的生日创建提醒
    public void addBirthdayReminders(List<Pet> pets) {
        pets.forEach(pet -> {
            String birthdayNote = "请祝" + pet.getName() + "生日快乐！";
            Reminder birthdayReminder = new Reminder(pet.getBirthday(), birthdayNote);
            pet.addReminder(birthdayReminder);
        });
    }

    // 创建和添加一个通用提醒
    public void addGeneralReminder(Pet pet, LocalDate dueDate, String note, int daysBefore) {
        LocalDate reminderDate = dueDate.minusDays(daysBefore);
        Reminder newReminder = new Reminder(reminderDate, note);
        pet.addReminder(newReminder);
    }

}

