package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;

/**
 * Topic 10: Data-driven testing. One test method runs once per row of data.
 * Rows could also come from Excel, CSV or JSON files.
 */
public class T10_DataDrivenTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
                // username,   password,                 expected message
                {"tomsmith", "SuperSecretPassword!", "You logged into a secure area!"},
                {"tomsmith", "wrong", "Your password is invalid!"},
                {"nobody", "SuperSecretPassword!", "Your username is invalid!"},
        };
    }

    @Test(dataProvider = "loginData")
    public void login(String user, String pass, String expected) {
        LoginPage page = new LoginPage(driver).open();
        page.login(user, pass);
        Assert.assertTrue(page.getMessage().contains(expected),
                "For " + user + "/" + pass + " got: " + page.getMessage());
    }
}
