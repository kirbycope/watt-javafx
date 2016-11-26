package watt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import controller.Main;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.TestStep;

public class TestCase {

	public static void New() {
		// Reset Select/Deselect All check box
		CheckBox selectAll = (CheckBox) Watt.primaryStage.getScene().lookup("#deselect-select-all");
		if ( selectAll.isSelected() == false ) { selectAll.fire(); }
		// Clear Base URL
		TextField baseUrl = (TextField) Watt.primaryStage.getScene().lookup("#base-url");
		baseUrl.clear();
		// Reset Expand/Collapse button
		Button expandAll = (Button) Watt.primaryStage.getScene().lookup("#expand-collapse-all");
		if ( expandAll.getTooltip().getText().equals("Collapse All Steps") ) { expandAll.fire(); }
		// Clear any Test Step(s) in the container
		Watt.testStepsContainer.getChildren().clear();
	}

	public static void Open() {
		// Prompt User to select file
		// https://docs.oracle.com/javase/8/javafx/api/javafx/stage/FileChooser.html
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Test Case");
        //Extension filter
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("HTML (*.html)", "*.html");
        fileChooser.getExtensionFilters().add(extentionFilter);
		File fileName = fileChooser.showOpenDialog(null);
		if (fileName != null)
		{
			// Clear any existing test data
			New();
			// Read the HTML file and convert to a String
			StringBuilder contentBuilder = new StringBuilder();
			try
			{
				// Create the Buffered Reader for the File Reader with the given file name
			    BufferedReader in = new BufferedReader(new FileReader(fileName));
			    String str;
			    // REad each line and add to the string builder
			    while ((str = in.readLine()) != null)
			    {
			        contentBuilder.append(str);
			    }
			    // Close the Buffered Reader
			    in.close();
			    // Convert the string builder to a string
			    String content = contentBuilder.toString();
			    // Get the Base Address
				String baseAddress = content.substring( content.indexOf("<link")+32, content.lastIndexOf(" />")-1 );
				// Get the Base Address test field from the UI
				TextField baseUrl = UiHelpers.GetBaseUrlField();
				// Set the Base Address text
				baseUrl.setText(baseAddress);
				// Get the table of test steps
				Document doc = Jsoup.parse(content);
				Element table = doc.select("table").first();
				// Get the rows in the table
				Elements rows = table.select("tr");
				// Ignore the first row (the Test Case header)
				rows.remove(0);
				for (Element row : rows)
				{
					boolean execute;
					String description = "";
					String command = "";
					String target = "";
					String value = "";
					boolean continueOnFailure;
					// Get the previous node/element; the one before this table row (Looking for a HTML comment)
					Node previousSiblingNode = row.previousSibling();
					// Assure that the previous node...
					if 	(
						( previousSiblingNode != null ) && // is not null
						( row.previousSibling().getClass().getName() == "org.jsoup.nodes.Comment" ) && // is a Comment
						( ((Comment) previousSiblingNode).getData().length() > 0 ) // has some value
						)
					{
						description = ((Comment)previousSiblingNode).getData();
					}
					// Handle the <tr>
					String dataContinueOnFailure = row.attr("data-continueOnFailure");
					continueOnFailure = Boolean.parseBoolean(dataContinueOnFailure);
					String dataExecute = row.attr("data-execute");
					execute = Boolean.parseBoolean(dataExecute);
					// Handle each <td> in the <tr>
					for (int i = 0; i < row.childNodes().size(); i++)
					{
						Node childNode = row.childNodes().get(i);

						if (i == 1) { command = ((Element) childNode).text(); }
						else if (i==3) { target = ((Element) childNode).text(); }
						else if (i==5) { value = ((Element) childNode).text(); }
					}
					// Finally Add the test step to the Test Steps Pane
					Main.AddStep(execute, description, command, target, value, continueOnFailure);
				}
			}
			catch (IOException e)
			{
				UiHelpers.ShowToast("Error reading file!");
			}
		}
	}

	public static void Save() {
		// https://docs.oracle.com/javase/8/javafx/api/javafx/stage/FileChooser.html
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Test Case");
        //Extension filter
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("HTML (*.html)", "*.html");
        fileChooser.getExtensionFilters().add(extentionFilter);
		File fileName = fileChooser.showSaveDialog(null);
		if (fileName != null)
		{
			// Create a string that holds the content of the HTML file
			String content = createHtmlString();
			// Open the file and write its contents to the string (above)
			FileWriter fileWriter = null;
            try
            {
            	fileWriter = new FileWriter(fileName);
                fileWriter.write(content);
                fileWriter.close();
            } catch (IOException e)
            {
            	UiHelpers.ShowToast("Error saving file!");
			}
		}
	}

	private static String createHtmlString()
	{
		String testName = "TestCase";
		String newLine = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append(newLine);
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		sb.append(newLine);
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
		sb.append(newLine);
		sb.append("<head profile=\"http://selenium-ide.openqa.org/profiles/test-case\">");
		sb.append(newLine);
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		sb.append(newLine);
		// Get BaseAddress
		String baseAddress = ((TextField) Watt.primaryStage.getScene().lookup("#base-url")).getText();
		// Continue writing the HTML file
		sb.append("<link rel=\"selenium.base\" href=\"" + baseAddress + "\" />");
		sb.append(newLine);
		sb.append("<title>" + testName +"</title>");
		sb.append(newLine);
		sb.append("</head>");
		sb.append(newLine);
		sb.append("<body>");
		sb.append(newLine);
		sb.append("<table cellpadding=\"1\" cellspacing=\"1\" border=\"1\">");
		sb.append(newLine);
		sb.append("<thead>");
		sb.append(newLine);
		sb.append("<tr><td rowspan=\"1\" colspan=\"3\">" + testName + "</td></tr>");
		sb.append(newLine);
		sb.append("</thead>");
		sb.append(newLine);
		sb.append("<tbody>");
		sb.append(newLine);
		// Get Test Step(s)
		ObservableList<javafx.scene.Node> testSteps = Watt.testStepsContainer.getChildren();
		int stepCount = testSteps.size();
		// For each test step in the Test Steps container...
		for (int i = 0; i < stepCount; i++)
		{
			// Get the Test Step container
			VBox testStepContainer = (VBox) testSteps.get(i);
			TestStep testStep = UiHelpers.GetTestStepDetails(testStepContainer);
			// Add the Description
			sb.append("	<!--" + testStep.Description + "-->");
			sb.append(newLine);
			// Add the table row // <tr data-continueOnFailure="true" data-execute="true">
			sb.append("<tr data-continueOnFailure=\""+ testStep.ContinueOnFailure + "\" data-execute=\"" + testStep.ExecuteStep + "\">");
			sb.append(newLine);
			// Add the Command
			sb.append("	<td>" + testStep.Command + "</td>");
			sb.append(newLine);
			// Add the Target
			sb.append("	<td>" + testStep.Target + "</td>");
			sb.append(newLine);
			// Get the Value
			sb.append("	<td>" + testStep.Value + "</td>");
			sb.append(newLine);
			// End the table row
			sb.append("</tr>");
			sb.append(newLine);
		}
		// Finish the HTML file
		sb.append("</tbody>");
		sb.append(newLine);
		sb.append("</table>");
		sb.append(newLine);
		sb.append("</body>");
		sb.append(newLine);
		sb.append("</html>");
		sb.append(newLine);
		// Return the completed HTML string
		return sb.toString();
	}
}
