package main;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

public class StudentForm extends JDialog implements ActionListener, PropertyChangeListener{
	private JOptionPane optionPane;
	private String okString = "Enter";
	private String cancelString = "Cancel";
	private String[] student = new String[2];
	
	private JPanel panel;
	private JLabel nameLbl, idLbl;
	private JTextField name, id;
	
	public String[] getResult() {
		return student;
	}
	
	//parentFrame should be panel
	public StudentForm(Frame parentFrame) {
		super(parentFrame, true);
		setTitle("Add Student");
		//setPreferredSize(new Dimension());
		Object[] options = {okString, cancelString};
		
		panel = new JPanel(new GridLayout(3, 2));
		
		nameLbl = new JLabel("Student:", SwingConstants.RIGHT);
		idLbl = new JLabel("ID:", SwingConstants.RIGHT);
		name = new JTextField();
		id = new JTextField();
		
		panel.add(nameLbl);
		panel.add(name);
		panel.add(idLbl);
		panel.add(id);	
		
		optionPane = new JOptionPane(panel, //fields to display
				JOptionPane.PLAIN_MESSAGE, //message type
				JOptionPane.YES_NO_OPTION, //number of options (2)
				null, //icons
				options); //option list (custom buttons)
		
		//Make this dialog display it.
		setContentPane(optionPane);
		
		//Don't close
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				optionPane.setValue(JOptionPane.CLOSED_OPTION);
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				name.requestFocusInWindow();
			}
		});
		
		name.addActionListener(this);
		id.addActionListener(this);
		optionPane.addPropertyChangeListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(okString);
	}
		
	
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		
		if (isVisible()
		         && (e.getSource() == optionPane)
		         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
		             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
		            Object value = optionPane.getValue();

		            if (value == JOptionPane.UNINITIALIZED_VALUE) {
		                return;
		            }

		            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
		            
		            if(okString.equals(value)) {
		            	if(name.getText().equals("") && id.getText().equals("")) {
		            		student = new String[2];
		            		clearAndHide();
		            	} else if (!(name.getText().equals("") || id.getText().equals(""))) {
		            		student[0] = name.getText();
		            		student[1] = id.getText();
		            		clearAndHide();
		            	} else {
		            		JOptionPane.showMessageDialog(StudentForm.this, "Please fill in all fields.", "Incomplete form" ,JOptionPane.ERROR_MESSAGE);
		            		name.requestFocusInWindow();
		            	}
		            } else {
		            	student = new String[2];
		            	clearAndHide();
		            }
		}
	}
	
	public void clearAndHide() {
		name.setText(null);
		id.setText(null);
		setVisible(false);
	}
}
