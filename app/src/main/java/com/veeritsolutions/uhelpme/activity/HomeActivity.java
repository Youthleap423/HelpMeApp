package com.veeritsolutions.uhelpme.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.enums.RegisterBy;
import com.veeritsolutions.uhelpme.fragments.home.ChatDashboardFragment;
import com.veeritsolutions.uhelpme.fragments.home.DashboardFragment;
import com.veeritsolutions.uhelpme.fragments.home.HomeFragment;
import com.veeritsolutions.uhelpme.fragments.home.PostDetailFragment;
import com.veeritsolutions.uhelpme.fragments.home.PostHelpBasicInfoFragment;
import com.veeritsolutions.uhelpme.fragments.home.SearchFragment;
import com.veeritsolutions.uhelpme.fragments.profile.MyProfileFragment;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.MyContextWrapper;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Admin on 5/11/2017.
 */

public class HomeActivity extends AppCompatActivity implements OnBackPressedEvent
        , ResultCallback<LocationSettingsResult>,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DataObserver, OnClickEvent {

    //private NavigationView navigationView;
    // private TextView navHeaderName, navHeaderLocation, navRating, navPoint, navHelpme, navOffered, navRatingText, navPointText, navHelpmeText, navOfferedText;
    //private DrawerLayout drawer;
    //private ActionBarDrawerToggle toggle;
    public LinearLayout linHome, linSearch, linDashBoard, linChatRoom, linFooterView;
    public ImageView imgHome;
    public ImageView imgSearch;
    public ImageView imgHelpMe;
    public ImageView imgDashbord;
    public ImageView imgChatRoom;
    public TextView tvHome, tvSearch, tvDashBoard, tvChatRoom;
    // location related
    protected LocationSettingsRequest mLocationSettingsRequest;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private ArrayList<String> permission;
    private float lat, lon, altitude;

    OnClickEvent onClickEvent;
    OnBackPressedEvent onBackPressedEvent;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment currentFragment;
    boolean doubleBackToExitPressedOnce = false;
    private Bundle bundle;

    private LoginUserModel loginUserModel;
//    private boolean isHome = true, isSearch = true, isDashBord = true, isChatRoom = true;
//    private String lang = "en";
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //do something based on the intent's action
            //Toast.makeText(HomeActivity.this, "message Recieved", Toast.LENGTH_LONG).show();

            String msg = intent.getStringExtra(Constants.KEY_FROM_NOTIFICATION);
            ToastHelper.getInstance().showMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.KEY_SHOW_POP_UP);
        filter.addAction("SOME_OTHER_ACTION");

        registerReceiver(receiver, filter);

        init();

        permission = new ArrayList<>();
        permission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permission.add(Manifest.permission.ACCESS_NETWORK_STATE);

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();

        fragmentManager = getSupportFragmentManager();

        loginUserModel = LoginUserModel.getLoginUserModel();
        if (getIntent().getIntExtra(Constants.IS_FROM_SIGN_UP, 0) == 1) {
            bundle = new Bundle();
            bundle.putInt(Constants.IS_FROM_SIGN_UP, 1);
            pushFragment(new MyProfileFragment(), true, false, bundle);
        } else if (getIntent().getBooleanExtra(Constants.IS_FROM_TRACK, false)) {
            pushFragment(new PostHelpBasicInfoFragment(), true, false, null);

        } else if (getIntent().getBooleanExtra(Constants.IS_FROM_AR_VIEW, false)) {
            bundle = new Bundle();
            bundle.putBoolean(Constants.IS_FROM_AR_VIEW, true);
            bundle.putSerializable(Constants.HELP_DATA, getIntent().getSerializableExtra(Constants.HELP_DATA));
            pushFragment(new PostDetailFragment(), true, false, bundle);

        } else {
            pushFragment(new HomeFragment(), true, false, null);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient != null) {

            if (mGoogleApiClient.isConnected())
                startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
        stopLocationUpdates();
        //unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (PrefHelper.getInstance().containKey(PrefHelper.LANGUAGE)) {
            String str = PrefHelper.getInstance().getString(PrefHelper.LANGUAGE, "en");
            super.attachBaseContext(MyContextWrapper.wrap(newBase, str));
        } else {
            super.attachBaseContext(MyContextWrapper.wrap(newBase, "en"));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        recreate();
        super.onConfigurationChanged(newConfig);
    }

    private void init() {

        linHome = (LinearLayout) findViewById(R.id.lin_home);
        //linHome.setOnClickListener(this);
        linSearch = (LinearLayout) findViewById(R.id.lin_fab_search);
        // linSearch.setOnClickListener(this);
        linDashBoard = (LinearLayout) findViewById(R.id.lin_dashboard);
        // linDashBoard.setOnClickListener(this);
        linChatRoom = (LinearLayout) findViewById(R.id.lin_chatroom);
        // linChatRoom.setOnClickListener(this);
        linFooterView = (LinearLayout) findViewById(R.id.lin_homeFooter);

        imgHome = (ImageView) findViewById(R.id.img_home);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgHelpMe = (ImageView) findViewById(R.id.img_helpMe);
        imgDashbord = (ImageView) findViewById(R.id.img_dashboard);
        imgChatRoom = (ImageView) findViewById(R.id.img_chatRoom);

        tvHome = (TextView) findViewById(R.id.tv_home);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        tvDashBoard = (TextView) findViewById(R.id.tv_dashBoard);
        tvChatRoom = (TextView) findViewById(R.id.tv_chatRoom);

        // admob
        if (!getResources().getString(R.string.ad_banner_id).equals("")) {
            // Look up the AdView as a resource and load a request.
            AdView adView = (AdView) findViewById(R.id.home_adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } else {
            AdView adView = (AdView) findViewById(R.id.home_adView);
            adView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        onClickEvent.onClick(view);
        switch (view.getId()) {

            case R.id.img_helpMe:

                Utils.buttonClickEffect(view);
                if (!(currentFragment instanceof PostHelpBasicInfoFragment)) {
                    removeAllFragment();
                    bundle = new Bundle();
                    bundle.putInt(Constants.IS_FROM_HOME_ACTIVITY, 0);
                    pushFragment(new PostHelpBasicInfoFragment(), true, false, bundle);
                }

                break;

            case R.id.lin_home:
                Utils.buttonClickEffect(view);
                if (!(currentFragment instanceof HomeFragment)) {
                    removeAllFragment();
                    pushFragment(new HomeFragment(), true, false, null);
                }
                break;

            case R.id.lin_fab_search:

                Utils.buttonClickEffect(view);
                if (!(currentFragment instanceof SearchFragment)) {
                    removeAllFragment();
                    pushFragment(new SearchFragment(), true, false, null);
                }
                break;

            case R.id.lin_dashboard:

                Utils.buttonClickEffect(view);
                if (!(currentFragment instanceof DashboardFragment)) {
                    removeAllFragment();
                    pushFragment(new DashboardFragment(), true, false, null);
                }
                break;

            case R.id.lin_chatroom:
                //ChatRoomFragment
                Utils.buttonClickEffect(view);
                if (!(currentFragment instanceof ChatDashboardFragment)) {
                    removeAllFragment();
                    pushFragment(new ChatDashboardFragment(), true, false, null);
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {

        //if (doubleBackToExitPressedOnce) {
        //super.onBackPressed();
        if (onBackPressedEvent != null) {
            onBackPressedEvent.onBackPressed();
        }
        //  return;
        // }
        // this.doubleBackToExitPressedOnce = true;
        //  ToastHelper.getInstance().showMessage("Please click BACK again to exit");
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

       /* if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {

            }
        }*/
    }

    public void pushFragment(Fragment fragment, boolean addToBackStack, boolean shouldAnimate, Bundle bundle) {

        currentFragment = fragment;
        onClickEvent = (OnClickEvent) fragment;
        onBackPressedEvent = (OnBackPressedEvent) fragment;

        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction = fragmentManager.beginTransaction();

        if (shouldAnimate) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        }
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getCanonicalName());
        }

        // Replace whatever is in the fragment_container dataView with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getCanonicalName());

        //currentFragment = (Fragment) onClickEvent;

        // Commit the transaction
        fragmentTransaction.commit();

    }

    public void popBackFragment() {

        try {
            int backStackCount = fragmentManager.getBackStackEntryCount();

            if (backStackCount > 1) {

                FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(backStackCount - 2);

                String className = backStackEntry.getName();

                Fragment fragment = fragmentManager.findFragmentByTag(className);

                currentFragment = fragment;
                onClickEvent = (OnClickEvent) fragment;
                onBackPressedEvent = (OnBackPressedEvent) fragment;

                fragmentManager.popBackStack();
            } else {

                if (doubleBackToExitPressedOnce) {
                    finish();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                ToastHelper.getInstance().showMessage(getString(R.string.str_click_back_again));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Remove all fragments from back stack*/
    public void removeAllFragment() {

        int fragmentsCount = fragmentManager.getBackStackEntryCount();

        if (fragmentsCount > 0) {
            // MyApplication.disableFragmentAnimations = true;
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //		manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commit();

            // MyApplication.disableFragmentAnimations = false;
            //fragmentManager.popBackStack("myfancyname", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        //removeFragmentUntil(DrinkListFragment);
    }

    /*Remove Fragments until provided Fragment class*/
    public void removeFragmentUntil(Class<?> fragmentClass) {

        try {
            int backStackCountMain = fragmentManager.getBackStackEntryCount();
            if (backStackCountMain > 1) {
                /*Note: To eliminate pop menu fragments and push base menu fragment animation effect at a same times*/
                //MyApplication.disableFragmentAnimations = true;
                int backStackCount = backStackCountMain;
                for (int i = 0; i < backStackCountMain; i++) {
                    FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(backStackCount - 1);
                    String str = backEntry.getName();
                    Fragment fragment = fragmentManager.findFragmentByTag(str);
                    if (fragment.getClass().getCanonicalName().equals(fragmentClass.getCanonicalName())) {
                        currentFragment = fragment;
                        onClickEvent = (OnClickEvent) fragment;
                        onBackPressedEvent = (OnBackPressedEvent) fragment;
                        break;
                    } else
                        fragmentManager.popBackStack();

                    backStackCount--;
                }

            } else
                finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused LocationModel Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    protected void buildLocationSettingsRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionClass.checkPermission(this, Constants.REQUEST_LOCATION, permission)) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (mLastLocation == null) {
                    checkLocationSettings();
                }
            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation == null) {
                checkLocationSettings();
            }
        }
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(this);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient,
                    this
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {

                }
            });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
/* If the initial location was never previously requested, we use
         FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
         its value in the Bundle and check for it in onCreate(). We
         do not request it again unless the user specifically requests location updates by pressing
         the Start Updates button.

         Because we cache the value of the initial location in the Bundle, it means that if the
         user launches the activity,
         moves to a new location, and then changes the device orientation, the original location
         is displayed as the activity is re-created.*/

        if (mLastLocation == null) {
            if (mGoogleApiClient.isConnected())
                startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        buildGoogleApiClient();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

        final Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {

            case LocationSettingsStatusCodes.SUCCESS:
                Debug.trace("All location settings are satisfied.");
                if (mGoogleApiClient.isConnected())
                    startLocationUpdates();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Debug.trace("LocationModel settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                /* Show the dialog by calling startResolutionForResult(), and check the result
                  in onActivityResult().
                 */
                try {
                    status.startResolutionForResult(this, Constants.REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Debug.trace("LocationModel settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        //  mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //  updateLocationUI();
        if (mLastLocation != null) {
            lat = (float) location.getLatitude();
            lon = (float) location.getLongitude();
            altitude = (long) location.getAltitude();

            Debug.trace("latitude: " + lat + "," + "longitude" + lon);

            PrefHelper.getInstance().setFloat(PrefHelper.LATITUDE, lat);
            PrefHelper.getInstance().setFloat(PrefHelper.LONGITUDE, lon);
            PrefHelper.getInstance().setFloat(PrefHelper.ALTITUDE, altitude);
            updateClientLocation();

        } else {
            checkLocationSettings();
        }
    }

    private void updateClientLocation() {

        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.SET_CLIENT_LOCATION);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("Latitude", String.valueOf(PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0)));
        params.put("Longitude", String.valueOf(PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0)));
        params.put("Altitude", String.valueOf(PrefHelper.getInstance().getFloat(PrefHelper.ALTITUDE, 0)));

        if (Utils.isInternetAvailable())
            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.SET_CLIENT_LOCATION, false,
                    RequestCode.SetClientLocation, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case Constants.REQUEST_CHECK_SETTINGS:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //  Log.i(TAG, "User agreed to make required location settings changes.");
                        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                            startLocationUpdates();
                        }

                        break;

                    case Activity.RESULT_CANCELED:

                        ToastHelper.getInstance().showMessage(getString(R.string.correct_distance_data));
                        //   Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> shouldPermit = new ArrayList<>();

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION) {

            if (grantResults.length > 0 || grantResults.length != 0) {

                for (int i = 0; i < grantResults.length; i++) {
                    //  permissions[i] = Manifest.permission.CAMERA; //for specific permission check
                    grantResults[i] = PackageManager.PERMISSION_DENIED;
                    shouldPermit.add(permissions[i]);
                }
                if (PermissionClass.verifyPermission(grantResults)) {
                    startLocationUpdates();
                } else {
                    PermissionClass.checkPermission(this, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION, permission);
                }
            }
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

    }
}
