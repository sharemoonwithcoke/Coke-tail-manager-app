
package ReminderEmail;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import Customer.CustomerManager;
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


    
    
    //更改地方！！！
    public List<Reminder> getAllReminders(List<Pet> pets) {
        return pets.stream()
                .flatMap(pet -> pet.getReminders().stream())
                .collect(Collectors.toList());
    }

    CustomerManager customerManager = new CustomerManager();
    // Assume customers and their pets are added to customerManager here

    ReminderManager reminderManager = new ReminderManager();
    List<Reminder> reminders = reminderManager.getAllReminders(customerManager.getAllPets());
    public List<Reminder> getAllReminders() {
        List<Pet> allPets = customerManager.getAllPets(); // 假设这个方法返回所有客户的所有宠物
        return allPets.stream()
                      .flatMap(pet -> pet.getReminders().stream())
                      .collect(Collectors.toList());
    }
    

    public void removeReminder(Reminder reminder) {
        List<Pet> allPets = customerManager.getAllPets(); // 获取所有宠物
        for (Pet pet : allPets) {
            if (pet.getReminders().contains(reminder)) {
                pet.removeReminder(reminder); // 移除找到的提醒
                break; // 假设每个提醒只属于一个宠物，找到后即可停止搜索
            }
        }
    }

  
    

}
