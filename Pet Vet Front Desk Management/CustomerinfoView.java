import javax.swing.*;

public class CustomerInfoView extends JDialog {
    public CustomerInfoView(JFrame parent) {
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
