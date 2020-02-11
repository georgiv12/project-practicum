import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TeachersFrame extends JFrame{
    Connection conn = null;
    public static Object AddAction;
    ResultSet result = null;
    MyModel model = null;

    JTable table = new JTable();
    JScrollPane scroller = new JScrollPane(table);





    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JButton addButton = new JButton("Add");
    JButton delButton = new JButton("Delete");
    JButton editButton = new JButton("Edit");
    JButton cancelBtn = new JButton("Cancel");
    JButton saveChangesButton = new JButton("Save");

    JLabel nameLabel = new JLabel("Name:");
    JLabel townLabel = new JLabel("Town:");
    JLabel titleLabel = new JLabel("Title:");
    JLabel universityLabel = new JLabel("University:");
    JTextField nameTField = new JTextField();
    JTextField townTField = new JTextField();
    JTextField titleTField = new JTextField();
    JTextField universityTField = new JTextField();

    private PreparedStatement state;

    JPanel panel2 = new JPanel();


    public TeachersFrame() {

        panel2.add(upPanel);
        panel2.add(midPanel);
        panel2.add(downPanel);
        //upPanel
        upPanel.setLayout(new GridLayout(4,2));
        upPanel.add(nameLabel);
        upPanel.add(nameTField);
        upPanel.add(townLabel);
        upPanel.add(townTField);
        upPanel.add(titleLabel);
        upPanel.add(titleTField);
        upPanel.add(universityLabel);
        upPanel.add(universityTField);
        //midPanel
        midPanel.add(addButton);
        midPanel.add(delButton);
        midPanel.add(editButton);

        addButton.addActionListener(new AddAction());
        delButton.addActionListener(new DelAction());
        editButton.addActionListener(new EditAction());
        cancelBtn.addActionListener(new CancelAction());
        saveChangesButton.addActionListener(new SaveAction());
        //downPanel
        scroller.setPreferredSize(new Dimension(300,100));
        downPanel.add(scroller);



        table.setModel(getAllFromTable());
        table.removeColumn(table.getColumnModel().getColumn(0));
    }//end constructor

    public JPanel getPanel2() {
        return panel2;
    }
//end constructor

    public MyModel getAllFromTable(){
        conn = DBConnector.getConnection();
        String sql_query = " select * from teachers";

        try {
            state = conn.prepareStatement(sql_query);
            result = state.executeQuery();
            model = new MyModel(result);


        } catch (SQLException e) {
            e.printStackTrace();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            return model;
        }

    }

    public class AddAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameTField.getText();
            String town = townTField.getText();
            String title  = titleTField.getText();
            String university = universityTField.getText();
            conn = DBConnector.getConnection();
            //0,1,2,3 like arrays
            String query = "insert into teachers values(null, ?,?,?,?);";
            try {
                state = conn.prepareStatement(query);
                state.setString(1, name);
                state.setString(2, town);
                state.setString(3, title);
                state.setString(4, university);
                state.execute();
                table.setModel(getAllFromTable());
                table.removeColumn(table.getColumnModel().getColumn(0));
                nameTField.setText("");
                townTField.setText("");
                titleTField.setText("");
                universityTField.setText("");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public class DelAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            //this three lines of code selects the first field of row or simply the ID of every student
            int column = 0;
            int row = table.getSelectedRow();
            String currID = table.getModel().getValueAt(row, column).toString();
            //end of selection

            String query = "delete from teachers where id = ?;";
            try {
                state = conn.prepareStatement(query);
                state.setString(1, currID);
                state.execute();
                table.setModel(getAllFromTable());
                table.removeColumn(table.getColumnModel().getColumn(0));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }
    public class EditAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            nameTField.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
            townTField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
            titleTField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
            universityTField.setText(table.getValueAt(table.getSelectedRow(),3).toString());


            editButton.setVisible(false);
            midPanel.add(cancelBtn);
            midPanel.add(saveChangesButton);
            cancelBtn.setVisible(true);
            saveChangesButton.setVisible(true);
            midPanel.validate();
        }
    }
    public class CancelAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            nameTField.setText("");
            townTField.setText("");
            titleTField.setText("");
            universityTField.setText("");

            cancelBtn.setVisible(false);
            saveChangesButton.setVisible(false);
            editButton.setVisible(true);

            midPanel.validate();
        }
    }
    public class SaveAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            int column = 0;
            int row = table.getSelectedRow();
            String curID = table.getModel().getValueAt(row,column).toString();

            String editedName = nameTField.getText();
            String editTown = townTField.getText();
            String editTitle = titleTField.getText();
            String editUniversity = universityTField.getText();

            conn = DBConnector.getConnection();
            //0,1,2,3 like arrays
            String update_query = "UPDATE TEACHERS SET NAME = ?, TOWN = ?, TITLE = ?, UNIVERSITY = ? WHERE ID = ?";
            try {
                state = conn.prepareStatement(update_query);
                state.setString(1, editedName);
                state.setString(2, editTown);
                state.setString(3, editTitle);
                state.setString(4, editUniversity);
                state.setString(5, curID);
                state.execute();
                table.setModel(getAllFromTable());
                table.removeColumn(table.getColumnModel().getColumn(0));
                nameTField.setText("");
                townTField.setText("");
                titleTField.setText("");
                universityTField.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }
}//end class MyFrame
