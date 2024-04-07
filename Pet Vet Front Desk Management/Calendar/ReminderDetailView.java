import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class ReminderDetailView extends JDialog {
    public ReminderDetailView(JFrame parent, Reminder reminder) {
        super(parent, "Reminder Detail", true);
        setSize(300, 300);
        setLayout(new GridLayout(0, 1)); // 简化布局为网格布局

        add(new JLabel("Note: " + reminder.getNote()));
        add(new JLabel("Due Date: " + reminder.getDueDate().toString()));

        // 假设宠物信息和宠物主人信息在reminder中可获取
        String petName = "Pet's name"; // 从reminder获取
        String ownerName = "Owner's name"; // 从reminder获取
        String ownerEmail = "email@example.com"; // 从reminder获取

        add(new JLabel("Pet: " + petName));
        JLabel ownerLabel = new JLabel("Owner: " + ownerName);
        ownerLabel.setForeground(Color.BLUE);
        ownerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ownerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().mail(new URI("mailto:" + ownerEmail));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(ownerLabel);
    }
}
