package Appointment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppointmentManager {
    private List<Appointment> appointmentList;

    public AppointmentManager() {
        this.appointmentList = new ArrayList<>();
    }

    // add a new appointment
    public void addAppointment(Appointment appointment) {
        appointmentList.add(appointment);
    }

    // cancel this appointment
    public boolean cancelAppointment(long appointmentId) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentId() == appointmentId && appointment.getStatus() == Appointment.Status.PENDING) {
                appointment.setStatus(Appointment.Status.CANCELLED);
                return true;
            }
        }
        return false;
    }

    // complete this appointment
    public boolean completeAppointment(long appointmentId) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentId() == appointmentId && appointment.getStatus() == Appointment.Status.PENDING) {
                appointment.setStatus(Appointment.Status.COMPLETED);
                return true;
            }
        }
        return false;
    }

    // get all appointments
    public List<Appointment> getAppointmentsByStatus(Appointment.Status status) {
        return appointmentList.stream()
                .filter(appointment -> appointment.getStatus() == status)
                .collect(Collectors.toList());
    }

    // 在AppointmentManager类中添加此方法
public Map<String, Integer> getStatusCountsForDay(LocalDate date) {
    Map<String, Integer> statusCounts = new HashMap<>();
    List<Appointment> appointmentsForDay = getAppointmentsForDay(date);

    for (Appointment appointment : appointmentsForDay) {
        String statusKey = appointment.getStatus().toString().toLowerCase();
        statusCounts.put(statusKey, statusCounts.getOrDefault(statusKey, 0) + 1);
    }

    return statusCounts;
}


    //get all appointments by date
    public List<Appointment> getAppointmentsForDate(LocalDate date) {
        return appointmentList.stream()
                .filter(appointment -> appointment.getDate().equals(date))
                .collect(Collectors.toList());
    }

    //get appointment count
    public int getAppointmentCount() {
        return appointmentList.size();
    }

  

    public List<Appointment> getAppointmentsForDay(LocalDate date) {
        return appointmentList.stream()
                .filter(appointment -> appointment.getDate().equals(date))
                .collect(Collectors.toList());
    }

    // get all appointments
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointmentList);
    }

    //get appointment by date
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentList.stream()
                .filter(appointment -> appointment.getDate().equals(date))
                .collect(Collectors.toList());
    }
  


}
