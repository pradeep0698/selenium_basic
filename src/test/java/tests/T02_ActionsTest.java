package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Topic 2: Basic element actions — click, type, clear, read text, check state.
 */
public class T02_ActionsTest extends BaseTest {

    @Test
    public void typeClearAndClick() {
        open("/login");

        WebElement user = driver.findElement(By.id("username"));
        user.sendKeys("wrong-user");                       // type
        Assert.assertEquals(user.getAttribute("value"), "wrong-user");
        user.clear();                                       // clear
        Assert.assertEquals(user.getAttribute("value"), "");

        driver.findElement(By.cssSelector("button[type='submit']")).click(); // click
        String msg = driver.findElement(By.id("flash")).getText();           // read text
        Assert.assertTrue(msg.contains("Your username is invalid!"));
    }

    @Test
    public void checkboxes() {
        open("/checkboxes");
        WebElement first = driver.findElement(By.cssSelector("#checkboxes input:nth-of-type(1)"));
        Assert.assertFalse(first.isSelected());
        first.click();
        Assert.assertTrue(first.isSelected());               // state checks: isSelected, isEnabled, isDisplayed
    }

    @Test
    public void browserCommands() {
        open("/");
        Assert.assertEquals(driver.getTitle(), "The Internet");
        driver.findElement(By.linkText("Checkboxes")).click();
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/checkboxes"));
        driver.navigate().back();                           // also: forward(), refresh()
        Assert.assertTrue(driver.getCurrentUrl().endsWith("herokuapp.com/"));
    }
}
