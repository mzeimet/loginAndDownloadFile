package pdfDownloadBot;

/**
 * A class showing some examples to copy into the Config.java file 
 */
public class ExampleConfig {
	// The filename of the file the dowload will be saved in
	public static final String FILE_NAME = "Filename.pdf";
	// The website where the login form will be
	public static final String LOGIN_WEBSITE_URL = "https://facebook.com";
	// The website where we'll search the download-link
	public static final String DOWNLOAD_WEBSITE_URL = "https://facebook.com";
	//Xpath expression for finding possible links, should be as specific as possible
	public static final String XPATH_LINKS = "//td[@class='col_docname']/a";

	public static final String XPATH_LOGIN_FORM_ID = "login-form";
	public static final String XPATH_LOGIN_FORM_SUBMIT_BUTTON_ID = "login-Button";
	public static final String XPATH_LOGIN_FORM_USERNAME_FIELD_NAME = "username";
	public static final String USERNAME = "yourUsernameExample";
	public static final String XPATH_LOGIN_FORM_PASSWORD_FIELD_NAME = "password";
	public static final String PASSWORD = "yourPasswordExample";
}
