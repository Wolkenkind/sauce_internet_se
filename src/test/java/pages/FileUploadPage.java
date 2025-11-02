package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.common.WaitingPage;

public class FileUploadPage extends WaitingPage {
    public static final String FILE_INPUT_ID = "file-upload";
    public static final String UPLOAD_BUTTON_ID = "file-submit";

    public static final String UPLOADED_FILES_ID = "uploaded-files";
    public static final String SUCCESS_CSS = "#content > div > h3";

    @FindBy(id = FILE_INPUT_ID)
    WebElement fileInput;

    @FindBy(id = UPLOAD_BUTTON_ID)
    WebElement uploadButton;

    @FindBy(id = UPLOADED_FILES_ID)
    WebElement uploadedFilesElement;

    @FindBy(css = SUCCESS_CSS)
    WebElement successHeader;

    public FileUploadPage(WebDriver webDriver, int timeout) {
        super(webDriver, timeout);
        PageFactory.initElements(driver, this);
    }

    public void setFileInputFile(String file) {
        fileInput.sendKeys(file);
    }

    public void clickUploadButton() {
        uploadButton.click();
    }

    public boolean isUploadButtonPresent() {
        return !driver.findElements(By.id(UPLOAD_BUTTON_ID)).isEmpty();
    }

    public void waitForUploadedFilesToBeVisible() {
        wait.until(ExpectedConditions.visibilityOf(uploadedFilesElement));
    }

    public String getUploadedFilesText() {
        return uploadedFilesElement.getText();
    }

    public String getSuccessHeaderText() {
        return successHeader.getText();
    }
}
