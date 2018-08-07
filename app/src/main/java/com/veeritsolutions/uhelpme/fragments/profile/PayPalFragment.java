package com.veeritsolutions.uhelpme.fragments.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.Packages;
import com.veeritsolutions.uhelpme.models.PayPalModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/13/2017.
 */

public class PayPalFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    private View rootView;

    private ProfileActivity profileActivity;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(Constants.STRIPE_LIVE_API_KEY);
    private Bundle bundle;
    private Packages packages;
    private Map<String, String> params;
    private LoginUserModel loginUserModel;
    private HomeActivity homeActivity;
    private Activity activity;

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
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(new BigDecimal(packages.getAmount()), "CAD", packages.getPackageName(),
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent;
        if (activity instanceof ProfileActivity) {
            intent = new Intent(profileActivity, PaymentActivity.class);
        } else {
            intent = new Intent(homeActivity, PaymentActivity.class);
        }

        // send the same configuration for restart resiliency

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_paypal, container, false);

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
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case SubscriptionInsert:

                loginUserModel.setPoints(loginUserModel.getPoints() + packages.getCreditPoint());
                LoginUserModel.setLoginCredentials(RestClient.getGsonInstance().toJson(loginUserModel));
               // PrefHelper.getInstance().setString(PrefHelper.CLIENT_CREDENTIALS, Utils.objectToString(loginUserModel));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Debug.trace("paymentExample", confirm.toJSONObject().toString(4));
                    PayPalModel payPalModel = RestClient.getGsonInstance().fromJson(confirm.toJSONObject().getJSONObject("response").toString(), PayPalModel.class);
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
                    //ToastHelper.getInstance().showMessage(confirm.toJSONObject().toString(4));
                    insertSubscription(payPalModel);

                } catch (JSONException e) {
                    Debug.trace("paymentExample", "an extremely unlikely failure occurred: " + e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Debug.trace("paymentExample", "The user canceled.");
            ToastHelper.getInstance().showMessage(getString(R.string.payment_cancelled));
            if (activity instanceof ProfileActivity) {
                profileActivity.popBackFragment();
            } else {
                homeActivity.popBackFragment();
            }

        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Debug.trace("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            ToastHelper.getInstance().showMessage(getString(R.string.invalid_payment));
            if (activity instanceof ProfileActivity) {
                profileActivity.popBackFragment();
            } else {
                homeActivity.popBackFragment();
            }

        }
    }

    private void insertSubscription(PayPalModel payPalModel) {

        params = new HashMap<>();
        params.put("op", ApiList.SUBSCRIPTION_INSERT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("PackageId", String.valueOf(packages.getPackageId()));
        params.put("CreditPost", String.valueOf(packages.getCreditPost()));
        params.put("CreditPoint", String.valueOf(packages.getCreditPoint()));
        params.put("PaymentAmount", String.valueOf(packages.getAmount()));
        params.put("PaymentTime", payPalModel.getCreate_time());
        params.put("PaymentId", payPalModel.getId());
        params.put("PaymentStatus", payPalModel.getState());
        params.put("PaymentResponse", payPalModel.getIntent());

        if (activity instanceof ProfileActivity) {
            RestClient.getInstance().post(profileActivity, Request.Method.POST, params, ApiList.SUBSCRIPTION_INSERT,
                    true, RequestCode.SubscriptionInsert, this);

        } else {
            RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.SUBSCRIPTION_INSERT,
                    true, RequestCode.SubscriptionInsert, this);

        }

    }
}
