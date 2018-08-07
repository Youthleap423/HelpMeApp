package com.veeritsolutions.uhelpme.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ABC on 8/11/2017.
 */

public class ForgotPasswordActivity extends AppCompatActivity implements OnClickEvent, DataObserver, OnBackPressedEvent {

    // new data arrived
    private Button btnSave;
    private EditText edtEmail;
    // private LinearLayout ParentView;
    private String email;
    private ProfileActivity profileActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        Utils.printFbKeyHash();
        init();
    }

    private void init() {


        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);


        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            btnSave.setBackgroundResource(R.drawable.drw_button_shape);
        } else {
            btnSave.setBackgroundResource(R.drawable.drw_button_shape_two);
        }


        edtEmail = (EditText) findViewById(R.id.edt_email_address);
        edtEmail.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        // ParentView = (LinearLayout) findViewById(R.id.parentView);

       // Utils.setupOutSideTouchHideKeyboard(findViewById(R.layout.activity_forgot_password));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSave:

                if (validateLoginForm()) {
                    Map<String, String> params = new HashMap<>();

                    //JSONObject params = new JSONObject();
                    params.put("op", ApiList.FORGOT_PASSWORD);
                    params.put("AuthKey", ApiList.AUTH_KEY);
                    params.put("EmailId", email);

                    RestClient.getInstance().post(this, Request.Method.POST, params,
                            ApiList.FORGOT_PASSWORD, true, RequestCode.ForgotPassword, this);
                }
                break;

            case R.id.img_back_header:
              /*  Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);*/
                finish();
                break;

        }
    }

    private boolean validateLoginForm() {

        email = edtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.enter_valid_email));
            return false;

        } else {
            return true;
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {
            case ForgotPassword:
                finish();
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


