package code;
import java.io.*;
import java.util.*;

public class StudentGrp implements PersonGrp<Student>{
	private Student[] studentList;
	private int capacity;
	
	public StudentGrp(String[] array) {
		//assign capacity
		this.capacity = array.length;
		
		//assigns studentList
		studentList = new Student[array.length];
		//String element format
		// name, id
		//Student constr format
		//name, id
		//don't know how to throw exception if more than two params.
		for(int i = 0; i < capacity; i++) {
			if(array[i] != null) {
				String[] details = array[i].split(", ");
				if(details.length != 0) studentList[i] = new Student(details[0], details[1]);
				else studentList[i] = null;
			}
			else studentList[i] = null;
		}
	}
	
	public StudentGrp(int capacity) {
		this.capacity = capacity;
		studentList = new Student[capacity];
	}

	@Override
	public String[] getNames() {
		String[] names = new String[studentList.length];
		for(int i = 0; i < studentList.length; i++) {
			if(studentList[i] != null) names[i] = studentList[i].getName();
			else names[i] = " ";
		}
		return names;
	}
	
	public String[] getIDs() {
		String[] ids = new String[studentList.length];
		for(int i = 0; i < studentList.length; i++) {
			if(studentList[i] != null) ids[i] = studentList[i].getID();
		}
		
		return ids;
	}

	@Override
	public Student getPerson(int index) {
		return studentList[index];
	}

	@Override
	//returns {name, id}
	public String[] getPersonDetails(int index) {
		if(getPerson(index) != null) {
			Student student = getPerson(index);
			String[] details = {student.getName(), student.getID()};
			return details;
		} else
			return null;
	}

	@Override
	//entry format {name, id}
	public int add(String[] entry){
		//check if have 2 elements. returns 1 otherwise.
		if(entry.length != 2) {
			return 1; //parameter error
		}
		
		//check if adding will exceed capacity by checking if null, and checking last entry. returns 3 otherwise.
		if(studentList.length == 0)
			return 3;
		
		if(studentList[capacity - 1] != null)
			return 3;
		
		//check if duplicate. returns 2 otherwise.
		//if Students.txt does not exist, returns 4. Prompt for system restart.
		Scanner sc;
		try {
			sc = new Scanner(new File("Students.txt"));
			while(sc.hasNext()) {
				if(entry[1].equals(sc.next()))
					return 2;
			}
		} catch (FileNotFoundException e) {
			return 4;
		}
		
		
		//adds student to next null entry
		int i = 0;
		while(studentList[i] != null) {
			i++;
		}
		studentList[i] = new Student(entry[0], entry[1]);
		
		//adds student ID to Students.txt
		try {
			FileWriter fw = new FileWriter(new File("Students.txt"), true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(entry[1]);
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0; //success
	}

	@Override
	//entry format {name, id}
	public boolean edit(int index, String[] entry) {
		//check if have 3 elements.
		if(entry.length != 2) {
			return false;
		}
		
		//changes index with new entry
		studentList[index] = new Student(entry[0], entry[1]);
		return true;
	}

	@Override
	//remember to update GUI with new info after running this.
	public void delete(int index) {
		//delete student from Students.txt
		String deleteStd = getPersonDetails(index)[1];
		File stdList = new File("Students.txt");
		String[] raw = Fx.fileToArray(stdList);
		for(int j = 0; j < raw.length; j++) {
			if(raw[j] != null) {
				if(raw[j].equals(deleteStd)) {
					raw[j] = null;
				}
			}
		}
		try {
			FileWriter fw = new FileWriter("Students.txt");
			PrintWriter pw = new PrintWriter(fw);
			for(String i : raw) {
				if(i != null)
					pw.println(i);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//new temp list
		Student[] newList = new Student[capacity];

		//turns entries to delete into null in the old entry
		studentList[index] = null;

		//place remaining entries into new entry
		for(int i = 0, j = 0; i < capacity; i++) {
			if(!(studentList[i] == null)) {
				newList[j] = studentList[i];
				j++;
			}
		}
		
		

		
		//update studentList to newList
		studentList = newList;
	}
	
	@Override
	public Student[] getList() {
		return studentList;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
}
