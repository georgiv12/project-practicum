import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UniversityFrame extends JFrame{
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
    JLabel postalCodeLabel = new JLabel("Postal code:");

    JTextField nameTField = new JTextField();
    JTextField townTField = new JTextField();
    JTextField postalCodeTField = new JTextField();


    private PreparedStatement state;

    JPanel panel3 = new JPanel();


    public UniversityFrame() {

        panel3.add(upPanel);
        panel3.add(midPanel);
        panel3.add(downPanel);
        //upPanel
        upPanel.setLayout(new GridLayout(4,2));
        upPanel.add(nameLabel);
        upPanel.add(nameTField);
        upPanel.add(townLabel);
        upPanel.add(townTField);
        upPanel.add(postalCodeLabel);
        upPanel.add(postalCodeTField);

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
        scroller.setPreferredSize(new Dimension(450,200));
        downPanel.add(scroller);


        table.setGridColor(Color.gray);

        table.setModel(getAllFromTable());
        table.removeColumn(table.getColumnModel().getColumn(0));

    }//end constructor

    public JPanel getPanel3() {
        return panel3;
    }
//end constructor

    public MyModel getAllFromTable(){
        conn = DBConnector.getConnection();
        String sql_query = " select * from university";

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
            String postalCode  = postalCodeTField.getText();

            conn = DBConnector.getConnection();
            //0,1,2,3 like arrays
            String query = "insert into university values(null,?,?,?);";
            try {
                state = conn.prepareStatement(query);
                state.setString(1, name);
                state.setString(2, town);
                state.setString(3, postalCode);
                state.execute();
                table.setModel(getAllFromTable());
                table.removeColumn(table.getColumnModel().getColumn(0));

                nameTField.setText("");
                townTField.setText("");
                postalCodeTField.setText("");



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

            String query = "delete from university where id = ?;";
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
            postalCodeTField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());



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
            postalCodeTField.setText("");


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
            String editTeacherName = townTField.getText();
            String editStCount = postalCodeTField.getText();


            conn = DBConnector.getConnection();
            //0,1,2,3 like arrays
            String update_query = "UPDATE UNIVERSITY SET NAME = ?, TOWN = ?, POSTAL_CODE = ? WHERE ID = ?";
            try {
                state = conn.prepareStatement(update_query);
                state.setString(1, editedName);
                state.setString(2, editTeacherName);
                state.setString(3, editStCount);
                state.setString(4, curID);
                state.execute();
                table.setModel(getAllFromTable());
                table.removeColumn(table.getColumnModel().getColumn(0));
                nameTField.setText("");
                townTField.setText("");
                postalCodeTField.setText("");

                cancelBtn.setVisible(false);
                saveChangesButton.setVisible(false);
                editButton.setVisible(true);

                midPanel.validate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }
}//end class MyFrame
