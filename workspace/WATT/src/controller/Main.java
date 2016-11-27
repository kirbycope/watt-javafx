package controller;

import java.io.IOException;
import com.sun.javafx.stage.StageHelper;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.TestStep;
import watt.TestCase;
import watt.TestRunner;
import watt.UiHelpers;
import watt.Watt;

@SuppressWarnings({"rawtypes", "unchecked", "restriction"})
public class Main {

	public void AddStep() {
		// Get the "Description"
		TextField description = (TextField) Watt.primaryStage.getScene().lookup("#test-step-builder-description");
		// Get the "Command"
		ComboBox command = (ComboBox) Watt.primaryStage.getScene().lookup("#test-step-builder-command");
		// Get the "Target"
		TextField target = (TextField) Watt.primaryStage.getScene().lookup("#test-step-builder-target");
		// Get the "Value"
		TextField value = (TextField) Watt.primaryStage.getScene().lookup("#test-step-builder-value");
		// Add step to test steps pane
		AddStep(true, description.getText(), command.getValue(), target.getText(), value.getText(), false);
	}

	public static void AddStep(boolean executeStep, String description, Object command, String target, String value, boolean continueOnFailure) {

		// Create a new Test Step Container
		VBox testStep = new VBox();
		testStep.getStyleClass().add("test-step-container-collapsed");

		// Create the first row
		HBox firstRow = new HBox();
		firstRow.setAlignment(Pos.CENTER_LEFT);
		// Create the "Execute Step" CheckBox
		CheckBox cbExecuteStep = new CheckBox();
		cbExecuteStep.setSelected(executeStep);
		cbExecuteStep.setTooltip(new Tooltip("Execute Step"));
		// Create the "Description" TextField
		TextField tfDescription = new TextField();
		HBox.setHgrow(tfDescription, Priority.ALWAYS);
		tfDescription.setPromptText("Description");
		tfDescription.getStyleClass().add("form-control");
		tfDescription.setText(description);
		// Create "Expand Test Step" Button
		Button bExpand = new Button("➕");
		bExpand.getStyleClass().addAll("btn", "btn-default");
		bExpand.setTooltip(new Tooltip("Expand Test Step"));
		// Set on click action
		bExpand.setOnMouseClicked(
			new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					ExpandCollapse(mouseEvent);
				}
			}
		);
		// Create "Remove Test Step" Button
		Button bRemove = new Button("❌");
		bRemove.getStyleClass().addAll("btn", "btn-danger");
		bRemove.setTooltip(new Tooltip("Remove Test Step"));
		// Set on click action
		bRemove.setOnMouseClicked(
			new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					RemoveStep(mouseEvent);
				}
			}
		);
		// Add elements to first row
		firstRow.getChildren().addAll(cbExecuteStep, tfDescription, bExpand, bRemove);

		// Create the second row
		HBox secondRow = new HBox();
		secondRow.setPadding(new Insets(0, 0, 0, 21));
		// Create the "Command" ComboBox
		ComboBox cbCommand = new ComboBox();
		cbCommand.getStyleClass().addAll("combobox", "form-control");
		cbCommand.prefWidthProperty().bind(firstRow.widthProperty());
		cbCommand.setPromptText("Command");
		// Define the list of options
		UiHelpers.LoadCommandOptions(cbCommand);
		// Select the "Command"
		cbCommand.setValue(command);
		// Add element to second row
		secondRow.getChildren().add(cbCommand);

		// Create the third row
		HBox thirdRow = new HBox();
		thirdRow.setPadding(new Insets(0, 0, 0, 21));
		// Create the "Target" TextField
		TextField tfTarget = new TextField();
		HBox.setHgrow(tfTarget, Priority.ALWAYS);
		tfTarget.setPromptText("Target");
		tfTarget.setText(target);
		// Add element to third row
		thirdRow.getChildren().add(tfTarget);

		// Create the fourth row
		HBox fourthRow = new HBox();
		fourthRow.setPadding(new Insets(0, 0, 0, 21));
		// Create the "Value" TextField
		TextField tfValue = new TextField();
		HBox.setHgrow(tfValue, Priority.ALWAYS);
		tfValue.setPromptText("Value");
		tfValue.setText(value);
		// Add element to fourth row
		fourthRow.getChildren().add(tfValue);

		// Create the fifth row
		HBox fifthRow = new HBox();
		fifthRow.setPadding(new Insets(0, 0, 0, 21));
		// Create the "Continue on Failure" label
		Label lContinueOnFailure = new Label("Continue on Failure: ");
		// Create the "Continue on Failure" CheckBox
		CheckBox cbContinueOnFailure = new CheckBox();
		cbContinueOnFailure.setSelected(continueOnFailure);
		// <Pane HBox.hgrow="ALWAYS" />
		Pane fillerPane = new Pane();
		HBox.setHgrow(fillerPane, Priority.ALWAYS);
		// Create the "Duplicate Step" label
		Label lDuplicateStep = new Label("Duplicate Step");
		lDuplicateStep.getStyleClass().add("duplicate-test-step");
		// Set on click action
		lDuplicateStep.setOnMouseClicked(
			new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					DuplicateStep(mouseEvent);
				}
			}
		);
		// Add elements to fifth row
		fifthRow.getChildren().addAll(lContinueOnFailure, cbContinueOnFailure, fillerPane, lDuplicateStep);

		// Hide Rows 2-5
		secondRow.setVisible(false);
		secondRow.setManaged(false);
		thirdRow.setVisible(false);
		thirdRow.setManaged(false);
		fourthRow.setVisible(false);
		fourthRow.setManaged(false);
		fifthRow.setVisible(false);
		fifthRow.setManaged(false);

		//Add Rows to Container
		testStep.getChildren().add(firstRow);
		testStep.getChildren().add(secondRow);
		testStep.getChildren().add(thirdRow);
		testStep.getChildren().add(fourthRow);
		testStep.getChildren().add(fifthRow);

		// Add new Test Step to the Test Steps Container
		Watt.testStepsContainer.getChildren().add(testStep);
		// Re-add Test Step Container to the ScrollPane
		ScrollPane testStepsScrollPane = (ScrollPane) Watt.primaryStage.getScene().lookup("#test-step-scrollpane");
		testStepsScrollPane.setContent(Watt.testStepsContainer);

		// Clear form
		UiHelpers.ClearTestStepBuilder();
	}

	private boolean BrowserIsOpen() {
		// Get all observable windows
		ObservableList<Stage> allWindows = StageHelper.getStages();
		if (allWindows.size() > 1) {
			return true;
		}
		else {
			Watt.browserStage = null;
			return false;
		}
	}

	public void ClearForm() {
		UiHelpers.ClearTestStepBuilder();
	}

	public void ClearResults() {
		// Disable the Clear-Results button
		UiHelpers.ClearResultsButtonEnabled(false);
		// Remove pass/fail styles on all Test Steps
		UiHelpers.ResetAllTestStepStyles();

	}

	private static void CollapseTestStep(Button source, ObservableList<Node> rows) {
		// Change the expand/collapse Button text to ➕
		source.setText("➕");
		// Change the Tooltip
		source.getTooltip().setText("Expand All Steps");
		// Make each row (except the first) hidden
		for (int i = 0; i < rows.size(); i++) {
			// Handle the first row
			if (i == 0) {
				// Change the Test Step container class
				VBox testStepContainer = (VBox) rows.get(i).getParent();
				testStepContainer.getStyleClass().remove("test-step-container-collapsed");
				testStepContainer.getStyleClass().remove("test-step-container-expanded");
				testStepContainer.getStyleClass().add("test-step-container-collapsed");
				// Get the Expand/Collapse Button
				Button expandCollpase = (Button) ((HBox) rows.get(i)).getChildren().get(2);
				// Change the expand/collapse Button text to ➕
				expandCollpase.setText("➕");
				// Change the Tooltip
				expandCollpase.getTooltip().setText("Expand Test Step");
			}
			else {
				rows.get(i).setManaged(false);
				rows.get(i).setVisible(false);
			}
		}
	}

	public void DeselectSelectAll(MouseEvent mouseEvent) {
		// Get the deselect-select-all Checkbox
		CheckBox source = (CheckBox) mouseEvent.getSource();
		// Change the CheckBox's Tooltip
		if (source.getTooltip().getText().equals("Deselect All Steps")) {
			source.getTooltip().setText("Select All Steps");
		}
		else {
			source.getTooltip().setText("Deselect All Steps");
		}
		// Get all Test Steps
		ObservableList<Node> testStepsContainer = Watt.testStepsContainer.getChildren();
		// Check that there are steps to deselect/select
		if (testStepsContainer.size() > 0) {
			// Get the Deselect-Select-All CheckBox
			CheckBox checkBox = (CheckBox) Watt.primaryStage.getScene().lookup("#deselect-select-all");
			// Handle each Test Step container in the Test Steps container
			for (Node testStepContainer : testStepsContainer) {
				// Get the first row (HBox) of the Test Step container (VBox)
				HBox firstRow = (HBox) ((VBox) testStepContainer).getChildren().get(0);
				// Get the CheckBox for the step (it is the first element in the first row)
				CheckBox stepCheckBox = (CheckBox) firstRow.getChildren().get(0);
				// Deselect or Select the CheckBox
				if (checkBox.isSelected()) {
					stepCheckBox.setSelected(true);
				}
				else {
					stepCheckBox.setSelected(false);
				}
			}
		}
	}

	public static void DuplicateStep(MouseEvent mouseEvent) {
		Label source = (Label) mouseEvent.getSource();
		HBox parent = (HBox) source.getParent();
		VBox grandParent = (VBox) parent.getParent();
		TestStep testStep = UiHelpers.GetTestStepDetails(grandParent);
		Main.AddStep(testStep.ExecuteStep, testStep.Description, testStep.Command, testStep.Target, testStep.Value, testStep.ContinueOnFailure);
	}

	public static void ExpandCollapse(MouseEvent mouseEvent) {
		Button source = (Button) mouseEvent.getSource();
		HBox parent = (HBox) source.getParent();
		VBox grandParent = (VBox) parent.getParent();
		// Get the Expand/Collapse Button initial Tooltip value
		String initialTooltipText = source.getTooltip().getText();
		// Expand or Collapse the rows
		if (initialTooltipText.equals("Expand Test Step")){
			// Make each row visible
			ExpandTestStep(source, grandParent.getChildren());
		}
		else {
			// Make each row (except the first) hidden
			CollapseTestStep(source, grandParent.getChildren());
		}

	}

	public void ExpandCollapseAll(){
		// Get all Test Steps
		ObservableList<Node> testStepsContainer = Watt.testStepsContainer.getChildren();
		// Check that there are steps to expand or collapse
		if (testStepsContainer.size() > 0) {
			// Get the Expand/Collapse button
			Button btnExpandCollapse = (Button) Watt.primaryStage.getScene().lookup("#expand-collapse-all");
			// Get the ExpandAll/CollapseAll Button initial Tooltip value
			String initialTooltipText = btnExpandCollapse.getTooltip().getText();
			// Expand or Collapse the rows
			if (initialTooltipText.equals("Expand All Steps")){
				// Update Tooltip
				btnExpandCollapse.getTooltip().setText("Collapse All Steps");
				// Handle each Test Step container in the Test Steps container
				for (Node testStepContainer : testStepsContainer) {
					// Expand the Test Step(s) in the container
					ExpandTestStep(btnExpandCollapse, ((VBox) testStepContainer).getChildren());
				}
			}
			else {
				// Update Tooltip
				btnExpandCollapse.getTooltip().setText("Expand All Steps");
				// Handle each Test Step container in the Test Steps container
				for (Node testStepContainer : testStepsContainer) {
					// Collapse the Test Step(s) in the container
					CollapseTestStep(btnExpandCollapse, ((VBox) testStepContainer).getChildren());
				}
			}
		}
	}

	private static void ExpandTestStep(Button source, ObservableList<Node> rows) {
		// Change the expand/collapse Button text to ➖
		source.setText("➖");
		// Update Tooltip
		source.getTooltip().setText("Collapse All Steps");
		// Make each row visible
		for (int i = 0; i < rows.size(); i++) {
			// Handle the first row
			if (i == 0){
				// Change the Test Step container class
				VBox testStepContainer = (VBox) rows.get(i).getParent();
				testStepContainer.getStyleClass().remove("test-step-container-collapsed");
				testStepContainer.getStyleClass().remove("test-step-container-expanded");
				testStepContainer.getStyleClass().add("test-step-container-expanded");
				// Get the Expand/Collapse Button
				Button expandCollpase = (Button) ((HBox) rows.get(i)).getChildren().get(2);
				// Change the expand/collapse Button text to ➖
				expandCollpase.setText("➖");
				// Change the Tooltip
				expandCollpase.getTooltip().setText("Collapse Test Step");
			}
			rows.get(i).setManaged(true);
			rows.get(i).setVisible(true);
		}
	}

	public void NewTestCase() {
		TestCase.New();
	}

	public void OpenBrowserWindow() {
		if (BrowserIsOpen() == false){
			try {
				// Create a new FXML Loader (loads an object hierarchy from an XML document)
		 		FXMLLoader loader = new FXMLLoader();
		 		// Give the loader the location of the XML document
		 		loader.setLocation(Watt.class.getResource("../view/Browser.fxml"));
		 		// Load an object hierarchy from the FXML document into a generic (base class) node
		 		Parent root =  loader.load();
		 		// Create a new application (Stage) window
		        Watt.browserStage = new Stage();
		        // Application (Stage) Icon
		        Watt.browserStage.getIcons().add(Watt.applicationIcon);
				// Application (Stage) Title
		        Watt.browserStage.setTitle("WATT - WebView");
				// Set up the scene with a given size
				Scene scene = new Scene(root, 640, 480);
				// Load the "scene" into the "stage" and set the "stage" with the given dimensions
				Watt.browserStage.setScene(scene);
		        // Set a minimum height for the application (stage) window
				Watt.browserStage.setMinHeight(480);
				// Set a minimum width for the application (stage) window
				Watt.browserStage.setMinWidth(320);
				// Show the application (stage) window
				Watt.browserStage.show();
	        	// Open the window to the right of the primary stage
				Watt.browserStage.setX(Watt.primaryStage.getX() + Watt.primaryStage.getWidth());
				Watt.browserStage.setY(Watt.primaryStage.getY());
		        // Get the Web View
		        WebView webView = (WebView) scene.lookup("#webview");
		        // Get the Web Engine
		        Watt.webEngine = webView.getEngine();
		        // Add an event listener(s)
		        Browser.AddBrowserEventListeners();
		        // DEBUGGING: Load a site!
		        Watt.webEngine.load("https://www.google.com/");
	 		}
		 	catch (IOException e) {
		 		e.printStackTrace();
		 	}
		}
	}

	public void OpenTestCase() {
		TestCase.Open();
	}

	public void Play() {
		if (Watt.testStepsContainer.getChildren().size() > 0) {
			if (Watt.browserStage == null) {
				// Make a new Web View
				WebView webView = new WebView();
				// Set the Web Engine
				Watt.webEngine = webView.getEngine();
			}
			// Add an event listener(s)
			Browser.AddBrowserEventListeners();
			// Stop Recording
			Browser.StopRecordingScripts();
			// Disable Record button
			UiHelpers.RecordButtonEnabled(false);
			// Disable Play button
			UiHelpers.PlayButtonEnabled(false);
			// Disable the Add Step button
			UiHelpers.AddStepButtonEnabled(false);
			// Enable Stop button
			UiHelpers.StopButtonEnabled(true);
			// Set Playing flag
			Watt.playing = true;
			// Set/Reset queueIndex for the Test Runner
			TestRunner.queueIndex = 0;
			// Start the Test Runner
			TestRunner.NextTask();
		}
		else {
			UiHelpers.ShowToast("No Test Steps to Play");
		}
	}

	public void Record() {
		if (Watt.browserStage != null) {
			if (Watt.recording) {
				// Set Recording flag
				Watt.recording = false;
				// Change recording button style
				UiHelpers.SetRecordButtonStyle(Watt.recording);
				// Stop recording
				Browser.StopRecordingScripts();
			}
			else {
				// Set Recording flag
				Watt.recording = true;
				// Change recording button style
				UiHelpers.SetRecordButtonStyle(Watt.recording);
				// Start recording
				Browser.InjectRecordingScripts();
			}
		}
		else {
			UiHelpers.ShowToast("Browser not open!");
		}
	}

	public void RemoveAll() {
		// Get the Expand/Collapse button
		Button btnExpandCollapse = (Button) Watt.primaryStage.getScene().lookup("#expand-collapse-all");
		if (btnExpandCollapse.getTooltip().getText().equals("Collapse All Steps")) {
			// Reset Expand/Collapse button to its "collapsed" state
			ExpandCollapseAll();
		}
		// Clear any Test Step(s) in the container
		Watt.testStepsContainer.getChildren().clear();
	}

	public static void RemoveStep(MouseEvent mouseEvent) {
		Button source = (Button) mouseEvent.getSource();
		HBox parent = (HBox) source.getParent();
		VBox grandParent = (VBox) parent.getParent();
		VBox greatGrandParent = (VBox) grandParent.getParent();
		greatGrandParent.getChildren().remove(grandParent);
	}

	public void SaveTestCase() {
		TestCase.Save();
	}

	public void Stop() {
		// Enable the Add Step button
		UiHelpers.AddStepButtonEnabled(true);
		// Enable the Play button
		UiHelpers.PlayButtonEnabled(true);
		// Enable the Record button
		UiHelpers.RecordButtonEnabled(true);
		// Disable the Stop button
		UiHelpers.StopButtonEnabled(false);
		// Stop Playing
		Watt.playing = false;
	}
}
