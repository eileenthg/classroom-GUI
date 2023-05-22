package code;

public class Student extends Person{
	public Student(String name, String id) {
		super(name, id);
	}
	
	
	
	public String toString(){
		return getName() + ", " + getID();
	}
}
