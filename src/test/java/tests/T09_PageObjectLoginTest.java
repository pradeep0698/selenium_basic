package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

/**
 * Topic 9: Page Object Model. Compare with T02: no locators here, just readable steps.
 * Also shows TestNG features: priority, description, groups.
 */
public class T09_PageObjectLoginTest extends BaseTest {

    @Test(priority = 1, description = "Valid user can log in", groups = "smoke")
    public void validLogin() {
        LoginPage page = new LoginPage(getDriver()).open();
        page.login("tomsmith", "SuperSecretPassword!");
        Assert.assertTrue(page.getMessage().contains("You logged into a secure area!"));
    }

    @Test(priority = 2, description = "Wrong password shows an error", groups = "regression")
    public void wrongPassword() {
        LoginPage page = new LoginPage(getDriver()).open();
        page.login("tomsmith", "wrong");
        Assert.assertTrue(page.getMessage().contains("Your password is invalid!"));
    }
}
