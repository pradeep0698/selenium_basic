package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Topic 1: Locators — the 8 ways to find an element on a page.
 * Best order to prefer: id > name > cssSelector > xpath.
 */
public class T01_LocatorsTest extends BaseTest {

    @Test
    public void findElementsInDifferentWays() {
        open("/login");

        WebElement byId = getDriver().findElement(By.id("username"));
        WebElement byName = getDriver().findElement(By.name("password"));
        WebElement byCss = getDriver().findElement(By.cssSelector("button[type='submit']"));
        WebElement byXpath = getDriver().findElement(By.xpath("//h2[text()='Login Page']"));
        WebElement byTag = getDriver().findElement(By.tagName("h2"));
        WebElement byClass = getDriver().findElement(By.className("radius"));
        WebElement byLinkText = getDriver().findElement(By.linkText("Elemental Selenium"));
        WebElement byPartialLink = getDriver().findElement(By.partialLinkText("Elemental"));

        Assert.assertTrue(byId.isDisplayed());
        Assert.assertTrue(byName.isDisplayed());
        Assert.assertEquals(byCss.getText().trim(), "Login");
        Assert.assertEquals(byXpath.getText(), byTag.getText());
        Assert.assertTrue(byClass.isEnabled());
        Assert.assertEquals(byLinkText.getAttribute("href"), byPartialLink.getAttribute("href"));
    }

    @Test
    public void findManyElements() {
        open("/");
        // findElements returns a list (empty if none found); findElement throws if none found.
        List<WebElement> links = getDriver().findElements(By.cssSelector("ul li a"));
        System.out.println("Number of example links: " + links.size());
        Assert.assertTrue(links.size() > 10);
    }
}
