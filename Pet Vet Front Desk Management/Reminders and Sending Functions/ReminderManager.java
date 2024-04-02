import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class ReminderManager {

    public List<Reminder> getUpcomingReminders(List<Pet> pets) {
        LocalDate nextWeek = LocalDate.now().plusWeeks(1);
        return pets.stream()
                .flatMap(pet -> pet.getReminders().stream())
                .filter(reminder -> reminder.getDueDate().isBefore(nextWeek))
                .collect(Collectors.toList());
    }

}
