package com.veeritsolutions.uhelpme.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaishali on 7/12/2017.
 */

public class ChangePwdFragment extends Fragment implements OnClickEvent, DataObserver, OnBackPressedEvent {


    // private int loginUser;
    LoginUserModel loginUserModel;
    // xml components
    private Toolbar toolbar;
    private TextView tvHeader;
    private Button btnSave;
    private EditText edtOldPwd, edtNewPwd, edtConfirmPwd;
    private String remark;
    private View rootView;
    private Bundle bundle;
    // object and variable declaration
    private JSONObject params;
    private ProfileActivity profileActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileActivity = (ProfileActivity) getActivity();
        bundle = getArguments();
        loginUserModel = LoginUserModel.getLoginUserModel();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_change_pwd, container, false);

        edtOldPwd = (EditText) rootView.findViewById(R.id.edt_old_pwd);
        edtOldPwd.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        edtNewPwd = (EditText) rootView.findViewById(R.id.edt_new_pwd);
        edtNewPwd.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        edtConfirmPwd = (EditText) rootView.findViewById(R.id.edt_confirm_pwd);
        edtConfirmPwd.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnSave.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.setupOutSideTouchHideKeyboard(rootView);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case ClientChangePassword:

                ToastHelper.getInstance().showMessage(getString(R.string.passwrd_changed_successfully));
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

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                profileActivity.popBackFragment();
                break;

            case R.id.btnSave:
                Utils.buttonClickEffect(view);
                ClientChangePassword();
                break;

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;
        }
    }

    private void ClientChangePassword() {

        String OldPwd = edtOldPwd.getText().toString().trim();
        String NewPwd = edtNewPwd.getText().toString().trim();
        String ConfirmPwd = edtConfirmPwd.getText().toString().trim();

        if (OldPwd.isEmpty()) {
            edtOldPwd.requestFocus();
            edtOldPwd.setError(getString(R.string.enter_old_password));
            return;
        }

        if (NewPwd.isEmpty()) {
            edtNewPwd.requestFocus();
            edtNewPwd.setError(getString(R.string.enter_new_password));
            return;
        }

        if (ConfirmPwd.isEmpty()) {
            edtConfirmPwd.requestFocus();
            edtConfirmPwd.setError(getString(R.string.enter_re_enter_password));
            return;
        }

        if (!ConfirmPwd.equals(NewPwd)) {
            edtConfirmPwd.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.password_not_matched));
            return;
        }

        if (!OldPwd.equals(loginUserModel.getPassword())) {

            edtOldPwd.requestFocus();
            edtOldPwd.setError(getString(R.string.old_pass_invalid));
            return;
        }
        try {
            Map<String, String> params = new HashMap<>();
            params.put("op", "ClientChangePassword");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            // params.put("JobPostId",String.valueOf(loginUserModel.getClientId()));
            params.put("Password", ConfirmPwd);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_CHANGE_PASSWORD,
                    true, RequestCode.ClientChangePassword, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


