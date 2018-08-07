package com.veeritsolutions.uhelpme.fragments.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.adapters.AdpProductRedeem;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.ProductRedeem;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/15/2017.
 */

public class RedeemPointsFragment extends Fragment implements View.OnClickListener, OnClickEvent,
        OnBackPressedEvent, DataObserver, RewardedVideoAdListener {

    private View rootView;
    private ProgressBar progressBar;
    private TextView tvRedeemPoints;
    private Button btnRedeemPoints, btnLearnMore;

    private ProfileActivity profileActivity;
    private LoginUserModel loginUserModel;
    private Map<String, String> params;
    private Dialog productDialog;
    private ProductRedeem productRedeem;
    private AlertDialog.Builder builder;

    private RewardedVideoAd mRewardedVideoAd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginUserModel = LoginUserModel.getLoginUserModel();

        profileActivity = (ProfileActivity) getActivity();
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(profileActivity, Constants.APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(profileActivity);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //  pauseGame();
        mRewardedVideoAd.pause(profileActivity);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!mGameOver && mGamePaused) {
//            resumeGame();
//        }
        mRewardedVideoAd.resume(profileActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_rewards_points, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.circularProgressBar);
        tvRedeemPoints = (TextView) rootView.findViewById(R.id.txv_Redeem_Points);
        tvRedeemPoints.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        btnLearnMore = (Button) rootView.findViewById(R.id.btn_learnMore);
        btnLearnMore.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
        btnLearnMore.setOnClickListener(this);

        btnRedeemPoints = (Button) rootView.findViewById(R.id.btn_Redeem_points);
        btnRedeemPoints.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        btnRedeemPoints.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvRedeemPoints.setText(String.valueOf(loginUserModel.getPoints()));
        progressBar.setProgress((int) loginUserModel.getPoints());
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetProduct:

                ArrayList<ProductRedeem> productRedeemsList = (ArrayList<ProductRedeem>) mObject;
                if (productRedeemsList != null && !productRedeemsList.isEmpty()) {
                    showProductDialog(productRedeemsList);
                }
                break;

            case ProductRedeemInsert:

                int remainingPoints = (int) (loginUserModel.getPoints() - productRedeem.getPoint());
                tvRedeemPoints.setText(String.valueOf(remainingPoints));
                loginUserModel.setPoints(remainingPoints);
                LoginUserModel.setLoginCredentials(RestClient.getGsonInstance().toJson(loginUserModel));
                if (productDialog != null && productDialog.isShowing()) {
                    productDialog.dismiss();
                }
                //PrefHelper.getInstance().setString(PrefHelper.CLIENT_CREDENTIALS, Utils.objectToString(loginUserModel));
                break;
        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {
        profileActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                profileActivity.popBackFragment();
                break;

            case R.id.btn_Redeem_points:
                Utils.buttonClickEffect(view);
                getProductData();
                break;

            case R.id.lin_Redeem:
                Utils.buttonClickEffect(view);
                productRedeem = (ProductRedeem) view.getTag();

                if (productRedeem != null) {

                    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(profileActivity, R.style.dialogStyle);
                    //  } else {
                    //      builder = new AlertDialog.Builder(profileActivity);
                    //  }
                    // builder.create();
                    builder.setTitle(getString(R.string.redeem_points));
                    builder.setMessage(R.string.sure_want_to_redeem);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            if (productRedeem.getPoint() > loginUserModel.getPoints()) {
                                ToastHelper.getInstance().showMessage(getString(R.string.you_dont_have_enough_points));
                            } else {
                                insertRedeemProduct(productRedeem);
                            }

                        }
                    });
                    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                break;

            case R.id.btn_learnMore:
                Utils.buttonClickEffect(view);
                final AlertDialog.Builder builder;
                // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(profileActivity, R.style.dialogStyle);
                //  } else {
                //      builder = new AlertDialog.Builder(profileActivity);
                //  }
                // builder.create();
                builder.setTitle(getString(R.string.advertise_videos));
                builder.setMessage(R.string.are_you_want_to_see_videos);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        showRewardedVideo();
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }

    private void insertRedeemProduct(ProductRedeem productRedeem) {

        params = new HashMap<>();
        params.put("op", ApiList.PRODUCT_REDEEM_INSERT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("ProductId", String.valueOf(productRedeem.getProductId()));
        params.put("RedeemPoint", String.valueOf(productRedeem.getPoint()));

        RestClient.getInstance().post(profileActivity, Request.Method.POST, params, ApiList.PRODUCT_REDEEM_INSERT,
                true, RequestCode.ProductRedeemInsert, this);

    }

    private void getProductData() {

        params = new HashMap<>();
        params.put("op", ApiList.GET_PRODUCT);
        params.put("AuthKey", ApiList.AUTH_KEY);

        RestClient.getInstance().post(profileActivity, Request.Method.POST, params, ApiList.GET_PRODUCT,
                true, RequestCode.GetProduct, this);

    }

    private void showProductDialog(ArrayList<ProductRedeem> productList) {

        productDialog = new Dialog(profileActivity, R.style.dialogStyle);
        productDialog.setContentView(R.layout.custom_dialog_all_product);
        productDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tvHeader;
        ImageView imgClose;
        RecyclerView recyclerView;

        tvHeader = (TextView) productDialog.findViewById(R.id.tv_dialogHeader);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        imgClose = (ImageView) productDialog.findViewById(R.id.img_close);

        recyclerView = (RecyclerView) productDialog.findViewById(R.id.recyclerView_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdpProductRedeem(profileActivity, productList, false, this));

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDialog.dismiss();
                // profileActivity.pushFragment(new PayPalFragment(), true, false, null);
            }
        });

        productDialog.show();
    }

    private void showRewardedVideo() {
        CustomDialog.getInstance().showProgress(profileActivity, "", true);
        //mShowVideoButton.setVisibility(View.INVISIBLE);
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            loadRewardedVideoAd();
        }
    }

    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd(Constants.AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Debug.trace("MyVideo", "loaded");
        mRewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Debug.trace("MyVideo", "Opened");
        CustomDialog.getInstance().dismiss();
    }

    @Override
    public void onRewardedVideoStarted() {
        Debug.trace("MyVideo", "started");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Debug.trace("MyVideo", "closed");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Debug.trace("MyVideo", "rewarded");
        ToastHelper.getInstance().showMessage(getString(R.string.you_will_rewarded_video_points));
        insertVideoPoints();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Debug.trace("MyVideo", "Left app");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {

        ToastHelper.getInstance().showMessage("failed " + errorCode);
        CustomDialog.getInstance().dismiss();
    }

    private void insertVideoPoints() {

        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.CLIENT_WATCH_VIDEO_POINTS);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(profileActivity, Request.Method.POST, params,
                ApiList.CLIENT_WATCH_VIDEO_POINTS, true, RequestCode.ClientWatchVideo, this);
    }
}


