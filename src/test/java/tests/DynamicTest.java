package tests;

import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.DynamicPage;

public class DynamicTest extends BaseTest {
    private static final String HELLOWORLD_TEXT = "Hello World!";
    private static final String LINK_TEXT = "Dynamic Loading";
    private static final String LINK2_TEXT = "Example 1: Element on page that is hidden";
    private static final String LINK3_TEXT = "Example 2: Element rendered after the fact";
    private static final int TIMEOUT_SECONDS = 7;


    private void navigateAndWait(String linkText) {
        driver.findElement(By.linkText(LINK_TEXT)).click();
        driver.findElement(By.linkText(linkText)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#start button")));

        waitForPageToLoad();
    }

    @ParameterizedTest
    @ValueSource(strings = {LINK2_TEXT, LINK3_TEXT})
    public void testFinishElementVisibilityAndTextAfterClick(String exampleLinkText) {
        navigateAndWait(exampleLinkText);
        DynamicPage page = new DynamicPage(driver, TIMEOUT_SECONDS);
        page.clickButton();

        Assertions.assertTrue(page.isFinishElementVisible(), "Element should be displayed after button click");
        Assertions.assertEquals(HELLOWORLD_TEXT, page.getFinishText(), "Element text inconsistency");
    }

    @ParameterizedTest
    @ValueSource(strings = {LINK2_TEXT, LINK3_TEXT})
    public void testFinishElementInitialVisibility(String exampleLinkText) {
        navigateAndWait(exampleLinkText);
        DynamicPage page = new DynamicPage(driver, TIMEOUT_SECONDS);

        Assertions.assertFalse(page.isFinishElementVisible(), "Element should not be initially displayed");
    }
}
