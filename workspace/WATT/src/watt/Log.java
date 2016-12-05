package watt;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

public class Log {

	private static String logFileFolderPath;
	private static PrintWriter printWriter;
	public static String logFilePath;

	public static void CreateNewLogFile() {
		// Create a new log file using the Unix Time as the unique filename
		logFilePath = logFileFolderPath + Instant.now().getEpochSecond() + ".txt";
		try {
			FileWriter fileWriter = new FileWriter(logFilePath, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		    printWriter = new PrintWriter(bufferedWriter);
		}
		catch (Exception e) { e.printStackTrace(); }
	}

	public static void FinalizeLogFile() {
		printWriter.close();
	}

	public static void OpenLogFile() {
		File logFile = new File(Log.logFilePath);
		try { Desktop.getDesktop().open(logFile); }
		catch (IOException e) { e.printStackTrace(); }
	}

	public static void SetupLogFolder() {
		// Get the path of the first root drive
		String rootPath = File.listRoots()[0].getPath();
		// Create the WATT folder if not present
		try { Files.createDirectories(Paths.get(rootPath + "WATT")); }
		catch (Exception e) { e.printStackTrace(); }
		// Set the log file path
		logFileFolderPath = rootPath + "WATT\\";
	}

	public static void WriteLine(String text) {
	    printWriter.println(text);
	}
}
