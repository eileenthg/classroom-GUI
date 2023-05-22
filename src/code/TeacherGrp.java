package code;

public class TeacherGrp implements PersonGrp<Teacher>{
	private Teacher[] teacherList;
	
	public TeacherGrp(String[] array) {
		teacherList = new Teacher[array.length];
		
		//String element format
		// name, id, subject
		//Teacher constr format
		//name, id, subject
		//can throw exception if insufficient params (from accidental wrong assignment)
		for(int i = 0; i < array.length; i++) {
			String[] details = array[i].split(", ");
			teacherList[i] = new Teacher(details[0], details[1], details[2]);
		}
	}
	
	public TeacherGrp() {
		teacherList = new Teacher[0];
	}
	
	
	
	@Override
	public String[] getNames() {
		String[] names = new String[teacherList.length];
		for(int i = 0; i < teacherList.length; i++) {
			names[i] = teacherList[i].getName();
		}
		return names;
	}

	@Override
	public Teacher getPerson(int index) {
		return teacherList[index];
	}

	@Override
	//returns {name, id, subject}
	public String[] getPersonDetails(int index) {
		if(getPerson(index) != null) {
			Teacher teacher = getPerson(index);
			String[] details = {teacher.getName(), teacher.getID(), teacher.getSubject()};
			return details;
		} else
			return null;
	}

	@Override
	//entry format {name, id, subject}
	public int add(String[] entry) {
		//check if have 3 elements.
		if(entry.length != 3) {
			return 1; //parameter error
		}
		
		//creates temp newList, adds new teacher
		Teacher[] newList = new Teacher[teacherList.length + 1];
		for(int i = 0; i < teacherList.length; i++) {
			newList[i] = teacherList[i];
		}
		newList[newList.length - 1] = new Teacher(entry[0], entry[1], entry[2]);
		
		//updates teacherList with newList
		teacherList = newList;
		return 0; //success
	}

	@Override
	//entry format {name, id, subject}
	public boolean edit(int index, String[] entry) {
		//check if have 3 elements.
		if(entry.length != 3) {
			return false;
		}
		
		//changes index with new entry
		teacherList[index] = new Teacher(entry[0], entry[1], entry[2]);
		return true;
	}

	@Override
	//remember to update GUI with new info after running this.
	public void delete(int index) {
		//new temp list
		Teacher[] newList = new Teacher[teacherList.length - 1];
		//turns entries to delete into null in the old entry
		teacherList[index] = null;
		
		//place remaining entries into new entry
		for(int i = 0, j = 0; j < newList.length; i++) {
			if(!(teacherList[i] == null)) {
				newList[j] = teacherList[i];
				j++;
			}
		}
		
		//update teacherList to newList
		teacherList = newList;
	}

	@Override
	public Teacher[] getList() {
		return teacherList;
	}
}
