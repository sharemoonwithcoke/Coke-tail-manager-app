package Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

import Customer.Person;
import Customer.Pet;

public class Appointment {
    private static long appointmentCounter = 0;
    private long appointmentId;
    private LocalDate date;
    private Person person;
    private Pet pet;
    private LocalTime time; 
    private String reason;
    private Status status;
    private Doctor doctor;

    

    public enum Status { PENDING, CANCELLED, COMPLETED }
    public enum Doctor { Clair, Michell, Jay, Alex, Cam }

    public Appointment(LocalDate date, Person person, Pet pet, LocalTime time, String reason, Doctor doctor) {
        this.appointmentId = ++appointmentCounter;
        this.date = date;
        this.person = person;
        this.pet = pet;
        this.time = time;
        this.reason = reason;
        this.status = Status.PENDING;
        this.doctor = doctor;
    }

    public static long getAppointmentCounter() {
        return appointmentCounter;
    }



    public static void setAppointmentCounter(long appointmentCounter) {
        Appointment.appointmentCounter = appointmentCounter;
    }



    public long getAppointmentId() {
        return appointmentId;
    }



    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }



    public LocalDate getDate() {
        return date;
    }



    public void setDate(LocalDate date) {
        this.date = date;
    }



    public Person getPerson() {
        return person;
    }



    public void setPerson(Person person) {
        this.person = person;
    }



    public Pet getPet() {
        return pet;
    }



    public void setPet(Pet pet) {
        this.pet = pet;
    }



    public LocalTime getTime() {
        return time;
    }



    public void setTime(LocalTime time) {
        this.time = time;
    }



    public String getReason() {
        return reason;
    }



    public void setReason(String reason) {
        this.reason = reason;
    }



    public Status getStatus() {
        return status;
    }



    public void setStatus(Status status) {
        this.status = status;
    }



    public Doctor getDoctor() {
        return doctor;
    }



    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }





    @Override
public String toString() {
    // 基本信息
    String info = String.format("%s: %s with %s - %s, %s (%s)", date, time, person != null ? person.getLastName() : "Unknown",
                                pet != null ? pet.getName() : "Unknown", reason, doctor);

    // 根据状态添加标签
    switch (status) {
        case CANCELLED:
            info += " (cancelled)";
            break;
        case COMPLETED:
            info += " (completed)";
            break;
        case PENDING:
        default:
            // 不需要额外添加文字
            break;
    }
    return info;
}

}

