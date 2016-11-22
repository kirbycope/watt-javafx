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
		TestRunner.CompleteTask("pass");
	}

	public void clickAndWait() {
		// Get the element selector
		String selector = GetSelector();
		// Click the element
		Browser.ExecuteScript(selector + ".click()");
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	public void deleteAllVisibleCookies() {
		// Delete all cookies visible to the current location
		// Source: http://stackoverflow.com/a/27374365/6933359
		Browser.ExecuteScript("document.cookie.split(';').forEach(function(c) { document.cookie = c.replace(/^ +/, '').replace(/=.*/, '=;expires=' + new Date().toUTCString() + ';path=/'); });");
		// Complete Task
		TestRunner.CompleteTask("pass");
	}

	public void goBack() {
		// Go Back
		Browser.ExecuteScript("history.back()");
		// Complete Task
		TestRunner.CompleteTask("pass");
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
		TestRunner.CompleteTask("pass");
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

	public void pause() {
		// Pause
		Browser.ExecuteScript("setTimeout( function(){/* TODO: CompleteTask */}, " + this.Target + " );");
		// TODO: The function ran after the timeout needs to communicate from the WebView to the JavaFx app to complete the task
	}

	public void refresh() {
		// Reload page
		Browser.ExecuteScript("location.reload();");
		// Complete Task
		TestRunner.CompleteTask("pass");
	}

	public void refreshAndWait() {
		// Reload page
		Browser.ExecuteScript("location.reload();");
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	public void runScript() {
		// Run the script
		Browser.ExecuteScript(this.Target);
		// Complete Task
		TestRunner.CompleteTask("pass");
	}

	public void select() {
		// TODO
	}

	public void store() {
		// TODO
	}

	public void submit() {
		// TODO
	}

	public void type() {
		// TODO
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
