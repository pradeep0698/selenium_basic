package tests;

import base.BaseTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Topic 5: JavaScript pop-ups (alert, confirm, prompt). Switch to the alert, then accept/dismiss/type.
 */
public class T05_AlertsTest extends BaseTest {

    @Test
    public void simpleAlert() {
        open("/javascript_alerts");
        getDriver().findElement(By.xpath("//button[text()='Click for JS Alert']")).click();
        Alert alert = getDriver().switchTo().alert();
        Assert.assertEquals(alert.getText(), "I am a JS Alert");
        alert.accept();                                         // press OK
        Assert.assertEquals(result(), "You successfully clicked an alert");
    }

    @Test
    public void confirmCancel() {
        open("/javascript_alerts");
        getDriver().findElement(By.xpath("//button[text()='Click for JS Confirm']")).click();
        getDriver().switchTo().alert().dismiss();                    // press Cancel
        Assert.assertEquals(result(), "You clicked: Cancel");
    }

    @Test
    public void promptTypeText() {
        open("/javascript_alerts");
        getDriver().findElement(By.xpath("//button[text()='Click for JS Prompt']")).click();
        Alert prompt = getDriver().switchTo().alert();
        prompt.sendKeys("Selenium");
        prompt.accept();
        Assert.assertEquals(result(), "You entered: Selenium");
    }

    private String result() {
        return getDriver().findElement(By.id("result")).getText();
    }
}
