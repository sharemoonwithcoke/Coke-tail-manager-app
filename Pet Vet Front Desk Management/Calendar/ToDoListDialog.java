import java.util.*;

import javax.swing.*;

import toDo.ToDoList;

import java.awt.*;
import java.awt.event.*;

public class ToDoListDialog extends JDialog {
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private ToDoList toDoList;

    public ToDoListDialog(JFrame parent, ToDoList toDoList) {
        super(parent, "To-Do List", true);
        this.toDoList = toDoList;
        initializeUI();
    }

    private void initializeUI() {
        setSize(300, 400);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        updateTaskListModel();
        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton completeButton = new JButton("Complete");
        JButton removeButton = new JButton("Remove");

        addButton.addActionListener(e -> addTask());
        completeButton.addActionListener(e -> completeTask());
        removeButton.addActionListener(e -> removeTask());

        buttonPanel.add(addButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTaskListModel() {
        listModel.clear();  
        toDoList.getTasks().forEach(task -> listModel.addElement(task.toString()));
    }

    private void addTask() {
        String description = JOptionPane.showInputDialog(this, "Enter task description:");
        if (description != null && !description.trim().isEmpty()) {
            toDoList.addTask(description);
            updateTaskListModel();
        }
    }

    private void completeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            toDoList.completeTask(selectedIndex);
            updateTaskListModel();
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            toDoList.removeTask(selectedIndex);
            updateTaskListModel();
        }
    }
}
