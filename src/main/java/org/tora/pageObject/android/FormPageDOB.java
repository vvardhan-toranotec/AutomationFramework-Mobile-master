package org.tora.pageObject.android;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class FormPageDOB {
    //public  FormPageDOB(AppiumDriver driver)
    public  FormPageDOB(AndroidDriver androidDriver)
    {
        PageFactory.initElements(new AppiumFieldDecorator(androidDriver), this);
    }
    @AndroidFindBy(id="com.androidsample.generalstore:id/nameField")
    public WebElement nameField;

    @AndroidFindBy(xpath = "//android.widget.RadioButton[@text='Female']")
    public WebElement gender;

    @AndroidFindBy(id="com.androidsample.generalstore:id/spinnerCountry")
    public WebElement clickCountryDropdown;

    @AndroidFindBy(id="com.androidsample.generalstore:id/productAddCart")
    public WebElement addCart;


}
