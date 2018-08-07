package com.veeritsolutions.uhelpme.fragments.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.fragments.profile.OtherPersonProfileFragment;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.utility.BlurTransformation;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by VEER7 on 6/30/2017.
 */

public class PostDetailFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver, OnMapReadyCallback {

    private HomeActivity homeActivity;
    private View rootView;
    private ImageView imgHelpBannerPic, imgHelpPic, imgCategoryIcon;
    private TextView tvHelpTitle, tvHelpCategory, tvHelpDetails, tvBestOffer, tvCategoryLabel,
            tvBestOfferLabel, tvLocationLabel, tvAmountLabel, tvHelpAmount, tvHelpSeekerLabel, tvHelpSeekerName;
    private Button btnVieAllOffer;
    // private FrameLayout mapFrameLayout;
    private MapView mMapView;

    private Map<String, String> params;
    private Bundle bundle;
    private PostedJobModel postedJobModel;
    //  private SupportMapFragment spFragment;
    private float latitude, longitude;
    private GoogleMap mGoogleMap;
    private LoginUserModel loginUserModel;
    private boolean isFromARview = false;
    //  private View view;
    // private ArrayList<AllHelpOfferModel> chatListModels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        if (bundle != null) {
            postedJobModel = (PostedJobModel) bundle.getSerializable(Constants.HELP_DATA);
            isFromARview = bundle.getBoolean(Constants.IS_FROM_AR_VIEW);
        }
        if (isFromARview) {
            homeActivity.linFooterView.setVisibility(View.GONE);
        } else {
            homeActivity.linFooterView.setVisibility(View.VISIBLE);
        }
        loginUserModel = LoginUserModel.getLoginUserModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_help_post_detail, container, false);

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
        imgHelpBannerPic = (ImageView) rootView.findViewById(R.id.img_help_bannerPic);
        imgHelpPic = (ImageView) rootView.findViewById(R.id.img_help_ProfilePic);

        tvHelpTitle = (TextView) rootView.findViewById(R.id.tv_helpTitle);
        tvHelpTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvAmountLabel = (TextView) rootView.findViewById(R.id.tv_amount);
        tvAmountLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpAmount = (TextView) rootView.findViewById(R.id.tv_helpAmount);
        tvHelpAmount.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpSeekerLabel = (TextView) rootView.findViewById(R.id.tv_helpSeeker);
        tvHelpSeekerLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpSeekerName = (TextView) rootView.findViewById(R.id.tv_helpSeekerName);
        tvHelpSeekerName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvCategoryLabel = (TextView) rootView.findViewById(R.id.tv_category);
        tvCategoryLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpCategory = (TextView) rootView.findViewById(R.id.tv_helpCategory);
        tvHelpCategory.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpDetails = (TextView) rootView.findViewById(R.id.tv_helpDescription);
        tvHelpDetails.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvBestOffer = (TextView) rootView.findViewById(R.id.tv_money);
        tvBestOffer.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvBestOfferLabel = (TextView) rootView.findViewById(R.id.tv_best_offer);
        tvBestOfferLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvLocationLabel = (TextView) rootView.findViewById(R.id.tv_locationLabel);
        tvLocationLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        imgCategoryIcon = (ImageView) rootView.findViewById(R.id.img_categoryIcon);

        btnVieAllOffer = (Button) rootView.findViewById(R.id.btn_view_all_offer);
        btnVieAllOffer.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getHelpDetailData();
        postViewerData();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetPostedJobDetail:

                postedJobModel = (PostedJobModel) mObject;
                setData(postedJobModel);
                break;

            case JobPostViewInsert:


                break;

            case JobPostOfferInsert:

                ToastHelper.getInstance().showMessage(getString(R.string.offer_placed));
                getHelpDetailData();
                break;


            case GetAllHelpOffer:

                ArrayList<AllHelpOfferModel> list = (ArrayList<AllHelpOfferModel>) mObject;

                if (list != null && !list.isEmpty()) {
                    // chatListModels.addAll(list);
                    // if (!chatListModels.isEmpty()) {

                    CustomDialog.getInstance().showAllHelpOffers(homeActivity, false, list);
                    // }
                }
                break;
        }
    }

    private void setData(PostedJobModel postedJobModel) {

        tvHelpTitle.setText(postedJobModel.getJobTitle());
        tvHelpSeekerName.setText(postedJobModel.getFirstName() + " " + postedJobModel.getLastName());
        tvHelpCategory.setText(postedJobModel.getCategoryName());
        tvHelpDetails.setText(postedJobModel.getJobDescription());
        tvBestOffer.setText("$ " + postedJobModel.getBestOffer());
        if (postedJobModel.getJobAmountFlag() == 0) {
            tvHelpAmount.setText("$ " + postedJobModel.getJobAmount());
        } else if (postedJobModel.getJobAmountFlag() == 1) {
            tvHelpAmount.setText("$ " + postedJobModel.getJobAmount() + " / hour");
        } else if (postedJobModel.getJobAmountFlag() == 2) {
            tvHelpAmount.setText(getString(R.string.str_free_rate));
        }
        //tvHelpAmount.setText("$ " + postedJobModel.getJobAmount());
        // imgCategoryIcon.setColorFilter(Color.parseColor(postedJobModel.getColorCode()));

        //Utils.setImage(postedJobModel.getCategoryIcon1(), R.color.colorAccent, imgCategoryIcon);
        //imgCategoryIcon.setColorFilter(Color.parseColor(postedJobModel.getColorCode()));
        Utils.setImage(postedJobModel.getJobPhoto(), R.drawable.img_launcher_icon, imgHelpPic);


       /* Glide.with(homeActivity)
                .load(postedJobModel.getJobPhoto())
                .apply(new RequestOptions()
                        .transform(new BlurTransformation(homeActivity)))
                .into(imgHelpBannerPic);*/
        Picasso.with(homeActivity)
                .load(postedJobModel.getJobPhoto())
                .placeholder(R.drawable.img_launcher_icon)
                .error(R.drawable.img_launcher_icon)
                .transform(new BlurTransformation(homeActivity))
                .into(imgHelpBannerPic);

        Utils.setImage(postedJobModel.getCategoryIcon1(), R.drawable.img_pin_blue, imgCategoryIcon);
        /*Glide.with(homeActivity)
                .load(postedJobModel.getCategoryIcon1())
                .apply(new RequestOptions()
                        .error(R.drawable.img_pin_blue)
                        .placeholder(R.drawable.img_pin_blue)
                        .override(90, 120))
                // .transform(new BlurTransformation(homeActivity))
                .into(imgCategoryIcon);*/

//        spFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
//        spFragment.getMapAsync(this);

        if (mGoogleMap != null) {

            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(postedJobModel.getLatitude(), postedJobModel.getLongitude())).title("your location"));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(postedJobModel.getLatitude(), postedJobModel.getLongitude()), 5);
            mGoogleMap.animateCamera(cameraUpdate);
        }
        //new TheTask().execute(postedJobModel);
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {

        if (!isFromARview) {
            homeActivity.popBackFragment();
        } else {
            homeActivity.finish();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                if (!isFromARview) {
                    homeActivity.popBackFragment();
                } else {
                    homeActivity.finish();
                }
                break;

            case R.id.rel_make_offer:
                Utils.buttonClickEffect(view);
                if (loginUserModel.getClientId() == postedJobModel.getClientId()) {
                    ToastHelper.getInstance().showMessage(getString(R.string.can_not_post_offer));
                } else {
                    if (postedJobModel.getJobAmountFlag() == 1) {
                        CustomDialog.getInstance().showEnterOffer(homeActivity, false, getString(R.string.enter_offer_amount) + " (per hour)");
                    } else {
                        CustomDialog.getInstance().showEnterOffer(homeActivity, false, getString(R.string.enter_offer_amount));
                    }

                }
                break;

            case R.id.btn_actionOk:
                Utils.buttonClickEffect(view);
                Object o = view.getTag();
                insertOffer(o);
                break;

            case R.id.btn_view_all_offer:
                Utils.buttonClickEffect(view);
                if (postedJobModel != null) {
                    getAllHelpOfferData();
                }
                break;

            case R.id.img_help_ProfilePic:
                Utils.buttonClickEffect(view);
                if (postedJobModel != null) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(postedJobModel.getJobPhoto());
                    if (!postedJobModel.getJobPhoto1().isEmpty())
                        list.add(postedJobModel.getJobPhoto1());
                    if (!postedJobModel.getJobPhoto2().isEmpty())
                        list.add(postedJobModel.getJobPhoto2());
                    if (!postedJobModel.getJobPhoto3().isEmpty())
                        list.add(postedJobModel.getJobPhoto3());

                    CustomDialog.getInstance().showImageDialog(list, getActivity());
                }
                break;

            case R.id.tv_helpSeekerName:
                Utils.buttonClickEffect(view);
                if (postedJobModel != null) {
                    LoginUserModel loginUserModel = new LoginUserModel();
                    loginUserModel.setClientId(postedJobModel.getClientId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.USER_DATA, loginUserModel);
                    homeActivity.pushFragment(new OtherPersonProfileFragment(), true, false, bundle);
                }
                break;
        }

    }

    private void insertOffer(Object o) {
        if (o != null) {
            String offerAmount = (String) o;

            if (offerAmount.isEmpty()) {
                ToastHelper.getInstance().showMessage(getString(R.string.enter_amount));
                return;
            }
         /*   if (offerAmount.equals("0")) {
                ToastHelper.getInstance().showMessage(getString(R.string.can_not_put_zero_offer));
                return;
            }*/

            CustomDialog.getInstance().dismiss();
            params = new HashMap<>();
            params.put("op", "JobPostOfferInsert");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("JobPostId", String.valueOf(postedJobModel.getJobPostId()));
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("OfferAmount", offerAmount);
            params.put("Latitude", String.valueOf(PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0)));
            params.put("Longitude", String.valueOf(PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0)));
            params.put("Altitude", String.valueOf(PrefHelper.getInstance().getFloat(PrefHelper.ALTITUDE, 0)));

            RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.JOB_POST_OFFER_INSERT,
                    true, RequestCode.JobPostOfferInsert, this);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (PrefHelper.getInstance().containKey(PrefHelper.LATITUDE)) {
            latitude = PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0);
        }
        if (PrefHelper.getInstance().containKey(PrefHelper.LONGITUDE)) {
            longitude = PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0);
        }
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("your location"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude, longitude), 5);
        mGoogleMap.animateCamera(cameraUpdate);
        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.setIndoorEnabled(true);
        mGoogleMap.setBuildingsEnabled(true);
        // mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        String[] permission = new String[3];
        permission[0] = (android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permission[1] = (android.Manifest.permission.ACCESS_FINE_LOCATION);
        permission[2] = (Manifest.permission.ACCESS_NETWORK_STATE);

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                CustomDialog.getInstance().showMapDialog(postedJobModel, getActivity());
            }
        });
        requestPermissions(permission, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // List<String> shouldPermit = new ArrayList<>();

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION) {

            if (grantResults.length > 0 || grantResults.length != PackageManager.PERMISSION_GRANTED) {
                if (PermissionClass.verifyPermission(grantResults)) {
                    mGoogleMap.setMyLocationEnabled(true);
                }
            }
        }
    }


    private void getAllHelpOfferData() {

        params = new HashMap<>();
        params.put("op", ApiList.GET_CATEGORY_CHAT_DATA);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(postedJobModel.getJobPostId()));
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_CATEGORY_CHAT_DATA,
                true, RequestCode.GetAllHelpOffer, this);
    }

    private void postViewerData() {

        params = new HashMap<>();
        params.put("op", "JobPostViewInsert");
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(postedJobModel.getJobPostId()));
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.JOB_POSTVIEW_INSERT,
                false, RequestCode.JobPostViewInsert, this);

    }

    private void getHelpDetailData() {

        params = new HashMap<>();
        params.put("op", ApiList.GET_JOB_POST_DETAIL);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(postedJobModel.getJobPostId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.GET_JOB_POST_DETAIL, true, RequestCode.GetPostedJobDetail, this);
    }

    private void floodFill(Bitmap bmp, Point point, int targetColor, int newColor) {
        Queue<Point> q = new LinkedList<>();
        q.add(point);
        while (q.size() > 0) {
            Point n = q.poll();
            if (bmp.getPixel(n.x, n.y) != targetColor)
                continue;

            Point w = n, e = new Point(n.x + 1, n.y);
            while ((w.x > 0) && (bmp.getPixel(w.x, w.y) == targetColor)) {
                bmp.setPixel(w.x, w.y, newColor);
                if ((w.y > 0) && (bmp.getPixel(w.x, w.y - 1) == targetColor))
                    q.add(new Point(w.x, w.y - 1));
                if ((w.y < bmp.getHeight() - 1)
                        && (bmp.getPixel(w.x, w.y + 1) == targetColor))
                    q.add(new Point(w.x, w.y + 1));
                w.x--;
            }
            while ((e.x < bmp.getWidth() - 1)
                    && (bmp.getPixel(e.x, e.y) == targetColor)) {
                bmp.setPixel(e.x, e.y, newColor);

                if ((e.y > 0) && (bmp.getPixel(e.x, e.y - 1) == targetColor))
                    q.add(new Point(e.x, e.y - 1));
                if ((e.y < bmp.getHeight() - 1)
                        && (bmp.getPixel(e.x, e.y + 1) == targetColor))
                    q.add(new Point(e.x, e.y + 1));
                e.x++;
            }
        }
    }

    private class TheTask extends AsyncTask<PostedJobModel, Void, Bitmap> {

        PostedJobModel[] postedJobModel;

        @Override
        protected Bitmap doInBackground(PostedJobModel... params) {
            postedJobModel = params;
            URL url;
            Bitmap bmp = null;
            try {
                url = new URL(params[0].getCategoryIcon1());

                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bmp = Bitmap.createScaledBitmap(bmp, imgCategoryIcon.getWidth(), imgCategoryIcon.getHeight(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Bitmap myBitmap) {

            super.onPostExecute(myBitmap);

            imgCategoryIcon.setImageBitmap(myBitmap);
        }
    }
}

