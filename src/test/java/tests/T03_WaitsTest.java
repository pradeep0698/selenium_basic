package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Topic 3: Waits. Pages load things late; tests must wait for them.
 * - Implicit wait: set once in BaseTest, applies to every findElement.
 * - Explicit wait (below): wait for ONE condition, e.g. visible / clickable. Preferred.
 * - Never use Thread.sleep: it always waits the full time and still breaks on slow days.
 */
public class T03_WaitsTest extends BaseTest {

    @Test
    public void explicitWaitForHiddenElement() {
        open("/dynamic_loading/1");
        driver.findElement(By.cssSelector("#start button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));

        Assert.assertEquals(result.getText(), "Hello World!");
    }

    @Test
    public void explicitWaitForElementAddedLater() {
        open("/dynamic_loading/2");
        driver.findElement(By.cssSelector("#start button")).click();

        WebElement result = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#finish h4")));

        Assert.assertEquals(result.getText(), "Hello World!");
    }
}
