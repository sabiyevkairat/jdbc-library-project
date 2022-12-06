package com.cydeo.pages;

import com.cydeo.utilities.ConfigurationReader;
import com.cydeo.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    public LoginPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(id = "inputEmail")
    public WebElement emailBox;

    @FindBy(id = "inputPassword")
    public WebElement passwordBox;

    @FindBy(tagName = "button")
    public WebElement loginButton;


    public void login(String userName, String password) {

        userName = ConfigurationReader.getProperty("libraryapp.librarianUsername");
        password = ConfigurationReader.getProperty("libraryapp.librarianPassword");


        emailBox.sendKeys(userName);
        passwordBox.sendKeys(password);
        loginButton.click();
    }

}
