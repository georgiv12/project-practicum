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

    JButton addButton = new JButton("Добави");
    JButton delButton = new JButton("Изтрий");
    JButton editButton = new JButton("Промени");
    JButton cancelBtn = new JButton("Откажи");
    JButton saveChangesButton = new JButton("Запази");

    JLabel nameLabel = new JLabel("Name:");
    JLabel secondName = new JLabel("Second Name:");
    JLabel titleLabel = new JLabel("Title:");
    JLabel subjectLabel = new JLabel("Subject:");
    JTextField nameTField = new JTextField();
    JTextField secondNameTField = new JTextField();
    JTextField titleTField = new JTextField();
    JTextField subjectTField = new JTextField();

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
        upPanel.add(secondName);
        upPanel.add(secondNameTField);
        upPanel.add(titleLabel);
        upPanel.add(titleTField);
        upPanel.add(subjectLabel);
        upPanel.add(subjectTField);
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
            String secondName = secondNameTField.getText();
            String title  = titleTField.getText();
            String subject = subjectTField.getText();
            conn = DBConnector.getConnection();
            //0,1,2,3 like arrays
            String query = "insert into teachers values(null, ?,?,?,?);";
            try {
                state = conn.prepareStatement(query);
                state.setString(1, name);
                state.setString(2, secondName);
                state.setString(3, title);
                state.setString(4, subject);
                state.execute();
                table.setModel(getAllFromTable());
                nameTField.setText("");
                secondNameTField.setText("");
                titleTField.setText("");
                subjectTField.setText("");

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

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }
    public class EditAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            nameTField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
            secondNameTField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
            titleTField.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
            subjectTField.setText(table.getValueAt(table.getSelectedRow(),4).toString());


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
            secondNameTField.setText("");
            titleTField.setText("");
            titleTField.setText("");

            cancelBtn.setVisible(false);
            saveChangesButton.setVisible(false);
            editButton.setVisible(true);

            midPanel.validate();
        }
    }
    public class SaveAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            String editedName = nameTField.getText();
            String editSecondName = secondNameTField.getText();
            String editTitle = titleTField.getText();
            String editSubject = subjectTField.getText();

            conn = DBConnector.getConnection();
            //0,1,2,3 like arrays
            String update_query = "UPDATE TEACHERS SET NAME = ?, SECONDNAME = ?, TITLE = ?, SUBJECT = ? WHERE ID = ?";
            try {
                state = conn.prepareStatement(update_query);
                state.setString(1, editedName);
                state.setString(2, editSecondName);
                state.setString(3, editTitle);
                state.setString(4, editSubject);
                state.setString(5, table.getValueAt(table.getSelectedRow(), 0).toString());
                state.execute();
                table.setModel(getAllFromTable());
                nameTField.setText("");
                secondNameTField.setText("");
                titleTField.setText("");
                subjectTField.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }
}//end class MyFrame
