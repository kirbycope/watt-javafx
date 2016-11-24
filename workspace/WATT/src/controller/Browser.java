package controller;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import watt.BaseUrl;
import watt.TestCommandHelpers;
import watt.Watt;

public class Browser {

	public static Object scriptResult;

	public static void ExecuteScript(String script) {
		// Reset the script result variable
		scriptResult = null;
		System.out.println("[Browser.java:15] Script |:| " + script);
		try {
			scriptResult = Watt.webEngine.executeScript(script);
		} catch (Exception e) {
			// do nothing
		}
		System.out.println("[Browser.java:21] Result |:| " + scriptResult);
	}

	public void Back() {
		ExecuteScript("history.back()");
		if (Watt.recording) {
			Main.AddStep(true, "Go Back", "goBack", null, null, false);
		}
	}

	public void ClearCookies() {
		// Delete all cookies visible to the current location
		TestCommandHelpers.DeleteAllVisibleCookies();
		// Add test step
		Main.AddStep(true, "Delete all cookies", "deleteAllVisibleCookies", null, null, false);
	}

	public void Desktop() {
		// Close menu
		Menu();
		// Resize the viewport to 960px wide
		Watt.browserStage.setWidth(960);
	}

	public void DesktopWide() {
		// Close menu
		Menu();
		// Resize the viewport to 1280px wide
		Watt.browserStage.setWidth(1280);
	}

	public void Forward() {
		ExecuteScript("history.forward()");
		if (Watt.recording) {
			Main.AddStep(true, "Go Forward", "goForward", null, null, false);
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

	public void Mobile() {
		// Close menu
		Menu();
		// Resize the viewport to 320px wide
		Watt.browserStage.setWidth(320);
	}

	public void MobileWide() {
		// Close menu
		Menu();
		// Resize the viewport to 480px wide
		Watt.browserStage.setWidth(480);
	}

	public void Open() {
		// Get the text of the address bar
		String url = ((TextField) Watt.browserStage.getScene().lookup("#addressBar")).getText();
		// Run script
		ExecuteScript("location.href = '" + url + "'");
		// If recording, add test step
		if (Watt.recording) {
			Main.AddStep(true, "Open: " + BaseUrl.StubUrl(url), "open", BaseUrl.StubUrl(url), null, false);
		}
	}

	public void Reload() {
		ExecuteScript("location.reload()");
		if (Watt.recording) {
			Main.AddStep(true, "Reload", "refresh", null, null, false);
		}
	}

	public void Tablet() {
		// Close menu
		Menu();
		// Resize the viewport to 640px wide
		Watt.browserStage.setWidth(640);
	}

	public void VerifyLocation() {
		// Close menu
		Menu();
		// Get the URL of the current page (result saved in global variable "Broswer.scriptResult", above)
		ExecuteScript("location.href");
		// Add Test Step
		Main.AddStep(true, "Verify Location: " + BaseUrl.StubUrl(scriptResult.toString()), "verifyLocation", BaseUrl.StubUrl(scriptResult.toString()), null, false);
	}

	public void VerifyTitle() {
		// Close menu
		Menu();
		// Get the title of the current page (result saved in global variable "Broswer.scriptResult", above)
		ExecuteScript("document.title");
		// Add Test Step
		Main.AddStep(true, "Verify Title: " + scriptResult.toString(), "verifyTitle", BaseUrl.StubUrl(scriptResult.toString()), null, false);
	}
}
