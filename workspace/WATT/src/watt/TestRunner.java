package watt;

import java.lang.reflect.Method;
import java.util.Map;

import controller.Browser;
import javafx.scene.layout.VBox;
import model.TestStep;

public class TestRunner {

	public static int queueIndex;
	public static boolean continueOnFailure;
	public static Map<String, String> store;

 	public static void CompleteTask(String result) {
		// Log Result
		Log.WriteLine("RESULT: " + result);
		Log.WriteLine("");
		// Style the test step based on the result
		UiHelpers.StyleTestStepByResult(result);
		// Increment the queueIndex
		queueIndex++;
		// If there are no more steps to run, then complete the test run
		if (queueIndex == Watt.testStepsContainer.getChildren().size()) {
			// Complete test
			CompleteTest();
		}
		else {
			// Test Step was skipped
			if (result.equals("skip")) {
				// Execute the next task
				NextTask();
			}
			// Test Step was stopped
			else if (result.equals("stop")) {
				// Complete test
				CompleteTest();
			}
			// Test Step timed out
			else if (result.equals("timeout")) {
				// Complete test
				CompleteTest();
			}
			// Test Step failed
			else if (result.equals("fail")) {
				// If the Continue on Failure flag is set to true
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
		// Enable the Highlight button
		UiHelpers.HighlightButtonEnabled(true);
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
		// Finalize the log file
		Log.FinalizeLogFile();
		// Open the log file
		//Log.OpenLogFile(); // DEBUGGING
		// Alert the user
		UiHelpers.ShowToast("Test Run Complete!");
	}

	private static TestStep GetCurrentTestStepDetails() {
		// Get the current Test Step container
		VBox testStepContainer = UiHelpers.GetCurrentTestStepContiner();
		// Return the new Test Step object
		return UiHelpers.GetTestStepDetails(testStepContainer);
	}

	public static void NextTask() {
		if (Watt.playing) {
			// Reset the scriptResult flag
			Browser.scriptResult = null;
			// Style the Test Step
			UiHelpers.StyleCurrentTestStep();
			// Define the Test Step
			TestStep testStep = GetCurrentTestStepDetails();
			// Log Current Step
			Log.WriteLine("Test Step: " + (queueIndex + 1));
			Log.WriteLine("  [Description] " + testStep.Description);
			Log.WriteLine("  [Command] " + testStep.Command);
			Log.WriteLine("  [Target] " + testStep.Target);
			Log.WriteLine("  [Value] " + testStep.Value);
			Log.WriteLine("  [Execute Step] " + testStep.ExecuteStep);
			Log.WriteLine("  [Continue on Failure] " + testStep.ContinueOnFailure);
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
				catch (Exception e) {
					// Fail Test Step
					CompleteTask("fail");
				}
			}
			else {
				CompleteTask("skip");
			}

		}
	}
}
