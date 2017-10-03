package com.test.selenium.testscript;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.test.selenium.testscript.Keywords;
import com.test.selenium.testscript.TestDriver;

public class MITTestDriver {

    public static Logger APP_LOGS;

    public static void main(String[] args) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, IOException,
            NumberFormatException, InterruptedException, JSONException {
        Keywords kw = new Keywords();

        TestDriver ts = new TestDriver(kw,"/Users/212316563/rd/test/astautotest/trunk/SeleniumAutomation/src/com/ge/selenium/config/config.properties");
                ts.start();
    }
}