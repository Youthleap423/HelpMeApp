package com.veeritsolutions.uhelpme.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.enums.ImageUpload;
import com.veeritsolutions.uhelpme.enums.RegisterBy;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.MyContextWrapper;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity implements OnClickEvent, DataObserver {

    // xml components
    private LinearLayout linParentView;
    private EditText edtFirstName, edtEmail, edtLastName, edtPassword, edtConfirmPass;
    private Button btnSingUp;
    private Toolbar toolbar;
    private TextView tvHeader, tvTermsAndConditions;
    private ImageView imgClientPhoto;
    private CheckBox chkTermsConditions;

    //object or variable declaration
    private String firstName, mEmailAddress, lastName, mPassword, confirmPassword;
    //private JSONObject params;
    private String lang = "en";
    private List<String> permissionList;
    private Dialog mDialog;
    private String image64Base = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        Utils.setupOutSideTouchHideKeyboard(linParentView);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (PrefHelper.getInstance().containKey(PrefHelper.LANGUAGE)) {
            String str = PrefHelper.getInstance().getString(PrefHelper.LANGUAGE, "en");
            super.attachBaseContext(MyContextWrapper.wrap(newBase, str));
        } else {
            super.attachBaseContext(MyContextWrapper.wrap(newBase, "en"));
        }
    }

    private void init() {

        linParentView = (LinearLayout) findViewById(R.id.parentView);

        imgClientPhoto = (ImageView) findViewById(R.id.img_clientPicInsert);

        edtFirstName = (EditText) findViewById(R.id.edt_firstname);
        edtFirstName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtEmail.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtLastName = (EditText) findViewById(R.id.edt_lastname);
        edtLastName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtPassword.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtConfirmPass = (EditText) findViewById(R.id.edt_confirmPassword);
        edtConfirmPass.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        btnSingUp = (Button) findViewById(R.id.btn_sign_up);
        btnSingUp.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvTermsAndConditions = (TextView) findViewById(R.id.tv_termsAndConditions);
        tvTermsAndConditions.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvTermsAndConditions.setLinkTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTermsAndConditions.setText(Html.fromHtml(getString(R.string.general_conditions)
                    + getString(R.string.link), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvTermsAndConditions.setText(Html.fromHtml(getString(R.string.general_conditions)
                    + getString(R.string.link)));
        }
        tvTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        chkTermsConditions = (CheckBox) findViewById(R.id.chk_termsConditions);
    }


    private boolean validateForm() {


        firstName = edtFirstName.getText().toString().trim();
        mEmailAddress = edtEmail.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        mPassword = edtPassword.getText().toString().trim();
        confirmPassword = edtConfirmPass.getText().toString().trim();

        if (firstName.isEmpty()) {
            edtFirstName.requestFocus();
            edtFirstName.setError(getString(R.string.enter_first_name));
            return false;
        } else if (lastName.isEmpty()) {
            edtLastName.requestFocus();
            edtLastName.setError(getString(R.string.enter_last_name));
            return false;
        } else if (mEmailAddress.isEmpty()) {
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!mEmailAddress.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (mPassword.isEmpty()) {
            edtPassword.requestFocus();
            edtPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (confirmPassword.isEmpty()) {
            edtConfirmPass.requestFocus();
            edtConfirmPass.setError(getString(R.string.str_enter_confirm_pass));
            return false;
        } else if (!(mPassword.equals(confirmPassword))) {
            edtConfirmPass.requestFocus();
            edtConfirmPass.setError(getString(R.string.str_confirm_pass_does_not_matches));
            return false;
        } else if (!chkTermsConditions.isChecked()) {
            ToastHelper.getInstance().showMessage(getString(R.string.str_terms_and_condtions));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case clientInsert:

                PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                // signUpInFireBase();
                break;
        }
    }

    /*private void signUpInFireBase() {
        CustomDialog.getInstance().showProgress(this, "", false);
        LoginUserModel loginUserModel = LoginUserModel.getLoginUserModel();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(loginUserModel.getEmailId(), loginUserModel.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        CustomDialog.getInstance().dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        } else {
                            ToastHelper.getInstance().showMessage(task.getException().getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CustomDialog.getInstance().dismiss();
                        ToastHelper.getInstance().showMessage(e.getLocalizedMessage());
                    }
                });
    }*/

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_sign_up:
                Utils.buttonClickEffect(view);
                if (validateForm()) {
                    insertClient();
                }
                break;
            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                finish();
                break;
            case R.id.img_clientPicInsert:
                Utils.buttonClickEffect(view);
                //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), false);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    // ContextCompat.checkSelfPermission(getContext(), permissionList.get(0));
                    permissionList = new ArrayList<>();
                    permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissionList.add(Manifest.permission.CAMERA);
                    if (PermissionClass.checkPermission(this, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList)) {
                        // start cropping activity for pre-acquired image saved on the device
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setActivityTitle("Crop")
                                .setRequestedSize(400, 400)
                                .setAspectRatio(1, 1)
                                .start(SignUpActivity.this);
                        //showImageSelect(this, getString(R.string.str_select_profile_photo), true);

                    } /*else {
                        PermissionClass.checkPermission(getActivity(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList);
                    }*/
                } else {
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setRequestedSize(400, 400)
                            .setAspectRatio(1, 1)
                            .start(SignUpActivity.this);
                    //showImageSelect(this, getString(R.string.str_select_profile_photo), true);
                }
                break;
        }
    }

    private void insertClient() {
        try {
            //params = new JSONObject();
            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.CLIENT_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("FirstName", firstName);
            params.put("LastName", lastName);
            params.put("EmailId", mEmailAddress);
            params.put("Password", mPassword);
            params.put("AcTokenId", "");
            params.put("RegisteredBy", RegisterBy.APP.getRegisterBy());
            params.put("ProfilePic", image64Base);

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.CLIENT_INSERT,
                    true, RequestCode.clientInsert, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showImageSelect(Context mContext, String mTitle, boolean mIsCancelable) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View dataView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_select_image, null, false);
        mDialog.setContentView(R.layout.custom_dialog_select_image);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);

        TextView tvTitle, tvCamera, tvGallery;

        tvTitle = (TextView) mDialog.findViewById(R.id.tv_selectImageTitle);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        tvTitle.setText(mTitle);

        tvCamera = (TextView) mDialog.findViewById(R.id.tv_camera);
        tvCamera.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvGallery = (TextView) mDialog.findViewById(R.id.tv_gallery);
        tvGallery.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        LinearLayout linCamera, linGallery;

        linCamera = (LinearLayout) mDialog.findViewById(R.id.lin_camera);
        linGallery = (LinearLayout) mDialog.findViewById(R.id.lin_gallery);

        linCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constants.REQUEST_CAMERA_PROFILE);
            }
        });

        linGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("Crop")
                        .setRequestedSize(400, 400)
                        .start(SignUpActivity.this);

               /* Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image*//*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        Constants.REQUEST_FILE_PROFILE);*/
            }
        });
        ImageView imgCancel = (ImageView) mDialog.findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        try {
            if (mDialog != null) {
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (mDialog != null) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case Constants.REQUEST_CAMERA_PROFILE:

                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    Uri selectedImageUri = Utils.getImageUri(this, thumbnail);
                    beginCrop(selectedImageUri);
                    break;

                case Constants.REQUEST_FILE_PROFILE:

                    selectedImageUri = data.getData();
                    beginCrop(selectedImageUri);
                    break;

                case Constants.CROP_REQUEST_CODE:

                    // handleCrop(resultCode, data);
                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    handleCrop(0, result.getUri().getPath());
                    break;
            }
        }
    }

    private void beginCrop(Uri source) {

        // start cropping activity for pre-acquired image saved on the device
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Crop")
                .setRequestedSize(400, 400)
                .setAspectRatio(1, 1)
                .start(this);
    }

    private void handleCrop(int resultCode, String data) {

        image64Base = Utils.getStringImage(data, ImageUpload.ClientProfile);
        //imgClientPhoto.setVisibility(View.VISIBLE);
        //imgClientPhoto.setImageURI(null);
        //imgClientPhoto.setImageURI(Crop.getOutput(data));
        Utils.setImage(data, R.drawable.img_create_account, imgClientPhoto);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA) {

            if (grantResults.length > 0 || grantResults.length != 0) {

                if (PermissionClass.verifyPermission(grantResults)) {
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setRequestedSize(400, 400)
                            .setAspectRatio(1, 1)
                            .start(SignUpActivity.this);
                    //showImageSelect(this, getString(R.string.str_select_profile_photo), true);
                } else {
                    permissionList = new ArrayList<>();
                    permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissionList.add(Manifest.permission.CAMERA);
                    PermissionClass.checkPermission(this, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
