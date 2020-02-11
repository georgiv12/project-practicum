import javafx.scene.text.Text;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class SearchFrame extends JFrame {
    Connection conn = null;
    public static Object AddAction;
    ResultSet result = null;
    ResultSet result1 = null;
    ResultSet result2 = null;

    MyModel model = null;
    MyModel model1 = null;
    MyModel model2 = null;

    JTable table = new JTable();
    JTable table1 = new JTable();
    JTable table2 = new JTable();
    JScrollPane scroller = new JScrollPane(table);
    JScrollPane scroller1 = new JScrollPane(table1);
    JScrollPane scroller2 = new JScrollPane(table2);


    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JButton searchBtn = new JButton("Search");

    JLabel townLabel = new JLabel("Town:");
    JLabel uniLabel = new JLabel("University:");

    JTextField townTField = new JTextField();
    JTextField uniTField = new JTextField();

    private PreparedStatement state;
    private PreparedStatement state1;
    private PreparedStatement state2;

    JPanel panel1 = new JPanel();


    public SearchFrame() {

        panel1.add(upPanel);
        panel1.add(midPanel);
        panel1.add(downPanel);
        //upPanel
        upPanel.setLayout(new GridLayout(2, 2));

        upPanel.add(townLabel);
        upPanel.add(townTField);

        upPanel.add(uniLabel);
        upPanel.add(uniTField);

        midPanel.add(searchBtn);
        //midPanel
        scroller.setPreferredSize(new Dimension(450, 100));
        scroller1.setPreferredSize(new Dimension(450, 100));
        scroller2.setPreferredSize(new Dimension(450, 100));
        scroller.setVisible(false);
        scroller1.setVisible(false);
        scroller2.setVisible(false);

        downPanel.setLayout(new GridLayout(3, 1));
        downPanel.add(scroller);
        downPanel.add(scroller1);
        downPanel.add(scroller2);
        searchBtn.addActionListener(new SearchAction());


        table.setModel(getAllFromTable("students"));
        table1.setModel(getAllFromTable("teachers"));
        table2.setModel(getAllFromTable("university"));
        table.removeColumn(table.getColumnModel().getColumn(0));
        table1.removeColumn(table1.getColumnModel().getColumn(0));
        table2.removeColumn(table2.getColumnModel().getColumn(0));

    }//end constructor

    public JPanel getPanel4() {
        return panel1;
    }

    public MyModel getAllFromTable(String type) {
        conn = DBConnector.getConnection();
        String sql_query = " select * from " + type;

        try {
            state = conn.prepareStatement(sql_query);
            result = state.executeQuery();
            model = new MyModel(result);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return model;
        }

    }

    public class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String town = townTField.getText();
            String uni = uniTField.getText();
            conn = DBConnector.getConnection();



            if (!town.isEmpty() && !uni.isEmpty()) {
                String query = "SELECT * from students WHERE (town = ?) AND (university = ?);";
                String query1 = "SELECT * from teachers WHERE (town = ?) AND (university = ?);";
                String query2 = "SELECT * from university where (town = ?) AND (name = ?)";
                tryCatchBlockForSearch(query, query1, query2, town, uni);
            }
            else if(!town.isEmpty() && uni.isEmpty()){
                String query = "SELECT * from students WHERE town = ?";
                String query1 = "SELECT * from teachers WHERE town = ?";
                String query2 = "SELECT * from university where town = ?";
                tryCatchBlockForSearch(query, query1, query2, town, uni);
            }
            else if(!uni.isEmpty() && town.isEmpty()){
                String query = "SELECT * from students WHERE university = ?";
                String query1 = "SELECT * from teachers WHERE university = ?";
                String query2 = "SELECT * from university where name = ?";
                tryCatchBlockForSearch(query, query1, query2, town, uni);
            }
            else{
                JOptionPane.showMessageDialog(SearchFrame.this,
                        "You must fill at least one of the fields!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        public void checkForFieldsEmptiness(String town, String uni, PreparedStatement state) throws SQLException {
            if (!town.isEmpty() && !uni.isEmpty()) {
                state.setString(1,town);
                state.setString(2,uni);
            }
            else if(!town.isEmpty() && uni.isEmpty()){
                state.setString(1, town);
            }
            else if(!uni.isEmpty() && town.isEmpty()){
                state.setString(1, uni);
            }
        }
        public void tryCatchBlockForSearch(String query, String query1, String query2, String town, String uni) {
            scroller.setVisible(true);
            scroller1.setVisible(true);
            scroller2.setVisible(true);
            try {
                state = conn.prepareStatement(query);
                state1 = conn.prepareStatement(query1);
                state2 = conn.prepareStatement(query2);


                checkForFieldsEmptiness(town,uni,state);
                result = state.executeQuery();
                model = new MyModel(result);

                checkForFieldsEmptiness(town,uni,state1);

                result1 = state1.executeQuery();
                model1 = new MyModel(result1);

                checkForFieldsEmptiness(town,uni,state2);
                result2 = state2.executeQuery();
                model2 = new MyModel(result2);

                if(model.getRowCount() == 0 && model1.getRowCount() == 0 && model.getRowCount() == 0){
                    scroller.setVisible(false);
                    scroller1.setVisible(false);
                    scroller2.setVisible(false);
                    JOptionPane.showMessageDialog(SearchFrame.this,
                            "Sorry, but there aren't any results from search!",
                            "Error", JOptionPane.ERROR_MESSAGE);

                }

                table.setModel(model);
                table1.setModel(model1);
                table2.setModel(model2);

                townTField.setText("");
                uniTField.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}//end class MyFrame