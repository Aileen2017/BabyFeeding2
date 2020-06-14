package com.projects.babyfeeding2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BabyFeeding2Test {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.projects.babyfeeding2", appContext.getPackageName());
    }

    @Test
    public void testCalendar(){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND,0);
        today.set(Calendar.SECOND,0);
        today.set(Calendar.MINUTE,0);
        today.set(Calendar.HOUR,0);
        Calendar pagerDay = Calendar.getInstance();
        pagerDay.set(Calendar.DAY_OF_MONTH, 12);
        pagerDay.set(Calendar.MONTH, Calendar.MAY);
        pagerDay.set(Calendar.YEAR, 2020);
        pagerDay.set(Calendar.MILLISECOND,0);
        pagerDay.set(Calendar.SECOND,0);
        pagerDay.set(Calendar.MINUTE,0);
        pagerDay.set(Calendar.HOUR,0);
        long laggedNumber = TimeUnit.DAYS.convert((today.getTime().getTime() - pagerDay.getTime().getTime()),  TimeUnit.MILLISECONDS);
        System.out.println(laggedNumber);
        assertEquals(33, laggedNumber);


    }

}