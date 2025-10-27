package tests;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.CheckboxesPage;

import static org.junit.jupiter.api.Assertions.*;

public class CheckboxesTest extends BaseTest {

    @BeforeEach
    public void navigateAndWait() {
        driver.findElement(By.linkText("Checkboxes")).click();
        // Wait for checkboxes page to load
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(CheckboxesPage.CHECKBOXES_CSS)));
    }

    @Test
    public void testCheckbox1StartsUnchecked() {
        CheckboxesPage page = new CheckboxesPage(driver);
        assertFalse(page.getCheckbox1Value(), "Checkbox 1 should start unchecked");
    }

    @Test
    public void testCheckbox2StartsChecked() {
        CheckboxesPage page = new CheckboxesPage(driver);
        assertTrue(page.getCheckbox2Value(), "Checkbox 2 should start checked");
    }

    @Test
    public void testCanToggleCheckbox1() {
        CheckboxesPage page = new CheckboxesPage(driver);

        boolean initial = page.getCheckbox1Value();
        page.clickCheckbox1();
        assertNotEquals(initial, page.getCheckbox1Value(), "Click should toggle state");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testSetCheckboxToSpecificState(boolean desiredState) {
        CheckboxesPage page = new CheckboxesPage(driver);

        page.setCheckbox1(desiredState);
        assertEquals(desiredState, page.getCheckbox1Value(),
                "Checkbox should be set to " + desiredState);
    }

    // Test the PATTERN works for multiple elements
    @Test
    public void testBothCheckboxesCanBeIndependentlyControlled() {
        CheckboxesPage page = new CheckboxesPage(driver);

        // Set to opposite states
        page.setCheckbox1(true);
        page.setCheckbox2(false);

        // Verify they maintain independent states
        assertTrue(page.getCheckbox1Value(), "Checkbox 1 should be true");
        assertFalse(page.getCheckbox2Value(), "Checkbox 2 should be false");
    }

}
