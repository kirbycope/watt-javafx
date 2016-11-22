package watt;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
		styleClasses.remove("test-step-container-passed");
		styleClasses.remove("test-step-container-skipped");
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

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// Wait
				Thread.sleep(3500);
				// Hide the toaster
				toaster.setManaged(false);
				toaster.setVisible(false);
				return null;
			}
		};
		new Thread(task).start();
	}

	public static void StopButtonEnabled(boolean value) {
		// Get the Stop button
		Label stopLabel = (Label) Watt.primaryStage.getScene().lookup("#stop");
		// Handle request
		stopLabel.setDisable(!value);
	}

	public static void StyleTestStepByResult(String result) {
		VBox vbox = GetCurrentTestStepContiner();
		// Get/Reset the Test Steps's style
		ObservableList<String> styleClasses = ResetTestStepStyle(vbox);
		// Handle the result
		if (result.equals("pass")) {
			// Update the styleClass of the firstRow
			styleClasses.add("test-step-container-passed");
		}
		else if (result.equals("fail")){
			// Update the styleClass of the firstRow
			styleClasses.add("test-step-container-failed");
		}
		else {
			// Update the styleClass of the firstRow
			styleClasses.add("test-step-container-skipped");
		}
	}

	public static void ToggleRecordButton() {
		// Get the recording label
		Label recordingLabel = (Label) Watt.primaryStage.getScene().lookup("#record");
		// Get the styleClass of the Recording label
		ObservableList<String> styleClasses = recordingLabel.getStyleClass();
		// Handle the current state
		if (Watt.recording) {
			// Stop Recording
			Watt.recording = false;
			// Update the styleClass of the recording label
			styleClasses.remove("test-step-header-button-recording");
			styleClasses.add("test-step-header-button");
			// Update the Tooltip
			recordingLabel.getTooltip().setText("Record");
		}
		else {
			// Start Recording
			Watt.recording = true;
			// Update the styleClass of the recording label
			styleClasses.remove("test-step-header-button");
			styleClasses.add("test-step-header-button-recording");
			// Update the Tooltip
			recordingLabel.getTooltip().setText("Stop Recording");
		}
	}

}
