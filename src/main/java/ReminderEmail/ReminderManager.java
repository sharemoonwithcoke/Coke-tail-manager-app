package ReminderEmail;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import Customer.CustomerManager;
import Customer.Pet;

public class ReminderManager {
    private CustomerManager customerManager;

    public ReminderManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    // Get reminders for the next week
    public List<Reminder> getUpcomingReminders() {
        LocalDate nextWeek = LocalDate.now().plusWeeks(1);
        List<Pet> pets = customerManager.getAllPets(); // Get all pets using CustomerManager
        return pets.stream()
                .flatMap(pet -> pet.getReminders().stream())
                .filter(reminder -> reminder.getDueDate().isBefore(nextWeek))
                .collect(Collectors.toList());
    }

    // Add birthday reminders for all pets
    public void addBirthdayReminders() {
        List<Pet> pets = customerManager.getAllPets(); // Get all pets using CustomerManager
        pets.forEach(pet -> {
            String birthdayNote = "Celebrate" + pet.getName() + "Happy birthday!";
            Reminder birthdayReminder = new Reminder(pet.getBirthday(), birthdayNote);
            pet.addReminder(birthdayReminder);
        });
    }

    // Add a general reminder for a pet
    public void addGeneralReminder(Pet pet, LocalDate dueDate, String note, int daysBefore) {
        LocalDate reminderDate = dueDate.minusDays(daysBefore);
        Reminder newReminder = new Reminder(reminderDate, note);
        pet.addReminder(newReminder);
    }

    // Get all reminders
    public List<Reminder> getAllReminders() {
        List<Pet> pets = customerManager.getAllPets(); // Get all pets using CustomerManager
        return pets.stream()
                .flatMap(pet -> pet.getReminders().stream())
                .collect(Collectors.toList());
    }

    // Add a birthday reminder for a pet
    public void addBirthdayReminder(Pet pet) {
        String birthdayNote = "Celebrate" + pet.getName() + "Happy birthday!";
        Reminder birthdayReminder = new Reminder(pet.getBirthday(), birthdayNote);
        pet.addReminder(birthdayReminder);
    }

    // Remove a reminder
    public void removeReminder(Reminder reminder) {
        List<Pet> allPets = customerManager.getAllPets(); // Get all pets using CustomerManager
        for (Pet pet : allPets) {
            if (pet.getReminders().contains(reminder)) {
                pet.removeReminder(reminder); // Remove the reminder from the pet
                break; // Exit the loop after removing the reminder
            }
        }
    }
}
