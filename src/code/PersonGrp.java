package code;

public interface PersonGrp<T extends Person> {
	public String[] getNames();
	public T getPerson(int index);
	public String[] getPersonDetails(int index);
	public int add(String[] entry);
	public boolean edit(int index, String[] entry);
	public void delete(int index);
	public T[] getList();
}
