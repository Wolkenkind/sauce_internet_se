package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.common.WaitingPage;

public class FileUploadPage extends WaitingPage {
    private static final String FILE_BUTTON_ID = "file-upload";
    private static final String UPLOAD_BUTTON_ID = "file-submit";

    @FindBy(id = FILE_BUTTON_ID)
    WebElement fileButton;

    @FindBy(id = UPLOAD_BUTTON_ID)
    WebElement uploadButton;

    public FileUploadPage(WebDriver webDriver, int timeout) {
        super(webDriver, timeout);
        PageFactory.initElements(driver, this);
    }


}
