import javax.swing.*;

public class Tab extends JFrame {

    JTabbedPane tab = new JTabbedPane();

    JPanel panel1 = new StudentsFrame().getPanel1();
    JPanel panel2 = new TeachersFrame().getPanel2();

    public Tab() {
        this.setSize(500, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        tab.add("paneltest", new JPanel());
        tab.add("panel1", panel1);
        tab.add("panel2",panel2);
        this.add(tab);
    }
}
