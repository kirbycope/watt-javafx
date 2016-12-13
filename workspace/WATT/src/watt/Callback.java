package watt;

import controller.Main;

public class Callback {

	public void addTestStep(Object command, Object target, Object value) {
		String description = "";
		if (command == null) { command = ""; };
		if (target == null) { target = ""; };
		if (value == null) { value = ""; };
		// Create 'description' based off of 'command' and 'value'
		if ( (command.toString().length() > 0) && (value.toString().length() > 0) ) {
			description = command.toString() + " " + value.toString();
		}
		// Create 'description' based off of 'command' and 'target'
		else if ( (command.toString().length() > 0) && (target.toString().length() > 0) ) {
			description = command.toString() + " " + target.toString();
		}
		// Add Test Step
		Main.AddStep(true, description, command.toString(), target.toString(), value.toString(), false);
	}

	public void printToJavaConsole(Object object) {
		System.out.println(object);
	}
}
