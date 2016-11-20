package watt;

import javafx.scene.control.TextField;

public class BaseUrl {
	/**
	 * Gets the Base URL from the TextField and returns the full URL with the Base removed.
	 * @param fullUrl
	 * @return
	 */
	public static String StubUrl(String fullUrl) {
		// Get the Base URL field
		TextField tfBaseUrl = (TextField) Watt.primaryStage.getScene().lookup("#base-url");
		String baseUrl = tfBaseUrl.getText();
		if (fullUrl.startsWith(baseUrl)) {
			return fullUrl.replace(baseUrl, "");
		}
		else {
			return fullUrl;
		}
	}
}
