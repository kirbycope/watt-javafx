package model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import controller.Browser;
import javafx.concurrent.Task;
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
		// Get the length of time to wait (in milliseconds)
		Long time = Long.parseLong(this.Target);
		// Set a background Task to wait the given length of time (in milliseconds)
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// Wait the given length of time (in milliseconds)
				Thread.sleep(time);
				// Complete Task
				TestRunner.CompleteTask("pass");
				return null;
			}
		};
		// Start the Task
		new Thread(task).start();
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
		// Get the element selector
		String selector = GetSelector();
		// Get the option locator
		String optionLocator = this.Value;
		// Create a variable to hold the JavaScript string
		String javaScript = "";
		// Handle option locator type
		if (optionLocator.startsWith("label=")) {
			// Trim off label=
			optionLocator = optionLocator.replace("label=", "");
			// Build the JavaScript string
			StringBuilder sb = new StringBuilder();
			sb.append("var select = " + selector + ";");
			sb.append("for (var i = 0; i < select.options.length; i++) {");
				sb.append("if (select.options[i].text === '" + optionLocator + "') {");
					sb.append("select.selectedIndex = i;");
					sb.append("break;");
				sb.append("}");
			sb.append("}");
			// Select the option by label
			javaScript = sb.toString();
		}
		else if (optionLocator.startsWith("value=")) {
			// Trim off value=
			optionLocator = optionLocator.replace("value=", "");
			// Select the option by value
			javaScript = selector + ".value = '" + optionLocator + "'";
		}
		else if (optionLocator.startsWith("id=")) {
			// Trim off id=
			optionLocator = optionLocator.replace("id=", "");
			// Select the option by id
			javaScript = "document.getElementById('#" + optionLocator + "').selected = true;";
		}
		else if (optionLocator.startsWith("index=")) {
			// Trim off index=
			optionLocator = optionLocator.replace("index=", "");
			// Select the option by id
			javaScript = selector + ".selectedIndex = " + optionLocator + "";
		}
		// Select the option
		Object result = Browser.ExecuteScript(javaScript);
		// Handle the result
		if ( (result != null) && (!result.equals("undefined")) ) {
			// Complete Task
			TestRunner.CompleteTask("pass");
		}
		else {
			// Complete Task
			TestRunner.CompleteTask("fail");
		}
	}

	public void store() {
		// TODO: Store in JavaFx
	}

	public void submit() {
		// Get the element selector
		String selector = GetSelector();
		// Submit the form
		Browser.ExecuteScript(selector + ".submit()");
		// Complete Task
		TestRunner.CompleteTask("pass");
	}

	public void submitAndWait() {
		// Get the element selector
		String selector = GetSelector();
		// Submit the form
		Browser.ExecuteScript(selector + ".submit()");
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	public void type() {
		// Get the element selector
		String selector = GetSelector();
		// Type value in the target element
		Browser.ExecuteScript(selector + ".value='" + this.Value + "'");
		// Complete Task
		TestRunner.CompleteTask("pass");
	}

	public void verifyChecked() {
		// Get the element selector
		String selector = GetSelector();
		// Get the element's checked value
		Object result = Browser.ExecuteScript(selector + ".checked");
		// Handle result
		if (result.equals("true")) {
			// Complete Task
			TestRunner.CompleteTask("pass");
		}
		else {
			// Complete Task
			TestRunner.CompleteTask("fail");
		}
	}

	public void verifyElementNotPresent() {
		// Check if element is present and pass/fail accordingly
		if (IsElementPresent()) {
			TestRunner.CompleteTask("fail");
		}
		else {
			TestRunner.CompleteTask("pass");
		}
	}

	public void verifyElementPresent() {
		// Check if element is present and pass/fail accordingly
		if (IsElementPresent()) {
			TestRunner.CompleteTask("pass");
		}
		else {
			TestRunner.CompleteTask("fail");
		}
	}

	public void verifyNotChecked() {
		// Get the element selector
		String selector = GetSelector();
		// Get the element's checked value
		Object result = Browser.ExecuteScript(selector + ".checked");
		// Handle result
		if (result.equals("false")) {
			// Complete Task
			TestRunner.CompleteTask("pass");
		}
		else {
			// Complete Task
			TestRunner.CompleteTask("fail");
		}
	}

	public void verifyText() {
		// Get the element selector
		String selector = GetSelector();
		// Get the element's text
		Object result = Browser.ExecuteScript(selector + ".textContent");
		// Pass/fail test accordingly
		if (result.equals(this.Target)) {
			TestRunner.CompleteTask("pass");
		}
		else {
			TestRunner.CompleteTask("fail");
		}
	}

	public void verifyTitle() {
		// Get the current title
		Object result = Browser.ExecuteScript("document.title");
		// Pass/fail test accordingly
		if (result.equals(this.Target)) {
			TestRunner.CompleteTask("pass");
		}
		else {
			TestRunner.CompleteTask("fail");
		}
	}

	public void verifyLocation() {
		// Get the current location
		Object result = Browser.ExecuteScript("document.location.href");
		// Pass/fail test accordingly
		if (result.equals(this.Target)) {
			TestRunner.CompleteTask("pass");
		}
		else {
			TestRunner.CompleteTask("fail");
		}
	}

	public void waitForLocation() {
		WaitFor("document.location.href == '" + this.Target + "'");
		// Note: the result is handled by the background task
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

	private boolean IsElementPresent() {
		// Get the element selector
		String selector = GetSelector();
		// Locate the element
		Object result = Browser.ExecuteScript(selector);
		// Parse and return the result
		if (result != null) {
			return true;
		}
		else {
			return false;
		}
	}

	// TODO: escape after 30 seconds
	private void WaitFor(final String javaScript) {
		// Get start time
		//TODO
		// Source: http://stackoverflow.com/a/15990400/6933359
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
	     exec.schedule(new Runnable() {
	         public void run() {
	        	 // Run the script
	        	 Object result = Browser.ExecuteScript(javaScript);
	        	 // Handle the result
	        	 System.out.println(result);
	        	 if (result.equals("true")){
	        		 // Shutdown the ScheduledThreadPoolExecutor
	        		 exec.shutdown();
	        		 // Complete Test
	        		 TestRunner.CompleteTask("pass");
	        	 }
	         }
	     }, 250, TimeUnit.MILLISECONDS);
	}
}
