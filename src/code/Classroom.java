package code;
import java.io.*;
import java.util.*;

public class Classroom implements Printable{
	private String name;
	private String loc;
	private StudentGrp student;
	private Teacher classTeacher = new Teacher("None", "None", "None");
	private TeacherGrp teacher;
	
	//Note: StudntGrp and TeacherGrp already setup during input phase.
	public Classroom(String name, String loc, StudentGrp student, Teacher classTeach, TeacherGrp teacher) {
		this.name = name;
		this.loc = loc;
		this.student = student;
		this.classTeacher = classTeach;
		this.teacher = teacher;
	}
	
	public Classroom(File file) {
		try {
			Scanner sc = new Scanner(file);
			//general class data
			name = sc.nextLine();
			loc = sc.nextLine();
			
			//Student data
			int capacity = sc.nextInt();
			sc.nextLine();
			String[] rawStudent = new String[capacity];
			for(int i = 0; i < capacity; i++) {
				rawStudent[i] = sc.nextLine();
			}
			student = new StudentGrp(rawStudent);
			
			//ClassTeach data
			String rawClassTeach = sc.nextLine();
			String[] arrayClassTeach = rawClassTeach.split(", ");
			classTeacher = new Teacher(arrayClassTeach[0], arrayClassTeach[1], arrayClassTeach[2]);
						
			//Teacher data
			String[] tempTeacher = new String[100];
			int tempCount = 0;
			while(sc.hasNextLine()) {
				tempTeacher[tempCount] = sc.nextLine();
				tempCount++;
			}
			String[] rawTeacher = new String[tempCount];
			for(int i = 0; i < tempCount; i++) {
				rawTeacher[i] = tempTeacher[i];
			}
			teacher = new TeacherGrp(rawTeacher);
			sc.close();
			
		} catch (FileNotFoundException e) {
			// Main code: Make sure to check if classroom name is null.
			// If null, report missing, delete class entry. (Or just ask for system restart.)
			e.printStackTrace();
		}	
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public String getLoc() {
		return loc;
	}
	
	public int getCapacity() {
		return student.getCapacity();
	}
	
	public Student[] getStudents() {
		return student.getList();
	}
	
	public Teacher[] getTeachers() {
		return teacher.getList();
	}
	
	public String[] getStudentNames(){
		return student.getNames();
	}
	
	public String[] getTeacherNames() {
		return teacher.getNames();
	}
	
	public String[] getStudentIDs() {
		return student.getIDs();
	}
	
	public String[] getStudentDetails(int index){
		return student.getPersonDetails(index);
	}
	
	public String[] getTeacherDetails(int index) {
		return teacher.getPersonDetails(index);
	}
	
	public String[] getClassTeacherDetails() {
		String[] details = new String[3];
		details[0] = classTeacher.getName();
		details[1] = classTeacher.getID();
		details[2] = classTeacher.getSubject();
		return details;
	}
	
	public int addStudent(String name, String id) {
		String[] entry = {name, id};
		return student.add(entry);
	}
	
	public int addStudent(String[] entry) {
		return student.add(entry);
	}
	
	public int addTeacher(String name, String id, String subject) {
		String[] entry = {name, id, subject};
		return teacher.add(entry);
	}
	
	public int addTeacher(String[] entry) {
		return teacher.add(entry);
	}
	
	public void editClassTeacher(String name, String id, String subject) {
		classTeacher = new Teacher(name, id, subject);
	}
	
	public void editClassTeacher(String[] teach) {
		classTeacher = new Teacher(teach[0], teach[1], teach[2]);
	}
	
	public void deleteStudent(int index) {
		student.delete(index);
	}
	
	public void deleteTeacher(int index) {
		teacher.delete(index);
	}

	@Override
	public void printDetails(PrintWriter pw) {
		pw.println(getName());
		pw.println(getLoc());
		pw.println(getCapacity());
		
		//All students
		Person[] queue = getStudents();
		for(Person x : queue) {
			if(x != null)
				pw.println(x.toString());
			else
				pw.println(", ");
		}
		
		//Class teacher
		pw.println(classTeacher.toString());
		
		//All teachers
		queue = getTeachers();
		for(Person x : queue) {
			pw.println(x.toString());
		}
	}
}
