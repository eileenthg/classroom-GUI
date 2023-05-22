package code;

public class Person {
	private String name;
	private String id;
	
	protected Person(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getID() {
		return id;
	}
}
