package com.veeritsolutions.uhelpme.fragments.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.adapters.AdpCategory;
import com.veeritsolutions.uhelpme.adapters.AdpHelpNeeded;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.arview.ARView;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.ARViewModel;
import com.veeritsolutions.uhelpme.models.CategoryModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.EndlessRecyclerViewScrollListener;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.SENSOR_SERVICE;


public class HomeFragment extends Fragment implements OnClickEvent, DataObserver, OnBackPressedEvent,
        SwipeRefreshLayout.OnRefreshListener {

    private static final int REQUEST_PERMISIONS = 112;
    //private final int SPLASH_DISPLAY_LENGTH = 2000;
    //GoogleMap mGoogleMap;
    //xml components
    private RecyclerView recyclerViewHelp, recyclerViewCategory;
    private View rootView;
    //  private Toolbar toolbar;
    private ImageView imgUserProfile, imgSearch, imgSearchBackground;
    private EditText edtSearch;
    private RelativeLayout/* relHomeView,*/ linRecyclerView;
    private LinearLayout linSearchView, linSearch;
    private FloatingActionButton fabSearch, fabCateSearch, fabKeyWordSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvMapSearch, tvARviewSearch, tvCategorySearch, tvKeyWordSearch;
    //private AppBarLayout appBarLayout;
    private View itemView;
    //object and variable declaration
    private ArrayList<PostedJobModel> postedJobList;
    private HomeActivity homeActivity;
    private Map<String, String> params;
    private AdpHelpNeeded adpHelpNeeded;
    //private int totalRecords;
    private int totalPages;
    private PostedJobModel postedJobModel;
    private LoginUserModel loginUserModel;
    private boolean isSearchClosed = true, isCateClosed = true, isKeywordClosed = true;
    private String keywordSearch = "", categoryId = "";
    private AdpCategory adpCategory;
    private ArrayList<CategoryModel> categoryModelsList;
    private CategoryModel categoryModel;
    // private StringBuilder stringBuilder;
    private LinearLayoutManager mLayoutManager;
    private int totalItemsCount;
    private SensorManager sensorMgr;
    private List<Sensor> sensors;
    private Sensor sensorGrav;
    private Sensor sensorMag;
    private ArrayList<ARViewModel> arViewList;
    private ArrayList<String> category = new ArrayList<>();
    private String lang = "en";
    //private Animation animShow, animHide;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        imgUserProfile.setEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginUserModel = LoginUserModel.getLoginUserModel();

        homeActivity.imgHome.setImageResource(R.drawable.img_home_active);
        homeActivity.imgSearch.setImageResource(R.drawable.img_search_tabbar_inactive);
        homeActivity.imgDashbord.setImageResource(R.drawable.img_dashboard_inactive);
        homeActivity.imgChatRoom.setImageResource(R.drawable.img_chat_room_inactive);
        homeActivity.imgHelpMe.setImageResource(R.drawable.img_helpme);

        homeActivity.tvHome.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        homeActivity.tvSearch.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvDashBoard.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvChatRoom.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));

        //animShow = AnimationUtils.loadAnimation(getActivity(), R.anim.view_show);
        //animHide = AnimationUtils.loadAnimation(getActivity(), R.anim.view_hide);

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appBarLayout);

        tvMapSearch = (TextView) rootView.findViewById(R.id.tv_mapSearch);
        tvMapSearch.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvARviewSearch = (TextView) rootView.findViewById(R.id.tv_arViewSearch);
        tvARviewSearch.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvCategorySearch = (TextView) rootView.findViewById(R.id.tv_categorySearch);
        tvCategorySearch.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvKeyWordSearch = (TextView) rootView.findViewById(R.id.tv_KeyWordSearch);
        tvKeyWordSearch.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        linRecyclerView = (RelativeLayout) rootView.findViewById(R.id.lin_recyclerView);
        // toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        // homeActivity.setSupportActionBar(toolbar);
        mLayoutManager = new LinearLayoutManager(homeActivity);
        recyclerViewHelp = (RecyclerView) rootView.findViewById(R.id.recyclerView_offer);
        imgUserProfile = (ImageView) rootView.findViewById(R.id.img_userProfile);

        //relHomeView = (RelativeLayout) rootView.findViewById(R.id.rel_homeView);
        // linearLayout = new LinearLayout(homeActivity);
        imgSearchBackground = new ImageView(homeActivity);
        imgSearchBackground.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imgSearchBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // imgSearchBackground.setBackground();
        imgSearchBackground.setBackgroundResource(R.drawable.img_search_background);
        //imgSearchBackground.setClickable(false);
        imgSearchBackground.setVisibility(View.GONE);
        linRecyclerView.addView(imgSearchBackground);
        //linRecyclerView.setClickable(false);
        //  linearLayout.setBackground(getResources().getDrawable(R.drawable.img_chat_background, null));
        //relHomeView.addView(linearLayout);
        linSearchView = (LinearLayout) rootView.findViewById(R.id.lin_fab_search);
        fabSearch = (FloatingActionButton) rootView.findViewById(R.id.fab_search);

        imgSearch = (ImageView) rootView.findViewById(R.id.img_search);
        recyclerViewCategory = (RecyclerView) rootView.findViewById(R.id.recyclerView_category);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(homeActivity, LinearLayoutManager.HORIZONTAL, false));

        recyclerViewCategory.getItemAnimator().setAddDuration(1000);
        recyclerViewCategory.getItemAnimator().setRemoveDuration(1000);
        recyclerViewCategory.getItemAnimator().setMoveDuration(1000);
        recyclerViewCategory.getItemAnimator().setChangeDuration(1000);

        linSearch = (LinearLayout) rootView.findViewById(R.id.lin_search);
        fabCateSearch = (FloatingActionButton) rootView.findViewById(R.id.fab_categorySearch);
        fabKeyWordSearch = (FloatingActionButton) rootView.findViewById(R.id.fab_keySearch);
        edtSearch = (EditText) rootView.findViewById(R.id.edt_searchHelp);
        edtSearch.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str = edtSearch.getText().toString().trim();

                keywordSearch = str;

                if (str.isEmpty()) {
                    //postedJobList.clear();
                    //getPostedJobData(categoryId, keywordSearch, 1, false);
                    imgSearch.setVisibility(View.GONE);
                } else {
                    imgSearch.setVisibility(View.VISIBLE);
                }
            }
        });
        setListener();
        //adpHelpNeeded = new AdpHelpNeeded(postedJobList, homeActivity);
        return rootView;
    }


    private void setListener() {
        mLayoutManager = new LinearLayoutManager(homeActivity);
        recyclerViewHelp.setLayoutManager(mLayoutManager);
        recyclerViewHelp.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager, 1) {
            @Override
            protected void onShow() {

                showViews();
            }

            @Override
            protected void onHide() {
                hideViews();
            }

            @Override
            public void onLoadMore(final int page, int totalItemsCount) {
                rootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page <= totalPages) {

                            adpHelpNeeded.showLoading(true);
                            getPostedJobData(categoryId, keywordSearch, page, false);

                        } else {
                            adpHelpNeeded.showLoading(false);
                        }
                    }
                }, 500);
            }
        });

    }

    private void hideViews() {
        homeActivity.linFooterView.setVisibility(View.GONE);
        // Utils.slideDown(homeActivity.linFooterView, getActivity());
        //slideToBottom(homeActivity.linFooterView);
        //slideToBottom(appBarLayout);
        fabSearch.setVisibility(View.GONE);
        linSearchView.setVisibility(View.GONE);

    }

    private void showViews() {
        homeActivity.linFooterView.setVisibility(View.VISIBLE);
        //Utils.slideUP(homeActivity.linFooterView, getActivity());
        //slideToTop(homeActivity.linFooterView);
        //slideToTop(appBarLayout);
        fabSearch.setVisibility(View.VISIBLE);
        if (isSearchClosed) {
            linSearchView.setVisibility(View.GONE);
        } else {
            linSearchView.setVisibility(View.VISIBLE);
        }
    }

    // To animate view slide out from top to bottom
    public void slideToBottom(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    // To animate view slide out from bottom to top
    public void slideToTop(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight(), 0);
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //stringBuilder = new StringBuilder();
        postedJobList = new ArrayList<>();
        getPostedJobData(categoryId, keywordSearch, 1, true);
        setClientToken();
    }


    @Override
    public void onBackPressed() {

        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_offer_more_detail:
                Utils.buttonClickEffect(view);
                Object o = view.getTag();
                if (o != null) {
                    postedJobModel = (PostedJobModel) o;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HELP_DATA, postedJobModel);
                    bundle.putBoolean(Constants.IS_FROM_AR_VIEW, false);

                    homeActivity.pushFragment(new PostDetailFragment(), true, false, bundle);
                }
                break;

            case R.id.tv_offer_title:
                Utils.buttonClickEffect(view);
                o = view.getTag();
                if (o != null) {
                    postedJobModel = (PostedJobModel) o;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HELP_DATA, postedJobModel);
                    bundle.putBoolean(Constants.IS_FROM_AR_VIEW, false);
                    homeActivity.pushFragment(new PostDetailFragment(), true, false, bundle);
                }
                break;

            case R.id.img_offer:
                Utils.buttonClickEffect(view);
                o = view.getTag(R.id.img_offer);
                if (o != null) {
                    postedJobModel = (PostedJobModel) o;
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

            case R.id.img_userProfile:
                Utils.buttonClickEffect(view);
                imgUserProfile.setEnabled(false);
                Intent intent = new Intent(homeActivity, ProfileActivity.class);
                startActivity(intent);
                homeActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.btn_help_decline:
                itemView = view;
                Utils.buttonClickEffect(view);
                o = view.getTag();
                if (o != null) {
                    postedJobModel = (PostedJobModel) o;
                    declineHelpPost(postedJobModel);
                }
                break;

            case R.id.fab_search:
                Utils.buttonClickEffect(view);
                manageFABsearchView();
                break;

            case R.id.fab_categorySearch:
                Utils.buttonClickEffect(view);
                manageFABCateView();
                break;

            case R.id.fab_keySearch:
                Utils.buttonClickEffect(view);
                manageFABKeywordView();
                break;

            case R.id.lin_categoryList:
                Utils.buttonClickEffect(view);
                categoryModel = (CategoryModel) view.getTag();

                if (!categoryModel.isSelected()) {

                    categoryModel.setSelected(true);
                    category.add(String.valueOf(categoryModel.getCategoryId()));

                } else {
                    categoryModel.setSelected(false);
                    category.remove(String.valueOf(categoryModel.getCategoryId()));
                }

                categoryModelsList.set(categoryModel.getPosition(), categoryModel);
                adpCategory.refreshList(categoryModelsList);

                categoryId = Utils.mytoString(category, ",");
                postedJobList.clear();
                getPostedJobData(categoryId, keywordSearch, 1, true);
                break;

            case R.id.img_search:
                Utils.buttonClickEffect(view);
                postedJobList.clear();
                getPostedJobData(categoryId, keywordSearch, 1, true);
                edtSearch.setText("");
                break;

            case R.id.fab_mapView:
                Utils.buttonClickEffect(view);
                homeActivity.pushFragment(new SearchFragment(), true, false, null);
                manageFABsearchView();
                break;

            case R.id.fab_ARView:
                Utils.buttonClickEffect(view);
                manageFABsearchView();
                handleArViewData();
                break;

            case R.id.img_offer_search:
                Utils.buttonClickEffect(view);
                PostedJobModel postedJobModel = (PostedJobModel) view.getTag();
                CustomDialog.getInstance().showMapDialog(postedJobModel, getActivity());
                break;
        }
    }

    private void handleArViewData() {

        sensorMgr = (SensorManager) homeActivity.getSystemService(SENSOR_SERVICE);
        sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            sensorGrav = sensors.get(0);
        } else {
            ToastHelper.getInstance().showMessage(getString(R.string.does_not_support_this_feature));
            return;
        }

        sensors = sensorMgr.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        if (sensors.size() > 0) {
            sensorMag = sensors.get(0);
        } else {
            ToastHelper.getInstance().showMessage(getString(R.string.does_not_support_this_feature));
            return;
        }
                /*New Handler to start the Menu-Activity
                  and close this Splash-Screen after some seconds.*/
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            boolean hasPermission_camera = (ContextCompat.checkSelfPermission(homeActivity,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

            if (!hasPermission_camera) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISIONS);
            } else {
                getARViewData();
                // hideScreen(arViewList);
            }
        } else {
            getARViewData();
            // hideScreen(arViewList);
        }
    }

    private void hideScreen(final ArrayList<ARViewModel> arViewList) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.AR_VIEW_DATA, arViewList);

        //homeActivity.pushFragment(new ArViewFragment(), true, false, bundle);
        Intent _intent = new Intent(homeActivity, ARView.class);
        _intent.putExtra(Constants.AR_VIEW_DATA, bundle);
        //_intent.putParcelableArrayListExtra(Constants.AR_VIEW_DATA, (ArrayList<? extends Parcelable>) arViewList);
        startActivity(_intent);
        getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, SPLASH_DISPLAY_LENGTH);*/
    }

    private void getARViewData() {
        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.GET_AR_VIEW);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.GET_AR_VIEW, true, RequestCode.GetARView, this);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetPostedJob:

                swipeRefreshLayout.setRefreshing(false);
                ArrayList<PostedJobModel> jobModelArrayList = (ArrayList<PostedJobModel>) mObject;
                if (jobModelArrayList != null && !jobModelArrayList.isEmpty()) {
                    postedJobList.addAll(jobModelArrayList);

                    if (!postedJobList.isEmpty()) {
                        PostedJobModel postedJobModel = postedJobList.get(0);
                        totalPages = postedJobModel.getTotalPage();
                        totalItemsCount = postedJobModel.getTotalRowNo();
                        adpHelpNeeded = (AdpHelpNeeded) recyclerViewHelp.getAdapter();
                        if (adpHelpNeeded != null && adpHelpNeeded.getItemCount() > 0) {
                            adpHelpNeeded.refreshList(postedJobList);
                        } else {
                            adpHelpNeeded = new AdpHelpNeeded(homeActivity, postedJobList, false);
                            recyclerViewHelp.setAdapter(adpHelpNeeded);
                        }
                    }
                }
                break;

            case JobPostDecline:

                postedJobList.remove(postedJobModel);
                int position = postedJobModel.getPosition();
                int listSize = postedJobList.size();
                if (listSize > 0) {
                    adpHelpNeeded.notifyItemRemoved(position);
                    adpHelpNeeded.notifyItemRangeChanged(position, postedJobList.size());
                } else {
                    adpHelpNeeded.refreshList(postedJobList);
                }
                break;

            case GetCategory:

                categoryModelsList = (ArrayList<CategoryModel>) mObject;
                if (categoryModelsList != null && !categoryModelsList.isEmpty()) {
                    recyclerViewCategory.setVisibility(View.VISIBLE);
                    adpCategory = (AdpCategory) recyclerViewCategory.getAdapter();
                    if (adpCategory != null && adpCategory.getItemCount() > 0) {
                        adpCategory.refreshList(categoryModelsList);
                        // adpCategory.notifyDataSetChanged();

                    } else {
                        adpCategory = new AdpCategory(homeActivity, categoryModelsList);
                        recyclerViewCategory.setAdapter(adpCategory);
                    }
                }
                break;

            case SetClientTokenId:

                //    ToastHelper.getInstance().showMessage("token updated!");
                break;

            case GetARView:

                arViewList = (ArrayList<ARViewModel>) mObject;
                if (arViewList != null && !arViewList.isEmpty()) {
                    hideScreen(arViewList);
                }
                break;

        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);

        switch (mRequestCode) {

            case GetPostedJob:
                if (adpHelpNeeded != null) {
                    adpHelpNeeded.showLoading(false);
                    //adpHelpNeeded.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    private void declineHelpPost(PostedJobModel postedJobModel) {

        params = new HashMap<>();
        params.put("op", ApiList.JOB_POST_DECLINE);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(postedJobModel.getJobPostId()));
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.JOB_POST_DECLINE,
                true, RequestCode.JobPostDecline, this);
    }

    private void manageFABsearchView() {

        if (isSearchClosed) {

            linSearchView.setVisibility(View.VISIBLE);
            imgSearchBackground.setVisibility(View.VISIBLE);
            fabSearch.setImageResource(R.drawable.img_fab_close);
            isSearchClosed = false;
        } else {
            linSearchView.setVisibility(View.GONE);
            imgSearchBackground.setVisibility(View.GONE);
            recyclerViewCategory.setVisibility(View.GONE);
            linSearch.setVisibility(View.GONE);
            fabSearch.setImageResource(R.drawable.img_fab_search);
            fabCateSearch.setImageResource(R.drawable.img_fab_category);
            fabKeyWordSearch.setImageResource(R.drawable.img_fab_keyword);
            isSearchClosed = true;
            isCateClosed = true;
            isKeywordClosed = true;
            //stringBuilder = new StringBuilder();

            /*categoryId = "";
            keywordSearch = "";*/
        }
    }

    private void getPostedJobData(String categoryId, String keyword, int pageNo, boolean isDialogRequired) {

        //LoginUserModel loginUser = (LoginUserModel) Utils.stringToObject(PrefHelper.getInstance().getString(PrefHelper.CLIENT_CREDENTIALS, ""));

        if (Utils.isInternetAvailable() && isDialogRequired)
            swipeRefreshLayout.setRefreshing(true);

        params = new HashMap<>();
        params.put("op", ApiList.GET_JOB_POST_HELPER);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("CategoryId", String.valueOf(categoryId));
        params.put("Keyword", keyword);
        params.put("PageNumber", String.valueOf(pageNo));
        params.put("Records", String.valueOf(10));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_JOB_POST_HELPER,
                false, RequestCode.GetPostedJob, this);
    }

    private void getCategoryData() {

        params = new HashMap<>();
        params.put("op", "GetCategory");
        params.put("AuthKey", ApiList.AUTH_KEY);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CATEGORY,
                true, RequestCode.GetCategory, this);
    }

    private void manageFABCateView() {

        if (isCateClosed) {

            fabCateSearch.setImageResource(R.drawable.img_fab_close);
            getCategoryData();

            isCateClosed = false;
        } else {
            recyclerViewCategory.setVisibility(View.GONE);
            fabCateSearch.setImageResource(R.drawable.img_fab_category);
            isCateClosed = true;
        }
    }

    private void manageFABKeywordView() {

        if (isKeywordClosed) {

            linSearch.setVisibility(View.VISIBLE);
            fabKeyWordSearch.setImageResource(R.drawable.img_fab_close);
            isKeywordClosed = false;
        } else {
            linSearch.setVisibility(View.GONE);
            fabKeyWordSearch.setImageResource(R.drawable.img_fab_keyword);
            isKeywordClosed = true;
        }
    }

    @Override
    public void onRefresh() {

        postedJobList.clear();
        if (adpHelpNeeded == null) {
            adpHelpNeeded = new AdpHelpNeeded(homeActivity, postedJobList, false);
        } else {
            adpHelpNeeded.refreshList(postedJobList);
        }
        categoryId = "";
        keywordSearch = "";
        getPostedJobData(categoryId, keywordSearch, 1, true);
        setListener();
    }

    private void setClientToken() {
        params = new HashMap<>();
        params.put("op", ApiList.SET_CLIENT_TOKEN_ID);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(LoginUserModel.getLoginUserModel().getClientId()));
        params.put("AcTokenId", PrefHelper.getInstance().getString(PrefHelper.FIREBASE_DEVICE_TOKEN, ""));
        params.put("DeviceType", String.valueOf(1));
        params.put("DeviceBrand", Build.BRAND);
        params.put("DeviceModel", Build.DEVICE + " " + Build.MODEL);
        params.put("DeviceProduct", Build.PRODUCT);
        params.put("DeviceSDKVersion", String.valueOf(Build.VERSION.SDK_INT));

        RestClient.getInstance().post(MyApplication.getInstance(), Request.Method.POST, params,
                ApiList.SET_CLIENT_TOKEN_ID, false, RequestCode.SetClientTokenId, this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Debug.trace("1", "1");
                    getARViewData();
                } else {
                    ToastHelper.getInstance().showMessage("The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission");
                    //Toast.makeText(actSplash.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
