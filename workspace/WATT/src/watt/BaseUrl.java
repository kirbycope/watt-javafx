package watt;

import java.util.Optional;

public class BaseUrl {

	private static String GetBaseUrlFromUi() {
		return UiHelpers.GetBaseUrlField().getText();
	}

	/**
	 * Gets the Base URL from the UI and returns the given URL with the Base URL substring added.
	 * @param url
	 * @return
	 */
	public static String FullUrl(String url) {
		// Ensure that the given URL is not null
		url = Optional.ofNullable(url).orElse("");
		// Get the Base URL
		String baseUrl = GetBaseUrlFromUi();
		// Combine the Base URL and given URL
		if (baseUrl.length() > 0) {
			if ( (url.startsWith("/")) || (baseUrl.endsWith("/")) ) {
				url = baseUrl + url;
			}
			else {
				url = baseUrl + "/" + url;
			}
		}
		return url;
	}

	/**
	 * Gets the Base URL from the UI and returns the given URL with the Base URL substring removed.
	 * @param url
	 * @return
	 */
	public static String StubUrl(String url) {
		// Ensure that the given URL is no null
		url = Optional.ofNullable(url).orElse("");
		// Get the Base URL
		String baseUrl = GetBaseUrlFromUi();
		// Handle presence of Base URL in the given URL
		if (url.startsWith(baseUrl)) {
			url = url.replace(baseUrl, "");
		}
		return url;
	}
}
