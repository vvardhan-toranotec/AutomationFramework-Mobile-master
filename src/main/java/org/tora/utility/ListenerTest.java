package org.tora.utility;


import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.tora.generic.Base;
import org.tora.generic.BaseAll;

import java.io.IOException;

public class ListenerTest implements ITestListener {
    @Override
    public void onFinish(ITestContext Result)
    {
        System.out.println(Result.getName()+" All test case execution finished");
    }

    @Override
    public void onStart(ITestContext Result)
    {

    }


    // When Test case get failed, this method is called.
    @Override
    public void onTestFailure(ITestResult Result)
    {
        System.out.println("The name of the testcase failed is :"+Result.getName());
        try {
            BaseAll.getScreenShot(Result.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // When Test case get Skipped, this method is called.
    @Override
    public void onTestSkipped(ITestResult Result)
    {
        System.out.println("The name of the testcase Skipped is :"+Result.getName());
    }

    // When Test case get Started, this method is called.
    @Override
    public void onTestStart(ITestResult Result)
    {
        System.out.println(Result.getName()+" test case started");
    }

    // When Test case get passed, this method is called.
    @Override
    public void onTestSuccess(ITestResult Result)
    {
        System.out.println("The name of the testcase passed is :"+Result.getName());
    }

}
