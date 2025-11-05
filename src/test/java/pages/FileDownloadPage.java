package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.common.WaitingPage;

import java.util.List;
import java.util.Optional;

public class FileDownloadPage extends WaitingPage {

    public static final String DOWNLOAD_LINKS_CSS = "#content div a";

    @FindBy(css = DOWNLOAD_LINKS_CSS)
    private List<WebElement> downloadLinks;

    public FileDownloadPage(WebDriver webDriver, int timeout) {
        super(webDriver, timeout);
        PageFactory.initElements(driver, this);
    }

    public List<String> getDownloadLinks() {
        return downloadLinks.stream().map(WebElement::getText).toList();
    }

    public boolean clickDownloadLink(String linkText) {
        Optional<WebElement> link = downloadLinks.stream().filter(we -> we.getText().equals(linkText)).findFirst();
        if (link.isPresent()) {
            link.get().click();
            System.out.println("Clicked " + linkText + " successfully!");
            return true;
        }

        return false;
    }
}
