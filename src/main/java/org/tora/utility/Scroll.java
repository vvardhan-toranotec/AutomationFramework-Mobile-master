package org.tora.utility;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class Scroll {
    //AndroidDriver androidDriver;
    AppiumDriver driver;
    public Scroll(AppiumDriver driver){
    //public Scroll(AndroidDriver androidDriver){
    this.driver=driver;
    }

public void scrollToSpecificItem(String name){
    driver.findElement(AppiumBy.androidUIAutomator(
            "new UiScrollable(new UiSelector().scrollable(true))" +
                    ".scrollIntoView(new UiSelector().resourceIdMatches(\"com.androidsample.generalstore:id/productName\").text(\""+name+"\"))")).click();

}
}
