import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class StudentsFrame extends JFrame{
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
	JLabel ageLabel = new JLabel("Age:");

	JLabel gradeLabel = new JLabel("Avarage Grade:");
	JLabel genderLabel = new JLabel("Gender:");
	JLabel uniLabel = new JLabel("University:");

	JTextField nameTField = new JTextField();
	JTextField townTField = new JTextField();

	JTextField ageTField = new JTextField();
	JTextField gradeTField = new JTextField();
	String[] genders = {"Female","Male"};
	JComboBox<String> genderCombo = new JComboBox<>(genders);
	JTextField uniTField = new JTextField();

	private PreparedStatement state;

	JPanel panel1 = new JPanel();


	public StudentsFrame() {

		panel1.add(upPanel);
		panel1.add(midPanel);
		panel1.add(downPanel);
		//upPanel
		upPanel.setLayout(new GridLayout(6,2));
		upPanel.add(nameLabel);
		upPanel.add(nameTField);

		upPanel.add(townLabel);
		upPanel.add(townTField);

		upPanel.add(ageLabel);
		upPanel.add(ageTField);

		upPanel.add(gradeLabel);
		upPanel.add(gradeTField);

		upPanel.add(genderLabel);
		upPanel.add(genderCombo);

		upPanel.add(uniLabel);
		upPanel.add(uniTField);
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

		table.setModel(getAllFromTable("students"));
		table.removeColumn(table.getColumnModel().getColumn(0));
	}//end constructor

	public JPanel getPanel1() {
		return panel1;
	}

	public MyModel getAllFromTable(String type){
		conn = DBConnector.getConnection();
		String sql_query = " select * from " + type;

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
			int age = Integer.parseInt(ageTField.getText());
			float avgGrade = Float.parseFloat(gradeTField.getText());
			String gender;
			if(genderCombo.getSelectedIndex() == 0){
				gender = "f";
			} else {
				gender = "m";
			}
			String uni = uniTField.getText();
			conn = DBConnector.getConnection();
			//0,1,2,3 like arrays
			String query = "insert into students values(null, ?,?,?,?,?,?);";
			try {
				state = conn.prepareStatement(query);
				state.setString(1, name);
				state.setString(2,town);
				state.setInt(3, age);
				state.setFloat(4, avgGrade);
				state.setString(5, gender);
				state.setString(6,uni);
				state.execute();
				table.setModel(getAllFromTable("students"));
				table.removeColumn(table.getColumnModel().getColumn(0));

				nameTField.setText("");
				townTField.setText("");
				ageTField.setText("");
				gradeTField.setText("");
				genderCombo.setSelectedItem("Female");
				uniTField.setText("");
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

			String query = "delete from students where id = ?;";
			try {
				state = conn.prepareStatement(query);
				state.setString(1, currID);
				state.execute();
				table.setModel(getAllFromTable("students"));
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

			ageTField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
			gradeTField.setText(table.getValueAt(table.getSelectedRow(), 3).toString());

			if (table.getValueAt(table.getSelectedRow(), 4).toString().equals("m")) {
				genderCombo.setSelectedItem("Male");
			} else {
				genderCombo.setSelectedItem("Female");
			}

			String gender1;
			System.out.println(genderCombo.getSelectedIndex());
			if(genderCombo.getSelectedIndex() == 0){
				gender1 = "f";
			} else {
				gender1 = "m";
			}
			uniTField.setText(table.getValueAt(table.getSelectedRow(), 5).toString());

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
			ageTField.setText("");
			gradeTField.setText("");
			genderCombo.setSelectedItem("Female");
			uniTField.setText("");
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
			String currID = table.getModel().getValueAt(row, column).toString();
			String editedName = nameTField.getText();
			String townName = townTField.getText();
			int editedAge = Integer.parseInt(ageTField.getText());
			float editedAvgGrade = Float.parseFloat(gradeTField.getText());
			String editedGender;
			if(genderCombo.getSelectedIndex() == 0){
				editedGender = "f";
			} else {
				editedGender = "m";
			}
			String uniName = uniTField.getText();
			conn = DBConnector.getConnection();
			//0,1,2,3 like arrays
			String update_query = "UPDATE STUDENTS SET NAME = ?, TOWN = ?, AGE = ?, AVRGRADE = ?, GENDER = ?, UNIVERSITY = ? WHERE ID = ?";
			try {
				state = conn.prepareStatement(update_query);
				state.setString(1, editedName);
				state.setString(2, townName);
				state.setInt(3, editedAge);
				state.setFloat(4, editedAvgGrade);
				state.setString(5, editedGender);
				state.setString(6,uniName);
				state.setString(7, currID);
				state.execute();

				table.setModel(getAllFromTable("students"));
				table.removeColumn(table.getColumnModel().getColumn(0));

				nameTField.setText("");
				townTField.setText("");
				ageTField.setText("");
				gradeTField.setText("");
				genderCombo.setSelectedItem("Female");
				uniTField.setText("");
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