package com.cydeo.step_definitions;

import com.cydeo.pages.DashBoardPage;
import com.cydeo.pages.LoginPage;
import com.cydeo.utilities.ConfigurationReader;
import com.cydeo.utilities.DB_util;
import com.cydeo.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class BorrowedNumberBooks_StepDef {

    LoginPage loginPage = new LoginPage();
    DashBoardPage dashBoardPage = new DashBoardPage();


    @Given("user login as a librarian")
    public void user_login_as_a_librarian() {
        Driver.getDriver().get(ConfigurationReader.getProperty("libraryapp.url"));
        loginPage.login(ConfigurationReader.getProperty("libraryapp.librarianUsername"),
                ConfigurationReader.getProperty("libraryapp.librarianPassword"));
    }

    String actualNumber;

    @When("user take borrowed books number")
    public void user_take_borrowed_books_number() {
        actualNumber = dashBoardPage.borrowedBooksNumber.getAttribute("innerHTML");
        System.out.println("actualNumber = " + actualNumber);
    }

    @Then("borrowed books number information must match with DB")
    public void borrowed_books_number_information_must_match_with_db() {
        String query = "select count(*) from book_borrow\n" +
                "where is_returned=0";
        DB_util.runQuery(query);
        String expectedNumber = DB_util.getFirstRowFirstColumn();
        Assert.assertEquals(expectedNumber, actualNumber);
    }

}
