
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Student {

	private String first;
	private String last;
	private Grade grade;
	private boolean male;
	private List<Course> preferences;
	private Course assignment;

	public Student() {

	}

	public Student(String first, String last, Grade grade, boolean male, List<Course> preferences) {
		this.first = first;
		this.last = last;
		this.grade = grade;
		this.male = male;
		this.preferences = preferences;
	}

	public String getFirst() {
		return first;
	}

	public String getLast() {
		return last;
	}

	public Grade getGrade() {
		return grade;
	}

	public boolean isMale() {
		return male;
	}

	public List<Course> getPreferences() {
		return preferences;
	}

	@PlanningVariable(valueRangeProviderRefs = { "courseRange" })
	public Course getAssignment() {
		return assignment;
	}

	@ValueRangeProvider(id = "courseRange")
	public List<Course> getLegalCourses() {
		return grade.getCourses();
	}

	public void setAssignment(Course c) {
		assignment = c;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignment == null) ? 0 : assignment.hashCode());
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result + ((last == null) ? 0 : last.hashCode());
		result = prime * result + (male ? 1231 : 1237);
		result = prime * result + ((preferences == null) ? 0 : preferences.hashCode());
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
		Student other = (Student) obj;
		if (assignment == null) {
			if (other.assignment != null)
				return false;
		} else if (!assignment.equals(other.assignment))
			return false;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (grade != other.grade)
			return false;
		if (last == null) {
			if (other.last != null)
				return false;
		} else if (!last.equals(other.last))
			return false;
		if (male != other.male)
			return false;
		if (preferences == null) {
			if (other.preferences != null)
				return false;
		} else if (!preferences.equals(other.preferences))
			return false;
		return true;
	}

	public String toString() {
		return first + " " + last + "(" + grade + (male ? "m" : "f") + ")";
	}

}
