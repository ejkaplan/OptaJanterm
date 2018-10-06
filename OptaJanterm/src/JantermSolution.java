
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class JantermSolution {

	private List<Course> courses;
	private List<Student> students;
	private List<Grade> grades;
	private HardMediumSoftScore score;

	public JantermSolution(List<Course> courses, List<Student> students, List<Grade> grades) {
		this.courses = courses;
		this.students = students;
		this.grades = grades;
	}

	public JantermSolution() {

	}

	@ProblemFactCollectionProperty
	public List<Course> getCourses() {
		return courses;
	}

	@PlanningEntityCollectionProperty
	public List<Student> getStudents() {
		return students;
	}

	@ProblemFactCollectionProperty
	public List<Grade> getGrades() {
		return grades;
	}

	@PlanningScore
	public HardMediumSoftScore getScore() {
		return score;
	}

	public void setScore(HardMediumSoftScore score) {
		this.score = score;
	}

	public HashMap<Course, Integer> getCourseCounts() {
		HashMap<Course, Integer> out = new HashMap<Course, Integer>();
		for (Course c : courses)
			out.put(c, 0);
		for (Student s : students) {
			int count = out.get(s.getAssignment()) + 1;
			out.put(s.getAssignment(), count);
		}
		return out;
	}

	public String csvOutput() {
		String out = "first,last,grade,gender,course,happiness,1st choice,2nd choice,3rd choice,4th choice,5th choice\n";
		Map<Course, List<Student>> courseLists = getCourseLists();
		for (Course c : courses) {
			for (Student s : courseLists.get(c)) {
				out += s.getFirst() + "," + s.getLast() + "," + s.getGrade() + "," + (s.isMale() ? "M" : "F") + ",\""
						+ s.getAssignment() + "\"," + (s.getPreferences().indexOf(s.getAssignment()) + 1) + ",";
				for (Course pref : s.getPreferences()) {
					if (pref != null)
						out += "\"" + pref.getName() + "\",";
					else
						out += "\"None\",";
				}
				out += "\n";
			}
		}
		return out;
	}

	public String summaryOutput() {
		String out = "title,max size,# of students,male,9th grade,10th grade,11th grade,12th grade,1st choice,2nd choice,3rd choice,4th choice,5th choice,bad\n";
		Map<Course, List<Student>> courseLists = getCourseLists();
		for (Course c : courses) {
			List<Student> members = courseLists.get(c);
			out += "\"" + c.getName() + "\"," + c.getCapacity() + "," + members.size() + ",";
			out += maleCount(members) + ",";
			for (int gr : gradeCounts(members)) {
				out += gr + ",";
			}
			for (int pr : preferenceCounts(c, members)) {
				out += pr + ",";
			}
			out += "\n";
		}
		return out;
	}

	public int maleCount(List<Student> members) {
		if (members.size() == 0)
			return 0;
		int n = 0;
		for (Student s : members) {
			if (s.isMale())
				n++;
		}
		return n;
	}

	public int[] gradeCounts(List<Student> members) {
		int[] out = new int[4];
		if (members.size() == 0)
			return out;
		for (Student s : members) {
			out[grades.indexOf(s.getGrade())]++;
		}
		return out;
	}

	public int[] preferenceCounts(Course c, List<Student> members) {
		int[] out = new int[6];
		if (members.size() == 0)
			return out;
		for (Student s : members) {
			if (s.getPreferences().contains(c)) {
				out[s.getPreferences().indexOf(c)]++;
			} else {
				out[5]++;
			}
		}
		return out;
	}

	public Map<Course, List<Student>> getCourseLists() {
		Map<Course, List<Student>> courseLists = new HashMap<Course, List<Student>>();
		for (Course c : courses) {
			courseLists.put(c, new ArrayList<Student>());
		}
		for (Student s : students) {
			courseLists.get(s.getAssignment()).add(s);
		}
		return courseLists;
	}

	public String toString() {
		String out = "JantermSolution [score=" + getScore() + "]";
		return out;
	}

}
