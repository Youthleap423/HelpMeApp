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
import com.veeritsolutions.uhelpme.adapters.AdpOtherOffer;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.EndlessRecyclerViewScrollListener;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/27/2017.
 */

public class OtherPersonOffersFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerViewDashboard;

   // private HomeActivity homeActivity;
    private ArrayList<PostedJobModel> myOfferList;
    private AdpOtherOffer adpMyOffer;
    private Map<String, String> params;
    private LoginUserModel loginUserModel;
    private int totalPages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   homeActivity = (HomeActivity) getActivity();
        loginUserModel = (LoginUserModel) getArguments().getSerializable(Constants.USER_DATA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_other_user_offers, container, false);
        recyclerViewDashboard = (RecyclerView) rootView.findViewById(R.id.recyclerView_dash_myoffer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDashboard.setLayoutManager(linearLayoutManager);

        recyclerViewDashboard.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager,1) {
            @Override
            protected void onShow() {

            }

            @Override
            protected void onHide() {

            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page <= totalPages) {

                    adpMyOffer.showLoading(true);
                    getMyOfferData(page, false);


                } else {
                    adpMyOffer.showLoading(false);
                }
            }
        });
        myOfferList = new ArrayList<>();

        getMyOfferData(1, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode){

            case GetJobPostMyOffers:

                ArrayList<PostedJobModel> list = (ArrayList<PostedJobModel>) mObject;

                if (!list.isEmpty()) {
                    myOfferList.addAll(list);
                    if (!myOfferList.isEmpty()) {
                        totalPages = myOfferList.get(0).getTotalPage();
                        adpMyOffer = (AdpOtherOffer) recyclerViewDashboard.getAdapter();
                        if (adpMyOffer != null && adpMyOffer.getItemCount() > 0) {
                            adpMyOffer.refreshList(myOfferList);
                        } else {
                            adpMyOffer = new AdpOtherOffer(getActivity(), myOfferList, false);
                            recyclerViewDashboard.setAdapter(adpMyOffer);
                        }
                    }
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
      //  homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
              //  homeActivity.popBackFragment();
                break;
        }
    }

    private void getMyOfferData(int pageNo, boolean isDialogRequired) {

        params = new HashMap<>();
        params.put("op", ApiList.GET_JOB_POST_MY_OFFERS);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("PageNumber", String.valueOf(pageNo));
        params.put("Records", String.valueOf(10));

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_JOB_POST_MY_OFFERS,
                isDialogRequired, RequestCode.GetJobPostMyOffers, this);

    }
}

