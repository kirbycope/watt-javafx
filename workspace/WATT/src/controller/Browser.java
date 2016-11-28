package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import netscape.javascript.JSObject;
import watt.BaseUrl;
import watt.Callback;
import watt.TestCommandHelpers;
import watt.TestRunner;
import watt.UiHelpers;
import watt.Watt;

public class Browser {

	public static Object scriptResult;
	public static JSObject window;

	public static void AddBrowserEventListeners() {
		if (Watt.browserStage != null) {
			// Set the On-Close action
			Watt.browserStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent windowEvent) {
		        	  Watt.browserStage = null;
		        	  Watt.webEngine = null;
		        	  // Set Highlighting flag;
		        	  Watt.highlighting = false;
		        	  // Change the highlighting button style
		        	  UiHelpers.SetRecordButtonStyle(Watt.highlighting);
		        	  // Set Recording flag
		        	  Watt.recording = false;
		        	  // Change recording button style
		        	  UiHelpers.SetRecordButtonStyle(Watt.recording);
		          }
			});
		}
		// Add a Listener to the WebEngine
		Watt.webEngine.getLoadWorker().stateProperty().addListener(
			// Listen for a "State" change
			new ChangeListener<State>() {
				// Define the method to run
				@SuppressWarnings("rawtypes")
				public void changed(ObservableValue ov, State oldState, State newState) {
					// If the "State" has changed to "SUCCEEDED"
					if (newState == State.SUCCEEDED) {
						// Update the Browser window
						if (Watt.browserStage != null) {
							// Get the Base Address test field from the UI
							TextField baseUrl = (TextField) Watt.browserStage.getScene().lookup("#addressBar");
							// Set the Base Address text
							baseUrl.setText(Watt.webEngine.getLocation());
							// Get the "window" variable of the Browser's DOM
							Browser.ExecuteScript("window");
							// Save the JavaScript "window" object
							window = (JSObject) Browser.scriptResult;
							// Map "app" in JS to a class in Java
	    					window.setMember("app", new Callback());
	    					// Add the Context Menu
	    					InjectContextMenuScripts();
						}
						// Complete any currently executing test step
						if (Watt.playing) {
							TestRunner.CompleteTask("pass");
						}
						// If highlighting, then add the Highlighting script(s)
						if (Watt.highlighting) {
							InjectHighlightingScripts();
						}
						// If recording, then add the Recording script(s)
						if (Watt.recording) {
							InjectRecordingScripts();
						}
                    }
				}
            }
		);
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

	public static void InjectContextMenuScripts() {
		// Inject style ContextMenu.css
		InjectCss( Watt.SourceFileToString("/assets/css/ContextMenu.css") );
		// Inject Script ContextMenu.js
		InjectJs( Watt.SourceFileToString("/assets/js/ContextMenu.js") );
		// Turn on the Context Menu
		Browser.ExecuteScript("document.addEventListener('contextmenu', contextMenuHandler, true);");
	}

	public static void InjectCss(String style) {org.w3c.dom.Document doc = Watt.webEngine.getDocument();
	org.w3c.dom.Node addStyle = doc.createElement("style");
	addStyle.setTextContent(style);
	org.w3c.dom.Element element = doc.getDocumentElement();
    element.appendChild(addStyle);
}

	public static void InjectJs(String script) {org.w3c.dom.Document doc = Watt.webEngine.getDocument();
		org.w3c.dom.Node scriptElement = doc.createElement("script");
		scriptElement.setTextContent(script);
		org.w3c.dom.Element element = doc.getDocumentElement();
	    element.appendChild(scriptElement);
	}

	public static void InjectHighlightingScripts() {
		// Inject style HightlightMouseoverElement.css
		InjectCss( Watt.SourceFileToString("/assets/css/HightlightMouseoverElement.css") );
		// Inject script HightlightMouseoverElement.js
		InjectJs( Watt.SourceFileToString("/assets/js/HightlightMouseoverElement.js") );
		// Turn on the Element Highlighter
		Browser.ExecuteScript("document.addEventListener('mousemove', hoverHandler, true);");
	}

	public static void InjectRecordingScripts() {
		// Inject script OnClick.js
		InjectJs( Watt.SourceFileToString("/assets/js/OnClick.js") );
		// Turn on the OnClick
		Browser.ExecuteScript("document.addEventListener('click', clickHandler, true);");
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

	public static void StopHighlightingScripts() {
		// Remove hover highlighter style
		Browser.ExecuteScript("if (prevElement!= null) {prevElement.classList.remove('mouseOn');}");
		// Remove hover highlighter handler
		Browser.ExecuteScript("document.removeEventListener('mousemove', hoverHandler, true);");
	}

	public static void StopRecordingScripts() {
		// Remove click handler
		Browser.ExecuteScript("document.removeEventListener('click', clickHandler, true);");
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
