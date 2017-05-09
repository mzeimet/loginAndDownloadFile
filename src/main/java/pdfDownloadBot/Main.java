package pdfDownloadBot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Main {

	private WebClient webClient;

	public Main() {
		webClient = new WebClient(BrowserVersion.CHROME);
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setDownloadImages(false);
	}

	public static void main(String[] args) {
		new Main().run();
	}

	public void run() {
		try {
			login();
			System.out.println("Logged in to " + Config.LOGIN_WEBSITE_URL); 
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlAnchor link = getLink();
			System.out.println("Link found: " + link.getHrefAttribute());
			writeFromLinkToFile(link);
			System.out.println("Wrote file to " + Config.FILE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the content given by the link to a local file.
	 * 
	 * @param link
	 *            The link which will download the file
	 */
	private void writeFromLinkToFile(HtmlAnchor link) throws IOException {
		InputStream iStream = link.click().getWebResponse().getContentAsStream();
		File file = new File(Config.FILE_NAME);
		OutputStream oStream = new FileOutputStream(file);
		byte[] buf = new byte[8192];
		int c = 0;

		while ((c = iStream.read(buf, 0, buf.length)) > 0) {
			oStream.write(buf, 0, c);
			oStream.flush();
		}
		oStream.close();
		iStream.close();
	}

	/**
	 * Finds the link of the file to download. In my case it was the first link
	 * found by the configured xpath (should be adjusted) on the download
	 * website.
	 */
	private HtmlAnchor getLink() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = webClient.getPage(Config.DOWNLOAD_WEBSITE_URL);
		List<HtmlAnchor> links = page.getByXPath(Config.XPATH_LINKS);
		return links.get(0);
	}

	/**
	 * Used for loggin in before requesting the download website. If no login is
	 * required this method should not be used.
	 */
	private void login() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// if the elements do not have an id, the can be found with xpath, e.g.
		// form.getByXPath("//input[@id='login_submit']").get(0) for an login
		// button
		HtmlPage page = webClient.getPage(Config.LOGIN_WEBSITE_URL);
		HtmlForm form = (HtmlForm) page.getHtmlElementById(Config.XPATH_LOGIN_FORM_ID);
		HtmlButton button = (HtmlButton) page.getHtmlElementById(Config.XPATH_LOGIN_FORM_SUBMIT_BUTTON_ID);
		HtmlTextInput usernameField = (HtmlTextInput) form.getInputByName(Config.XPATH_LOGIN_FORM_USERNAME_FIELD_NAME);
		usernameField.setValueAttribute(Config.USERNAME);
		HtmlPasswordInput passwordField = (HtmlPasswordInput) form
				.getInputByName(Config.XPATH_LOGIN_FORM_PASSWORD_FIELD_NAME);
		passwordField.setValueAttribute(Config.PASSWORD);
		webClient.waitForBackgroundJavaScript(1000);
		button.click();
	}
}