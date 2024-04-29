// This Program is JDBC Connection Using SWING Components
import javax.swing.*;
import java.sql.*;
import java.awt.Color;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.*; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class jdbcCRUDSwingOperate extends JFrame implements ActionListener {
	
	// CONNECTION VARIABLES
	static Connection connection;
	static String jdbcCDBase = "jdbc:mysql://localhost:3306/";
	static String jdbcURL = "jdbc:mysql://localhost:3306/addressbook";
	static String username = "root";
	static String password = "";
	
	// FORM VARIABLES
	static JTextField txtFirst, txtName, txtAdd, txtSearch;
	static DefaultTableModel model;
	static JTable table;
	static String idDetector;
	JButton btnSave, btnUpdate, btnDelete;
	
    public jdbcCRUDSwingOperate() {
		
		idDetector = ""; // SET TO NULL NOT TO ENCOUNTER EXCEPTIONS
		
		//Frame
        setTitle("Members Records");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(590, 560);  //(width,height)
		setLocation(300, 100);  //(x,y)
		//setLocationRelativeTo(null);
        setVisible(true);
		setResizable(false);
		getContentPane().setBackground(Color.BLUE);

		// Table
        model = new DefaultTableModel();
        table = new JTable(model);
		model.addColumn("ID");
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("Address");
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // pamamahala sa column 
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);
		scrollPane.setBounds(10,10,550,140); // (x,y,width,height)
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		//panel
		JPanel panel = new JPanel();
		panel.setBounds(10,160,550,350); // (x,y,width,height)
		panel.setBackground(Color.gray);
		
		//Labels
		JLabel lblFirst = new JLabel("First Name:"); 
		lblFirst.setBounds(40,180,400,30); // (x,y,width,height)
		lblFirst.setFont(new Font("Arial", Font.PLAIN, 20));
		lblFirst.setForeground(Color.white);
		JLabel lblName = new JLabel("Surname:"); 
		lblName.setBounds(58,230,400,30); // (x,y,width,height)
		lblName.setFont(new Font("Arial", Font.PLAIN, 20));
		lblName.setForeground(Color.white);
		JLabel lblAdd = new JLabel("Address:"); 
		lblAdd.setBounds(63,280,400,30); // (x,y,width,height)
		lblAdd.setFont(new Font("Arial", Font.PLAIN, 20));
		lblAdd.setForeground(Color.white);
		
		JLabel lblSearch = new JLabel("Search:"); 
		lblSearch.setBounds(63,420,400,30); // (x,y,width,height)
		lblSearch.setFont(new Font("Arial", Font.PLAIN, 20));
		lblSearch.setForeground(Color.white);
		
		//textbox
		txtFirst = new JTextField(12); 
		txtFirst.setBounds(150,180,300,30); // (x,y,width,height)
		txtFirst.setFont(new Font("Arial", Font.PLAIN, 20));
		txtName = new JTextField(12); 
		txtName.setBounds(150,230,300,30); // (x,y,width,height)
		txtName.setFont(new Font("Arial", Font.PLAIN, 20));
		txtAdd = new JTextField(12);
		txtAdd.setBounds(150,280,350,30); // (x,y,width,height)
		txtAdd.setFont(new Font("Arial", Font.PLAIN, 20));
		txtSearch = new JTextField(12);
		txtSearch.setBounds(150,420,350,30); // (x,y,width,height)
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 20));
		
		//buttons
		btnSave = new JButton("Save Record"); 
		btnSave.setBounds(40,350,150,30); // (x,y,width,height)
		btnSave.setFont(new Font("Arial", Font.PLAIN, 20));
		btnSave.setBackground(Color.green);
		btnSave.setForeground(Color.black);
		btnSave.addActionListener(this); 
		
		btnUpdate = new JButton("Update"); 
		btnUpdate.setBounds(210,350,150,30); // (x,y,width,height)
		btnUpdate.setFont(new Font("Arial", Font.PLAIN, 20));
		btnUpdate.setBackground(Color.blue);
		btnUpdate.setForeground(Color.white);
		btnUpdate.addActionListener(this); 
		
		btnDelete = new JButton("Delete"); 
		btnDelete.setBounds(380,350,150,30); // (x,y,width,height)
		btnDelete.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDelete.setBackground(Color.red);
		btnDelete.setForeground(Color.white);
		btnDelete.addActionListener(this); 
		
		//Add Labels
		add(lblFirst); 
		add(lblName); 
		add(lblAdd);
		add(lblSearch);
		
		//Add TextFields
		add(txtFirst); 
		add(txtName); 		
		add(txtAdd); 
		add(txtSearch);
		
		//Add Buttons
		add(btnSave);
		add(btnUpdate);
		add(btnDelete);
		
		//Add Panel
		add(panel);
		
		// THIS, for fix the position of ELEMENTS
		setLayout(null);
		
		// Call the View all Records Methood
		loadRecords(model);
    }
	
	public void actionPerformed(ActionEvent ev) {
		
		// ADD NEW RECORD
		if (ev.getSource() == btnSave) {
			if (txtFirst.getText().equals("") || txtName.getText().equals("") || txtAdd.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Please Fill-Up All TextFields.");
				return;
			}

			try{ 
				connection = conn();
				PreparedStatement pstm=connection.prepareStatement("INSERT INTO tbl_address_book (First_Name, Surname, Address) VALUES (?,?,?)"); 
				pstm.setString(1,txtFirst.getText()); 
				pstm.setString(2,txtName.getText()); 
				pstm.setString(3,txtAdd.getText()); 
				//pstm.executeUpdate(); 
				int rowsInserted = pstm.executeUpdate();
				if (rowsInserted > 0) {
					JOptionPane.showMessageDialog(this, "Record Added Successfully.");
					txtFirst.setText("");
					txtName.setText("");
					txtAdd.setText("");
				}
				
				pstm.close();
				connection.close();
				loadRecords(model);
			} 
			catch (SQLException sqe) { 
				System.out.println("SQL Error"); 
				JOptionPane.showMessageDialog(null,sqe.getMessage());
			}
		}	
		
		// UPDATE RECORD
		if (ev.getSource() == btnUpdate) {
			if (idDetector.equals("")) {
				JOptionPane.showMessageDialog(this, "Please Select 1 Record.");
				return;
			}
			try{ 
				connection = conn();
				PreparedStatement pstm=connection.prepareStatement("UPDATE tbl_address_book SET First_Name =?, SurName =?, Address =? WHERE ID =?"); 
				pstm.setString(4, idDetector); 
				pstm.setString(1, txtFirst.getText()); 
				pstm.setString(2, txtName.getText()); 
				pstm.setString(3, txtAdd.getText()); 
				pstm.executeUpdate();
				JOptionPane.showMessageDialog(this, "Record Update Successfully.");
				txtFirst.setText("");
				txtName.setText("");
				txtAdd.setText("");
				
				pstm.close();
				connection.close();
				loadRecords(model);
				idDetector = "";
			} 
			catch (SQLException sqe) { 
				System.out.println("SQL Error"); 
				JOptionPane.showMessageDialog(null,sqe.getMessage());
			}
		}
		
		// DELETE RECORD
		if (ev.getSource() == btnDelete) {
			if (idDetector.equals("")) {
				JOptionPane.showMessageDialog(this, "Please Select 1 Record.");
				return;
			}
			try{ 
				connection = conn();
				PreparedStatement pstm=connection.prepareStatement("DELETE FROM tbl_address_book WHERE ID =?"); 
				pstm.setString(1, idDetector); 
				pstm.executeUpdate();
				JOptionPane.showMessageDialog(this, "Record Deleted Successfully.");
				txtFirst.setText("");
				txtName.setText("");
				txtAdd.setText("");
					
				pstm.close();
				connection.close();
				loadRecords(model);
				idDetector = "";
			} 
			catch (SQLException sqe) { 
				System.out.println("SQL Error"); 
				JOptionPane.showMessageDialog(null,sqe.getMessage());
			}
		}		
	}
	
	// CLEAR ALL ROWS IN TABLE
	public static void clearTable(DefaultTableModel dm) {
		dm.setRowCount(0);
	}
	
	// VIEW ALL RECORDS
    private void loadRecords(DefaultTableModel model) {
		clearTable(model);
		
        try {
			connection = conn();
            String sql = "SELECT * FROM tbl_address_book order by ID desc";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
			
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
				String firstName = resultSet.getString("First_Name");
                String SureName = resultSet.getString("Surname");
                String Address = resultSet.getString("Address");

                model.addRow(new Object[]{id, firstName, SureName, Address});
            }

            resultSet.close();
            statement.close();
            connection.close();
        }
		catch (SQLException e) {
            e.printStackTrace();
			JOptionPane.showMessageDialog(null,e.getMessage());
			System.exit(0);
        }
    }
	
	// SEARCH A RECORD
    private static void searchRecord(DefaultTableModel model) {
		clearTable(model);
        try {
			connection = conn();
            String sql = "SELECT * FROM tbl_address_book WHERE ID LIKE ? OR First_Name LIKE ? OR SurName LIKE ? OR Address LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, "%" + txtSearch.getText() + "%"); 
			statement.setString(2, "%" + txtSearch.getText() + "%");
			statement.setString(3, "%" + txtSearch.getText() + "%");
			statement.setString(4, "%" + txtSearch.getText() + "%"); 
            ResultSet resultSet = statement.executeQuery();
			
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
				String firstName = resultSet.getString("First_Name");
                String SureName = resultSet.getString("Surname");
                String Address = resultSet.getString("Address");

                model.addRow(new Object[]{id, firstName, SureName, Address});
            }

            resultSet.close();
            statement.close();
            connection.close();
        }
		catch (SQLException e) {
            e.printStackTrace();
			JOptionPane.showMessageDialog(null,e.getMessage());
			System.exit(0);
        }
    }
	
	// CONNECTION CHECK:
	public static Connection conn() {
		try {
			connection = DriverManager.getConnection(jdbcURL, username, password);
		}		
		catch (Exception cn) { 
			
			// CREATE DATABASE:
			try {
				connection = DriverManager.getConnection(jdbcCDBase, username, password);
				PreparedStatement pstm = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS `addressbook`"); 
				pstm.executeUpdate();
				pstm=connection.prepareStatement("USE `addressbook`"); 
				pstm.executeUpdate();
				pstm=connection.prepareStatement("CREATE TABLE IF NOT EXISTS `tbl_address_book`" +
															"(`ID` int NOT NULL AUTO_INCREMENT," +
															"`First_Name` varchar(100) NOT NULL," +
															"`Surname` varchar(100) NOT NULL," +
															"`Address` varchar(100) NOT NULL," +
															"PRIMARY KEY (`ID`))"); 
				pstm.executeUpdate();
				pstm.close();
				connection = DriverManager.getConnection(jdbcURL, username, password);
				JOptionPane.showMessageDialog(null,"New DATABASE Created.");
			}
			catch (Exception cnf) { 
				System.out.println("Connection Failed"); 
				JOptionPane.showMessageDialog(null, cnf.getMessage());
				System.exit(0);
			}
		} 
		return connection;
	}
	
	// MAIN FUNCTION OF THE SYSTEM
    public static void main(String[] args) {
		
        SwingUtilities.invokeLater(() -> {
			
			// CALL ALL THE FUNCTION OF THE SYSTEM
            new jdbcCRUDSwingOperate();
			
			// OPERATE WHEN CLICKING A ROW IN TABLE IT WILL COPY TO TEXTFIELD
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					int selectedRow = table.getSelectedRow();
					if (selectedRow >= 0) {
						// Kunin ang mga detalye ng row
						idDetector = table.getValueAt(selectedRow, 0).toString();
						String column1Value = table.getValueAt(selectedRow, 1).toString();
						String column2Value = table.getValueAt(selectedRow, 2).toString();
						String column3Value = table.getValueAt(selectedRow, 3).toString();

						// I-set ang mga detalye sa mga JTextField
						txtFirst.setText(column1Value);
						txtName.setText(column2Value);
						txtAdd.setText(column3Value);
					}
				}
			});
			
			// OPERATE FOR SEARCH WHEN TYPING A WORD WILL GENERATE A PROGRAM
			txtSearch.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    searchRecord(model);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    searchRecord(model);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    // Hindi ito kailangan para sa plain text fields.
                }
            });
        });	
    }
}
