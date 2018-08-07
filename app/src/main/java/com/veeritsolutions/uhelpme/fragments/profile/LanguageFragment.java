package com.veeritsolutions.uhelpme.fragments.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.Locale;

/**
 * Created by VEER7 on 7/22/2017.
 */

public class LanguageFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private ImageView imgEnglish, imgFrench;
    private TextView tvEnglish, tvFrench;

    private ProfileActivity profileActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileActivity = (ProfileActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_language_activity, container, false);

        imgEnglish = (ImageView) rootView.findViewById(R.id.img_english);
        imgEnglish.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        // imgEnglish.setOnClickListener(this);
        imgFrench = (ImageView) rootView.findViewById(R.id.img_french);
        imgFrench.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        // imgFrench.setOnClickListener(this);

        tvEnglish = (TextView) rootView.findViewById(R.id.tv_english);
        tvEnglish.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvFrench = (TextView) rootView.findViewById(R.id.tv_french);
        tvFrench.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        if (PrefHelper.getInstance().containKey(PrefHelper.LANGUAGE)) {
            String language = PrefHelper.getInstance().getString(PrefHelper.LANGUAGE, "");
            if (language.equals("en")) {
                imgEnglish.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                imgFrench.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
            } else if (language.equals("fr")) {
                imgFrench.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                imgEnglish.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

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

            case R.id.img_english:
                Utils.buttonClickEffect(view);
                String lang = "en";

                PrefHelper.getInstance().setString(PrefHelper.LANGUAGE, lang);
                imgEnglish.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                imgFrench.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
                showLanguageDialog();
                Configuration config = profileActivity.getBaseContext().getResources().getConfiguration();
                if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
                    Locale locale = new Locale(lang);
                    Locale.setDefault(locale);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        config.setLocale(locale);
                        //profileActivity.getBaseContext().createConfigurationContext(config);
                        profileActivity.getBaseContext().getResources().updateConfiguration(config, profileActivity.getBaseContext().getResources().getDisplayMetrics());
                    } else {
                        config.locale = locale;
                        profileActivity.getBaseContext().getResources().updateConfiguration(config, profileActivity.getBaseContext().getResources().getDisplayMetrics());
                    }
                    //  profileActivity.recreate();
                }
                // reload();
                break;


            case R.id.img_french:
                Utils.buttonClickEffect(view);
                String  language = "fr";
                PrefHelper.getInstance().setString(PrefHelper.LANGUAGE, language);
                imgEnglish.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
                imgFrench.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                showLanguageDialog();
                config = profileActivity.getBaseContext().getResources().getConfiguration();

                if (!"".equals(language) && !config.locale.getLanguage().equals(language)) {
                    Locale locale = new Locale(language);
                    Locale.setDefault(locale);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        config.setLocale(locale);
                        //profileActivity.getBaseContext().createConfigurationContext(config);
                        profileActivity.getBaseContext().getResources().updateConfiguration(config, profileActivity.getBaseContext().getResources().getDisplayMetrics());
                    } else {
                        config.locale = locale;
                        profileActivity.getBaseContext().getResources().updateConfiguration(config, profileActivity.getBaseContext().getResources().getDisplayMetrics());
                    }
                    // profileActivity.recreate();
                }
                //  reload();
                break;
        }
    }

    private void reload() {
        getActivity().finish();
        Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
        startActivity(homeIntent);
    }

    public void showLanguageDialog() {
        final AlertDialog.Builder builder;
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder = new AlertDialog.Builder(profileActivity, R.style.dialogStyle);
        //  } else {
        //      builder = new AlertDialog.Builder(profileActivity);
        //  }
        // builder.create();
        builder.setTitle(getString(R.string.restart_application));
        builder.setMessage(R.string.for_language_change_restart_application);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                reload();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}


