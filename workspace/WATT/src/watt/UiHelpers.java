package watt;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UiHelpers {

	public static void AddStepButtonEnabled(boolean value) {
		// Get the Add Step button
		Button btnAddStep = (Button) Watt.primaryStage.getScene().lookup("#test-step-builder-add-step");
		// Handle request
		btnAddStep.setDisable(!value);
	}

	public static TextField GetBaseUrlField() {
		return (TextField) Watt.primaryStage.getScene().lookup("#base-url");
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

	public static void StopButtonEnabled(boolean value) {
		// Get the Stop button
		Label stopLabel = (Label) Watt.primaryStage.getScene().lookup("#stop");
		// Handle request
		stopLabel.setDisable(!value);
	}
}
