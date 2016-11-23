package model;

import watt.TestStepCommands;

public class TestStep {
	// Properties
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
		TestStepCommands.click(GetSelector());
	}

	public void clickAndWait() {
		TestStepCommands.clickAndWait(GetSelector());
	}

	public void deleteAllVisibleCookies() {
		TestStepCommands.deleteAllVisibleCookies();
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
		TestStepCommands.open(this.Target);
	}

	public void pause() {
		TestStepCommands.pause(this.Target);
	}

	public void refresh() {
		TestStepCommands.refresh();
	}

	public void refreshAndWait() {
		TestStepCommands.refreshAndWait();
	}

	public void runScript() {
		TestStepCommands.runScript(this.Target);
	}

	public void select() {
		TestStepCommands.select(GetSelector(), this.Value);
	}

	public void store() {
		// TODO: Store in JavaFx
	}

	public void submit() {
		TestStepCommands.submit(GetSelector());
	}

	public void submitAndWait() {
		TestStepCommands.submitAndWait(GetSelector());
	}

	public void type() {
		TestStepCommands.type(GetSelector(), this.Value);
	}

	public void verifyChecked() {
		TestStepCommands.verifyChecked(GetSelector());
	}

	public void verifyElementNotPresent() {
		TestStepCommands.verifyElementNotPresent(GetSelector());
	}

	public void verifyElementPresent() {
		TestStepCommands.verifyElementPresent(GetSelector());
	}

	public void verifyNotChecked() {
		TestStepCommands.verifyNotChecked(GetSelector());
	}

	public void verifyText() {
		TestStepCommands.verifyText(GetSelector(), this.Value);
	}

	public void verifyTitle() {
		TestStepCommands.verifyTitle(this.Target);
	}

	public void verifyLocation() {
		TestStepCommands.verifyLocation(this.Target);
	}

	public void waitForLocation() {
		TestStepCommands.waitForLocation(this.Target);
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
			// TODO
		}
		else if (target.startsWith("name=")) {
			// Remove name=
			target = target.substring(5);
			// TODO: Check for combination of name, type, and value
			// TODO
		}
		else if (target.startsWith("xpath=")) {
			// Remove xpath=
			target = target.substring(6);
			// TODO
		}
		return selector;
	}
}
