
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.incremental.AbstractIncrementalScoreCalculator;

/**
 * Scores how good a given configuration of students to courses is in the form
 * of a HardMediumSoftScore. The hard score is -1 * the number of students whose
 * presence in a course puts that course over capacity. The medium score is -1 *
 * the number of students who are not in a course in their top 5. The soft score
 * is determined by a number of factors, the importance of each of which can be
 * scaled by the user. These factors are:
 * - Size Balance: -1 point for each student below half capacity a course is.
 * Encourages the courses to have roughly equal numbers of students.
 * - Gender Balance: -1 * the difference between the number of girls and the
 * number of boys in each course. Encourages classes that are 50/50 male/female.
 * - Grade Balance: -1 * the difference between the number of students of the
 * most common grade in a class and the number of students of the least common
 * grade. (If there are 6 freshman, 5 sophomores, 6 juniors and 8 seniors in a
 * class, the grade balance will be -2)
 * - Happiness: -1 point for each student in their 2nd choice class, -3 for each
 * student in their 3rd, -6 for each student in their 4th and -10 for each
 * student in their 5th choice. Unaffected by students not in their top 5, since
 * the medium score already covers them.
 * 
 * @author Eliot J. Kaplan
 *
 */
public class JantermIncrementalScoreCalculator extends AbstractIncrementalScoreCalculator<JantermSolution> {

	private int hardScore, mediumScore, softScore;
	private Map<Course, List<Student>> courseLists;
	private Map<Course, Integer> genderBalance;
	private Map<Course, GradeBalance> gradeBalance;

	private int sizeWeight;
	private int happyWeight;
	private int genderWeight;
	private int gradeWeight;

	/**
	 * Modify the current score to take Student s into account. Includes their
	 * effect on course size, gender balance and grade balance as well as the
	 * student's happiness with their placement.
	 * 
	 * @param s The student in question.
	 */
	private void insert(Student s) {
		Course c = s.getAssignment();
		List<Student> students = courseLists.get(c);
		if (students.size() < c.getCapacity() / 2) // adding to a small class is good
			softScore += sizeWeight;
		students.add(s);
		if (students.size() > c.getCapacity()) // going over capacity is bad
			hardScore -= 1;
		List<Course> prefs = s.getPreferences();
		if (prefs.contains(c)) // unhappy students are bad
			softScore -= happyWeight * triangle(prefs.indexOf(c));
		else
			mediumScore -= 1;
		// Gender imbalance is bad
		int oldGenBal = genderBalance.get(c);
		int newGenBal = oldGenBal + (s.isMale() ? 1 : -1);
		genderBalance.put(c, newGenBal);
		softScore += genderWeight * (Math.abs(oldGenBal) - Math.abs(newGenBal));
		// Grade imbalance is bad
		GradeBalance gradeBal = gradeBalance.get(c);
		int oldGradeBal = gradeBal.getBalance();
		gradeBal.addStudent(s);
		int newGradeBal = gradeBal.getBalance();
		softScore += gradeWeight * (oldGradeBal - newGradeBal);
	}

	/**
	 * Modify the current score to eliminate the effect of Student s. Includes their
	 * effect on course size, gender balance and grade balance as well as the
	 * student's happiness with their placement.
	 * 
	 * @param s The student in question.
	 */
	private void retract(Student s) {
		Course c = s.getAssignment();
		List<Student> students = courseLists.get(c);
		if (students.size() > c.getCapacity()) // getting under capacity is good
			hardScore += 1;
		students.remove(s);
		if (students.size() < c.getCapacity() / 2) // removing from a small class is bad
			softScore -= sizeWeight;
		List<Course> prefs = s.getPreferences();
		if (prefs.contains(c)) // removing an unhappy student is good
			softScore += happyWeight * triangle(prefs.indexOf(c));
		else
			mediumScore += 1;
		// Gender imbalance is bad
		int oldGenBal = genderBalance.get(c);
		int newGenBal = oldGenBal - (s.isMale() ? 1 : -1);
		genderBalance.put(c, newGenBal);
		softScore += genderWeight * (Math.abs(oldGenBal) - Math.abs(newGenBal));
		// Grade imbalance is bad
		GradeBalance gradeBal = gradeBalance.get(c);
		int oldGradeBal = gradeBal.getBalance();
		gradeBal.removeStudent(s);
		int newGradeBal = gradeBal.getBalance();
		softScore += gradeWeight * (oldGradeBal - newGradeBal);
	}

	private int triangle(int n) {
		return n * (n + 1) / 2;
	}

	/**
	 * Reset the solution, recalculating the score from scratch.
	 * 
	 * @param sln
	 */
	public void resetWorkingSolution(JantermSolution sln) {
		hardScore = 0;
		mediumScore = 0;
		softScore = 0;
		happyWeight = JantermGUI.happyWeight;
		sizeWeight = JantermGUI.sizeWeight;
		genderWeight = JantermGUI.genderWeight;
		gradeWeight = JantermGUI.gradeWeight;
		courseLists = new HashMap<Course, List<Student>>();
		genderBalance = new HashMap<Course, Integer>();
		gradeBalance = new HashMap<Course, GradeBalance>();
		for (Course c : sln.getCourses()) {
			courseLists.put(c, new ArrayList<Student>());
			genderBalance.put(c, 0);
			gradeBalance.put(c, new GradeBalance(c));
		}
		for (Student s : sln.getStudents()) {
			insert(s);
		}
	}

	public void beforeEntityAdded(Object entity) {
		// Do nothing

	}

	/**
	 * Whenever a student is 
	 */
	public void afterEntityAdded(Object entity) {
		insert((Student) entity);
	}

	public void beforeVariableChanged(Object entity, String variableName) {
		retract((Student) entity);

	}

	public void afterVariableChanged(Object entity, String variableName) {
		insert((Student) entity);

	}

	public void beforeEntityRemoved(Object entity) {
		retract((Student) entity);

	}

	public void afterEntityRemoved(Object entity) {
		// Do nothing

	}

	public HardMediumSoftScore calculateScore() {
		return HardMediumSoftScore.valueOf(hardScore, mediumScore, softScore);
	}

	private class GradeBalance {

		private Map<Grade, Integer> grades;

		public GradeBalance(Course c) {
			grades = new HashMap<Grade, Integer>();
			for (Grade g : c.getGrades()) {
				grades.put(g, 0);
			}
		}

		public void addStudent(Student st) {
			int n = grades.get(st.getGrade());
			grades.put(st.getGrade(), n + 1);
		}

		public void removeStudent(Student st) {
			int n = grades.get(st.getGrade());
			grades.put(st.getGrade(), n - 1);
		}

		public int getBalance() {
			int max = Integer.MIN_VALUE;
			int min = Integer.MAX_VALUE;
			for (int i : grades.values()) {
				if (i > max)
					max = i;
				if (i < min)
					min = i;
			}
			return max - min;
		}

	}

}
