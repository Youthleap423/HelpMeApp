package com.veeritsolutions.uhelpme.fragments.profile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.enums.RegisterBy;
import com.veeritsolutions.uhelpme.fragments.FragmentNotificationSetting;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import static com.veeritsolutions.uhelpme.R.id.view_strip;


public class SettingFragment extends Fragment implements OnClickEvent, OnBackPressedEvent {

    // xml components
    // private Toolbar toolbar;
    private TextView tvChangeLanguage, tvNotification, tvRateApp, tvShareApp, tvChangePassword, tvAboutUs;
    private View rootView, viewStrip;
    private LinearLayout linChangePass;

    // object and variable declaration
    private ProfileActivity profileActivity;
    private LoginUserModel loginUserModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        profileActivity = (ProfileActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        tvRateApp = (TextView) rootView.findViewById(R.id.tv_rateApp);
        tvRateApp.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvShareApp = (TextView) rootView.findViewById(R.id.tv_shareApp);
        tvShareApp.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvChangePassword = (TextView) rootView.findViewById(R.id.tv_changePassword);
        tvChangePassword.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvNotification = (TextView) rootView.findViewById(R.id.tv_notification);
        tvNotification.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvChangeLanguage = (TextView) rootView.findViewById(R.id.tv_changeLanguage);
        tvChangeLanguage.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvAboutUs = (TextView) rootView.findViewById(R.id.tv_aboutUs);
        tvAboutUs.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        linChangePass = (LinearLayout) rootView.findViewById(R.id.lin_change_password);
        viewStrip = rootView.findViewById(view_strip);

        if (loginUserModel.getRegisteredBy().equals(RegisterBy.APP.getRegisterBy())) {
            linChangePass.setVisibility(View.VISIBLE);
            viewStrip.setVisibility(View.VISIBLE);
            //profileActivity.pushFragment(new ChangePwdFragment(), true, true, null);
        } else {
            linChangePass.setVisibility(View.GONE);
            viewStrip.setVisibility(View.GONE);
        }
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // backButton = (ImageView) getView().findViewById(R.id.img_back_header);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                profileActivity.popBackFragment();
                break;

            case R.id.lin_change_password:
                Utils.buttonClickEffect(view);
                profileActivity.pushFragment(new ChangePwdFragment(), true, true, null);

                break;

            case R.id.lin_shareApp:
                Utils.buttonClickEffect(view);
                shareApp();
                break;

            case R.id.lin_rate_app:
                Utils.buttonClickEffect(view);
                goToPlayStore();
                break;

            case R.id.lin_aboutUs:
                Utils.buttonClickEffect(view);
                profileActivity.pushFragment(new AboutUsFragment(), true, false, null);
                break;

            case R.id.lin_changeLanguage:
                Utils.buttonClickEffect(view);
                profileActivity.pushFragment(new LanguageFragment(), true, false, null);
                break;

            case R.id.lin_notification:
                Utils.buttonClickEffect(view);
                profileActivity.pushFragment(new FragmentNotificationSetting(), true, false, null);
                break;

        }
    }


    @Override
    public void onBackPressed() {
        profileActivity.popBackFragment();

    }

    private void shareApp() {
        String shareBody = "https://play.google.com/store/apps/details?id=" + profileActivity.getPackageName();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "UHelpMe  (Open it in Google Play Store to Download the Application)");

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    private void goToPlayStore() {

        final String appPackageName = profileActivity.getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
