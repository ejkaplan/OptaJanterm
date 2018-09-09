
import java.io.File;

import controlP5.CColor;
import controlP5.ControlP5;
import controlP5.Textfield;
import processing.core.PApplet;

public class JantermGUI extends PApplet {

	private ControlP5 cp5;
	private File grade9File, grade10File, grade11File, grade12File, sizeExceptionsFile;
	private CColor badButtonColor, goodButtonColor;
	public static int happyWeight, sizeWeight, genderWeight, gradeWeight;
	private JantermRunner jr;
	private Thread thr;

	public static void main(String[] args) {
		PApplet.main("JantermGUI");
	}

	public void settings() {
		size(310, 450);
	}

	public void setup() {
		badButtonColor = new CColor(color(200, 0, 0), color(255, 0, 0), color(150, 0, 0), 0, 0);
		goodButtonColor = new CColor(color(0, 200, 0), color(0, 255, 0), color(0, 0, 150), 0, 0);
		cp5 = new ControlP5(this);
		cp5.addButton("grade9").setPosition(10, 10).setLabel("9th Grade File").setColor(badButtonColor);
		cp5.addButton("grade10").setPosition(10, 50).setLabel("10th Grade File").setColor(badButtonColor);
		cp5.addButton("grade11").setPosition(10, 90).setLabel("11th Grade File").setColor(badButtonColor);
		cp5.addButton("grade12").setPosition(10, 130).setLabel("12th Grade File").setColor(badButtonColor);
		cp5.addButton("sizeExceptions").setPosition(10, 170).setLabel("Size Exceptions").setColor(badButtonColor);
		cp5.addTextfield("grade9Filename").setPosition(100, 10).setUserInteraction(false);
		cp5.addTextfield("grade10Filename").setPosition(100, 50).setUserInteraction(false);
		cp5.addTextfield("grade11Filename").setPosition(100, 90).setUserInteraction(false);
		cp5.addTextfield("grade12Filename").setPosition(100, 130).setUserInteraction(false);
		cp5.addTextfield("sizeExceptionsFilename").setPosition(100, 170).setUserInteraction(false);
		cp5.addSlider("happyWeight").setPosition(10, 210).setRange(0, 10).setNumberOfTickMarks(11).setSize(230, 20)
				.setValue(1).setLabel("Happy Weight");
		cp5.addSlider("sizeWeight").setPosition(10, 250).setRange(0, 10).setNumberOfTickMarks(11).setSize(230, 20)
				.setValue(1).setLabel("Size Weight");
		cp5.addSlider("genderWeight").setPosition(10, 290).setRange(0, 10).setNumberOfTickMarks(11).setSize(230, 20)
				.setValue(1).setLabel("Gender Weight");
		cp5.addSlider("gradeWeight").setPosition(10, 330).setRange(0, 10).setNumberOfTickMarks(11).setSize(230, 20)
				.setValue(1).setLabel("Grade Weight");
		cp5.addTextfield("currentScore").setPosition(10, 380).setLabel("Current Best Score").setUserInteraction(false);
		cp5.addButton("start").setPosition(10, 420);
		cp5.addButton("kill").setPosition(100, 420);
	}

	/**
	 * Every frame, update the live score in the GUI
	 */
	public void draw() {
		background(100);
		if (jr != null) {
			((Textfield) cp5.getController("currentScore")).setText("" + jr.currentScore());
		}
	}

	/**
	 * Starts the optimizer running in a new thread. Before starting, checks that
	 * all files have been loaded.
	 */
	public void start() {
		if (thr != null && thr.isAlive())
			kill();
		if (grade9File != null && grade10File != null && grade11File != null && grade12File != null) {
			jr = new JantermRunner(grade9File.getAbsolutePath(), grade10File.getAbsolutePath(),
					grade11File.getAbsolutePath(), grade12File.getAbsolutePath(), sizeExceptionsFile.getAbsolutePath());
			thr = new Thread(jr);
			thr.start();
		}
	}

	/**
	 * Kill the janterm optimizer thread.
	 */
	public void kill() {
		jr.kill();
	}

	public void grade9() {
		selectInput("Select the 9th grade preference csv", "select9");
	}

	public void select9(File selection) {
		if (selection == null || !selection.getName().endsWith(".csv")) {
			grade9File = null;
			cp5.getController("grade9").setColor(badButtonColor);
			((Textfield) cp5.getController("grade9Filename")).setText("");
		} else {
			grade9File = selection;
			cp5.getController("grade9").setColor(goodButtonColor);
			((Textfield) cp5.getController("grade9Filename")).setText(grade9File.getName());
		}
	}

	public void grade10() {
		selectInput("Select the 10th grade preference csv", "select10");
	}

	public void select10(File selection) {
		if (selection == null || !selection.getName().endsWith(".csv")) {
			grade10File = null;
			cp5.getController("grade10").setColor(badButtonColor);
			((Textfield) cp5.getController("grade10Filename")).setText("");
		} else {
			grade10File = selection;
			cp5.getController("grade10").setColor(goodButtonColor);
			((Textfield) cp5.getController("grade10Filename")).setText(grade10File.getName());
		}
	}

	public void grade11() {
		selectInput("Select the 11th grade preference csv", "select11");
	}

	public void select11(File selection) {
		if (selection == null || !selection.getName().endsWith(".csv")) {
			grade11File = null;
			cp5.getController("grade11").setColor(badButtonColor);
			((Textfield) cp5.getController("grade11Filename")).setText("");
		} else {
			grade11File = selection;
			cp5.getController("grade11").setColor(goodButtonColor);
			((Textfield) cp5.getController("grade11Filename")).setText(grade11File.getName());
		}
	}

	public void grade12() {
		selectInput("Select the 12th grade preference csv", "select12");
	}

	public void select12(File selection) {
		if (selection == null || !selection.getName().endsWith(".csv")) {
			grade12File = null;
			cp5.getController("grade12").setColor(badButtonColor);
			((Textfield) cp5.getController("grade12Filename")).setText("");
		} else {
			grade12File = selection;
			cp5.getController("grade12").setColor(goodButtonColor);
			((Textfield) cp5.getController("grade12Filename")).setText(grade12File.getName());
		}
	}

	public void sizeExceptions() {
		selectInput("Select the size exceptions csv", "selectExceptions");
	}

	public void selectExceptions(File selection) {
		if (selection == null || !selection.getName().endsWith(".csv")) {
			sizeExceptionsFile = null;
			cp5.getController("sizeExceptions").setColor(badButtonColor);
			((Textfield) cp5.getController("sizeExceptionsFilename")).setText("");
		} else {
			sizeExceptionsFile = selection;
			cp5.getController("sizeExceptions").setColor(goodButtonColor);
			((Textfield) cp5.getController("sizeExceptionsFilename")).setText(sizeExceptionsFile.getName());
		}
	}

}
