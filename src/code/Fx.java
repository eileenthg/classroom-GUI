package code;
import java.io.*;
import java.util.*;

public class Fx {
	//Run this during start of program
	//If return true, old session exists.
	public static boolean init() throws IOException{
		File student = new File("Students.txt");
		File classroom = new File("Classroom.txt");
		return !(student.createNewFile() && classroom.createNewFile());	
	}
	
	//deletes all old files, initialise new ones.
		public static void reset() throws IOException{
			//deletes all old classroom files
			String[] classList = fileToArray(new File("Classroom.txt"));
			for(int i = 0; i < classList.length; i++) {
				String name = classList[i].split(", ")[0];
				String fileName = name + ".txt";
				File delete = new File(fileName);
				delete.delete();
			}
			
			//resets following files
			FileWriter file = new FileWriter("Students.txt");
			file.close();
			file = new FileWriter("Classroom.txt");
			file.close();
		}
	
	//returns -1 if error
	public static int lineCount(File file) {
		try {
			Scanner sc = new Scanner(file);
			int counter = 0;
			while(sc.hasNextLine()) {
				sc.nextLine();
				counter++;
			}
			sc.close();
			return counter;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	//each line is a String element in the array
	public static String[] fileToArray(File file) {
		try {
			int counter = lineCount(file);
			if(counter == -1)
				return null;
			
			Scanner sc = new Scanner(file);
			String[] raw = new String[counter];
			counter = 0;
			while(sc.hasNextLine()) {
				raw[counter++] = sc.nextLine();
			}
			sc.close();
			return raw;
		} catch (FileNotFoundException e) {
			//just warn
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void classToFile(Printable x) throws IOException {
		String txtName = x.getName() + ".txt";
		FileWriter fw = new FileWriter(txtName);
		PrintWriter pw = new PrintWriter(fw);
		x.printDetails(pw);
		pw.close();
	}
	
	public static void deleteClassroom(String x) throws IOException{
		String txtName = x + ".txt";
		Classroom room = new Classroom(new File(txtName));
		
		//Remove students from studentlist
		String[] deleteStd = room.getStudentIDs();
		File stdList = new File("Students.txt");
		String[] raw = fileToArray(stdList);
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
		
		
		//Remove class from classlist.
		File classList= new File("Classroom.txt");
		int counter = lineCount(classList);
		Scanner sc = new Scanner(classList);
		raw = new String[counter - 1];
		
		String regex = "^" + x + ".*";
		for(int i = 0, j = 0; i < counter; i++) {
			String keep = sc.nextLine();
			if(!(keep.matches(regex))) {
				raw[j] = keep;
				j++;
			}
		}
		sc.close();
		fw = new FileWriter("Classroom.txt");
		pw = new PrintWriter(fw);
		
		for(String i : raw) {
			pw.println(i);
		}
		pw.close();
		
		//Delete text file
		File target = new File(txtName);
		target.delete();
	}
}
