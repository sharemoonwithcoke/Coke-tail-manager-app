package toDo;

import java.util.ArrayList;
import java.util.List;

public class ToDoList {
    private static class Task {
        String description;
        boolean completed;

        public Task(String description) {
            this.description = description.trim();
            this.completed = false;
        }

        public void complete() {
            this.completed = true;
        }
        
        @Override
        public String toString() {
            return description + (completed ? " (completed)" : "");
        }
    }

    private List<Task> tasks;

    public ToDoList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(String description) {
        if (description != null && !description.trim().isEmpty()) {
            tasks.add(new Task(description.trim()));
        } else {
            System.out.println("Task description cannot be empty.");
        }
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void completeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).complete();
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void displayTasks() {
        tasks.stream()
             .map(task -> (tasks.indexOf(task) + 1) + ". " + task.toString())
             .forEach(System.out::println);
    }
}

