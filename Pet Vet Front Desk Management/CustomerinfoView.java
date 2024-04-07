import java.awt.BorderLayout;

import javax.swing.*;

public class CustomerinfoView extends JDialog {
    public CustomerinfoView(JFrame parent) {
        super(parent, "Customer Information", true);
        setSize(500, 300);
        setLayout(new BorderLayout());

        JPanel personInfoPanel = new JPanel(); // Add components for person info
        JPanel petInfoPanel = new JPanel();    // Add components for pet info

        add(personInfoPanel, BorderLayout.NORTH);
        add(petInfoPanel, BorderLayout.CENTER);

        // Configure components and add action listeners as needed
    }
}
