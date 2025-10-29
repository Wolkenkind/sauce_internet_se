package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.common.WaitingPage;

import java.util.Optional;

public class DynamicPage extends WaitingPage {

    private static final String BUTTON_CSS = "#start button";
    private static final String ELEMENT_AFTER_CLICK_ID = "loading";
    private static final String FINISH_ELEMENT_ID = "finish";

    @FindBy(css = BUTTON_CSS)
    private WebElement startButton;

    public DynamicPage(WebDriver webDriver, int timeoutSeconds) {
        super(webDriver, timeoutSeconds);
        PageFactory.initElements(webDriver, this);
    }

    public void clickButton() {
        startButton.click();
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ELEMENT_AFTER_CLICK_ID)));
            System.out.println("Loading started");
        } catch (TimeoutException e) {
            System.out.println("WARNING! -------------------------- Loading did not start as expected");
        }
    }

    private Optional<WebElement> getFinishElement() {
        try {
            return Optional.ofNullable(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(FINISH_ELEMENT_ID))));
        } catch (TimeoutException e) {
            System.out.println("Timeout: " + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean isFinishElementVisible() {
        Optional<WebElement> finishElement = getFinishElement();
        return finishElement.map(WebElement::isDisplayed).orElse(false);
    }

    public String getFinishText() {
        Optional<WebElement> finishElement = getFinishElement();
        return finishElement.orElseThrow(() -> new IllegalStateException("Finish element was not found!")).getText();
    }
}
