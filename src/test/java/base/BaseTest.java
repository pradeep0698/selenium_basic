package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

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

    protected WebDriver driver;
    protected static final String BASE_URL = "https://the-internet.herokuapp.com";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        WebDriver chrome = new ChromeDriver(options);
        driver = SLOW_MS > 0 ? new EventFiringDecorator<WebDriver>(new SlowMotion(chrome)).decorate(chrome) : chrome;
        driver.manage().window().maximize();
        // Implicit wait: findElement retries up to 5 s before failing. Keep it small.
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            pause(SLOW_MS * 2);  // see the final page before it closes
            driver.quit();       // quit() closes every window and ends the session; close() closes only one window
        }
    }

    protected void open(String path) {
        driver.get(BASE_URL + path);
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
