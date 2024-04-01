package Appointment;

import Customer.Person;
import java.time.LocalDate;
import java.util.List;




class Appoinment {
    private LocalDate date;
    private Person namPerson;
    private String time;
    private String reason;
    private enum Status { PENDING, CANCELLED, COMPLETED }
    private enum Doctor;
    private enum Pet;

    public Appoinment(LocalDate date, Person namPerson, String time, String reason, Status status, Doctor doctor, Pet pet) {
        this.date = date;
        this.namPerson = namPerson;
        this.time = time;
        this.reason = reason;
        this.status = status;
        this.doctor = doctor;
        this.pet = pet;
    }

    public LocalDate getDate() {
        return date;
    }

    public Person getNamPerson() {
        return namPerson;
    }

    public String getTime() {
        return time;
    }

    public String getReason() {
        return reason;
    }

    public Status getStatus() {
        return status;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Pet getPet() {
        return pet;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void displayAppoinment() {
        System.out.println("Date: " + date);
        System.out.println("Name: " + namPerson.getName());
        System.out.println("Time: " + time);
        System.out.println("Reason: " + reason);
        System.out.println("Status: " + status);
        System.out.println("Doctor: " + doctor);
        System.out.println("Pet: " + pet);
    }

}
