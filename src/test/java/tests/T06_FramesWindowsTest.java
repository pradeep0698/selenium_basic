package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * Topic 6: Frames (a page inside a page) and multiple tabs/windows.
 * Selenium only sees the current frame/window, so you must switch to it first.
 */
public class T06_FramesWindowsTest extends BaseTest {

    @Test
    public void nestedFrames() {
        open("/nested_frames");
        driver.switchTo().frame("frame-top");      // by name or id
        driver.switchTo().frame("frame-middle");
        Assert.assertEquals(driver.findElement(By.id("content")).getText(), "MIDDLE");

        driver.switchTo().parentFrame();           // one level up
        driver.switchTo().frame("frame-left");
        Assert.assertTrue(driver.findElement(By.tagName("body")).getText().contains("LEFT"));

        driver.switchTo().defaultContent();        // back to the main page
        driver.switchTo().frame("frame-bottom");
        Assert.assertTrue(driver.findElement(By.tagName("body")).getText().contains("BOTTOM"));
    }

    @Test
    public void newWindow() {
        open("/windows");
        String mainWindow = driver.getWindowHandle();
        driver.findElement(By.linkText("Click Here")).click();

        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);  // go to the new tab
            }
        }
        Assert.assertEquals(driver.findElement(By.tagName("h3")).getText(), "New Window");

        driver.close();                            // close the new tab only
        driver.switchTo().window(mainWindow);
        Assert.assertEquals(driver.findElement(By.tagName("h3")).getText(), "Opening a new window");
    }
}
