package com.veeritsolutions.uhelpme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.R;
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
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/27/2017.
 */

public class OtherPersonReviewRatingFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerViewReward;

   // private HomeActivity homeActivity;
    private LoginUserModel loginUserModel;
    private ArrayList<ReviewModel> reviewModelsList;
    private AdpReviewRating adapterReward;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   homeActivity = (HomeActivity) getActivity();
        loginUserModel = (LoginUserModel) getArguments().getSerializable(Constants.USER_DATA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_other_user_review_rating, container, false);

        recyclerViewReward = (RecyclerView) rootView.findViewById(R.id.recyclerView_other_user_profile);
        recyclerViewReward.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        getReview(loginUserModel);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetReview:

                reviewModelsList = (ArrayList<ReviewModel>) mObject;

                if (!reviewModelsList.isEmpty()) {
                    adapterReward = (AdpReviewRating) recyclerViewReward.getAdapter();

                    if (adapterReward!=null&&adapterReward.getItemCount()>0){
                        adapterReward.refreshList(reviewModelsList);
                    }else {
                        adapterReward = new AdpReviewRating(reviewModelsList, getActivity());
                        recyclerViewReward.setAdapter(adapterReward);
                    }
                    /*if (adapterReward == null) {

                        adapterReward = new AdpReviewRating(reviewModelsList, getActivity());
                        recyclerViewReward.setAdapter(adapterReward);
                    } else {
                        adapterReward.refreshList(reviewModelsList);
                    }*/
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {
        //homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
               // homeActivity.popBackFragment();
                break;
        }
    }

    private void getReview(LoginUserModel loginUserModel) {

        try {

            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.GET_REVIEW);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_REVIEW,
                    false, RequestCode.GetReview, this);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}


