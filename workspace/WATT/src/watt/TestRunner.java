package watt;

import java.lang.reflect.Method;

import controller.Browser;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.TestStep;

public class TestRunner {

	public static int queueIndex;

	public static void CompleteTask(String result) {
		// Style the test step based on the result
		UiHelpers.StyleTestStepByResult(result);
		// Increment the queueIndex
		queueIndex++;
		if (queueIndex == Watt.testStepsContainer.getChildren().size()) {
			// Complete test
			CompleteTest();
		}
		else {
			// Execute the next task
			NextTask();
		}
	}

	public static void CompleteTest() {
		// Set Playing flag
		Watt.playing = false;
		// Reset queue index
		TestRunner.queueIndex = 0;
		// Enable the Add Step button
		UiHelpers.AddStepButtonEnabled(true);
		// Enable the Play button
		UiHelpers.PlayButtonEnabled(true);
		// Enable the Record button
		UiHelpers.RecordButtonEnabled(true);
		// Disable the Stop button
		UiHelpers.StopButtonEnabled(false);
		// Enable the Clear-Results button
		UiHelpers.ClearResultsButtonEnabled(true);


		System.out.println("[TestRunner.java:48] Test Complete!");
	}

	public static void NextTask() {
		if (Watt.playing) {
			// Reset the scriptResult flag
			Browser.scriptResult = null;
			// Define the Test Step
			TestStep testStep = GetCurrentTestStepDetails();
			// Set the method to run based on the Test Step's Command
			Method method = null;
			try {
				method = testStep.getClass().getMethod(testStep.Command);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Execute the method
			try {
				method.invoke(testStep);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static TestStep GetCurrentTestStepDetails() {
		// Create a new Test Step object
		TestStep testStep = new TestStep();
		// Get the Test Step container
		VBox vbox = UiHelpers.GetCurrentTestStepContiner();
		// Get the first row of the Test Step container
		HBox firstRow = (HBox) vbox.getChildren().get(0);
		// Get the Description
		testStep.Description = ((TextField) firstRow.getChildren().get(1)).getText();
		// Get the second row of the Test Step container
		HBox secondRow = (HBox) vbox.getChildren().get(1);
		// Get the Command
		testStep.Command = (String) ((ComboBox) secondRow.getChildren().get(0)).getValue();
		// Get the third row of the Test Step container
		HBox thirdRow = (HBox) vbox.getChildren().get(2);
		// Get the Target
		testStep.Target = ((TextField) thirdRow.getChildren().get(0)).getText();
		// Get the fourth row of the Test Step container
		HBox fourthRow = (HBox) vbox.getChildren().get(3);
		// Get the Value
		testStep.Value =  ((TextField) fourthRow.getChildren().get(0)).getText();
		// Return the defined Test Step
		return testStep;
	}
}
