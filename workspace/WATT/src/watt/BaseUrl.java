package watt;

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
		// Get the Base URL
		String baseUrl = GetBaseUrlFromUi();
		if ( (baseUrl.length() > 0) && (url.startsWith("/")) ) {
			return baseUrl + url;
		}
		else {
			return url;
		}
	}

	/**
	 * Gets the Base URL from the UI and returns the given URL with the Base URL substring removed.
	 * @param url
	 * @return
	 */
	public static String StubUrl(String url) {
		// Get the Base URL
		String baseUrl = GetBaseUrlFromUi();
		// Handle presence of Base URL in the given URL
		if (url.startsWith(baseUrl)) {
			return url.replace(baseUrl, "");
		}
		else {
			return url;
		}
	}
}
