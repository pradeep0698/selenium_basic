package driver;

import org.openqa.selenium.WebDriver;

/**
 * Topic 13: ThreadLocal = one browser per thread.
 *
 * When tests run in parallel, several threads run at the same time. A normal field
 * ("protected WebDriver driver") is SHARED, so thread B would overwrite thread A's browser.
 * ThreadLocal works like a locker per thread: same name, but each thread sees only its own value.
 *
 *   Thread 1: setDriver(chrome1) -> getDriver() returns chrome1
 *   Thread 2: setDriver(chrome2) -> getDriver() returns chrome2   (no mix-up)
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
        // only static methods; no objects needed
    }

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    /** The browser that belongs to the current thread. */
    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    /** Close this thread's browser and empty its locker (remove() prevents memory leaks in thread pools). */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
