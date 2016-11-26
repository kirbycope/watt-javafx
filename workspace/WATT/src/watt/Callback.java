package watt;

import controller.Main;

public class Callback {

	public void addTestStep(Object target) {
		System.out.println(target);
		Main.AddStep(true, "Click element", "click", target.toString(), "", false);
	}

	public void printToJavaConsole(Object object) {
		System.out.println(object);
	}
}
