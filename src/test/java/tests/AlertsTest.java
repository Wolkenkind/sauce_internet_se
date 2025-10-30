package tests;

import exceptions.AlertNotFoundException;
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
import tests.common.AlertType;
import tests.common.BaseTest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static exceptions.ComponentConstants.ALERTS;

public class AlertsTest extends BaseTest {
    private static final int TIMEOUT_SECONDS = 5;

    private static final String LINK_TEXT = "JavaScript Alerts";
    private static final String ALERT_TEXT = "You successfully clicked an alert";
    private static final String CONFIRM_TEXT = "You clicked: ";
    private static final String CONFIRM_OK_TEXT = "Ok";
    private static final String CONFIRM_CANCEL_TEXT = "Cancel";
    private static final String PROMPT_TEXT = "You entered: ";
    private static final String PROMPT_NULL_TEXT = "null";
    private static final String TEST_TEXT = "test";

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

    public record ResultCorrectTextTestData(
            Consumer<AlertsPage> pageClickFunction,
            AlertType type,
            String expectedText
    ){}

    public static List<Arguments> resultCorrectTextTestData() {
        List<Arguments> args = new ArrayList<>();

        Consumer<AlertsPage> alertClick = AlertsPage::clickAlertButton;
        ResultCorrectTextTestData clickData = new ResultCorrectTextTestData(alertClick, AlertType.ALERT, ALERT_TEXT);
        Consumer<AlertsPage> confirmClick = AlertsPage::clickConfirmButton;
        var confirmData = new ResultCorrectTextTestData(confirmClick, AlertType.CONFIRM, CONFIRM_TEXT);
        Consumer<AlertsPage> promptClick = AlertsPage::clickPromptButton;
        var promptData = new ResultCorrectTextTestData(promptClick, AlertType.PROMPT, PROMPT_TEXT);

        args.add(Arguments.of(Named.named("Alert", clickData)));
        args.add(Arguments.of(Named.named("Confirm", confirmData)));
        args.add(Arguments.of(Named.named("Prompt", promptData)));

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
        AlertsPage page = new AlertsPage(driver, TIMEOUT_SECONDS);
        Assertions.assertTrue(page.getResultText().isBlank(), "Result message must be empty");
    }

    @ParameterizedTest
    @MethodSource("alertsShowingTestData")
    public void testClickAlertShowsAlert(Consumer<AlertsPage> pageClickFunction) {
        AlertsPage page = new AlertsPage(driver, TIMEOUT_SECONDS);
        pageClickFunction.accept(page);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        Assertions.assertNotNull(alert, "Alert should be shown");
    }

    @ParameterizedTest
    @MethodSource("resultCorrectTextTestData")
    public void testResultShowsCorrespondingTextAfterClick(ResultCorrectTextTestData data) {
        AlertsPage page = new AlertsPage(driver, TIMEOUT_SECONDS);
        switch (data.type()) {
            case ALERT -> testAlert(data, page);
            case CONFIRM -> testConfirm(data, page);
            case PROMPT -> testPrompt(data, page);
        }
    }

    private void testAlert(ResultCorrectTextTestData data, AlertsPage page) {
        data.pageClickFunction().accept(page);
        waitForAlertAndThrowIfNotPresent();
        Alert alert = driver.switchTo().alert();
        alert.accept();
        Assertions.assertEquals(data.expectedText(), ALERT_TEXT, "Result text for alert differs from expected");
    }

    private void testConfirm(ResultCorrectTextTestData data, AlertsPage page) {
        data.pageClickFunction().accept(page);
        waitForAlertAndThrowIfNotPresent();
        Alert confirm = driver.switchTo().alert();
        confirm.accept();

        String expected = data.expectedText() + CONFIRM_OK_TEXT;
        String actual = page.getResultText();
        Assertions.assertEquals(expected, actual, "Result text for confirm differs from expected for OK");

        data.pageClickFunction().accept(page);
        waitForAlertAndThrowIfNotPresent();
        confirm = driver.switchTo().alert();
        confirm.dismiss();
        expected = data.expectedText() + CONFIRM_CANCEL_TEXT;
        actual = page.getResultText();
        Assertions.assertEquals(expected, actual, "Result text for confirm differs from expected for cancel");
    }

    private void testPrompt(ResultCorrectTextTestData data, AlertsPage page) {
        data.pageClickFunction().accept(page);
        waitForAlertAndThrowIfNotPresent();
        Alert prompt = driver.switchTo().alert();
        prompt.sendKeys(TEST_TEXT);
        prompt.accept();

        String expected = data.expectedText() + TEST_TEXT;
        String actual = page.getResultText();
        Assertions.assertEquals(expected, actual, "Result text for prompt differs from expected for accepting");

        data.pageClickFunction().accept(page);
        waitForAlertAndThrowIfNotPresent();
        prompt = driver.switchTo().alert();
        prompt.sendKeys(TEST_TEXT);
        prompt.dismiss();

        expected = data.expectedText() + PROMPT_NULL_TEXT;
        actual = page.getResultText();
        Assertions.assertEquals(expected, actual, "Result text for prompt differs from expected for canceling");
    }

    private void waitForAlertAndThrowIfNotPresent() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        if (alert == null) {
            throw new AlertNotFoundException(
                    ALERTS,
                    AlertsTest.class.toString(),
                    TIMEOUT_SECONDS,
                    "Alert for interaction not found!"
            );
        }
    }
}
