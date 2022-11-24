package com.cydeo.utilities;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BrowserUtils {

    public static void sleep(int seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            System.out.println("Exception happened in sleep method");
        }
    }


    public static void verifyTitle(String expectedTitle) {
        Assert.assertEquals(expectedTitle, Driver.getDriver().getTitle());
    }


    public static void switchWindowAndVerify(String expectedInUrl, String expectedTitle) {

        Set<String> allWindows = Driver.getDriver().getWindowHandles();

        for (String eachWindow : allWindows) {
            Driver.getDriver().switchTo().window(eachWindow);
            if (Driver.getDriver().getCurrentUrl().contains(expectedInUrl)) ;
            break;
        }

        Assert.assertEquals(expectedTitle, Driver.getDriver().getTitle(), "Title verification failed");
    }

    public static List<String> dropdownOptionsAsString(WebElement dropdown) {

        Select select = new Select(dropdown);
        List<WebElement> actualOptionsAsWebElements = select.getOptions();
        List<String> actualOptions = new ArrayList<>();

        for (WebElement each : actualOptionsAsWebElements) {
            actualOptions.add(each.getText());
        }

        return actualOptions;
    }

    public static List<String> getModuleNames(List<WebElement> modules) {

        List<String> moduleNamesAsText = new ArrayList<>();

        for (WebElement each : modules) {
            moduleNamesAsText.add(each.getAttribute("aria-label"));
        }

        return moduleNamesAsText;
    }

    public static boolean areAllFilesSelected(List<WebElement> fileCheckboxes) {

        List<Boolean> flags = new ArrayList<>();

        for (WebElement each : fileCheckboxes) {
            flags.add(each.isSelected());
        }

        return (!flags.contains(false));
    }

    /**
     * Accept WebElement and return the text between opening and closing tags even for cases when getText() doesn't work
     * @param webElement
     * @return String
     */
    public static String getText(WebElement webElement){

        String text = null;
        text = webElement.getText();

        if(text.equals("")){

            text = webElement.getAttribute("innerText");

            if(text.equals("")){
                text = webElement.getAttribute("textContent");
            }
        }
        return text;
    }

    /**
     * Accept List<WebElement> and return List<String> of each text between opening and closing tags for that list of web elements
     * @param webElementList
     * @return List<String>
     */

    public static List<String> getText(List<WebElement> webElementList){

        List<String> textAsList = new ArrayList<>();

        for(WebElement each : webElementList){
            textAsList.add(getText(each));
        }
        return textAsList;
    }


}
