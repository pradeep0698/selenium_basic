package tests;

import base.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Topic 8: Screenshots. Saved to the screenshots/ folder in the project.
 * Real projects take one automatically when a test fails (see TestNG listeners).
 */
public class T08_ScreenshotTest extends BaseTest {

    @Test
    public void takeScreenshot() throws IOException {
        open("/");
        File temp = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        Path target = Path.of("screenshots", "home.png");
        Files.createDirectories(target.getParent());
        Files.copy(temp.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

        Assert.assertTrue(Files.size(target) > 0);
        System.out.println("Screenshot saved: " + target.toAbsolutePath());
    }
}
