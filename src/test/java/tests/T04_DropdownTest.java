package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Topic 4: Dropdowns (<select> elements) with the Select class.
 */
public class T04_DropdownTest extends BaseTest {

    @Test
    public void selectOptions() {
        open("/dropdown");
        Select dropdown = new Select(getDriver().findElement(By.id("dropdown")));

        dropdown.selectByVisibleText("Option 1");
        Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option 1");

        dropdown.selectByValue("2");
        Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option 2");

        dropdown.selectByIndex(1);
        Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option 1");

        Assert.assertEquals(dropdown.getOptions().size(), 3); // includes the "Please select" placeholder
    }
}
