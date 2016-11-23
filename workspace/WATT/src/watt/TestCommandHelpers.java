package watt;

import java.time.LocalTime;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import controller.Browser;
import javafx.application.Platform;

public class TestCommandHelpers {
	public static boolean IsElementPresent(String selector) {
		// Locate the element
		Browser.ExecuteScript(selector);
		// Parse and return the result
		if (Browser.scriptResult != null) {
			return true;
		}
		else {
			return false;
		}
	}

	public static void StopWaitForAndCompleteTask(ScheduledThreadPoolExecutor exec, String reason) {
		// Run the following code in the FX application thread
		Platform.runLater(new Runnable() {
			@Override public void run() {
				// Complete Test
				TestRunner.CompleteTask(reason);
			}
		});
		// Shutdown the ScheduledThreadPoolExecutor
		exec.shutdown();
	}

	public static void WaitFor(final String javaScript) {
		// Get start time
		LocalTime startTime = LocalTime.now();
		// Create a ScheduledThreadPoolExecutor
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
		// Define the code to run (every 250 milliseconds)
		exec.scheduleAtFixedRate(new Runnable() {
			public void run() {
				// Check if Stop has been clicked
				if (Watt.playing == false)
				{
					StopWaitForAndCompleteTask(exec, "fail"); // TODO: Mark as test execution Stop requested
				}
				// Get the current time
				LocalTime currentTime = LocalTime.now();
				// If the ScheduledThreadPoolExecutor has been running for more than a minute, shut it down
				if ( (currentTime.getMinute() - startTime.getMinute()) != 0 ) {
					StopWaitForAndCompleteTask(exec, "fail"); // TODO: Mark as test execution timed out
				}
				// If the script has ran at least once
				if (Browser.scriptResult != null) {
					// Check if script has returned "true"
					if (Browser.scriptResult.toString().equals("true")) {
						StopWaitForAndCompleteTask(exec, "pass");
					}
				}
				// Run the following code in the FX application thread
				Platform.runLater(new Runnable() {
					@Override public void run() {
						// Run the script (the result will be checked in the next iteration)
	 		        	Browser.ExecuteScript(javaScript);
					}
				});
			}
		}, 0, 250, TimeUnit.MILLISECONDS);
	}
}
