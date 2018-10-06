
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import com.opencsv.CSVReader;

public class JantermRunner implements Runnable {

	private List<Course> courses;
	private List<Student> students;
	private List<Grade> gradeLevels;
	private Solver<JantermSolution> solver;
	private String sizeExceptions;

	public JantermRunner(String grade9, String grade10, String grade11, String grade12, String sizeExceptions) {
		courses = new ArrayList<Course>();
		students = new ArrayList<Student>();
		gradeLevels = new ArrayList<Grade>();
		this.sizeExceptions = sizeExceptions;
		for (int i = 9; i <= 12; i++) {
			gradeLevels.add(new Grade(i));
		}
		try {
			readCSV(grade9, 9);
			readCSV(grade10, 10);
			readCSV(grade11, 11);
			readCSV(grade12, 12);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SolverFactory<JantermSolution> factory = SolverFactory.createFromXmlResource("solverconfig.xml");
		solver = factory.buildSolver();
	}

	public void run() {
		JantermSolution unsolved = new JantermSolution(courses, students, gradeLevels);
		JantermSolution solved = solver.solve(unsolved);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("assignment.csv"));
			bw.write(solved.csvOutput());
			bw.close();
			bw = new BufferedWriter(new FileWriter("course_summary.csv"));
			bw.write(solved.summaryOutput());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void kill() {
		solver.terminateEarly();
	}

	public boolean isRunning() {
		return solver.isSolving();
	}

	public HardMediumSoftScore currentScore() {
		return (HardMediumSoftScore) solver.getBestScore();
	}

	public HashMap<String, Integer> getCapacities() throws IOException {
		HashMap<String, Integer> out = new HashMap<String, Integer>();
		CSVReader reader = new CSVReader(new FileReader(sizeExceptions));
		for (String[] row : reader.readAll()) {
			out.put(row[0], Integer.parseInt(row[1]));
		}
		reader.close();
		return out;
	}

	public void readCSV(String filename, int gradeLevel) throws IOException {
		Grade gr = gradeLevels.get(gradeLevel - 9);
		CSVReader reader = new CSVReader(new FileReader(filename));
		List<String[]> allRows = reader.readAll();
		reader.close();
		HashMap<String, Integer> capacities = getCapacities();
		for (int i = 6; i < allRows.get(0).length; i++) {
			String courseName = allRows.get(0)[i];
			Course c = findCourse(courseName);
			if (c == null) {
				int capacity;
				if (capacities.containsKey(courseName))
					capacity = capacities.get(courseName);
				else
					capacity = capacities.get("default");
				c = new Course(courseName, capacity);
				courses.add(c);
			}
			if (!c.getGrades().contains(gr)) {
				c.getGrades().add(gr);
			}
			gr.getCourses().add(c);
		}
		for (int i = 1; i < allRows.size(); i++) {
			String[] row = allRows.get(i);
			String first = row[1];
			String last = row[2];
			boolean gender = row[3].equals("M");
			List<Course> prefs = new ArrayList<Course>();
			List<String> rowList = Arrays.asList(row);
			int count = 0;
			for (int j = 1; j <= 5; j++) {
				int pref = rowList.indexOf("" + j);
				if (pref >= 0) {
					count++;
					String courseName = allRows.get(0)[pref];
					Course c = findCourse(courseName);
					prefs.add(c);
				} else {
					prefs.add(null);
				}
				if (count == 0)
					prefs.clear();
			}
			Student st = new Student(first, last, gr, gender, prefs);
			if (st.getPreferences().size() > 0) {
				do {
//					System.out.println(st.getFirst() + " " + st.getLast());
//					System.out.println(st.getPreferences());
					st.setAssignment(st.getPreferences().get((int) (st.getPreferences().size() * Math.random())));
				} while (st.getAssignment() == null);
			} else
				st.setAssignment(gr.getCourses().get((int) (gr.getCourses().size() * Math.random())));
			students.add(st);
		}
	}

	public Course findCourse(String name) {
		for (Course c : courses) {
			if (c.getName().equals(name))
				return c;
		}
		return null;
	}

}
