import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MyFrame extends JFrame{
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
	JLabel ageLabel = new JLabel("Age:");
	JLabel gradeLabel = new JLabel("Avarage Grade");
	JLabel genderLabel = new JLabel("Gender:");
	JTextField nameTField = new JTextField();
	JTextField ageTField = new JTextField();
	JTextField gradeTField = new JTextField();
	String[] genders = {"Female","Male"};
	JComboBox<String> genderCombo = new JComboBox<>(genders);
	private PreparedStatement state;

	public MyFrame() {
		this.setVisible(true);
		this.setSize(400, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(3,1));
		this.add(upPanel);
		this.add(midPanel);
		this.add(downPanel);
		
		//upPanel
		upPanel.setLayout(new GridLayout(4,2));
		upPanel.add(nameLabel);
		upPanel.add(nameTField);
		upPanel.add(ageLabel);
		upPanel.add(ageTField);
		upPanel.add(gradeLabel);
		upPanel.add(gradeTField);
		upPanel.add(genderLabel);
		upPanel.add(genderCombo);
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

	public MyModel getAllFromTable(){
		conn = DBConnector.getConnection();
		String sql_query = " select * from students";

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
			int age = Integer.parseInt(ageTField.getText());
			float avgGrade = Float.parseFloat(gradeTField.getText());
			String gender;
			if(genderCombo.getSelectedIndex() == 0){
				gender = "f";
			} else {
				gender = "m";
			}
			conn = DBConnector.getConnection();
															//0,1,2,3 like arrays
			String query = "insert into students values(null, ?,?,?,?);";
			try {
				state = conn.prepareStatement(query);
				state.setString(1, name);
				state.setInt(2, age);
				state.setFloat(3, avgGrade);
				state.setString(4, gender);
				state.execute();
				table.setModel(getAllFromTable());
				nameTField.setText("");
				ageTField.setText("");
				gradeTField.setText("");
				genderCombo.setSelectedItem("Female");

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
			ageTField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
			gradeTField.setText(table.getValueAt(table.getSelectedRow(), 3).toString());

			if (table.getValueAt(table.getSelectedRow(), 4).toString().equals("m")) {
				genderCombo.setSelectedItem("Male");
			} else {
				genderCombo.setSelectedItem("Female");
			}

			String gender1;
			if(genderCombo.getSelectedIndex() == 0){
				gender1 = "f";
			} else {
				gender1 = "m";
			}

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
			ageTField.setText("");
			gradeTField.setText("");
			genderCombo.setSelectedItem("Female");

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
			int editedAge = Integer.parseInt(ageTField.getText());
			float editedAvgGrade = Float.parseFloat(gradeTField.getText());
			String editedGender;
			if(genderCombo.getSelectedIndex() == 0){
				editedGender = "f";
			} else {
				editedGender = "m";
			}
			conn = DBConnector.getConnection();
			//0,1,2,3 like arrays
			String update_query = "UPDATE STUDENTS SET NAME = ?, AGE = ?, AVRGRADE = ?, GENDER = ? WHERE ID = ?";
			try {
				state = conn.prepareStatement(update_query);
				state.setString(1, editedName);
				state.setInt(2, editedAge);
				state.setFloat(3, editedAvgGrade);
				state.setString(4, editedGender);
				state.setString(5, table.getValueAt(table.getSelectedRow(), 0).toString());
				state.execute();
				table.setModel(getAllFromTable());
				nameTField.setText("");
				ageTField.setText("");
				gradeTField.setText("");
				genderCombo.setSelectedItem("Female");

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}//end class MyFrame
