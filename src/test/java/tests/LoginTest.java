package tests;

import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        driver.findElement(By.linkText("Form Authentication")).click();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.setUsername("tomsmith");
        loginPage.setPassword("SuperSecretPassword!");
        loginPage.clickLoginButton();

        String actualMessage = loginPage.getFlashMessageText();
        Assertions.assertTrue(
                actualMessage.contains("You logged into a secure area!"),
                "Actual message was: " + actualMessage
        );
    }

    @Test
    public void testLoginWithInvalidPassword() {
        driver.findElement(By.linkText("Form Authentication")).click();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.setUsername("tomsmith");
        loginPage.setPassword("WrongPass");
        loginPage.clickLoginButton();

        String actualMessage = loginPage.getFlashMessageText();
        Assertions.assertTrue(
                actualMessage.contains("Your password is invalid!"),
                "Actual message was: " + actualMessage
        );
    }
}
