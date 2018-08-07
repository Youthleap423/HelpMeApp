package com.veeritsolutions.uhelpme.fragments.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Admin on 6/23/2017.
 */

public class PostHelpMapFragment extends Fragment implements OnClickEvent, DataObserver, OnBackPressedEvent, OnMapReadyCallback {

    int PLACE_PICKER_REQUEST = 1;
    private View rootView;
    private TextView tvUhelpMe, tvYourLocation, tvLocation;
    //  private FrameLayout mapFrameLayout;
    private Button btnNextStep;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    // object and variable declaration
    private HomeActivity homeActivity;
    // private SupportMapFragment spFragment;
    private float latitude, longitude;
    private Bundle bundle;
    // private View view;
    private PlacePicker.IntentBuilder builder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        builder = new PlacePicker.IntentBuilder();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_post_help_location, container, false);

//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null)
//                parent.removeView(view);
//        }
//        try {
//            view = inflater.inflate(R.layout.map, container, false);
//        } catch (InflateException e) {
//        /* map is already there, just return dataView as it is */
//        }
//
//        mapFrameLayout = (FrameLayout) rootView.findViewById(R.id.map_framelayout);
//        mapFrameLayout.addView(view);
//
//        spFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
//
//        spFragment.getMapAsync(this);

        tvUhelpMe = (TextView) rootView.findViewById(R.id.txv_show);
        tvUhelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        String str = getString(R.string.view_a_u) + getString(R.string.font_color) + getString(R.string.helpme) + getString(R.string.font);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvUhelpMe.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
            //  tvUhelpMe.setText(Html.fromHtml("View a U" + "<a color=\"#0095d7\">HelpMe</a>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvUhelpMe.setText(Html.fromHtml(str));
            //tvUhelpMe.setText(Html.fromHtml("By creating your account, you accept the "
            //        + "<a color=\"#0095d7\"href=\"http://www.anivethub.com/terms-and-conditions\">general conditions</a> of <b>UHelpMe</b>"));
            //  tvUhelpMe.setText(Html.fromHtml("View a U" + "<a color=\"#0095d7\">HelpMe</a>"));
        }

        tvLocation = (TextView) rootView.findViewById(R.id.tv_location);
        tvLocation.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvYourLocation = (TextView) rootView.findViewById(R.id.tv_your_location);
        tvYourLocation.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        btnNextStep = (Button) rootView.findViewById(R.id.btn_next_help);
        btnNextStep.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(this);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

    }

    @Override
    public void onBackPressed() {

        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                homeActivity.popBackFragment();
                break;

            case R.id.btn_next_help:
                Utils.buttonClickEffect(view);
                homeActivity.pushFragment(new PostHelpFinalFragment(), true, false, bundle);
                break;

            case R.id.img_home:
                Utils.buttonClickEffect(view);
                try {
                    startActivityForResult(builder.build(homeActivity), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        if (PrefHelper.getInstance().containKey(PrefHelper.LATITUDE)) {
            latitude = PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0);
            bundle.putFloat(PrefHelper.LATITUDE, latitude);
        }
        if (PrefHelper.getInstance().containKey(PrefHelper.LONGITUDE)) {
            longitude = PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0);
            bundle.putFloat(PrefHelper.LONGITUDE, longitude);
            bundle.putFloat(PrefHelper.ALTITUDE, PrefHelper.getInstance().getFloat(PrefHelper.ALTITUDE, 0));
        }
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("your location"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude, longitude), 5);
        mGoogleMap.animateCamera(cameraUpdate);
        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.setIndoorEnabled(true);
        mGoogleMap.setBuildingsEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        String[] permission = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE};
        requestPermissions(permission, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //  List<String> shouldPermit = new ArrayList<>();

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION) {

            if (grantResults.length > 0 || grantResults.length != PackageManager.PERMISSION_GRANTED) {

                if (PermissionClass.verifyPermission(grantResults)) {
                    mGoogleMap.setMyLocationEnabled(true);
                } else {
                    requestPermissions(permissions, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(homeActivity, data);
                mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        place.getLatLng(), 5);
                mGoogleMap.animateCamera(cameraUpdate);
                bundle.putFloat(PrefHelper.LATITUDE, (float) place.getLatLng().latitude);
                bundle.putFloat(PrefHelper.LONGITUDE, (float) place.getLatLng().longitude);
            }
        }

    }
}
