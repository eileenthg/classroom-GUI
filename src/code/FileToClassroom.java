package code;
import java.io.*;
import java.util.*;

public class FileToClassroom {
	public static int read(File file) throws IOException {
		//Classlist.
		String[] classRaw = Fx.fileToArray(new File("Classroom.txt"));
		String[][] classList = new String[classRaw.length][2];
		for(int i = 0; i < classRaw.length; i++) {
			classList[i] = classRaw[i].split(", ");
		}
		try {
			Scanner sc = new Scanner(file);
			int count = sc.nextInt();
			sc.nextLine();
			
			for(int i = 0; i < count; i++) {
				String className = sc.nextLine();
				boolean notDuplicate = true;
				for(String[] classname : classList) {
					if(classname[0].equals(className)) {
						notDuplicate = false;
						break;
					}
				}
				
				String classLoc = sc.nextLine();
				int classCap = sc.nextInt();
				
				StudentGrp students;
				String[] studentRaw = new String[classCap];
				int studentNo = sc.nextInt();
				sc.nextLine();
				for(int j = 0; j < studentNo; j++) {
					studentRaw[j] = sc.nextLine();
				}
				for(int j = studentNo; j < classCap; j++) {
					studentRaw[j] = ", ";
				}
				students = new StudentGrp(studentRaw);
				String[] studentIDs = students.getIDs();
				
				if(notDuplicate) {
					try {
						FileWriter fw = new FileWriter(new File("Students.txt"), true);
						PrintWriter pw = new PrintWriter(fw);
						for(String studentID : studentIDs) {
							if(studentID != null) pw.println(studentID);
						}
						pw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				int teachCount = sc.nextInt();
				if(!(teachCount == 0)) sc.nextLine();
				Teacher classTeach;
				TeacherGrp regularTeach;
				if(teachCount > 0) {
					String[] rawTeachClass = sc.nextLine().split(", ");
					classTeach = new Teacher(rawTeachClass[0], rawTeachClass[1], rawTeachClass[2]);
					if(teachCount > 1) {
						String[] teacherRaw = new String[teachCount - 1];
						for(int j = 0; j < teachCount - 1; j++) {
							teacherRaw[j] = sc.nextLine();
						}
						regularTeach = new TeacherGrp(teacherRaw);
					} else {
						regularTeach = new TeacherGrp();
					}
				} else {
					classTeach = new Teacher("None", "None", "None");
					regularTeach = new TeacherGrp();
				}
				
				if(notDuplicate) {
					try {
						FileWriter fw = new FileWriter(new File("Classroom.txt"), true);
						PrintWriter pw = new PrintWriter(fw);
						pw.println(className + ", " + classTeach.getName());
						pw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				
					Classroom room = new Classroom(className, classLoc, students, classTeach, regularTeach);
					Fx.classToFile(room);
				}
			}
			sc.close();
			return 0;
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
		
	}
}
