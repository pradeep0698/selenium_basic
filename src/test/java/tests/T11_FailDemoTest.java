package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

/**
 * Topic 11: A test that FAILS ON PURPOSE.
 * Why: to prove the framework reports failures correctly. A broken test must show red,
 * with the error message and a screenshot of the page at the moment it failed.
 *
 * What you will see:
 * - Locally: red in the IDE, plus screenshots/FAILED_T11_FailDemoTest_wrongExpectedMessage.png
 * - On GitHub: the Actions run turns red; download "test-reports" to get the report + screenshot.
 *
 * After you have seen it, comment this class out in testng.xml so CI goes green again.
 */
public class T11_FailDemoTest extends BaseTest {

    @Test(description = "Fails on purpose: expects a message the page never shows")
    public void wrongExpectedMessage() {
        LoginPage page = new LoginPage(driver).open();
        page.login("tomsmith", "SuperSecretPassword!");

        // Real message is "You logged into a secure area!" -> this assert fails on purpose.
        Assert.assertTrue(page.getMessage().contains("Welcome to the dashboard"),
                "Expected welcome message, but page said: " + page.getMessage());
    }
}
