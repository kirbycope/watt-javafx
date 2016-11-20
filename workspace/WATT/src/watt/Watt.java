package watt;

import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

public class Watt extends Application {

	public static Image applicationIcon;
	public static Stage primaryStage;
	public static boolean playing;
	public static boolean recording;
	public static VBox testStepsContainer;
	public static ObservableList<String> options;
	public static Stage browserStage;
	public static WebEngine webEngine;

	@Override
	public void start(Stage primaryStage) {
		// Load Icon
		applicationIcon = new Image(Watt.class.getResourceAsStream("../assets/icon.png"));
		// Set the main stage
		initMainLayout(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Initializes the primary stage of the application
	 */
	public void initMainLayout(Stage stage){
		primaryStage = stage;
		// Load the FMXL and Scene
		try {
			// Create a new FXML Loader (loads an object hierarchy from an XML document)
			FXMLLoader loader = new FXMLLoader();
			// Give the loader the location of the XML document
			loader.setLocation(Watt.class.getResource("../view/Main.fxml"));
			// Load an object hierarchy from the FXML document into a generic (base class) node
			Parent root = loader.load();
			// Application (Stage) Icon
			primaryStage.getIcons().add(applicationIcon);
			// Application (Stage) Title
			primaryStage.setTitle("WATT");
			// Load the "scene" into the "stage" and set the "stage" with the given dimensions
			primaryStage.setScene(new Scene(root, 320, 480));
			// Set a minimum height for the application (stage) window
			primaryStage.setMinHeight(480);
			// Set a minimum width for the application (stage) window
			primaryStage.setMinWidth(320);
			// Show the application (stage) window
			primaryStage.show();
			// Initialize the test step container
			testStepsContainer = new VBox();
			// Set all the options for the "Command" ComboBox
			LoadCommandOptions();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void LoadCommandOptions() {
		// Get the "Command" ComboBox
		ComboBox command = (ComboBox) primaryStage.getScene().lookup("#test-step-builder-command");
		// Get the "Description" TextField
		TextField description = (TextField) primaryStage.getScene().lookup("#test-step-builder-description");
		// Set the "Command" ComboBox width to match the "Description" TextField width
		command.prefWidthProperty().bind(description.widthProperty());
		// Define the list of options
		options =
			FXCollections.observableArrayList(
				"click"
				,"clickAndWait"
				,"deleteAllVisibleCookies"
				,"goBack"
				,"goForward" // Selenium IDE ✘ | Selenium ✔
				,"open"
				,"openAndWait"
				,"pause"
				,"refresh"
				,"select"
				,"type"
				,"waitForElementPresent"
				,"waitForLocation"
				,"waitForText"
				,"waitForTitle"
				,"verifyElementPresent"
				,"verifyText"
				,"verifyTitle"
				,"verifyLocation"
			);
		// Set options for "Command" ComboBox
		command.setItems(options);
	}

}
