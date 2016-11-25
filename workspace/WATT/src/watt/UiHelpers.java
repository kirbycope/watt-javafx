package watt;

import java.lang.reflect.Method;
import java.util.Arrays;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.TestStep;

public class UiHelpers {

	public static void AddStepButtonEnabled(boolean value) {
		// Get the Add Step button
		Button btnAddStep = (Button) Watt.primaryStage.getScene().lookup("#test-step-builder-add-step");
		// Handle request
		btnAddStep.setDisable(!value);
	}

	public static void ClearResultsButtonEnabled(boolean value) {
		// Get the Clear-Results Step button
		Label btnAddStep = (Label) Watt.primaryStage.getScene().lookup("#clear-results");
		// Handle request
		btnAddStep.setDisable(!value);
	}

	public static TextField GetBaseUrlField() {
		return (TextField) Watt.primaryStage.getScene().lookup("#base-url");
	}

	public static VBox GetCurrentTestStepContiner() {
		return (VBox) Watt.testStepsContainer.getChildren().get(TestRunner.queueIndex);
	}

	@SuppressWarnings("rawtypes")
	public static TestStep GetTestStepDetails(VBox testStepContainer) {
		// Get the first row of the Test Step container
		HBox firstRow = (HBox) testStepContainer.getChildren().get(0);
		// Get Execute Step
		boolean executeStep = ((CheckBox) firstRow.getChildren().get(0)).isSelected();
		// Get the Description
		 String description = ((TextField) firstRow.getChildren().get(1)).getText();
		// Get the second row of the Test Step container
		HBox secondRow = (HBox) testStepContainer.getChildren().get(1);
		// Get the Command
		String command = (String) ((ComboBox) secondRow.getChildren().get(0)).getValue();
		// Get the third row of the Test Step container
		HBox thirdRow = (HBox) testStepContainer.getChildren().get(2);
		// Get the Target
		String target = ((TextField) thirdRow.getChildren().get(0)).getText();
		// Get the fourth row of the Test Step container
		HBox fourthRow = (HBox) testStepContainer.getChildren().get(3);
		// Get the Value
		String value =  ((TextField) fourthRow.getChildren().get(0)).getText();
		// Get the fifth row of the Test Step container
		HBox fifthRow = (HBox) testStepContainer.getChildren().get(4);
		// Get the Continue on Failure
		boolean continueOnFailure = ((CheckBox) fifthRow.getChildren().get(1)).isSelected();
		// Return the new Test Step object
		return new TestStep(executeStep, description, command, target, value, continueOnFailure);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void LoadCommandOptions(ComboBox command) {
		// Get all the methods of the TestStepCommands class
		Method[] methods = TestStepCommands.class.getDeclaredMethods();
		// Create a variable to hold the method names
		Object[] methodNames = new String[methods.length];
		// Iterate through the list of methods
		//for (Method method : methods) {
		for (int i = 0; i < methods.length; i++) {
			methodNames[i] = methods[i].getName();
		}
		// Array is in random order, sort it
		Arrays.sort(methodNames);
		// Add the method name to the list of options
		//command.getItems().add(method.getName());
		command.getItems().addAll(methodNames);
	}

 	public static void PlayButtonEnabled(boolean value) {
		// Get the Play button
		Label playLabel = (Label) Watt.primaryStage.getScene().lookup("#play");
		// Handle request
		playLabel.setDisable(!value);
	}

	public static void RecordButtonEnabled(boolean value) {
		// Get the Record button
		Label recordLabel = (Label) Watt.primaryStage.getScene().lookup("#record");
		// Handle request
		recordLabel.setDisable(!value);
	}

	public static void ResetAllTestStepStyles() {
		// Get all Test Step containers
		ObservableList<Node> testSteps = Watt.testStepsContainer.getChildren();
		for (Node testStep : testSteps) {
			ResetTestStepStyle((VBox) testStep);
		}
	}

	public static ObservableList<String> ResetTestStepStyle(VBox testStepContainer) {
		// Get the first row of the Test Step container
		HBox firstRow = (HBox) testStepContainer.getChildren().get(0);
		// Get the styleClass of the first row
		ObservableList<String> styleClasses = firstRow.getStyleClass();
		// Remove all know style classes
		styleClasses.remove("test-step-container-current");
		styleClasses.remove("test-step-container-failed");
		styleClasses.remove("test-step-container-passed");
		styleClasses.remove("test-step-container-skipped");
		// Return the styles
		return styleClasses;
	}

	public static void ShowToast(String text) {
		// Get the toaster
		HBox toaster = (HBox) Watt.primaryStage.getScene().lookup("#toaster");
		// Show the toaster
		toaster.setManaged(true);
		toaster.setVisible(true);
		// Get the toast
		Label toast = (Label) Watt.primaryStage.getScene().lookup("#toast");
		// Set toast's text
		toast.setText(text);
		// Set a background Task to close the toaster after 3.5 seconds
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// Wait 3.5 seconds
				Thread.sleep(3500);
				// Hide the toaster
				toaster.setManaged(false);
				toaster.setVisible(false);
				return null;
			}
		};
		// Start the Task
		new Thread(task).start();
	}

	public static void StopButtonEnabled(boolean value) {
		// Get the Stop button
		Label stopLabel = (Label) Watt.primaryStage.getScene().lookup("#stop");
		// Handle request
		stopLabel.setDisable(!value);
	}

	public static void StyleTestStepByResult(String result) {
		VBox testStepContainer = GetCurrentTestStepContiner();
		// Get/Reset the Test Steps's style
		ObservableList<String> styleClasses = ResetTestStepStyle(testStepContainer);
		// Handle the result
		if (result.equals("pass")) {
			// Update the styleClass of the firstRow
			styleClasses.add("test-step-container-passed");
		}
		else if (result.equals("fail")){
			// Update the styleClass of the firstRow
			styleClasses.add("test-step-container-failed");
		}
		else if (result.equals("skip")){
			// Update the styleClass of the firstRow
			styleClasses.add("test-step-container-skipped");
		}
		else {
			throw new UnsupportedOperationException("Result: " + result);
		}
	}

	public static void SetRecordButtonStyle(boolean recording) {
		// Get the recording label
		Label recordingLabel = (Label) Watt.primaryStage.getScene().lookup("#record");
		// Get the styleClass of the Recording label
		ObservableList<String> styleClasses = recordingLabel.getStyleClass();
		if (recording) {
			// Update the styleClass of the recording label
			styleClasses.remove("test-step-header-button");
			styleClasses.add("test-step-header-button-recording");
			// Update the Tooltip
			recordingLabel.getTooltip().setText("Stop Recording");
		}
		else {
			// Update the styleClass of the recording label
			styleClasses.remove("test-step-header-button-recording");
			styleClasses.add("test-step-header-button");
			// Update the Tooltip
			recordingLabel.getTooltip().setText("Record");
		}
	}

	public static void StyleCurrentTestStep() {
		// Get the current Test Step container
		VBox testStepContainer = UiHelpers.GetCurrentTestStepContiner();
		// Get/Reset the Test Steps's style
		ObservableList<String> styleClasses = ResetTestStepStyle(testStepContainer);
		// Update the styleClass of the firstRow
		styleClasses.add("test-step-container-current");
	}
}
