package model;

import controller.Browser;
import watt.BaseUrl;
import watt.TestRunner;

public class TestStep {
	public String Description;
	public String Command;
	public String Target;
	public String Value;

	// Constructor
	public TestStep() {
		Description = "";
		Command = "";
		Target = "";
		Value = "";
	}

	public void click() {
		// Get the element selector
		String selector = GetSelector();
		// Click the element
		Browser.ExecuteScript(selector + ".click()");
		// Complete Task
		TestRunner.CompleteTask();
	}

	public void goBack() {
		// Go Back
		Browser.ExecuteScript("history.back()");
		// Complete Task
		TestRunner.CompleteTask();
	}

	public void goBackAndWait() {
		// Go Back
		Browser.ExecuteScript("history.back()");
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	public void goForward() {
		// Go Forward
		Browser.ExecuteScript("history.forward()");
		// Complete Task
		TestRunner.CompleteTask();
	}

	public void goForwardAndWait() {
		// Go Forward
		Browser.ExecuteScript("history.forward()");
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	// NOTE: The "open" command waits for the page to load before proceeding, ie. the "AndWait" suffix is implicit.
	public void open() {
		// Get fully qualified URL to open
		String url = BaseUrl.FullUrl(this.Target);
		// Open the URL in the WebView
		Browser.ExecuteScript("location.href='" + url + "'");
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	private String GetSelector() {
		// Initialize the selector
		String selector = null;
		// Get the element locator
		String elementLocator = this.Target;
		if (this.Target.startsWith("css=")) {
			// Remove css=
			elementLocator = elementLocator.substring(4);
			// Create the selector
			selector = "document.querySelector('" + elementLocator + "')";
		}
		else if (this.Target.startsWith("document.")) {
			// Create the selector
			selector = elementLocator;
		}
		else if (this.Target.startsWith("dom=")) {
			// Remove dom=
			elementLocator = elementLocator.substring(4);
			// Create the selector
			selector = elementLocator;
		}
		else if (this.Target.startsWith("id=")) {
			// Remove id=
			elementLocator = elementLocator.substring(3);
			// Create the selector
			selector = "document.getElementById('" + elementLocator + "')";
		}
		else if (this.Target.startsWith("link=")) {
			// Remove link=
			elementLocator = elementLocator.substring(5);
			// TODO
		}
		else if (this.Target.startsWith("name=")) {
			// Remove name=
			elementLocator = elementLocator.substring(5);
			// TODO: Check for combination of name, type, and value
			// TODO
		}
		else if (this.Target.startsWith("xpath=")) {
			// Remove xpath=
			elementLocator = elementLocator.substring(6);
			// TODO
		}
		return selector;
	}
}
