package Calendar;

import javax.swing.*;
import toDo.ToDoList;
import java.awt.*;

public class ToDoListDialog extends JDialog {
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private ToDoList toDoList;

    // Constructor modified to accept a ToDoList instance
    public ToDoListDialog(JFrame parent, ToDoList toDoList2) {
        super(parent, "To-Do List", true);
        this.toDoList = toDoList2;
        initializeUI();
    }
    // Initialize the UI components
    private void initializeUI() {
        setSize(300, 400);
        setLayout(new BorderLayout());
        // Create the list model and list
        listModel = new DefaultListModel<>();
        updateTaskListModel();
        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);
        // Create the button panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton completeButton = new JButton("Complete");
        JButton removeButton = new JButton("Remove");
        // Add action listeners to the buttons
        addButton.addActionListener(e -> addTask());
        completeButton.addActionListener(e -> completeTask());
        removeButton.addActionListener(e -> removeTask());
        // Add buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTaskListModel() {
        listModel.clear();
        toDoList.getTasks().forEach(task -> listModel.addElement(task.toString()));
    }
    // Add a new task to the to-do list
    private void addTask() {
        String description = JOptionPane.showInputDialog(this, "Enter task description:");
        if (description != null && !description.trim().isEmpty()) {// Check if a description is provided
            toDoList.addTask(description);
            updateTaskListModel();
        }
    }
    // Mark the selected task as complete
    private void completeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {// Check if a task is selected
            toDoList.completeTask(selectedIndex);
            updateTaskListModel();
        }
    }
    // Remove the selected task from the to-do list
    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            toDoList.removeTask(selectedIndex);
            updateTaskListModel();
        }
    }

}
