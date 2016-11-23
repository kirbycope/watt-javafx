package controller;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import watt.BaseUrl;
import watt.Watt;

public class Browser {

	public static Object scriptResult;

	public static void ExecuteScript(String script) {
		// Reset the script result variable
		scriptResult = null;
		System.out.println("[Browser.java:15] Script |:| " + script);
		scriptResult = Watt.webEngine.executeScript(script);
		System.out.println("[Browser.java:17] Result |:| " + scriptResult);
	}

	public void Back() {
		ExecuteScript("history.back()");
		if (Watt.recording) {
			Main.AddStep("Go Back", "goBack", null, null);
		}
	}

	public void Forward() {
		ExecuteScript("history.forward()");
		if (Watt.recording) {
			Main.AddStep("Go Forward", "goForward", null, null);
		}
	}

	public void Menu() {
		// Get the menu container
		VBox menuContainer = (VBox) Watt.browserStage.getScene().lookup("#menu-container");
		// Handle the current state
		if (menuContainer.isVisible()){
			// Hide the menu
			menuContainer.setManaged(false);
			menuContainer.setVisible(false);
		}
		else {
			// Show the menu
			menuContainer.setManaged(true);
			menuContainer.setVisible(true);
		}
	}

	public void Open() {
		TextField addressBar = (TextField) Watt.browserStage.getScene().lookup("#addressBar");
			String url = addressBar.getText();
			ExecuteScript("location.href = '" + url + "'");
		if (Watt.recording) {
			Main.AddStep("Open: " + BaseUrl.StubUrl(url), "open", BaseUrl.StubUrl(url), null);
		}
	}

	public void Reload() {
		ExecuteScript("location.reload()");
		if (Watt.recording) {
			Main.AddStep("Reload", "refresh", null, null);
		}
	}
}
