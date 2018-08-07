package com.veeritsolutions.uhelpme.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaishali on 7/13/2017.
 */

public class AppFeedabckFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    private EditText edtAppFeedback;
    Button btnsubmit;
    LoginUserModel loginUserModel;
    private String remark;
    private View rootView;
    private Bundle bundle;
    private ProfileActivity profileActivity;
    private String title, description;
    private TextView lbl_FeddBack;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileActivity = (ProfileActivity) getActivity();
        bundle = getArguments();
        loginUserModel = LoginUserModel.getLoginUserModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_app_feedback, container, false);

        btnsubmit = (Button) rootView.findViewById(R.id.btn_submit_app_feedback);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        edtAppFeedback = (EditText) rootView.findViewById(R.id.edt_App_FeddBack);
        edtAppFeedback.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        lbl_FeddBack = (TextView) rootView.findViewById(R.id.lbl_FeddBack);
        lbl_FeddBack.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case SetClientAppFeedback:
                ToastHelper.getInstance().showMessage(getString(R.string.thank_you));
                profileActivity.popBackFragment();
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

            case R.id.btn_submit_app_feedback:
                Utils.buttonClickEffect(view);
                ClientFeedBack();
                break;

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                profileActivity.popBackFragment();
                break;

        }

    }

    private void ClientFeedBack() {

        String desc = edtAppFeedback.getText().toString().trim();

        if (desc.isEmpty()) {
            ToastHelper.getInstance().showMessage(getString(R.string.write_your_review));
            return;
        }

        remark = edtAppFeedback.getText().toString();
        try {
            Map<String, String> params = new HashMap<>();
            params.put("op", "SetClientAppFeedback");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            // params.put("JobPostId",String.valueOf(loginUserModel.getClientId()));
            params.put("AppFeedback", remark);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.SET_CLIENT_FEEDBACK,
                    true, RequestCode.SetClientAppFeedback, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void GetClientAppFeedback() {
//
//        String desc = edtAppFeedback.getText().toString();
//
//        if (desc.isEmpty()) {
//            edtAppFeedback.setError("Enter FeedBack");
//            return;
//        }
//
//        bundle = new Bundle();
//        bundle.putInt(Constants.IS_FROM_HOME_ACTIVITY, 0);
//        bundle.putString(Constants.APPFEEDBACK, desc);
////        profileActivity.pushFragment(new PostHelpCategoryFragment(), true, false, bundle);
//    }
}
