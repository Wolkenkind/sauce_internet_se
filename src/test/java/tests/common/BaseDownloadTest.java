package tests.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.FileUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseDownloadTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");

        options.addArguments("--window-size=1920,1080");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-web-security");

        Map<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", FileUtils.getDownloadPath());
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.directory_upgrade", true);
        chromePrefs.put("plugins.always_open_pdf_externally", true);
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("safebrowsing.enabled", true);
        chromePrefs.put("safebrowsing.disable_download_protection", true);

        options.setExperimentalOption("prefs", chromePrefs);

        driver = new ChromeDriver(options);
        //driver.manage().window().maximize();

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
