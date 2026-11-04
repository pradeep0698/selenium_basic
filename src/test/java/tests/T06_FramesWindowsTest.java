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
        getDriver().switchTo().frame("frame-top");      // by name or id
        getDriver().switchTo().frame("frame-middle");
        Assert.assertEquals(getDriver().findElement(By.id("content")).getText(), "MIDDLE");

        getDriver().switchTo().parentFrame();           // one level up
        getDriver().switchTo().frame("frame-left");
        Assert.assertTrue(getDriver().findElement(By.tagName("body")).getText().contains("LEFT"));

        getDriver().switchTo().defaultContent();        // back to the main page
        getDriver().switchTo().frame("frame-bottom");
        Assert.assertTrue(getDriver().findElement(By.tagName("body")).getText().contains("BOTTOM"));
    }

    @Test
    public void newWindow() {
        open("/windows");
        String mainWindow = getDriver().getWindowHandle();
        getDriver().findElement(By.linkText("Click Here")).click();

        Set<String> handles = getDriver().getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(mainWindow)) {
                getDriver().switchTo().window(handle);  // go to the new tab
            }
        }
        Assert.assertEquals(getDriver().findElement(By.tagName("h3")).getText(), "New Window");

        getDriver().close();                            // close the new tab only
        getDriver().switchTo().window(mainWindow);
        Assert.assertEquals(getDriver().findElement(By.tagName("h3")).getText(), "Opening a new window");
    }
}
