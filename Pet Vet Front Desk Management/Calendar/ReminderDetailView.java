import javax.swing.*;

import Customer.Person;
import Customer.Pet;
import ReminderEmail.Reminder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.mail.MessagingException;

public class ReminderDetailView extends JDialog {
    private JTextField subjectField;
    private JTextArea messageArea;
    private String recipientEmail;

    public ReminderDetailView(JFrame parent, Reminder reminder, Pet pet) {
        super(parent, "Reminder Detail", true);
        setLayout(new BorderLayout(5, 5));
        
        // 获取宠物主人信息
        Person owner = pet.getOwner();
        recipientEmail = owner.getEmail(); // 宠物主人的电子邮件地址

        // 构建提醒信息面板
        JPanel reminderInfoPanel = new JPanel();
        reminderInfoPanel.setLayout(new BoxLayout(reminderInfoPanel, BoxLayout.Y_AXIS));
        reminderInfoPanel.add(new JLabel("Note: " + reminder.getNote()));
        reminderInfoPanel.add(new JLabel("Due Date: " + reminder.getDueDate().toString()));
        reminderInfoPanel.add(new JLabel("Pet: " + pet.getName()));
        reminderInfoPanel.add(new JLabel("Owner: " + owner.getFirstName() + " " + owner.getLastName()));
        reminderInfoPanel.add(new JLabel("Contact: " + recipientEmail));
        
        // 构建邮件发送面板
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        subjectField = new JTextField(20);
        messageArea = new JTextArea(5, 20);
        JButton sendButton = new JButton("Send Email");

        emailPanel.add(new JLabel("Subject:"));
        emailPanel.add(subjectField);
        emailPanel.add(new JLabel("Message:"));
        emailPanel.add(new JScrollPane(messageArea)); // 使文本区域可滚动
        emailPanel.add(sendButton);

        // 添加面板到对话框
        add(reminderInfoPanel, BorderLayout.NORTH);
        add(emailPanel, BorderLayout.CENTER);
        
        // 调整对话框大小
        pack();
        
        // 发送按钮的事件处理
        sendButton.addActionListener(e -> {
            try {
                sendEmail();
            } catch (MessagingException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to send email.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void sendEmail() throws MessagingException {
        String subject = subjectField.getText();
        String message = messageArea.getText();

        // 假设你有一个可以发送邮件的服务
        // EmailService.sendEmail(recipientEmail, subject, message);
        JOptionPane.showMessageDialog(this, "Email sent successfully to " + recipientEmail);
        // 在实际应用中，你应该取消下面这行的注释，并确保你的EmailService正确配置
        // EmailService.sendEmail(recipientEmail, subject, message);
    }
}
