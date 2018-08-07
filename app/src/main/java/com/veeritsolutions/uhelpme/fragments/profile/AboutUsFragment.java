package com.veeritsolutions.uhelpme.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.maps.SupportMapFragment;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.AboutUsModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 7/22/2017.
 */

public class AboutUsFragment extends Fragment implements DataObserver, OnBackPressedEvent, OnClickEvent {

    private ProfileActivity profileActivity;
    private View rootView;

    private TextView tvaboutUs;

    private Map<String, String> params;
    private Bundle bundle;
    private AboutUsModel aboutUsModel;

    private SupportMapFragment spFragment;
    private LoginUserModel loginUserModel;
    private View view;
    private ArrayList<AboutUsModel> AboutUsContent;

    String fbUrl = "https://www.facebook.com/anivethub";
    String twitterUrl = "https://twitter.com/anivethub";
    String gplusUrl = "https://plus.google.com/u/0/113707138210597096384";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileActivity = (ProfileActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        tvaboutUs = (TextView) rootView.findViewById(R.id.tv_aboutUs);
        tvaboutUs.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GetDataAboutUs();

    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetAboutUs:

                aboutUsModel = (AboutUsModel) mObject;

                tvaboutUs.setText(aboutUsModel.getRemarks());
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
//            case R.id.img_fb:
//                Utils.buttonClickEffect(view);
//
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(fbUrl));
//                startActivity(i);
//                profileActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                break;
//
//            case R.id.img_twitter:
//                Utils.buttonClickEffect(view);
//
//                Intent i1 = new Intent(Intent.ACTION_VIEW);
//                i1.setData(Uri.parse(twitterUrl));
//                startActivity(i1);
//                profileActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                break;
//
//            case R.id.img_gplus:
//                Utils.buttonClickEffect(view);
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(gplusUrl));
//                startActivity(intent);
//                profileActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                break;

        }
    }

    private void GetDataAboutUs() {

        params = new HashMap<>();
        params.put("op", ApiList.GET_ABOUT_US);
        params.put("AuthKey", ApiList.AUTH_KEY);


        RestClient.getInstance().post(profileActivity, Request.Method.POST, params, ApiList.GET_ABOUT_US,
                true, RequestCode.GetAboutUs, this);
    }
}
