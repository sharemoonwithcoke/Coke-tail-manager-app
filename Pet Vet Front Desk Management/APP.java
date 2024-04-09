import javax.swing.SwingUtilities;
import Appointment.AppointmentManager;

public class APP {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppointmentManager appointmentManager = new AppointmentManager();
            MainPage mainPage = new MainPage(appointmentManager);
            mainPage.setVisible(true);
        });
    }
}
