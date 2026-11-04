package driver;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

import java.time.Duration;

/**
 * Topic 13: ThreadLocal = one browser per thread.
 *
 * When tests run in parallel, several threads run at the same time. A normal field
 * ("protected WebDriver driver") is SHARED, so thread B would overwrite thread A's browser.
 * ThreadLocal works like a locker per thread: same name, but each thread sees only its own value.
 *
 *   Thread 1: setDriver(chrome1) -> getDriver() returns chrome1
 *   Thread 2: setDriver(edge1)   -> getDriver() returns edge1     (no mix-up)
 *
 * Topic 14: Cross-browser. createDriver("chrome" | "edge" | "firefox") builds the right browser.
 * Polymorphism: the return type is always WebDriver, so tests never care which browser it is.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<String> BROWSER = new ThreadLocal<>();

    private DriverFactory() {
        // only static methods; no objects needed
    }

    /**
     * Builds a new browser. Headless (no window) when run with -Dheadless=true, e.g. on GitHub.
     *
     * synchronized = only ONE thread at a time may start a browser. Selenium Manager (which finds/downloads
     * chromedriver/msedgedriver) writes one shared cache file; 6 threads writing it at once corrupted it
     * ("se-metadata.json cannot be parsed"). Starting takes ~1 s; after that the tests run fully in parallel.
     */
    public static synchronized WebDriver createDriver(String browser) {
        boolean headless = Boolean.getBoolean("headless");
        switch (browser.toLowerCase()) {
            case "edge": {
                EdgeOptions options = new EdgeOptions();
                if (headless) options.addArguments("--headless=new", "--window-size=1920,1080");
                return new EdgeDriver(applyPageLoadRules(options));
            }
            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless", "--width=1920", "--height=1080");
                return new FirefoxDriver(applyPageLoadRules(options));
            }
            case "chrome": {
                ChromeOptions options = new ChromeOptions();
                if (headless) options.addArguments("--headless=new", "--window-size=1920,1080");
                return new ChromeDriver(applyPageLoadRules(options));
            }
            default:
                throw new IllegalArgumentException("Unknown browser '" + browser + "'. Use chrome, edge or firefox.");
        }
    }

    /**
     * Waiting strategy for page loads (same for every browser):
     * - EAGER: driver.get() returns when the HTML is ready (DOMContentLoaded), without waiting for every
     *   image/CSS file. A slow file on the server can no longer freeze the test. Elements are still
     *   waited for by the implicit/explicit waits.
     * - pageLoadTimeout 30 s: if a page does not come at all, fail after 30 s (with screenshot)
     *   instead of the default 300 s.
     */
    private static <T extends AbstractDriverOptions<?>> T applyPageLoadRules(T options) {
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.setPageLoadTimeout(Duration.ofSeconds(30));
        return options;
    }

    public static void setDriver(WebDriver driver, String browser) {
        DRIVER.set(driver);
        BROWSER.set(browser);
    }

    /** The browser that belongs to the current thread. */
    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    /** "chrome", "edge" or "firefox" for the current thread (used in logs and screenshot names). */
    public static String getBrowserName() {
        return BROWSER.get();
    }

    /** Close this thread's browser and empty its locker (remove() prevents memory leaks in thread pools). */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
        }
        DRIVER.remove();
        BROWSER.remove();
    }
}
