package main;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import code.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GUI extends JFrame{
	private static GUI frame;
	private TeacherForm teachForm = new TeacherForm(frame);
	private StudentForm studentForm = new StudentForm(frame);
	private ClassForm classForm = new ClassForm(frame);
	
	//main menu
	private void menuPane(Container pane) {
		pane.setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Classroom List: ");
		pane.add(title, BorderLayout.NORTH);
					
		String[] rawClassroom = Fx.fileToArray(new File("Classroom.txt"));
		int row;
		if(rawClassroom.length > 30) {
			row = rawClassroom.length;
		} else {
			row = 30;
		}
		String[][] classroom = new String[row][2];
		for(int i = 0; i < rawClassroom.length; i++) {
			classroom[i] = rawClassroom[i].split(", ");
		}
		String[] columnNames = {"Class Name", "Class teacher"};
		JTable listRaw = new JTable(classroom, columnNames);
		listRaw.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listRaw.getTableHeader().setReorderingAllowed(false);
		JScrollPane list = new JScrollPane(listRaw);
		pane.add(list, BorderLayout.CENTER);
		
		JPanel button = new JPanel(new FlowLayout());
		JButton addFromFile = new JButton("Add from file");
		JButton addFromGUI = new JButton("Add new...");
		JButton delete = new JButton("Delete class");
		JButton view = new JButton("View class");
		delete.setEnabled(false);
		view.setEnabled(false);
		button.add(addFromFile);
		button.add(addFromGUI);
		button.add(delete);
		button.add(view);
		pane.add(button, BorderLayout.SOUTH);
		
		addFromFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String)JOptionPane.showInputDialog(
						pane,
						"Enter file name.\n"
						+ ".txt files only.",
						"Add from file...",
						JOptionPane.PLAIN_MESSAGE,
						null,
						null,
						null);
				if((s != null) && (s.length() > 0)) {
					File parse;
					if(!s.matches(".*\\.txt")) {
						s = s + ".txt";
					}
					parse = new File(s);
					try {
						int status = FileToClassroom.read(parse);
						if(status == 0) {
							JOptionPane.showMessageDialog(pane, 
									"Operation successful.");
						} else if(status == 1) {
							JOptionPane.showMessageDialog(pane,
									"Error occured. No file exists.");
						} else if(status == 2) {
							JOptionPane.showMessageDialog(pane,
									"Error occured. Check input format/values.");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					//just refresh everything to get the new classroom array. lazy to improve.
					//could try tableModel.fireTableDataChanged();
					pane.removeAll();
					menuPane(pane);
					pane.doLayout();
					update(getGraphics());
					//adjust the frame size a bit so the button pops up. Buttons will not show otherwise.
					frame.setSize(450, 593);
					frame.setSize(450, 592); 
					
				}
			}
		});
		
		addFromGUI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = (String)JOptionPane.showInputDialog(
						pane,
						"Input maximum number of students in class:",
						"New Classroom Creation",
						JOptionPane.PLAIN_MESSAGE);
				if(s != null) {
					try {
						int cap = Integer.parseInt(s);
						pane.removeAll();
						addNewClass(pane, cap);
						pane.doLayout();
						update(getGraphics());
						//adjust the frame size a bit so the button pops up. Buttons will not show otherwise.
						frame.setSize(450, 593);
						frame.setSize(450, 592);
					} catch(NumberFormatException e) {
						JOptionPane.showMessageDialog(pane, "Entered value not a number. \n Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		listRaw.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(listRaw.getValueAt(listRaw.getSelectedRow(), 0) == null) {
					delete.setEnabled(false);
					view.setEnabled(false);
				} else {
					delete.setEnabled(true);
					view.setEnabled(true);
				}
			}
		});
		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Fx.deleteClassroom((String) listRaw.getValueAt(listRaw.getSelectedRow(), 0));
				} catch (IOException e) {
					e.printStackTrace();
				}
				pane.removeAll();
				menuPane(pane);
				pane.doLayout();
				update(getGraphics());
				//adjust the frame size a bit so the button pops up. Buttons will not show otherwise.
				frame.setSize(450, 593);
				frame.setSize(450, 592);
				
			}	
		});
		
		
		view.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.removeAll();
				String fileName = (String) listRaw.getValueAt(listRaw.getSelectedRow(), 0) + ".txt";
				classroomView(pane, new File(fileName));
				pane.doLayout();
				update(getGraphics());
				frame.setSize(450, 593);
				frame.setSize(450, 592);
			}
		});
	}
	
	private void addNewClass(Container pane, int cap) {
		pane.setLayout(new BorderLayout());
		
		JPanel stats = new JPanel(new GridLayout(2,1));
		
		int classCap = cap;
		String[] classStatsList = {"", ""};
		StudentGrp students = new StudentGrp(new String[cap]);
		String[] classTeachOut = {"None", "None", "None"};
		TeacherGrp regularTeach = new TeacherGrp(new String[0]);
		
		JPanel classStats = new JPanel(new GridLayout(5, 1));
		JLabel classNameLbl = new JLabel("Class name: " + classStatsList[0]);
		JLabel classLocLbl = new JLabel("Location: " + classStatsList[1]);
		JLabel classCapLbl = new JLabel("Capacity: " + String.valueOf(classCap));
		JButton classEdit = new JButton("Edit class details");
		JLabel blank1 = new JLabel(" ");
		classStats.add(classNameLbl);
		classStats.add(classLocLbl);
		classStats.add(classCapLbl);
		classStats.add(classEdit);
		classStats.add(blank1);
		classStats.setLayout(new BoxLayout(classStats, BoxLayout.Y_AXIS));
		
		JPanel classTeachStats = new JPanel(new GridLayout(5, 1));
		JLabel classTeachName = new JLabel("Class Teacher: " + classTeachOut[0]);
		JLabel classTeachID = new JLabel("ID: " + classTeachOut[1]);
		JLabel classTeachSubject = new JLabel("Subject: " + classTeachOut[2]);
		JButton classTeachEdit = new JButton("Edit class teacher");
		JLabel blank2 = new JLabel(" ");
		classTeachStats.add(classTeachName);
		classTeachStats.add(classTeachID);
		classTeachStats.add(classTeachSubject);
		classTeachStats.add(classTeachEdit);
		classTeachStats.add(blank2);
		classTeachStats.setLayout(new BoxLayout(classTeachStats, BoxLayout.Y_AXIS));
		
		stats.add(classStats);
		stats.add(classTeachStats);
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		Dimension preferredInfoSize = new Dimension(400, 100);
		
		//card student
		JPanel student = new JPanel();
		JPanel studentList = new JPanel(new BorderLayout());
		JLabel studentLbl = new JLabel("Student name");
		studentLbl.setHorizontalAlignment(SwingConstants.LEFT);
		JList<String> studentNames = new JList<String>(students.getNames());
		studentNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane studentScroll = new JScrollPane(studentNames);
		studentList.add(studentLbl, BorderLayout.NORTH);
		studentList.add(studentScroll, BorderLayout.CENTER);
		
		JTextArea infoStudent = new JTextArea();
		infoStudent.setEditable(false);
		infoStudent.setPreferredSize(preferredInfoSize);
		
		JPanel buttonStudent = new JPanel(new FlowLayout());
		JButton addNewStudent = new JButton("Add new...");
		JButton deleteStudent = new JButton("Delete");
		deleteStudent.setEnabled(false);
		buttonStudent.add(addNewStudent);
		buttonStudent.add(deleteStudent);
		if(students.getNames().length == 0) {
			addNewStudent.setEnabled(false);
		}
		
		student.add(studentList);
		student.add(infoStudent);
		student.add(buttonStudent);
		student.setLayout(new BoxLayout(student, BoxLayout.Y_AXIS));
		
		//cardTeacher
		JPanel teacher = new JPanel();
		JPanel teacherList = new JPanel(new BorderLayout());
		JLabel teacherLbl = new JLabel("Teacher name");
		teacherLbl.setHorizontalAlignment(SwingConstants.LEFT);
		JList<String> teacherNames = new JList<String>(regularTeach.getNames());
		teacherNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane teacherScroll = new JScrollPane(teacherNames);
		teacherList.add(teacherLbl, BorderLayout.NORTH);
		teacherList.add(teacherScroll, BorderLayout.CENTER);
		
		JTextArea infoTeacher = new JTextArea();
		infoTeacher.setEditable(false);
		infoTeacher.setPreferredSize(preferredInfoSize);
		
		JPanel buttonTeacher = new JPanel(new FlowLayout());
		JButton addNewTeacher= new JButton("Add new...");
		JButton deleteTeacher = new JButton("Delete");
		deleteTeacher.setEnabled(false);
		buttonTeacher.add(addNewTeacher);
		buttonTeacher.add(deleteTeacher);
		
		teacher.add(teacherList);
		teacher.add(infoTeacher);
		teacher.add(buttonTeacher);
		teacher.setLayout(new BoxLayout(teacher, BoxLayout.Y_AXIS));
		
		
		tabbedPane.addTab("Students", student);
		tabbedPane.addTab("Teachers", teacher);
		
		JPanel button = new JPanel(new GridLayout(1,2));
		JButton back = new JButton("Cancel");
		JButton proceed = new JButton("Save");
		button.add(back);
		button.add(proceed);
		
		pane.add(stats, BorderLayout.NORTH);
		pane.add(tabbedPane, BorderLayout.CENTER);
		pane.add(button, BorderLayout.SOUTH);
		
		classEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				classForm.setLocationRelativeTo(frame);
				classForm.setVisible(true);
				String[] newClass = classForm.getResult();
				if(newClass[0] != null) {
					classStatsList[0] = newClass[0];
					classStatsList[1] = newClass[1];
					classNameLbl.setText("Class name: " + classStatsList[0]);
					classLocLbl.setText("Location: " + classStatsList[1]);
				}
			}
		});
		
		classTeachEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				teachForm.setLocationRelativeTo(frame);
				teachForm.setVisible(true);
				String[] newClassTeach = teachForm.getResult();
				if(newClassTeach[0] != null) {
					classTeachOut[0] = newClassTeach[0];
					classTeachOut[1] = newClassTeach[1];
					classTeachOut[2] = newClassTeach[2];
					classTeachName.setText("Class Teacher: " + classTeachOut[0]);
					classTeachID.setText("ID: " + classTeachOut[1]);
					classTeachSubject.setText("Subject: " + classTeachOut[2]);
				}
			}
		});
		
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					//Remove students from studentlist
					String[] deleteStd = students.getIDs();
					File stdList = new File("Students.txt");
					String[] raw = Fx.fileToArray(stdList);
					for(int i = 0; i < deleteStd.length; i++) {
						for(int j = 0; j < raw.length; j++) {
							if(raw[j] != null) {
								if(raw[j].equals(deleteStd[i])) {
									raw[j] = null;
								}
							}
						}
					}
					
					FileWriter fw = new FileWriter("Students.txt");
					PrintWriter pw = new PrintWriter(fw);
					for(String i : raw) {
						if(i != null) pw.println(i);
					}
					pw.close();
					
					pane.removeAll();
					menuPane(pane);
					pane.doLayout();
					update(getGraphics());
					frame.setSize(450, 593);
					frame.setSize(450, 592);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		proceed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!(classStatsList[0].equals("") || classStatsList[1].equals(""))) {	
					Teacher classTeach = new Teacher(classTeachOut[0], classTeachOut[1], classTeachOut[2]);
					Classroom room = new Classroom(classStatsList[0], classStatsList[1], students, classTeach, regularTeach);
					try {
						Fx.classToFile(room);
						FileWriter fw = new FileWriter(new File("Classroom.txt"), true);
						PrintWriter pw = new PrintWriter(fw);
						pw.println(classStatsList[0] + ", " + classTeach.getName());
						pw.close();
						pane.removeAll();
						menuPane(pane);
						pane.doLayout();
						update(getGraphics());
						frame.setSize(450, 593);
						frame.setSize(450, 592);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					JOptionPane.showMessageDialog(pane, "Please enter class name and class location.", "Incomplete form" ,JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//student card actions
		
		addNewStudent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				studentForm.setLocationRelativeTo(frame);
				studentForm.setVisible(true);
				String[] newStudent = studentForm.getResult();
				if(newStudent[0] != null) {
					System.out.println(students.add(newStudent));
					studentNames.setListData(students.getNames());
				}
				if(!(students.getPerson(classCap - 1) == null))
					addNewStudent.setEnabled(false);
			}
		});
		
		studentNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(studentNames.getSelectedValue() == null) {
					deleteStudent.setEnabled(false);
				} else if(studentNames.getSelectedValue().equals(" ")) {
					deleteStudent.setEnabled(false);
					infoStudent.setText(null);
				} else {
					deleteStudent.setEnabled(true);
					String[] details = students.getPersonDetails(studentNames.getSelectedIndex());
					infoStudent.setText("Name: " + details[0] + "\n ID: " + details[1]);
				}
			}
		});
		
		deleteStudent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				students.delete(studentNames.getSelectedIndex());
				studentNames.setListData(students.getNames());
				infoStudent.setText(null);
				if(students.getPerson(classCap - 1) == null)
					addNewStudent.setEnabled(true);
			}
			
		});
		
		//teacher card actions
	
		addNewTeacher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				teachForm.setLocationRelativeTo(frame);
				teachForm.setVisible(true);
				String[] newTeacher = teachForm.getResult();
				if(newTeacher[0] != null) {
					if(!(newTeacher[0].equals("None"))){
						System.out.println(regularTeach.add(newTeacher));
						teacherNames.setListData(regularTeach.getNames());
					}
				}
			}
		});
		
		teacherNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(teacherNames.getSelectedValue() == null) {
					deleteTeacher.setEnabled(false);
				} else if(teacherNames.getSelectedValue().equals(" ")) {
					deleteTeacher.setEnabled(false);
					infoTeacher.setText(null);
				} else {
					deleteTeacher.setEnabled(true);
					String[] details = regularTeach.getPersonDetails(teacherNames.getSelectedIndex());
					infoTeacher.setText("Name: " + details[0] + "\n ID: " + details[1] + "\n Subject: " + details[2]);
				}
			}
		});
		
		deleteTeacher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				regularTeach.delete(teacherNames.getSelectedIndex());
				teacherNames.setListData(regularTeach.getNames());
				infoTeacher.setText(null);
			}
			
		});
	}
	
	private void classroomView(Container pane, File classFile) {
		Classroom classroom = new Classroom(classFile);
		pane.setLayout(new BorderLayout());
		
		JPanel stats = new JPanel(new GridLayout(2,1));
		
		JPanel classStats = new JPanel(new GridLayout(4, 1));
		JLabel className = new JLabel("Class name: " + classroom.getName());
		JLabel classLoc = new JLabel("Location: " + classroom.getLoc());
		JLabel classCap = new JLabel("Capacity: " + String.valueOf(classroom.getCapacity()));
		JLabel blank1 = new JLabel(" ");
		classStats.add(className);
		classStats.add(classLoc);
		classStats.add(classCap);
		classStats.add(blank1);
		classStats.setLayout(new BoxLayout(classStats, BoxLayout.Y_AXIS));
		
		JPanel classTeachStats = new JPanel(new GridLayout(5, 1));
		String[] classTeach = classroom.getClassTeacherDetails();
		JLabel classTeachName = new JLabel("Class Teacher: " + classTeach[0]);
		JLabel classTeachID = new JLabel("ID: " + classTeach[1]);
		JLabel classTeachSubject = new JLabel("Subject: " + classTeach[2]);
		JButton classTeachEdit = new JButton("Edit class teacher");
		JLabel blank2 = new JLabel(" ");
		classTeachStats.add(classTeachName);
		classTeachStats.add(classTeachID);
		classTeachStats.add(classTeachSubject);
		classTeachStats.add(classTeachEdit);
		classTeachStats.add(blank2);
		classTeachStats.setLayout(new BoxLayout(classTeachStats, BoxLayout.Y_AXIS));
		
		stats.add(classStats);
		stats.add(classTeachStats);
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		Dimension preferredInfoSize = new Dimension(400, 100);
		
		//card student
		JPanel student = new JPanel();
		JPanel studentList = new JPanel(new BorderLayout());
		JLabel studentLbl = new JLabel("Student name");
		studentLbl.setHorizontalAlignment(SwingConstants.LEFT);
		JList<String> studentNames = new JList<String>(classroom.getStudentNames());
		studentNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane studentScroll = new JScrollPane(studentNames);
		studentList.add(studentLbl, BorderLayout.NORTH);
		studentList.add(studentScroll, BorderLayout.CENTER);
		
		JTextArea infoStudent = new JTextArea();
		infoStudent.setEditable(false);
		infoStudent.setPreferredSize(preferredInfoSize);
		
		JPanel buttonStudent = new JPanel(new FlowLayout());
		JButton addNewStudent = new JButton("Add new...");
		JButton deleteStudent = new JButton("Delete");
		deleteStudent.setEnabled(false);
		buttonStudent.add(addNewStudent);
		buttonStudent.add(deleteStudent);
		if(classroom.getStudentNames().length == 0) {
			addNewStudent.setEnabled(false);
		}
		
		student.add(studentList);
		student.add(infoStudent);
		student.add(buttonStudent);
		student.setLayout(new BoxLayout(student, BoxLayout.Y_AXIS));
		
		//cardTeacher
		JPanel teacher = new JPanel();
		JPanel teacherList = new JPanel(new BorderLayout());
		JLabel teacherLbl = new JLabel("Teacher name");
		teacherLbl.setHorizontalAlignment(SwingConstants.LEFT);
		JList<String> teacherNames = new JList<String>(classroom.getTeacherNames());
		teacherNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane teacherScroll = new JScrollPane(teacherNames);
		teacherList.add(teacherLbl, BorderLayout.NORTH);
		teacherList.add(teacherScroll, BorderLayout.CENTER);
		
		JTextArea infoTeacher = new JTextArea();
		infoTeacher.setEditable(false);
		infoTeacher.setPreferredSize(preferredInfoSize);
		
		JPanel buttonTeacher = new JPanel(new FlowLayout());
		JButton addNewTeacher= new JButton("Add new...");
		JButton deleteTeacher = new JButton("Delete");
		deleteTeacher.setEnabled(false);
		buttonTeacher.add(addNewTeacher);
		buttonTeacher.add(deleteTeacher);
		
		teacher.add(teacherList);
		teacher.add(infoTeacher);
		teacher.add(buttonTeacher);
		teacher.setLayout(new BoxLayout(teacher, BoxLayout.Y_AXIS));
		
		
		tabbedPane.addTab("Students", student);
		tabbedPane.addTab("Teachers", teacher);
		
		JButton back = new JButton("Save and return");
		
		pane.add(stats, BorderLayout.NORTH);
		pane.add(tabbedPane, BorderLayout.CENTER);
		pane.add(back, BorderLayout.SOUTH);
		
		classTeachEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				teachForm.setLocationRelativeTo(frame);
				teachForm.setVisible(true);
				String[] newClassTeach = teachForm.getResult();
				if(newClassTeach[0] != null) {
					classroom.editClassTeacher(newClassTeach);
					try {
						Fx.classToFile(classroom);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String[] classTeach = classroom.getClassTeacherDetails();
					classTeachName.setText("Class Teacher: " + classTeach[0]);
					classTeachID.setText("ID: " + classTeach[1]);
					classTeachSubject.setText("Subject: " + classTeach[2]);
				}
			}
		});
		
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Fx.classToFile(classroom);
					pane.removeAll();
					menuPane(pane);
					pane.doLayout();
					update(getGraphics());
					frame.setSize(450, 593);
					frame.setSize(450, 592);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		//student card actions
		
		addNewStudent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				studentForm.setLocationRelativeTo(frame);
				studentForm.setVisible(true);
				String[] newStudent = studentForm.getResult();
				if(newStudent[0] != null) {
					System.out.println(classroom.addStudent(newStudent));
					try {
						Fx.classToFile(classroom);
					} catch (IOException e) {
						e.printStackTrace();
					}
					studentNames.setListData(classroom.getStudentNames());
				}
				if(!(classroom.getStudentDetails(classroom.getCapacity() - 1) == null))
					addNewStudent.setEnabled(false);
			}
		});
		
		studentNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(studentNames.getSelectedValue() == null) {
					deleteStudent.setEnabled(false);
				} else if(studentNames.getSelectedValue().equals(" ")) {
					deleteStudent.setEnabled(false);
					infoStudent.setText(null);
				} else {
					deleteStudent.setEnabled(true);
					String[] details = classroom.getStudentDetails(studentNames.getSelectedIndex());
					infoStudent.setText("Name: " + details[0] + "\n ID: " + details[1]);
				}
			}
		});
		
		deleteStudent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				classroom.deleteStudent(studentNames.getSelectedIndex());
				try {
					Fx.classToFile(classroom);
				} catch (IOException e) {
					e.printStackTrace();
				}
				studentNames.setListData(classroom.getStudentNames());
				infoStudent.setText(null);
				if(classroom.getStudentDetails(classroom.getCapacity() - 1) == null)
					addNewStudent.setEnabled(true);
			}
			
		});
		
		//teacher card actions
	
		addNewTeacher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				teachForm.setLocationRelativeTo(frame);
				teachForm.setVisible(true);
				String[] newTeacher = teachForm.getResult();
				if(newTeacher[0] != null) {
					if(!(newTeacher[0].equals("None"))){
						System.out.println(classroom.addTeacher(newTeacher));
						try {
							Fx.classToFile(classroom);
						} catch (IOException e) {
							e.printStackTrace();
						}
						teacherNames.setListData(classroom.getTeacherNames());
					}
				}
			}
		});
		
		teacherNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(teacherNames.getSelectedValue() == null) {
					deleteTeacher.setEnabled(false);
				} else if(teacherNames.getSelectedValue().equals(" ")) {
					deleteTeacher.setEnabled(false);
					infoTeacher.setText(null);
				} else {
					deleteTeacher.setEnabled(true);
					String[] details = classroom.getTeacherDetails(teacherNames.getSelectedIndex());
					infoTeacher.setText("Name: " + details[0] + "\n ID: " + details[1] + "\n Subject: " + details[2]);
				}
			}
		});
		
		deleteTeacher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				classroom.deleteTeacher(teacherNames.getSelectedIndex());
				try {
					Fx.classToFile(classroom);
				} catch (IOException e) {
					e.printStackTrace();
				}
				teacherNames.setListData(classroom.getTeacherNames());
				infoTeacher.setText(null);
			}
			
		});
	}
	
	public GUI() throws IOException {
		Container pane = getContentPane();

		teachForm.pack();
		studentForm.pack();
		classForm.pack();
		
		if(Fx.init()) {
			int n = JOptionPane.showConfirmDialog(
					pane, 
					"Old session detected.\n"
					+ "Would you like to create new session? \n"
					+ "WARNING: Starting new session DELETES previous session.",
					"Classroom Manager",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				Fx.reset();
			}
		}
		menuPane(pane);
		
	}
	
	
	public static void main(String[] args) throws IOException {
		new File("Classroom.txt");
		frame = new GUI();
		frame.setTitle("Classroom Manager");
		frame.setSize(450, 592);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}
