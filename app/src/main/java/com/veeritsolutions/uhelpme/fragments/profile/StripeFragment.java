package com.veeritsolutions.uhelpme.fragments.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.Packages;
import com.veeritsolutions.uhelpme.models.StripeModel;
import com.veeritsolutions.uhelpme.models.SubscriptionModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/13/2017.
 */

public class StripeFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    private View rootView;
    private CardInputWidget mCardInputWidget;

    private ProfileActivity profileActivity;
    private LoginUserModel loginUserModel;
    private HomeActivity homeActivity;
    private Activity activity;
    private Bundle bundle;
    private Packages packages;
    private SubscriptionModel subscriptionModel;
    private Map<String, String> params;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        if (activity instanceof ProfileActivity) {
            profileActivity = (ProfileActivity) getActivity();
        } else {
            homeActivity = (HomeActivity) getActivity();
        }

        loginUserModel = LoginUserModel.getLoginUserModel();
        bundle = getArguments();
        if (bundle != null) {
            packages = (Packages) bundle.getSerializable(Constants.PACKAGE_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_stripe, container, false);
        mCardInputWidget = (CardInputWidget) rootView.findViewById(R.id.card_input_widget);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onBackPressed() {

        if (activity instanceof ProfileActivity) {
            profileActivity.popBackFragment();
        } else {
            homeActivity.popBackFragment();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                if (activity instanceof ProfileActivity) {
                    profileActivity.popBackFragment();
                } else {
                    homeActivity.popBackFragment();
                }

                break;
            case R.id.btn_submit:
                Utils.buttonClickEffect(view);
                saveCard();
                break;
        }
    }

    private void saveCard() {
        Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            ToastHelper.getInstance().showMessage(getString(R.string.invalid_card));
        } else {
            //cardToSave.setName(loginUserModel.getFirstName() + " " + loginUserModel.getLastName());
            //cardToSave.setAddressZip("12345");
            //cardToSave.setCurrency("USD");
            //cardToSave.setAddressCountry("U.S");
            Stripe stripe;

            stripe = new Stripe(getActivity(), Constants.STRIPE_LIVE_API_KEY);

            stripe.createToken(cardToSave, new TokenCallback() {
                        public void onSuccess(Token token) {
                            CustomDialog.getInstance().dismiss();
                            // Send token to your server
                            //ToastHelper.getInstance().showMessage("Payment id : " + token.getId());
                            Debug.trace("StripeTokenId", token.getId());
                            //insertStripePayment(token.getId());
                        }

                        public void onError(Exception error) {
                            CustomDialog.getInstance().dismiss();
                            // Show localized error message
                            ToastHelper.getInstance().showMessage(getString(R.string.payment_failed_stripe));
                            //ToastHelper.getInstance().showMessage(error.getMessage());
                        }
                    }
            );
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case DoStripePayment:

                StripeModel stripeModel = (StripeModel) mObject;
                if (stripeModel.getStatus().equals("succeeded")) {

                    insertSubscription(stripeModel);
                } else {
                    ToastHelper.getInstance().showMessage(getString(R.string.str_error_doing_payment));
                }
                break;

            case SubscriptionInsert:

                loginUserModel.setPoints(loginUserModel.getPoints() + packages.getCreditPoint());
                LoginUserModel.setLoginCredentials(RestClient.getGsonInstance().toJson(loginUserModel));
                //PrefHelper.getInstance().setString(PrefHelper.CLIENT_CREDENTIALS, Utils.objectToString(loginUserModel));
                if (activity instanceof ProfileActivity) {
                    profileActivity.popBackFragment();
                } else {
                    homeActivity.popBackFragment();
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    public void insertStripePayment(String tokenId) {

        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.DO_STRIPE_PAYMENT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("iAmount", String.valueOf(packages.getAmount()));
        params.put("sCurrency", "CAD");
        params.put("sDescription", packages.getDescription());
        params.put("sTokenId", tokenId);


        RestClient.getInstance().post(activity, Request.Method.POST, params, ApiList.DO_STRIPE_PAYMENT,
                true, RequestCode.DoStripePayment, this);
    }

    private void insertSubscription(StripeModel payPalModel) {

        params = new HashMap<>();
        params.put("op", ApiList.SUBSCRIPTION_INSERT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("PackageId", String.valueOf(packages.getPackageId()));
        params.put("CreditPost", String.valueOf(packages.getCreditPost()));
        params.put("CreditPoint", String.valueOf(packages.getCreditPoint()));
        params.put("PaymentAmount", String.valueOf(packages.getAmount()));
        params.put("PaymentTime", Utils.dateFormat(System.currentTimeMillis(), Constants.MM_DD_YYYY_HH_MM_SS_A));
        params.put("PaymentId", payPalModel.getId());
        params.put("PaymentStatus", payPalModel.getStatus());
        params.put("PaymentResponse", payPalModel.getStatus());

        // if (activity instanceof ProfileActivity) {
        RestClient.getInstance().post(activity, Request.Method.POST, params, ApiList.SUBSCRIPTION_INSERT,
                true, RequestCode.SubscriptionInsert, this);

        //} else {
        //     RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.SUBSCRIPTION_INSERT,
        //             true, RequestCode.SubscriptionInsert, this);

        // }

    }
}

