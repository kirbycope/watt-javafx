package watt;

import controller.Browser;
import javafx.concurrent.Task;

public class TestStepCommands {

	public static void click(String selector) {
		// Click the element
		Browser.ExecuteScript(selector + ".click()");
		// Handle Result of script
		TestCommandHelpers.PassFailTestBasedOnScriptResult();
	}

	public static void clickAndWait(String selector) {
		// Click the element
		Browser.ExecuteScript(selector + ".click()");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail");
		}
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	public static void deleteAllVisibleCookies() {
		// Delete all cookies visible to the current location
		TestCommandHelpers.DeleteAllVisibleCookies();
		// Handle Result of script
		TestCommandHelpers.PassFailTestBasedOnScriptResult();
	}

	public static void goBack() {
		// Go Back
		Browser.ExecuteScript("history.back()");
		// Handle Result of script
		TestCommandHelpers.PassFailTestBasedOnScriptResult();
	}

	public static void goBackAndWait() {
		// Go Back
		Browser.ExecuteScript("history.back()");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail");
		}
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	public static void goForward() {
		// Go Forward
		Browser.ExecuteScript("history.forward()");
		// Handle Result of script
		TestCommandHelpers.PassFailTestBasedOnScriptResult();
	}

	public static void goForwardAndWait() {
		// Go Forward
		Browser.ExecuteScript("history.forward()");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail");
		}
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	// NOTE: The "open" command waits for the page to load before proceeding, ie. the "AndWait" suffix is implicit.
	public static void open(String target) {
		// Get fully qualified URL to open
		String url = BaseUrl.FullUrl(target);
		// Open the URL in the WebView
		Browser.ExecuteScript("location.href='" + url + "'");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail");
		}
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	public static void pause(String target) {
		// Get the length of time to wait (in milliseconds)
		Long time = Long.parseLong(target);
		// Set a background Task to wait the given length of time (in milliseconds)
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				// Wait the given length of time (in milliseconds)
				try {
					Thread.sleep(time);
				}
				catch (Exception e) {
					// Complete Task
					TestRunner.CompleteTask("fail");
				}
				// Complete Task
				TestRunner.CompleteTask("pass");
				return null;
			}
		};
		// Start the Task
		new Thread(task).start();
	}

	public static void refresh() {
		// Reload page
		Browser.ExecuteScript("location.reload();");
		// Handle Result of script
		TestCommandHelpers.PassFailTestBasedOnScriptResult();
	}

	public static void refreshAndWait() {
		// Reload page
		Browser.ExecuteScript("location.reload();");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail");
		}
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	public static void runScript(String script) {
		// Run the script
		Browser.ExecuteScript(script);
		// Complete Task
		TestRunner.CompleteTask("pass");
	}

	public static void select(String selector, String optionLocator) {
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
		Browser.ExecuteScript(javaScript);
		// Handle Result of script
		TestCommandHelpers.PassFailTestBasedOnScriptResult();
	}

	public static void submit(String selector) {
		// Submit the form
		Browser.ExecuteScript(selector + ".submit()");
		// Handle Result of script
		TestCommandHelpers.PassFailTestBasedOnScriptResult();
	}

	public static void submitAndWait(String selector) {
		// Submit the form
		Browser.ExecuteScript(selector + ".submit()");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail");
		}
		// Note: After the WebView's state becomes "SUCCEEDED", TestRunner.CompleteTask() is called.
	}

	public static void type(String selector, String value) {
		// Type value in the target element
		Browser.ExecuteScript(selector + ".value='" + value + "'");
		// Handle Result of script
		TestCommandHelpers.PassFailTestBasedOnScriptResult();
	}

	public static void verifyChecked(String selector) {
		// Get the element's checked value
		Browser.ExecuteScript(selector + ".checked");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail"); // isNull
		}
		else {
			if (Browser.scriptResult.equals("true")) {
				// Complete Task
				TestRunner.CompleteTask("pass");
			}
			else {
				// Complete Task
				TestRunner.CompleteTask("fail");
			}
		}
	}

	public static void verifyElementNotPresent(String selector) {
		// Check if element is present and pass/fail accordingly
		if (TestCommandHelpers.IsElementPresent(selector)) {
			TestRunner.CompleteTask("fail");
		}
		else {
			TestRunner.CompleteTask("pass");
		}
	}

	public static void verifyElementPresent(String selector) {
		// Check if element is present and pass/fail accordingly
		if (TestCommandHelpers.IsElementPresent(selector)) {
			TestRunner.CompleteTask("pass");
		}
		else {
			TestRunner.CompleteTask("fail");
		}
	}

	public static void verifyLocation(String target) {
		// Get the current location
		Browser.ExecuteScript("document.location.href");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail"); // isNull
		}
		else {
			// Pass/fail test accordingly
			if (Browser.scriptResult.equals(target)) {
				TestRunner.CompleteTask("pass");
			}
			else {
				TestRunner.CompleteTask("fail");
			}
		}
	}

	public static void verifyNotChecked(String selector) {
		// Get the element's checked value
		Browser.ExecuteScript(selector + ".checked");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail"); // isNull
		}
		else {
			if (Browser.scriptResult.equals("false")) {
				// Complete Task
				TestRunner.CompleteTask("pass");
			}
			else {
				// Complete Task
				TestRunner.CompleteTask("fail");
			}
		}
	}

	public static void verifyTable(String selector, String value) {
		// Handle Selenium IDE selector (Ex., "table.3.2...")
		if (selector.startsWith("table")) {
			// Get the table value
			String table = selector.substring(selector.indexOf("table"));
			// Get the table row and column
			int row = Integer.parseInt(table.substring(6,7));
			int column = Integer.parseInt(table.substring(8, 9));
			// Handle the Row
			if (row == 0) {
				selector = selector.replaceFirst("." + row, " > thead > tr");
			}
			else {
				selector = selector.replaceFirst("." + row, " > tbody > tr:nth-child(" + (row) + ")");
			}
			// Handle the Column
			if (column == 0) {
				selector = selector.replace("." + column, " > td");
			}
			else {
				selector = selector.replace("." + column, " > td:nth-child(" + (column + 1) + ")");
			}
		}
		// Get the element's text
		Browser.ExecuteScript(selector + ".textContent");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail"); // isNull
		}
		else {
			// Pass/fail test accordingly
			if (Browser.scriptResult.equals(value)) {
				TestRunner.CompleteTask("pass");
			}
			else {
				TestRunner.CompleteTask("fail");
			}
		}
	}

	public static void verifyText(String selector, String value) {
		// Get the element's text
		Browser.ExecuteScript(selector + ".textContent");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail"); // isNull
		}
		else {
			// Pass/fail test accordingly
			if (Browser.scriptResult.equals(value)) {
				TestRunner.CompleteTask("pass");
			}
			else {
				TestRunner.CompleteTask("fail");
			}
		}
	}

	public static void verifyTitle(String target) {
		// Get the current title
		Browser.ExecuteScript("document.title");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail"); // isNull
		}
		else {
			// Pass/fail test accordingly
			if (Browser.scriptResult.equals(target)) {
				TestRunner.CompleteTask("pass");
			}
			else {
				TestRunner.CompleteTask("fail");
			}
		}
	}

	public static void verifyValue(String selector, String value) {
		// Get the element's value
		Browser.ExecuteScript(selector + ".value");
		// Handle script result
		if (Browser.scriptResult == null) {
			TestRunner.CompleteTask("fail"); // isNull
		}
		else {
			// Pass/fail test accordingly
			if (Browser.scriptResult.equals(value)) {
				TestRunner.CompleteTask("pass");
			}
			else {
				TestRunner.CompleteTask("fail");
			}
		}
	}

	public static void waitForLocation(String target) {
		TestCommandHelpers.WaitFor("document.location.href == '" + target + "'");
		// Note: the result is handled by the background task
	}
}
