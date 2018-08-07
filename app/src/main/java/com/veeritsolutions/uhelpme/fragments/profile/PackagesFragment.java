package com.veeritsolutions.uhelpme.fragments.profile;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.adapters.AdpPackages;
import com.veeritsolutions.uhelpme.adapters.AdpSubscription;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.Packages;
import com.veeritsolutions.uhelpme.models.SubscriptionModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaishali on 7/10/2017.
 */

public class PackagesFragment extends Fragment implements DataObserver, OnBackPressedEvent, OnClickEvent {

    //private TextView tvUhelpMe;
    private RecyclerView recyclerViewSubscription;
    private View rootView;
    private RatingBar rbRating;
    private ImageView imgProfilePhoto;
    private TextView tvUserName, tvLocation, tvRatingLabel, tvRating, tvPointlabel, tvPoint, tvHelpMeLabel,
            tvHelpMe, tvOfferedLabel, tvOffered;
    private Button btnViewPackages;

    private Bundle bundle;
    private ProfileActivity profileActivity;
    private HomeActivity homeActivity;
    private Map<String, String> params;
    private AdpSubscription adpSubscription;
    private ArrayList<Packages> categoryPackages;
    //  private Packages PackagesModel;
    private LoginUserModel loginUserModel;
    private Dialog packageDialog, paymentDialog;
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

        // bundle = getArguments();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_packages, container, false);

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

        recyclerViewSubscription = (RecyclerView) rootView.findViewById(R.id.recyclerView_subscription);
        recyclerViewSubscription.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        btnViewPackages = (Button) rootView.findViewById(R.id.btn_view_packages);
        btnViewPackages.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        loginUserModel = LoginUserModel.getLoginUserModel();

        return rootView;
        //  return super.onCreateView(inflater,container,savedInstanceState);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.setImage(loginUserModel.getProfilePic(), R.drawable.img_user_placeholder, imgProfilePhoto);
        tvUserName.setText(loginUserModel.getFirstName() + " " + loginUserModel.getLastName());
        rbRating.setRating(loginUserModel.getRating());
        if (loginUserModel.getState().length() == 0) {
            tvLocation.setText(loginUserModel.getCountry());
        } else {
            tvLocation.setText(loginUserModel.getState() + ", " + loginUserModel.getCountry());
        }
        //tvRatingLabel.setText(String.valueOf(loginUserModel.getRating()));
        tvPointlabel.setText(String.valueOf(loginUserModel.getPoints()));
        tvHelpMeLabel.setText(String.valueOf(loginUserModel.getHelpMe()));
        tvOfferedLabel.setText(String.valueOf(loginUserModel.getOffered()));

        // getPackagesData();

        getSubscriptionData();
    }

    private void getSubscriptionData() {

        params = new HashMap<>();
        params.put("op", ApiList.GET_SUBSCRIPTION);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        if (activity instanceof ProfileActivity) {
            RestClient.getInstance().post(profileActivity, Request.Method.POST, params, ApiList.GET_SUBSCRIPTION,
                    true, RequestCode.GetSubscription, this);
        } else {
            RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_SUBSCRIPTION,
                    true, RequestCode.GetSubscription, this);
        }
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetPackage:

                categoryPackages = (ArrayList<Packages>) mObject;

                if (categoryPackages != null && !categoryPackages.isEmpty()) {

                    showPackagesDialog(categoryPackages);
                }
                break;

            case GetSubscription:

                ArrayList<SubscriptionModel> list = (ArrayList<SubscriptionModel>) mObject;

                if (list != null && !list.isEmpty()) {

                    adpSubscription = (AdpSubscription) recyclerViewSubscription.getAdapter();
                    if (adpSubscription != null && adpSubscription.getItemCount() > 0) {
                        adpSubscription.refreshList(list);
                    } else {
                        if (activity instanceof ProfileActivity) {
                            adpSubscription = new AdpSubscription(profileActivity, list);
                        } else {
                            adpSubscription = new AdpSubscription(homeActivity, list);
                        }

                        recyclerViewSubscription.setAdapter(adpSubscription);
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

            case R.id.lin_packages:
                Utils.buttonClickEffect(view);
                Packages packages = (Packages) view.getTag();

                if (packages != null) {
                    //dismiss();
                    showPaymentSelectionDialog(packages);
                }
                break;

            case R.id.btn_view_packages:
                Utils.buttonClickEffect(view);
                getPackagesData();
                break;
        }
    }


    private void getPackagesData() {

        params = new HashMap<>();
        params.put("op", "GetPackage");
        params.put("AuthKey", ApiList.AUTH_KEY);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_PACKAGE,
                true, RequestCode.GetPackage, this);
    }

    private void showPaymentSelectionDialog(Packages packages) {

        if (activity instanceof ProfileActivity) {
            paymentDialog = new Dialog(profileActivity, R.style.dialogStyle);
        } else {
            paymentDialog = new Dialog(homeActivity, R.style.dialogStyle);
        }

        paymentDialog.setContentView(R.layout.custom_dialog_payment);
        paymentDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tvHeader, tvPayPal, tvStripe;

        tvHeader = (TextView) paymentDialog.findViewById(R.id.tv_dialogHeader);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvPayPal = (TextView) paymentDialog.findViewById(R.id.tv_payapl);
        tvPayPal.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvStripe = (TextView) paymentDialog.findViewById(R.id.tv_stripe);
        tvStripe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        bundle = new Bundle();
        bundle.putSerializable(Constants.PACKAGE_DATA, packages);

        tvPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentDialog.dismiss();
                packageDialog.dismiss();

                if (activity instanceof ProfileActivity) {
                    profileActivity.pushFragment(new PayPalFragment(), true, false, bundle);
                } else {
                    homeActivity.pushFragment(new PayPalFragment(), true, false, bundle);
                }

            }
        });

        tvStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentDialog.dismiss();
                packageDialog.dismiss();
                if (activity instanceof ProfileActivity) {
                    profileActivity.pushFragment(new StripeFragment(), true, false, bundle);
                } else {
                    homeActivity.pushFragment(new StripeFragment(), true, false, bundle);
                }

            }
        });
        paymentDialog.show();
    }

    private void showPackagesDialog(ArrayList<Packages> packagesList) {

        if (activity instanceof ProfileActivity) {
            packageDialog = new Dialog(profileActivity, R.style.dialogStyle);
        } else {
            packageDialog = new Dialog(homeActivity, R.style.dialogStyle);
        }

        packageDialog.setContentView(R.layout.custom_dialog_packages);
        packageDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tvHeader;
        ImageView imgClose;
        RecyclerView recyclerView;

        tvHeader = (TextView) packageDialog.findViewById(R.id.tv_dialogHeader);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        imgClose = (ImageView) packageDialog.findViewById(R.id.img_close);

        recyclerView = (RecyclerView) packageDialog.findViewById(R.id.recyclerView_packages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (activity instanceof ProfileActivity) {
            recyclerView.setAdapter(new AdpPackages(profileActivity, packagesList));
        } else {
            recyclerView.setAdapter(new AdpPackages(homeActivity, packagesList));
        }


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packageDialog.dismiss();
                // profileActivity.pushFragment(new PayPalFragment(), true, false, null);
            }
        });

        packageDialog.show();
    }
}
