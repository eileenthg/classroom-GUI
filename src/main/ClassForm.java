package main;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.File;

import javax.swing.*;

import code.Fx;

public class ClassForm extends JDialog implements ActionListener, PropertyChangeListener{
	private JOptionPane optionPane;
	private String okString = "Enter";
	private String cancelString = "Cancel";
	private String[] classroom = new String[2];
	private String[][] classList;
	
	private JPanel panel;
	private JLabel nameLbl, locLbl;
	private JTextField name, loc;
	
	public String[] getResult() {
		return classroom;
	}
	
	//parentFrame should be panel
	public ClassForm(Frame parentFrame) {
		super(parentFrame, true);
		setTitle("Add Classroom");
		//setPreferredSize(new Dimension());
		Object[] options = {okString, cancelString};
		
		panel = new JPanel(new GridLayout(3, 2));
		
		nameLbl = new JLabel("Classroom:", SwingConstants.RIGHT);
		locLbl = new JLabel("Location:", SwingConstants.RIGHT);
		name = new JTextField();
		loc = new JTextField();
		
		panel.add(nameLbl);
		panel.add(name);
		panel.add(locLbl);
		panel.add(loc);
		
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
		loc.addActionListener(this);
		optionPane.addPropertyChangeListener(this);
		
		//For name check
		String[] classRaw = Fx.fileToArray(new File("Classroom.txt"));
		classList = new String[classRaw.length][2];
		for(int i = 0; i < classRaw.length; i++) {
			classList[i] = classRaw[i].split(", ");
		}
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
		            	if(!name.getText().equals("")) {
		            		boolean notDuplicate = true;
		            		for(String[] classname : classList) {
		            			if(classname[0].equals(name.getText())) {
		            				notDuplicate = false;
		            				break;
		            			}
		            		}
		            		if(notDuplicate) {
		            			if (!loc.getText().equals("")) {
		            				classroom[0] = name.getText();
		            				classroom[1] = loc.getText();
		            				clearAndHide();
		            			} else {
		            				JOptionPane.showMessageDialog(ClassForm.this, "Please fill in all fields.", "Incomplete form" ,JOptionPane.ERROR_MESSAGE);
		            				name.requestFocusInWindow();
		            			}
		            		} else {
		            			JOptionPane.showMessageDialog(ClassForm.this, "Class name already exists.", "Duplicate name" ,JOptionPane.ERROR_MESSAGE);
		            		}
		            	} else {
		            		JOptionPane.showMessageDialog(ClassForm.this, "Please fill in all fields.", "Incomplete form" ,JOptionPane.ERROR_MESSAGE);
		            		name.requestFocusInWindow();
		            	}
		            } else {
		            	classroom = new String[2];
		            	clearAndHide();
		            }
		}
	}
	
	public void clearAndHide() {
		name.setText(null);
		loc.setText(null);
		setVisible(false);
	}
}
