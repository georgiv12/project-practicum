import javax.swing.*;

public class Tab extends JFrame {

    JTabbedPane tab = new JTabbedPane();

    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JPanel panel3 = new JPanel();
    JPanel panel4 = new JPanel();
    JPanel panel5 = new JPanel();

    public Tab(){
        this.setSize(500,500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        tab.add("panel1",panel1);
        tab.add("panel2",panel2);
        tab.add("panel3",panel3);
        tab.add("panel4",panel4);
        tab.add("panel5",panel5);

        this.add(tab);
    }

}
