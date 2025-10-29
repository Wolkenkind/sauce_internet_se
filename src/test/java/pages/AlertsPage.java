package pages;

import exceptions.ElementNotFound;
import exceptions.SelectorType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.common.WaitingPage;

public class AlertsPage extends WaitingPage {
    public static final String ALERT_BUTTON_XPATH = "//*[@id='content']//button[contains(.,'Alert')]";
    public static final String CONFIRM_BUTTON_XPATH = "//*[@id='content']//button[contains(.,'Confirm')]";
    public static final String PROMPT_BUTTON_XPATH = "//*[@id='content']//button[contains(.,'Prompt')]";
    private static final String RESULT_ID = "result";
    @FindBy(xpath = ALERT_BUTTON_XPATH)
    private WebElement alertButton;

    @FindBy(xpath = CONFIRM_BUTTON_XPATH)
    private WebElement confirmButton;

    @FindBy(xpath = PROMPT_BUTTON_XPATH)
    private WebElement promptButton;

    @FindBy(id = RESULT_ID)
    private WebElement resultElement;

    public AlertsPage(WebDriver webDriver, int timeout) {
        super(webDriver, timeout);
        PageFactory.initElements(driver, this);
    }

    public String getResultText() {
        if (resultElement == null) {
            throw new ElementNotFound(
                    "alerts",
                    "resultElement",
                    SelectorType.ID,
                    RESULT_ID,
                    "Result element was not found");
        }
        return resultElement.getText();
    }

    public void clickAlertButton() {
        alertButton.click();
    }

    public void clickConfirmButton() {
        confirmButton.click();
    }

    public void clickPromptButton() {
        promptButton.click();
    }
}
