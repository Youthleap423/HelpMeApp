package com.veeritsolutions.uhelpme.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.veeritsolutions.uhelpme.activity.SignInActivity;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.enums.RegisterBy;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/4/2017.
 */

public class ProfileHomeFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private ImageView imgProfilePhoto;
    private TextView tvUserName, tvLocation, tvRatingLabel, tvRating, tvPointlabel, tvPoint, tvHelpMeLabel,
            tvHelpMe, tvOfferedLabel, tvOffered, tvMyProfile, tvReviewAndRating, tvRewards, tvPackages, tvPayments,
            tvSettings, tvAppFeedback, tvSignOut;
    private RatingBar rbRating;

    private ProfileActivity profileActivity;
    private LoginUserModel loginUserModel;
    private String lang;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileActivity = (ProfileActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile_home, container, false);

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

        // tvRating = (TextView) rootView.findViewById(R.id.tv_nav_header_rating);
        // tvRating.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvPoint = (TextView) rootView.findViewById(R.id.tv_nav_header_points);
        tvPoint.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpMe = (TextView) rootView.findViewById(R.id.tv_nav_header_helpme);
        tvHelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvOffered = (TextView) rootView.findViewById(R.id.tv_nav_header_offered);
        tvOffered.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvMyProfile = (TextView) rootView.findViewById(R.id.tv_myProfile);
        tvMyProfile.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvReviewAndRating = (TextView) rootView.findViewById(R.id.tv_reviewAndRating);
        tvReviewAndRating.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvRewards = (TextView) rootView.findViewById(R.id.tv_rewards);
        tvRewards.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvPackages = (TextView) rootView.findViewById(R.id.tv_packages);
        tvPackages.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvPayments = (TextView) rootView.findViewById(R.id.tv_payments);
        tvPayments.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvSettings = (TextView) rootView.findViewById(R.id.tv_settings);
        tvSettings.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvAppFeedback = (TextView) rootView.findViewById(R.id.tv_appFeedback);
        tvAppFeedback.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvSignOut = (TextView) rootView.findViewById(R.id.tv_signOut);
        tvSignOut.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        getClientInfo();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientInfo:
                loginUserModel = (LoginUserModel) mObject;
                setData(loginUserModel);
                break;
        }
    }

    private void setData(LoginUserModel loginUserModel) {

        Utils.setImage(loginUserModel.getProfilePic(), R.drawable.img_user_placeholder, imgProfilePhoto);
        rbRating.setRating(loginUserModel.getRating());
        tvUserName.setText(loginUserModel.getFirstName() + " " + loginUserModel.getLastName());
        if (loginUserModel.getState().length() == 0) {
            tvLocation.setText(loginUserModel.getCountry());
        } else {
            tvLocation.setText(loginUserModel.getState() + ", " + loginUserModel.getCountry());
        }
        // tvRating.setText(String.valueOf(loginUserModel.getRating()));
        tvPoint.setText(String.valueOf(loginUserModel.getPoints()));
        tvHelpMe.setText(String.valueOf(loginUserModel.getHelpMe()));
        tvOffered.setText(String.valueOf(loginUserModel.getOffered()));

        LoginUserModel.setLoginCredentials(RestClient.getGsonInstance().toJson(loginUserModel));
        // PrefHelper.getInstance().setString(PrefHelper.CLIENT_CREDENTIALS, Utils.objectToString(loginUserModel));
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

            case R.id.tv_myProfile:
                Utils.buttonClickEffect(view);
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.IS_FROM_SIGN_UP, 0);
                profileActivity.pushFragment(new MyProfileFragment(), true, false, bundle);
                break;

            case R.id.tv_reviewAndRating:
                Utils.buttonClickEffect(view);
                profileActivity.pushFragment(new ReviewRatingFragment(), true, false, null);
                break;

            case R.id.tv_rewards:
                Utils.buttonClickEffect(view);
                profileActivity.pushFragment(new RewardFragment(), true, false, null);
                break;

            case R.id.tv_packages:
                Utils.buttonClickEffect(view);
                profileActivity.pushFragment(new PackagesFragment(), true, false, null);

                break;

            case R.id.tv_payments:
                Utils.buttonClickEffect(view);
                profileActivity.pushFragment(new PaymentFragment(), true, false, null);
                break;

            case R.id.tv_settings:
                Utils.buttonClickEffect(view);
                profileActivity.pushFragment(new SettingFragment(), true, false, null);
                break;

            case R.id.tv_appFeedback:
                Utils.buttonClickEffect(view);
                profileActivity.pushFragment(new AppFeedabckFragment(), true, false, null);
                break;

            case R.id.tv_signOut:
                Utils.buttonClickEffect(view);
                LoginUserModel loginUser = LoginUserModel.getLoginUserModel();

                if (loginUser != null)

                    if (loginUser.getRegisteredBy().equals(RegisterBy.APP.getRegisterBy())) {

                        PrefHelper.getInstance().clearAllPrefs();
//                        if (FirebaseAuth.getInstance().getCurrentUser() != null)
//                            FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(profileActivity, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                        startActivity(intent);
                        profileActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        profileActivity.finish();

                    } else if (loginUser.getRegisteredBy().equals(RegisterBy.FACEBOOK.getRegisterBy())) {

                        PrefHelper.getInstance().clearAllPrefs();
                        SignInActivity.logoutToFacebook();
                        Intent intent = new Intent(profileActivity, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                        startActivity(intent);
                        profileActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        profileActivity.finish();

                        /*if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            CustomDialog.getInstance().showProgress(getActivity(), "", false);
                            //FirebaseAuth.getInstance().signOut();
                            FirebaseAuth.getInstance().getCurrentUser().delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            CustomDialog.getInstance().dismiss();
                                            if (task.isSuccessful()) {
                                                PrefHelper.getInstance().clearAllPrefs();
                                                SignInActivity.logoutToFacebook();
                                                Intent intent = new Intent(profileActivity, SignInActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                                                startActivity(intent);
                                                profileActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                profileActivity.finish();
                                            } else {
                                                ToastHelper.getInstance().showMessage(getString(R.string.str_signout_failed));
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            CustomDialog.getInstance().dismiss();
                                            ToastHelper.getInstance().showMessage(getString(R.string.str_signout_failed));
                                        }
                                    });
                        }*/
                    }
                break;
        }
    }

    private void getClientInfo() {

        try {

            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.GET_CLIENT_INFO);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CLIENT_INFO,
                    false, RequestCode.GetClientInfo, this);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
