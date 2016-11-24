package watt;

import java.lang.reflect.Method;

import controller.Browser;
import javafx.scene.layout.VBox;
import model.TestStep;

public class TestRunner {

	public static int queueIndex;
	public static boolean continueOnFailure;

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
			// If the test step did not pass...
			if (result.equals("pass") == false) {
				if (continueOnFailure) {
					// Execute the next task
					NextTask();
				}
				else {
					// Complete test
					CompleteTest();
				}
			}
			else {
				// Execute the next task
				NextTask();
			}
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
		// Dispose of the WebView if Browser stage is closed
		if (Watt.browserStage == null) {
			Watt.webEngine = null;
		}

		System.out.println("[TestRunner.java:48] Test Complete!");
	}

	public static void NextTask() {
		if (Watt.playing) {
			// Reset the scriptResult flag
			Browser.scriptResult = null;
			// Style the Test Step
			UiHelpers.StyleCurrentTestStep();
			// Define the Test Step
			TestStep testStep = GetCurrentTestStepDetails();
			// Check the Test Step should execute
			if (testStep.ExecuteStep) {
				// Set the Continue on Failure flag
				continueOnFailure = testStep.ContinueOnFailure;
				// Set the method to run based on the Test Step's Command
				Method method = null;
				try {
					method = testStep.getClass().getMethod(testStep.Command);
				}
				catch (Exception e) { e.printStackTrace(); }
				// Execute the method
				try {
					method.invoke(testStep);
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			else {
				CompleteTask("skip");
			}

		}
	}

	private static TestStep GetCurrentTestStepDetails() {
		// Get the current Test Step container
		VBox testStepContainer = UiHelpers.GetCurrentTestStepContiner();
		// Return the new Test Step object
		return UiHelpers.GetTestStepDetails(testStepContainer);
	}
}
