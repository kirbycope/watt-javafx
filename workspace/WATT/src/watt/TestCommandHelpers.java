package watt;

import java.time.LocalTime;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import controller.Browser;
import javafx.application.Platform;

public class TestCommandHelpers {

	public static void DeleteAllVisibleCookies() {
		// Source: http://stackoverflow.com/a/27374365/6933359
		Browser.ExecuteScript("document.cookie.split(';').forEach(function(c) { document.cookie = c.replace(/^ +/, '').replace(/=.*/, '=;expires=' + new Date().toUTCString() + ';path=/'); });");
	}

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

	public static void PassFailTestBasedOnScriptResult() {
		// Handle JavaSript result
		if (Browser.scriptResult != null) {
			if (Browser.scriptResult.toString().equals("undefined")) {
				// Complete Task
				TestRunner.CompleteTask("pass");
			}
			else {
				TestRunner.CompleteTask("fail"); // is unexpected value
			}
		}
		else {
			TestRunner.CompleteTask("fail"); // isNull
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
