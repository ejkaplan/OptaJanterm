
import java.util.ArrayList;
import java.util.List;

/**
 * An object to represent a grade level. Used to track which courses are
 * available to students of a given grade level.
 * 
 * @author Eliot J. Kaplan
 *
 */
public class Grade {

	private int gradeNumber;
	private List<Course> courses;

	/**
	 * 
	 * @param gradeNumber The grade number (9,10,11,12) that this Grade object
	 *                    represents.
	 */
	public Grade(int gradeNumber) {
		this.gradeNumber = gradeNumber;
		courses = new ArrayList<Course>();
	}

	public int getGradeNumber() {
		return gradeNumber;
	}

	/**
	 * 
	 * @return The list of courses available to students in this grade level.
	 */
	public List<Course> getCourses() {
		return courses;
	}

	public String toString() {
		return String.valueOf(gradeNumber);
	}

}
