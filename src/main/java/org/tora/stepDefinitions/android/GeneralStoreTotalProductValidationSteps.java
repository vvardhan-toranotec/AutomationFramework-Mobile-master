package org.tora.stepDefinitions.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.testng.Assert;
import org.tora.generic.BaseAll;
import org.tora.pageObject.android.FormPageDOB;
import org.tora.utility.Scroll;

import java.time.Duration;

public class GeneralStoreTotalProductValidationSteps extends BaseAll{

        @Given("^User launch the app$")
        public void user_launch () {
            AndroidDriver androidDriver = (AndroidDriver) appiumDriver;
            TouchActions touchActions = new TouchActions(androidDriver);
            FormPageDOB formPage = new FormPageDOB(androidDriver);
            Scroll scroll = new Scroll(androidDriver);
            androidDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(14000));
            formPage.nameField.sendKeys("Bhanu");
            androidDriver.hideKeyboard();
            formPage.gender.click();
            formPage.clickCountryDropdown.click();
            androidDriver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().text(\"Angola\"))")).click();
            androidDriver.findElement(By.id("com.androidsample.generalstore:id/btnLetsShop")).click();
            scroll.scrollToSpecificItem("Nike SFB Jungle");
        /*androidDriver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().resourceIdMatches(\"com.androidsample.generalstore:id/productName\").text(\"Nike SFB Jungle\"))")).click();
       */
            int count = androidDriver.findElements(By.id("com.androidsample.generalstore:id/productName")).size();
            for (int i = 0; i < count; i++) {
                String prodName = androidDriver.findElements(By.id("com.androidsample.generalstore:id/productName")).get(i).getText();
                if (prodName.equalsIgnoreCase("Nike SFB Jungle")) {
                    //androidDriver.findElement(By.id("com.androidsample.generalstore:id/productAddCart")).click();
                    androidDriver.findElements(By.id("com.androidsample.generalstore:id/productAddCart")).get(i).click();
                    break;
                }
            }
            androidDriver.findElement(By.id("com.androidsample.generalstore:id/appbar_btn_cart")).click();
            int countProductPrice = androidDriver.findElements(By.id("com.androidsample.generalstore:id/productPrice")).size();
            double sum = 0;
            for (int i = 0; i < countProductPrice; i++) {
                String stAmount = androidDriver.findElements(By.id("com.androidsample.generalstore:id/productPrice")).get(i).getText();
                System.out.println("Amount is in string :" + stAmount);
                String amount = stAmount.substring(1);
                double integerAmount = Double.parseDouble(amount);
                sum = sum + integerAmount;//280.97+116.97

            }

            System.out.println("Total Amount is :" + sum);
            String total = androidDriver.findElement(By.id("com.androidsample.generalstore:id/totalAmountLbl")).getText();
            total = total.substring(1);
            double totalValue = Double.parseDouble(total);
            Assert.assertEquals(sum, totalValue, 116.97);
            //Assert.assertEquals("Toatal amount verify", sum, totalValue);

        }

        @When("^User added products to cart$")
        public void user_added_products_to_cart () {

            Assert.assertTrue(true);
        }

        @When("^Verify total product values$")
        public void verify_total_products_values (){
            Assert.assertTrue(true);
        }

    @Given("^User launch the app1$")
    public void launch_app1 (){
        Assert.assertTrue(true);
    }
}
