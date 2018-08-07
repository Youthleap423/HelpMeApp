package com.veeritsolutions.uhelpme.fragments.profile;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.adapters.AdpPayment;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PaymentModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/12/2017.
 */

public class PaymentFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerViewPayment;
    private TextView tvPayment, tvActivePayment, tvAddPayment, tvNoPayment;
    private TextView tvUserName, tvLocation, tvRatingLabel, tvRating, tvPointlabel, tvPoint, tvHelpMeLabel,
            tvHelpMe, tvOfferedLabel, tvOffered;
    private ImageView imgProfilePhoto;
    private RatingBar rbRating;

    private ProfileActivity profileActivity;
    private Map<String, String> params;
    private ArrayList<PaymentModel> paymentModelsList;
    private LoginUserModel loginUserModel;
   // private CharSequence[] paymentMethod = {"PayPal", "Strip"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileActivity = (ProfileActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_payments, container, false);

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
        //tvRating.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvPoint = (TextView) rootView.findViewById(R.id.tv_nav_header_points);
        tvPoint.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpMe = (TextView) rootView.findViewById(R.id.tv_nav_header_helpme);
        tvHelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvOffered = (TextView) rootView.findViewById(R.id.tv_nav_header_offered);
        tvOffered.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvPayment = (TextView) rootView.findViewById(R.id.tv_payments);
        tvPayment.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvActivePayment = (TextView) rootView.findViewById(R.id.tv_active_payment_method);
        tvActivePayment.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvAddPayment = (TextView) rootView.findViewById(R.id.txv_add_another_method);
        tvAddPayment.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvNoPayment = (TextView) rootView.findViewById(R.id.tv_no_active_payment);
        tvNoPayment.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        recyclerViewPayment = (RecyclerView) rootView.findViewById(R.id.recyclerView_payment);
        recyclerViewPayment.setLayoutManager(new LinearLayoutManager(profileActivity, LinearLayoutManager.VERTICAL, false));
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
        //tvRatingLabel.setText(String.valueOf(loginUserModel.getRating()));
        tvPointlabel.setText(String.valueOf(loginUserModel.getPoints()));
        tvHelpMeLabel.setText(String.valueOf(loginUserModel.getHelpMe()));
        tvOfferedLabel.setText(String.valueOf(loginUserModel.getOffered()));
        getPaymentData();
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientPayment:

                paymentModelsList = (ArrayList<PaymentModel>) mObject;

                if (paymentModelsList != null && !paymentModelsList.isEmpty()) {
                    recyclerViewPayment.setVisibility(View.VISIBLE);
                    tvNoPayment.setVisibility(View.GONE);
                    recyclerViewPayment.setAdapter(new AdpPayment(paymentModelsList, profileActivity));
                } else {
                    recyclerViewPayment.setVisibility(View.GONE);
                    tvNoPayment.setVisibility(View.VISIBLE);
                }
                break;

            case SetClientPayment:

                getPaymentData();
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetClientPayment:
                ToastHelper.getInstance().showMessage(mError);
                recyclerViewPayment.setVisibility(View.GONE);
                tvNoPayment.setVisibility(View.VISIBLE);
                break;
            case SetClientPayment:
                ToastHelper.getInstance().showMessage(mError);
                break;
        }

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

            case R.id.lin_addPayment:
                Utils.buttonClickEffect(view);
                showPaymentSelectionDialog();
                break;
        }
    }

    private void showPaymentSelectionDialog() {
        final Dialog dialog = new Dialog(profileActivity, R.style.dialogStyle);
        dialog.setContentView(R.layout.custom_dialog_payment);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tvHeader, tvPayPal, tvStripe;

        tvHeader = (TextView) dialog.findViewById(R.id.tv_dialogHeader);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvPayPal = (TextView) dialog.findViewById(R.id.tv_payapl);
        tvPayPal.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvStripe = (TextView) dialog.findViewById(R.id.tv_stripe);
        tvStripe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                insertClientPayment(0);
            }
        });

        tvStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                insertClientPayment(1);
            }
        });
        dialog.show();
    }

    private void insertClientPayment(int paymentType) {

        params = new HashMap<>();
        params.put("op", ApiList.SET_CLIENT_PAYMENT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("PaymentType", String.valueOf(paymentType));

       // params.put("tok_customer",String.valueOf(loginUserModel.getAcTokenId()));

        RestClient.getInstance().post(profileActivity, Request.Method.POST, params, ApiList.SET_CLIENT_PAYMENT,
                true, RequestCode.SetClientPayment, this);
    }

    private void getPaymentData() {

        params = new HashMap<>();
        params.put("op", ApiList.GET_CLIENT_PAYMENT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(profileActivity, Request.Method.POST, params, ApiList.GET_CLIENT_PAYMENT,
                true, RequestCode.GetClientPayment, this);
    }
}
