package model;

import watt.TestRunner;
import watt.TestStepCommands;

public class TestStep {
	// Properties
	public boolean ExecuteStep;
	public String Description;
	public String Command;
	public String Target;
	public String Value;
	public boolean ContinueOnFailure;

	// Constructor
	public TestStep() {
		ExecuteStep = true;
		Description = "";
		Command = "";
		Target = "";
		Value = "";
		ContinueOnFailure = false;
	}

	// Constructor
	public TestStep(boolean executeStep, String description, String command, String target, String value, boolean continueOnFailure) {
		ExecuteStep = executeStep;
		Description = description;
		Command = command;
		Target = target;
		Value = value;
		ContinueOnFailure = continueOnFailure;
	}

	public void click() {
		String selector = GetSelector();
		TestStepCommands.click(selector);
	}

	public void clickAndWait() {
		String selector = GetSelector();
		TestStepCommands.clickAndWait(selector);
	}

	public void deleteAllVisibleCookies() {
		TestStepCommands.deleteAllVisibleCookies();
	}

	public void echo() {
		String text = GetText(this.Target);
		TestStepCommands.echo(text);
	}

	public void goBack() {
		TestStepCommands.goBack();
	}

	public void goBackAndWait() {
		TestStepCommands.goBackAndWait();
	}

	public void goForward() {
		TestStepCommands.goForward();
	}

	public void goForwardAndWait() {
		TestStepCommands.goForwardAndWait();
	}

	public void open() {
		String text = GetText(this.Target);
		TestStepCommands.open(text);
	}

	public void pause() {
		String text = GetText(this.Target);
		TestStepCommands.pause(text);
	}

	public void refresh() {
		TestStepCommands.refresh();
	}

	public void refreshAndWait() {
		TestStepCommands.refreshAndWait();
	}

	public void runScript() {
		// Warning: Does not allow "stored" variables
		TestStepCommands.runScript(this.Target);
	}

	public void select() {
		String selector = GetSelector();
		String text = GetText(this.Value);
		TestStepCommands.select(selector, text);
	}

	public void store() {
		TestStepCommands.store(this.Target, this.Value);
	}

	public void submit() {
		String selector = GetSelector();
		TestStepCommands.submit(selector);
	}

	public void submitAndWait() {
		TestStepCommands.submitAndWait(GetSelector());
	}

	public void type() {
		String selector = GetSelector();
		String text = GetText(this.Value);
		TestStepCommands.type(selector, text);
	}

	public void verifyChecked() {
		String selector = GetSelector();
		TestStepCommands.verifyChecked(selector);
	}

	public void verifyElementNotPresent() {
		String selector = GetSelector();
		TestStepCommands.verifyElementNotPresent(selector);
	}

	public void verifyElementPresent() {
		String selector = GetSelector();
		TestStepCommands.verifyElementPresent(selector);
	}

	public void verifyLocation() {
		String text = GetText(this.Target);
		TestStepCommands.verifyLocation(text);
	}

	public void verifyNotChecked() {
		String selector = GetSelector();
		TestStepCommands.verifyNotChecked(selector);
	}

	public void verifyTable() {
		String selector = GetSelector();
		String text = GetText(this.Value);
		TestStepCommands.verifyTable(selector, text);
	}

	public void verifyText() {
		String selector = GetSelector();
		String text = GetText(this.Value);
		TestStepCommands.verifyText(selector, text);
	}

	public void verifyTitle() {
		String text = GetText(this.Target);
		TestStepCommands.verifyTitle(text);
	}

	public void verifyValue() {
		String selector = GetSelector();
		String text = GetText(this.Value);
		TestStepCommands.verifyValue(selector, text);
	}

	public void waitForLocation() {
		String text = GetText(this.Target);
		TestStepCommands.waitForLocation(text);
	}

	private String GetSelector() {
		// Initialize the selector
		String selector = null;
		// Get the target
		String target = this.Target;
		if (target.startsWith("css=")) {
			// Remove css=
			target = target.substring(4);
			// Create the selector
			selector = "document.querySelector('" + target + "')";
		}
		else if (target.startsWith("document.")) {
			// Create the selector
			selector = target;
		}
		else if (target.startsWith("dom=")) {
			// Remove dom=
			target = target.substring(4);
			// Create the selector
			selector = target;
		}
		else if (target.startsWith("id=")) {
			// Remove id=
			target = target.substring(3);
			// Create the selector
			selector = "document.getElementById('" + target + "')";
		}
		else if (target.startsWith("link=")) {
			// Remove link=
			target = target.substring(5);
			// Create the selector
			selector = "document.evaluate('//*[text()=\"" + target + "\"]', document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE).snapshotItem(0)";
		}
		else if (target.startsWith("name=")) {
			String targetName = null;
			String targetType = null;
			String targetValue = null;
			String[] targetValues = target.split("\\s+");
			// Parse each String in the array
			for (String str : targetValues) {
				if (str.startsWith("name=")) {
					targetName = "[name='" + str.substring(5) + "']";
				}
				else if (str.startsWith("type=")) {
					targetType = "[type='" + str.substring(5) + "']";
				}
				else if (str.startsWith("value=")) {
					targetValue = "[value='" + str.substring(6) + "']";
				}
			}
			selector = "document.querySelector(\"" +  targetName + targetType + targetValue + "\")";
		}
		else if (target.startsWith("xpath=")) {
			// Remove xpath=
			target = target.substring(6);
			// Build selector
			selector = "document.evaluate(\"" + target + "\", document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE).snapshotItem(0)";
		}
		return selector;
	}

	private String GetText(String text) {
		// Handle "stored" variables. Ex., "${i}"
		if(text.contains("${")) {
			// Clean the text to get the variable name
			text = text.replace("${", "");
			text = text.replace("}", "");
			// Find the value for the given text
			text = TestRunner.store.get(text);
		}
		// Return the fully qualified text
		return text;
	}
}
