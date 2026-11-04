package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Topic 7: Mouse and keyboard with the Actions class, plus JavascriptExecutor.
 * Actions also does: doubleClick, contextClick (right click), dragAndDrop, clickAndHold.
 */
public class T07_MouseKeyboardTest extends BaseTest {

    @Test
    public void hover() {
        open("/hovers");
        WebElement firstAvatar = getDriver().findElement(By.cssSelector(".figure"));
        new Actions(getDriver()).moveToElement(firstAvatar).perform();   // mouse over
        Assert.assertTrue(getDriver().findElement(By.xpath("//h5[text()='name: user1']")).isDisplayed());
    }

    @Test
    public void rightClick() {
        open("/context_menu");
        new Actions(getDriver()).contextClick(getDriver().findElement(By.id("hot-spot"))).perform();
        Assert.assertEquals(getDriver().switchTo().alert().getText(), "You selected a context menu");
        getDriver().switchTo().alert().accept();
    }

    @Test
    public void keyboardKeys() {
        open("/key_presses");
        new Actions(getDriver()).sendKeys(Keys.ENTER).perform();
        Assert.assertEquals(getDriver().findElement(By.id("result")).getText(), "You entered: ENTER");
    }

    @Test
    public void javascriptScroll() {
        open("/large");
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Long scrolled = (Long) js.executeScript("return Math.round(window.pageYOffset)");
        Assert.assertTrue(scrolled > 0);
    }
}
