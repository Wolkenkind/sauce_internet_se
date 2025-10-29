package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AlertsPage;
import tests.common.BaseTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class AlertsTest extends BaseTest {

    private static final String LINK_TEXT = "JavaScript Alerts";

    public static List<Arguments> alertsShowingTestData() {
        List<Arguments> args = new ArrayList<>();

        Consumer<AlertsPage> alertClick = AlertsPage::clickAlertButton;
        Consumer<AlertsPage> confirmClick = AlertsPage::clickConfirmButton;
        Consumer<AlertsPage> promptClick = AlertsPage::clickPromptButton;

        args.add(Arguments.of(Named.named("Alert", alertClick)));
        args.add(Arguments.of(Named.named("Confirm", confirmClick)));
        args.add(Arguments.of(Named.named("Prompt", promptClick)));

        return args;
    }

    @BeforeEach
    public void navigateAndWait() {
        driver.findElement(By.linkText(LINK_TEXT)).click();
        // Wait for buttons page to load
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(AlertsPage.ALERT_BUTTON_XPATH)));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(AlertsPage.CONFIRM_BUTTON_XPATH)));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(AlertsPage.PROMPT_BUTTON_XPATH)));
    }

    @Test
    public void testInitiallyResultShowsNoText() {
        AlertsPage page = new AlertsPage(driver, 5);
        Assertions.assertTrue(page.getResultText().isBlank(), "Result message must be empty");
    }

    @ParameterizedTest
    @MethodSource("alertsShowingTestData")
    public void testClickAlertShowsAlert(Consumer<AlertsPage> pageClickFunction) throws InterruptedException {
        AlertsPage page = new AlertsPage(driver, 5);
        pageClickFunction.accept(page);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        Thread.sleep(5000);
        Assertions.assertNotNull(alert, "Alert should be shown");
    }

    @Test
    public void testResultShowsCorrespondingTextAfterClick() {

    }
}
