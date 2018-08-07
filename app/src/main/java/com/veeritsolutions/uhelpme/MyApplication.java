package com.veeritsolutions.uhelpme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.MapsInitializer;
import com.google.firebase.database.FirebaseDatabase;
import com.veeritsolutions.uhelpme.api.ServerConfig;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.ExceptionHandler;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Admin on 5/11/2017.
 */
/*@ReportsCrashes(
        mailTo = "veerkrupa.hitesh@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)*/
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = MyApplication.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    public static Activity currentActivity;
    private static MyApplication mInstance;
    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:
    // Replace the four variables above with these four
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;
    public final int VOLLEY_TIMEOUT = 60000;
    //private final Context mContext;
    private final String LINE_SEPARATOR = "\n";
    public boolean isAppRunnning;
    public Typeface FONT_WORKSANS_MEDIUM, FONT_WORKSANS_REGULAR, FONT_WORKSANS_LIGHT;
    String response;
    private RequestQueue mRequestQueue;
    private FirebaseDatabase firebaseDatabase;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }

    public static boolean isApplicationInBackGround() {
        return paused > resumed;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;
        MapsInitializer.initialize(this);
        isAppRunnning = true;
        FONT_WORKSANS_MEDIUM = getTypeFace(Constants.FONT_WORKSANS_MEDIUM);
        FONT_WORKSANS_REGULAR = getTypeFace(Constants.FONT_WORKSANS_REGULAR);
        FONT_WORKSANS_LIGHT = getTypeFace(Constants.FONT_WORKSANS_LIGHT);
        registerActivityLifecycleCallbacks(this);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        firebaseDatabase = FirebaseDatabase.getInstance(ServerConfig.FCM_APP_URL);
        firebaseDatabase.setPersistenceEnabled(true);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        //   isAppRunnning = false;
    }

    public Typeface getTypeFace(String typeFaceName) {
        return Typeface.createFromAsset(getAssets(), typeFaceName);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        // isAppRunnning = true;
        // Debug.trace("appState", String.valueOf(isAppRunnning));

        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //  isAppRunnning = true;
        //  Debug.trace("appState", String.valueOf(isAppRunnning));

        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //  isAppRunnning = true;
        //  Debug.trace("appState", String.valueOf(isAppRunnning));

        ++resumed;
        isAppRunnning = true;
        Debug.trace("MyActivityLifeCycle", "onActivityResumed isApplicationInForeground() " + isApplicationInForeground());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //  isAppRunnning = false;
        //  Debug.trace("appState", String.valueOf(isAppRunnning));

        ++paused;
        Debug.trace("test", "application is in foreground: " + (resumed > paused));
        Debug.trace("MyActivityLifeCycle", "onActivityPaused isApplicationInForeground() " + isApplicationInForeground());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //  isAppRunnning = false;
        //  Debug.trace("appState", String.valueOf(isAppRunnning));

        ++stopped;
        Debug.trace("test", "application is visible: " + (started > stopped));
        Debug.trace("MyActivityLifeCycle", "onActivityStopped isApplicationInForeground() " + isApplicationInForeground());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

        Debug.trace("test", "onActivityDestroyed: started" + started + "resumed: " + resumed + " paused: " + paused + " stopped" + stopped);

        if (resumed == paused && resumed == started && resumed == stopped) {
            Debug.trace("MyLifecycleHandler", "App is closed");
            isAppRunnning = false;

        } else
            isAppRunnning = true;
    }

    public boolean isAppRunnning() {
        return isAppRunnning;
    }

    public void setAppRunnning(boolean appRunnning) {
        isAppRunnning = appRunnning;
    }

    /**
     * This method set timeout duration of Request
     *
     * @param req(Request) : volley request type e.g. JSONObject,JSONArray,JSONString
     */
    public <T> void setRequestTimeout(Request<T> req) {
        //  int requestTimeout = VOLLEY_TIMEOUT;
        req.setRetryPolicy(new DefaultRetryPolicy(
                VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }
}

