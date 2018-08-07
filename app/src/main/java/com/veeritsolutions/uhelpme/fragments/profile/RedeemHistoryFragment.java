package com.veeritsolutions.uhelpme.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.adapters.AdpProductRedeem;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.ProductRedeem;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaishali on 7/14/2017.
 */

public class RedeemHistoryFragment extends Fragment implements DataObserver, OnBackPressedEvent, OnClickEvent, View.OnClickListener {

    private TextView tvUhelpMe;
    private RecyclerView recyclerViewReddem;

    private Bundle bundle;
    private ProfileActivity profileActivity;
    private Map<String, String> params;
    private AdpProductRedeem adpProductRedeem;
    private ArrayList<ProductRedeem> productRedeemList;
    private ProductRedeem productRedeem;

    private View rootView;
    private LoginUserModel loginUserModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginUserModel = LoginUserModel.getLoginUserModel();

        profileActivity = (ProfileActivity) getActivity();
        bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_redeem_history, container, false);

        recyclerViewReddem = (RecyclerView) rootView.findViewById(R.id.recyclerView_redeem_hostory);
        recyclerViewReddem.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return rootView;
        //  return super.onCreateView(inflater,container,savedInstanceState);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GetProductRedeemData();
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case GetProductRedeem:

                productRedeemList = (ArrayList<ProductRedeem>) mObject;

                if (productRedeemList != null && !productRedeemList.isEmpty()) {

                    if (adpProductRedeem != null && adpProductRedeem.getItemCount() > 0) {
                        adpProductRedeem.refreshList(productRedeemList);
                    } else {
                        adpProductRedeem = new AdpProductRedeem(getActivity(), productRedeemList, true, this);
                        recyclerViewReddem.setAdapter(adpProductRedeem);
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
        profileActivity.popBackFragment();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_RedeemList:
                Utils.buttonClickEffect(view);
                productRedeem = (ProductRedeem) view.getTag();
                adpProductRedeem.refreshList(productRedeemList);
                break;
        }
    }

    private void GetProductRedeemData() {

        params = new HashMap<>();
        params.put("op", ApiList.GET_PRODUCT_REDEEM);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));


        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_PRODUCT_REDEEM,
                false, RequestCode.GetProductRedeem, this);

    }
}
