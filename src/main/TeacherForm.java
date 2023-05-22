package main;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

public class TeacherForm extends JDialog implements ActionListener, PropertyChangeListener{
	private JOptionPane optionPane;
	private String okString = "Enter";
	private String cancelString = "Cancel";
	private String[] teacher = new String[3];
	
	private JPanel panel;
	private JLabel nameLbl, idLbl, subjectLbl;
	private JTextField name, id, subject;
	
	public String[] getResult() {
		return teacher;
	}
	
	//parentFrame should be panel
	public TeacherForm(Frame parentFrame) {
		super(parentFrame, true);
		setTitle("Add Teacher");
		//setPreferredSize(new Dimension());
		Object[] options = {okString, cancelString};
		
		panel = new JPanel(new GridLayout(3, 2));
		
		nameLbl = new JLabel("Teacher:", SwingConstants.RIGHT);
		idLbl = new JLabel("ID:", SwingConstants.RIGHT);
		subjectLbl = new JLabel("Subject:", SwingConstants.RIGHT);
		name = new JTextField();
		id = new JTextField();
		subject = new JTextField();
		
		panel.add(nameLbl);
		panel.add(name);
		panel.add(idLbl);
		panel.add(id);
		panel.add(subjectLbl);
		panel.add(subject);	
		
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
		subject.addActionListener(this);
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
		            	if(name.getText().equals("") && id.getText().equals("") && subject.getText().equals("")) {
		            		teacher[0] = "None";
		            		teacher[1] = "None";
		            		teacher[2] = "None";
		            		clearAndHide();
		            	} else if (!(name.getText().equals("") || id.getText().equals("") || subject.getText().equals(""))) {
		            		teacher[0] = name.getText();
		            		teacher[1] = id.getText();
		            		teacher[2] = subject.getText();
		            		clearAndHide();
		            	} else {
		            		JOptionPane.showMessageDialog(TeacherForm.this, "Please fill in all fields.", "Incomplete form" ,JOptionPane.ERROR_MESSAGE);
		            		name.requestFocusInWindow();
		            	}
		            } else {
		            	teacher = new String[3];
		            	clearAndHide();
		            }
		}
	}
	
	public void clearAndHide() {
		name.setText(null);
		id.setText(null);
		subject.setText(null);
		setVisible(false);
	}
}
