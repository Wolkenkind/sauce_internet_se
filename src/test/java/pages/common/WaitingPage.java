package pages.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitingPage extends BasePage {
    public static final int DEFAULT_TIMEOUT = 10;
    protected WebDriverWait wait;

    public WaitingPage(WebDriver webDriver) {
        super(webDriver);
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WaitingPage(WebDriver webDriver, int timeout) {
        super(webDriver);
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(timeout));
    }
}
