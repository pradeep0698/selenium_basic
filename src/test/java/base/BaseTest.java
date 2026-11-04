package base;

import driver.DriverFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

/**
 * Every test class extends this. It opens Chrome before each test and closes it after.
 * Run without a visible browser (needed in CI): mvn test -Dheadless=true
 *
 * Slow mode (for learning): every click / typing is outlined in red and pauses SLOW_MS,
 * and the browser stays open a moment at the end. Set SLOW_MS = 0 (or -Dslow=0) for full speed.
 */
public class BaseTest {

    private static final long SLOW_MS = Long.getLong("slow", 1000);

    protected static final String BASE_URL = "https://the-internet.herokuapp.com";

    /**
     * Before: "protected WebDriver driver" field -> shared, breaks in parallel.
     * Now: each thread keeps its own browser in DriverFactory (ThreadLocal). Tests call getDriver().
     */
    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    @BeforeMethod
    public void setUp(Method method) {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        WebDriver chrome = new ChromeDriver(options);
        WebDriver driver = SLOW_MS > 0 ? new EventFiringDecorator<WebDriver>(new SlowMotion(chrome)).decorate(chrome) : chrome;
        driver.manage().window().maximize();
        // Implicit wait: findElement retries up to 5 s before failing. Keep it small.
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        DriverFactory.setDriver(driver);   // put this browser in THIS thread's locker

        log("started Chrome for " + getClass().getSimpleName() + "." + method.getName());
    }

    /**
     * TestNG passes ITestResult = the outcome of the test that just ran.
     * If it failed, save a screenshot BEFORE quitting the browser (after quit() there is no page to capture).
     */
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (getDriver() != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                saveFailureScreenshot(result);
            }
            pause(SLOW_MS * 2);           // see the final page before it closes
            log("closing Chrome for " + getClass().getSimpleName() + "." + result.getMethod().getMethodName());
            DriverFactory.quitDriver();   // quit() closes every window; close() would close only one
        }
    }

    protected void open(String path) {
        getDriver().get(BASE_URL + path);
    }

    /** Prints with the thread name, so in parallel runs you can see which thread did what. */
    private static void log(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message);
    }

    /** Saves screenshots/FAILED_<Class>_<method>.png. CI uploads this folder with the reports. */
    private void saveFailureScreenshot(ITestResult result) {
        String name = "FAILED_" + result.getTestClass().getRealClass().getSimpleName()
                + "_" + result.getMethod().getMethodName() + ".png";
        try {
            File temp = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            Path target = Path.of("screenshots", name);
            Files.createDirectories(target.getParent());
            Files.copy(temp.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Test failed, screenshot saved: " + target.toAbsolutePath());
        } catch (IOException | RuntimeException e) {
            // never let a screenshot problem hide the real test failure
            System.out.println("Could not save failure screenshot: " + e.getMessage());
        }
    }

    private static void pause(long ms) {
        try {
            Thread.sleep(ms); // only for watching; real tests use waits (see T03_WaitsTest)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Listener: Selenium calls these before/after each action. Used here only to slow down and highlight. */
    private static class SlowMotion implements WebDriverListener {
        private final JavascriptExecutor js;

        SlowMotion(WebDriver realDriver) {
            this.js = (JavascriptExecutor) realDriver;
        }

        private void highlight(WebElement element) {
            try {
                js.executeScript("arguments[0].style.outline='3px solid red'", element);
            } catch (RuntimeException ignored) {
                // element may be gone already; highlighting is only a visual aid
            }
            pause(SLOW_MS);
        }

        @Override
        public void afterGet(WebDriver driver, String url) {
            pause(SLOW_MS);
        }

        @Override
        public void beforeClick(WebElement element) {
            highlight(element);
        }

        @Override
        public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
            highlight(element);
        }

        @Override
        public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
            pause(SLOW_MS);
        }
    }
}
