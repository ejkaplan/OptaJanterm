
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A single janterm course. Each student will be assigned one of these.
 * 
 * @author Eliot J. Kaplan
 *
 */
public class Course implements Serializable {

	private static final long serialVersionUID = 896756409632077451L;
	private String name;
	private int capacity;
	private List<Grade> grades;

	/**
	 * Create a new course object. This is done automatically when the csv file is
	 * read.
	 * 
	 * @param name     The name of the course
	 * @param capacity The maximum number of students who can take this course.
	 */
	public Course(String name, int capacity) {
		this.name = name;
		this.capacity = capacity;
		this.grades = new ArrayList<Grade>();
	}

	public String getName() {
		return name;
	}

	/**
	 * @return The max number of students who can fit in this course.
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * 
	 * @return A list of integers representing the grade levels that are permitted
	 *         to enroll in this course.
	 */
	public List<Grade> getGrades() {
		return grades;
	}

	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
		result = prime * result + ((grades == null) ? 0 : grades.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (capacity != other.capacity)
			return false;
		if (grades == null) {
			if (other.grades != null)
				return false;
		} else if (!grades.equals(other.grades))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
