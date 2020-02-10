import javax.swing.*;

public class Tab extends JFrame {

    JTabbedPane tab = new JTabbedPane();

    JPanel panel1 = new StudentsFrame().getPanel1();

    JPanel panel2 = new TeachersFrame().getPanel2();

    JPanel panel3 = new UniversityFrame().getPanel3();

    JPanel panel4 = new SearchFrame().getPanel4();

    public Tab() {
        this.setSize(500, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        tab.add("students", panel1);

        tab.add("teachers",panel2);

        tab.add("universities",panel3);

        tab.add("search engine", panel4);

        this.add(tab);
    }
}
