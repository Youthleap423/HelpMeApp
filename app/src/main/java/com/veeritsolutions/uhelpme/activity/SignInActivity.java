package com.veeritsolutions.uhelpme.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.enums.RegisterBy;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Admin on 5/11/2017.
 */

public class SignInActivity extends AppCompatActivity implements OnClickEvent, DataObserver {

    private final String FIELDS = "fields";
    private final String FB_REQUEST_PARAMETER = "id, first_name, last_name, email,gender, birthday, location";
    private Button btnLogin, btnLoginFacebook, btnCreateAccount;
    private EditText edtEmail, edtPassword;
    private TextView txvForgotPassword, txvOr;
    private RelativeLayout relParentView;
    private String email, password;
    private Intent intent;
    private CallbackManager callbackManager;
    private AccessToken FBAccessToken;
    private List<String> accessUserDetailPermission = Arrays.asList("public_profile", "email");
    private DataObserver dataObserver;
    private String lang = "en";
    //private FirebaseAuth auth;

    /**
     * This method destroy the current access token.
     */
    public static void logoutToFacebook() {
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFacebookSdk();
        setContentView(R.layout.activity_sign_in);
        //   auth = FirebaseAuth.getInstance();
        Utils.printFbKeyHash();
        if (PrefHelper.getInstance().containKey(PrefHelper.LANGUAGE)) {
            lang = PrefHelper.getInstance().getString(PrefHelper.LANGUAGE, "en");
        }
        Configuration config = getBaseContext().getResources().getConfiguration();
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale);
                //profileActivity.getBaseContext().createConfigurationContext(config);
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            } else {
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            }
            //  profileActivity.recreate();
        }
        init();
        dataObserver = this;
    }

    private void init() {

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            btnLogin.setBackgroundResource(R.drawable.drw_button_shape);
        } else {
            btnLogin.setBackgroundResource(R.drawable.drw_button_shape_two);
        }


        btnLoginFacebook = (Button) findViewById(R.id.btn_login_facebook);
        btnLoginFacebook.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
        btnCreateAccount = (Button) findViewById(R.id.btn_create_account);
        btnCreateAccount.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
        edtEmail = (AppCompatEditText) findViewById(R.id.edt_UserName);
        edtEmail.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        edtPassword = (AppCompatEditText) findViewById(R.id.edt_password);
        txvForgotPassword = (TextView) findViewById(R.id.txv_forget_pwd);
        txvForgotPassword.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
        edtPassword.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        txvOr = (TextView) findViewById(R.id.txv_or);
        txvOr.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        relParentView = (RelativeLayout) findViewById(R.id.parentView);

        Utils.setupOutSideTouchHideKeyboard(relParentView);
        showInterstitial();
        // admob
        if (!getResources().getString(R.string.ad_banner_id).equals("")) {
            // Look up the AdView as a resource and load a request.
            AdView adView = (AdView) findViewById(R.id.sigin_in_adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } else {
            AdView adView = (AdView) findViewById(R.id.sigin_in_adView);
            adView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.btn_login:
                Utils.buttonClickEffect(view);
                if (validateLoginForm()) {
                    Map<String, String> params = new HashMap<>();

                    //JSONObject params = new JSONObject();
                    params.put("op", ApiList.GET_USER);
                    params.put("AuthKey", ApiList.AUTH_KEY);
                    params.put("EmailId", email);
                    params.put("Password", password);


                    RestClient.getInstance().post(this, Request.Method.POST, params,
                            ApiList.GET_USER, true, RequestCode.GetUser, this);
                }
                break;

            case R.id.btn_create_account:
                Utils.buttonClickEffect(view);
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //finish();
                break;

            case R.id.btn_login_facebook:
                Utils.buttonClickEffect(view);
                if (Utils.isInternetAvailable()) {

                    showTermsAndConditionsDialog();

                    //LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                } else {

                    CustomDialog.getInstance().showAlert(this, getString(R.string.str_no_internet_connection_available), true);
                }
                break;
            case R.id.txv_forget_pwd:

                Intent intent1 = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent1);
                break;
        }

    }

    private void showTermsAndConditionsDialog() {
        final Dialog mDialog = new Dialog(this, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View dataView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_alert, null, false);
        mDialog.setContentView(R.layout.custom_dialog_terms_conditions);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        //mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvTermsAndConditions = (TextView) mDialog.findViewById(R.id.tv_termsAndConditions);
        tvTermsAndConditions.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTermsAndConditions.setText(Html.fromHtml(getString(R.string.general_conditions)
                    + getString(R.string.link), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvTermsAndConditions.setText(Html.fromHtml(getString(R.string.general_conditions)
                    + getString(R.string.link)));
        }
        tvTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        // mDialogTitle.setText(mTitle);

        TextView tvOk = (TextView) mDialog.findViewById(R.id.btn_alertOk);
        tvOk.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                loginToFaceBook();
            }
        });
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void initFacebookSdk() {
        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(MyApplication.getInstance());
        AppEventsLogger.activateApp(MyApplication.getInstance());
        callbackManager = CallbackManager.Factory.create();
        Utils.printFbKeyHash();
    }

    private void getFBUserDetails() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        String firstName = object.optString("first_name");
                        String lastName = object.optString("last_name");
                        String email = object.optString("email");
                        String socialMediaUserId = object.optString("id");
                        String birthday = object.optString("birthday ");

                        /*bundle = new Bundle();
                        bundle.putString(Constants.NAME, firstName);
                        bundle.putString(Constants.EMAIL, email);
                        bundle.putString(Constants.USERNAME, firstName + lastName);
                        bundle.putString(Constants.REGISTER_BY, RegisterBy.FACEBOOK.getRegisterBy());*/
                        LoginUserModel loginUser = new LoginUserModel();
                        loginUser.setFirstName(firstName);
                        loginUser.setLastName(lastName);
                        loginUser.setEmailId(email);
                        loginUser.setRegisteredBy(RegisterBy.FACEBOOK.getRegisterBy());

                        insertClient(loginUser);


                        // RestClient.getInstance().post(SignInActivity.this, Request.Method.POST, );
                        //PrefHelper.getInstance().setString(PrefHelper.CLIENT_CREDENTIALS, Utils.objectToString(loginUser));
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString(FIELDS, FB_REQUEST_PARAMETER);
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void insertClient(LoginUserModel loginUser) {

        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.CLIENT_INSERT);
        params.put("AuthKey", ApiList.AUTH_KEY);

        params.put("FirstName", loginUser.getFirstName());
        params.put("LastName", loginUser.getLastName());
        params.put("EmailId", loginUser.getEmailId());
        params.put("Password", "");
        params.put("AcTokenId", "");
        params.put("RegisteredBy", loginUser.getRegisteredBy());
        params.put("ProfilePic", "");

        RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.CLIENT_INSERT,
                true, RequestCode.clientInsert, this);
    }

    /**
     * This method check user logged in with facebook id than it will return it's access token
     *
     * @return FBAccessToken (boolean)  : it return current logged in user's active access token
     */
    public boolean isLoggedInWithFacebook() {
        FBAccessToken = AccessToken.getCurrentAccessToken();
        return FBAccessToken != null;
    }

    public void loginToFaceBook() {

        LoginManager.getInstance().logInWithReadPermissions(this, accessUserDetailPermission);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                getFBUserDetails();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                ToastHelper.getInstance().showMessage(getString(R.string.str_login_failed));
            }
        });
    }

    private boolean validateLoginForm() {

        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (password.isEmpty()) {

            edtPassword.requestFocus();
            edtPassword.setError(getString(R.string.enter_password));
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetUser:
                PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                intent.putExtra(Constants.IS_FROM_SIGN_UP, 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                // singInToFireBase(false);
                break;

            case clientInsert:
                PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                intent = new Intent(SignInActivity.this, HomeActivity.class);
                if (LoginUserModel.getLoginUserModel().getRegisteredBy().equals(RegisterBy.FACEBOOK.getRegisterBy())) {
                    intent.putExtra(Constants.IS_FROM_SIGN_UP, 1);
                } else {
                    intent.putExtra(Constants.IS_FROM_SIGN_UP, 0);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                //singInToFireBase(true);
                break;

        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    /*private void singInToFireBase(boolean fromFacebookLogin) {
        final LoginUserModel loginUserModel = LoginUserModel.getLoginUserModel();

        if (fromFacebookLogin) {
            CustomDialog.getInstance().showProgress(this, "", false);
            auth.createUserWithEmailAndPassword(loginUserModel.getEmailId(), loginUserModel.getEmailId())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            CustomDialog.getInstance().dismiss();
                            if (task.isSuccessful()) {
                                PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else {
                                CustomDialog.getInstance().showProgress(SignInActivity.this, "", false);
                                auth.signInWithEmailAndPassword(loginUserModel.getEmailId(), loginUserModel.getEmailId())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                CustomDialog.getInstance().dismiss();
                                                PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                finish();
                                            }
                                        });
                                //ToastHelper.getInstance().showMessage(getString(R.string.str_sigin_failed));
                            }
                        }
                    });
                    *//*.addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CustomDialog.getInstance().dismiss();
                            ToastHelper.getInstance().showMessage(e.getLocalizedMessage());
                            //ToastHelper.getInstance().showMessage(e.getLocalizedMessage());
                        }
                    });*//*
        } else {
            CustomDialog.getInstance().showProgress(this, "", false);
            auth.signInWithEmailAndPassword(loginUserModel.getEmailId(), loginUserModel.getPassword())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            CustomDialog.getInstance().dismiss();
                            if (task.isSuccessful()) {
                                PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else {
                                ToastHelper.getInstance().showMessage(getString(R.string.str_sigin_failed));
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CustomDialog.getInstance().dismiss();
                            ToastHelper.getInstance().showMessage(getString(R.string.str_sigin_failed));
                        }
                    });
        }
    }*/


    /**
     * Show an interstitial ad
     */
    private void showInterstitial(){
        //if (fromPager) return;
        if (getResources().getString(R.string.ad_interstitial_id).length() == 0) return;

        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_interstitial_id));
        AdRequest adRequestInter = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });
        mInterstitialAd.loadAd(adRequestInter);


    }
}
