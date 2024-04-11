
package ReminderEmail;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import Customer.CustomerManager;
import Customer.Pet;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReminderManager {
    private CustomerManager customerManager;

    public ReminderManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    // 获取接下来一周内的提醒
    public List<Reminder> getUpcomingReminders() {
        LocalDate nextWeek = LocalDate.now().plusWeeks(1);
        List<Pet> pets = customerManager.getAllPets(); // 使用CustomerManager获取所有宠物
        return pets.stream()
                .flatMap(pet -> pet.getReminders().stream())
                .filter(reminder -> reminder.getDueDate().isBefore(nextWeek))
                .collect(Collectors.toList());
    }

    // 为宠物的生日创建提醒
    public void addBirthdayReminders() {
        List<Pet> pets = customerManager.getAllPets(); // 同上
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

    // 获取所有提醒，不再需要pets参数
    public List<Reminder> getAllReminders() {
        List<Pet> pets = customerManager.getAllPets(); // 使用CustomerManager获取所有宠物
        return pets.stream()
                .flatMap(pet -> pet.getReminders().stream())
                .collect(Collectors.toList());
    }

    // 修改为为单个宠物添加生日提醒的方法
public void addBirthdayReminder(Pet pet) {
    String birthdayNote = "请祝" + pet.getName() + "生日快乐！";
    Reminder birthdayReminder = new Reminder(pet.getBirthday(), birthdayNote);
    pet.addReminder(birthdayReminder);
}

// 在addReminder方法中调用


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
