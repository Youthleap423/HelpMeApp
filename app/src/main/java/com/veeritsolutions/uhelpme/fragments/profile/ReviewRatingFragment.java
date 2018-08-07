package com.veeritsolutions.uhelpme.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;

import com.veeritsolutions.uhelpme.adapters.AdpReviewRating;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;


import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.ReviewModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 6/27/2017.
 */

public class ReviewRatingFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private ProfileActivity homeActivity;
    private RecyclerView recyclerViewReward;
    private TextView tvUserName, tvLocation, tvRatingLabel, tvRating, tvPointlabel, tvPoint, tvHelpMeLabel,
            tvHelpMe, tvOfferedLabel, tvOffered, tvReview;
    private ImageView imgProfilePhoto;
    private FloatingActionButton fabChat;
    private RatingBar rbRating;

    private LoginUserModel loginUserModel;
    private ArrayList<ReviewModel> reviewModelsList;
    private AdpReviewRating adapterReward;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginUserModel = LoginUserModel.getLoginUserModel();
        homeActivity = (ProfileActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_review_rating, container, false);

        fabChat = (FloatingActionButton) rootView.findViewById(R.id.fab_chat);
        fabChat.setVisibility(View.GONE);
        imgProfilePhoto = (ImageView) rootView.findViewById(R.id.img_navHeaderProfilePhoto);
        rbRating = (RatingBar) rootView.findViewById(R.id.rb_rating);

        tvUserName = (TextView) rootView.findViewById(R.id.tv_navHeaderName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvLocation = (TextView) rootView.findViewById(R.id.tv_navHeaderLocation);
        tvLocation.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

        //tvRatingLabel = (TextView) rootView.findViewById(R.id.tv_nav_header_rating_text);
        //tvRatingLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

        tvPointlabel = (TextView) rootView.findViewById(R.id.tv_nav_header_points_text);
        tvPointlabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

        tvHelpMeLabel = (TextView) rootView.findViewById(R.id.tv_nav_header_help_me_text);
        tvHelpMeLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

        tvOfferedLabel = (TextView) rootView.findViewById(R.id.tv_nav_header_offered_text);
        tvOfferedLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

        //tvRating = (TextView) rootView.findViewById(R.id.tv_nav_header_rating);
       // tvRating.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvPoint = (TextView) rootView.findViewById(R.id.tv_nav_header_points);
        tvPoint.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpMe = (TextView) rootView.findViewById(R.id.tv_nav_header_helpme);
        tvHelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvOffered = (TextView) rootView.findViewById(R.id.tv_nav_header_offered);
        tvOffered.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvReview = (TextView) rootView.findViewById(R.id.tv_review);
        tvReview.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        recyclerViewReward = (RecyclerView) rootView.findViewById(R.id.recyclerView_other_user_profile);
        recyclerViewReward.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.setImage(loginUserModel.getProfilePic(), R.drawable.img_user_placeholder, imgProfilePhoto);
        rbRating.setRating(loginUserModel.getRating());
        tvUserName.setText(loginUserModel.getFirstName() + " " + loginUserModel.getLastName());
        if (loginUserModel.getState().length() == 0) {
            tvLocation.setText(loginUserModel.getCountry());
        } else {
            tvLocation.setText(loginUserModel.getState() + ", " + loginUserModel.getCountry());
        }
       // tvRatingLabel.setText(String.valueOf(loginUserModel.getRating()));
        tvPointlabel.setText(String.valueOf(loginUserModel.getPoints()));
        tvHelpMeLabel.setText(String.valueOf(loginUserModel.getHelpMe()));
        tvOfferedLabel.setText(String.valueOf(loginUserModel.getOffered()));
        GetReview();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                homeActivity.popBackFragment();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetReview:

                reviewModelsList = (ArrayList<ReviewModel>) mObject;

                if (!reviewModelsList.isEmpty()) {
                    if (adapterReward == null) {

                        adapterReward = new AdpReviewRating(reviewModelsList, getActivity());
                        recyclerViewReward.setAdapter(adapterReward);
                    } else {
                        adapterReward.refreshList(reviewModelsList);
                        //adapterReward.notifyDataSetChanged();
                    }
                }
                break;
        }

    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    private void GetReview() {
        try {

            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.GET_REVIEW);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_REVIEW,
                    true, RequestCode.GetReview, this);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
