package tests.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://the-internet.herokuapp.com/");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void waitForPageToLoad() {
        try {
            // Wait for jQuery to be ready (if used)
            wait.until(webDriver -> {
                Boolean isJQueryReady = (Boolean) ((JavascriptExecutor) webDriver)
                        .executeScript("return (window.jQuery != null) && (jQuery.active === 0)");
                return isJQueryReady;
            });
        } catch (Exception e) {
            // jQuery not present, continue
        }

        // Wait for document to be ready
        wait.until(webDriver -> {
            String readyState = (String) ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState");
            return "complete".equals(readyState);
        });

        System.out.println("Page is fully loaded and ready");
    }
}
