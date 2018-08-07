package com.veeritsolutions.uhelpme.utility;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import com.veeritsolutions.uhelpme.MyApplication;


public class MyLifecycleHandler implements ActivityLifecycleCallbacks {
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    private int resumed;
    private int paused;
    private int started;
    private int stopped;
    

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    	Debug.trace("test", "onActivityDestroyed: started" + started + "resumed: " + resumed + " paused: "+ paused + " stopped" + stopped);
    	
    	if(resumed==paused && resumed==started && resumed==stopped)
    	{
            Debug.trace("MyLifecycleHandler", "App is closed");
            MyApplication.getInstance().setAppRunnning(false);
    		//MyApplication.isAppRunnning=false;
//            ToastHelper.displayInfo("App is closed", Gravity.CENTER);
            //Toast.makeText(activity,"App is closed",Toast.LENGTH_SHORT).show();

            /*Clean Temp Directory, on close app */
            //cleanImageDirectories();
    	}
    	else
            MyApplication.getInstance().setAppRunnning(true);
    		//MyApplication.isAppRunnning=true;
    	
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
        MyApplication.getInstance().setAppRunnning(true);
       // MyApplication.isAppRunnning=true;
        //Log.w("MyActivityLifeCycle", "onActivityResumed isApplicationInForeground() " + isApplicationInForeground());
        
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
//        Log.w("test", "application is in foreground: " + (resumed > paused));
        //Log.w("MyActivityLifeCycle", "onActivityPaused isApplicationInForeground() " + isApplicationInForeground());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
        
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
//        Log.w("test", "application is visible: " + (started > stopped));
        //Log.w("MyActivityLifeCycle", "onActivityStopped isApplicationInForeground() " + isApplicationInForeground());
    }

    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:
    // Replace the four variables above with these four
    /*private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }*/




}