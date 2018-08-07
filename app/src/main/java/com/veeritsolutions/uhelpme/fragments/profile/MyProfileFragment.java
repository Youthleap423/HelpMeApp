package com.veeritsolutions.uhelpme.fragments.profile;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.adapters.AdpLocation;
import com.veeritsolutions.uhelpme.adapters.AdpPaymentTypeList;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.enums.ImageUpload;
import com.veeritsolutions.uhelpme.fragments.home.HomeFragment;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.CityModel;
import com.veeritsolutions.uhelpme.models.CountryModel;
import com.veeritsolutions.uhelpme.models.GenderModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PaymentTypeModel;
import com.veeritsolutions.uhelpme.models.StateModel;
import com.veeritsolutions.uhelpme.utility.BlurTransformation;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Admin on 6/28/2017.
 */

public class MyProfileFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {


    private View rootView;
    private EditText edtFirstName, edtLastName, edtEmail, /*edtPassword,*/
            edtTelephone, edtPostalCode, edtAddress1, edtAddress2, edtPersonalNo, edtBusinessTexId;
    private Button btnSubmit;
    private LoginUserModel loginUserModel;
    // private Spinner spgender;
    private ImageView imgProfilePhoto, imgBannerPhoto, imgLegaleDocument, imgBackHeader;
    private TextView tvProfileInfo, tvPersonalInfo, edtCity,
            edtState, edtCountry, tvGender, tvBirthdate, tvTermsAndConditions, tvPaymentType;
    private ProgressBar prgBanner, prgProfile, prgLegalDocument;
    private CheckBox chkTermsAndConditions;

    private List<String> genderList;
    private ArrayList<GenderModel> genderModelList;
    private ArrayList<PaymentTypeModel> paymentTypeModelsList;
    private ArrayList<CountryModel> countryList;
    private ArrayList<StateModel> stateList;
    private ArrayList<CityModel> cityList;
    private Dialog mDialog;
    private String countryId = "", stateId = "", cityId = "";
    private List<String> permissionList;
    private String image64Base = "";
    private String image64Document = "";
    private Boolean PageProfilepicUPadte;
    private int genderId = 0;
    private int paymentTypeId = 0, fromSignUp = 0;


    private EditText edtBankAccountNumber;
    private TextInputLayout inputLlayoutBankaccountNumber;

    private LinearLayout linSelectDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        fromSignUp = getArguments().getInt(Constants.IS_FROM_SIGN_UP);

        //profileActivity = (ProfileActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();
        genderList = new ArrayList<>();
        genderList.add(getString(R.string.select_gender));
        genderList.add(getString(R.string.male));
        genderList.add(getString(R.string.female));

        genderModelList = new ArrayList<>();
        genderModelList.add(new GenderModel(1, "Male"));
        genderModelList.add(new GenderModel(2, "Female"));
        permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.CAMERA);

        paymentTypeModelsList = new ArrayList<>();
        paymentTypeModelsList.add(new PaymentTypeModel(1, "E-Transfer"));
        paymentTypeModelsList.add(new PaymentTypeModel(2, "Paypal Transfer"));
        paymentTypeModelsList.add(new PaymentTypeModel(3, "Stripe Transfer"));
        paymentTypeModelsList.add(new PaymentTypeModel(4, "Send Cheque"));


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        imgProfilePhoto = (ImageView) rootView.findViewById(R.id.img_profilePhoto);
        imgBannerPhoto = (ImageView) rootView.findViewById(R.id.img_help_bannerPic);
        imgLegaleDocument = (ImageView) rootView.findViewById(R.id.img_file_upload);
        imgBackHeader = (ImageView) rootView.findViewById(R.id.img_back_header);

        tvPersonalInfo = (TextView) rootView.findViewById(R.id.tv_profileInfo);
        tvPersonalInfo.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvProfileInfo = (TextView) rootView.findViewById(R.id.tv_personalInfo);
        tvProfileInfo.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtFirstName = (EditText) rootView.findViewById(R.id.edt_firstName);
        edtFirstName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtLastName = (EditText) rootView.findViewById(R.id.edt_lastName);
        edtLastName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtEmail = (EditText) rootView.findViewById(R.id.edt_email);
        edtEmail.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        // edtPassword = (EditText) rootView.findViewById(R.id.edt_password);
        // edtPassword.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtTelephone = (EditText) rootView.findViewById(R.id.edt_telephone);
        edtTelephone.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtCity = (TextView) rootView.findViewById(R.id.edt_city);
        edtCity.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtState = (TextView) rootView.findViewById(R.id.edt_state);
        edtState.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtCountry = (TextView) rootView.findViewById(R.id.edt_country);
        edtCountry.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvGender = (TextView) rootView.findViewById(R.id.tv_gender);
        tvGender.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvPaymentType = (TextView) rootView.findViewById(R.id.tv_payment_type);
        tvPaymentType.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtPostalCode = (EditText) rootView.findViewById(R.id.edt_postalCode);
        edtPostalCode.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtAddress1 = (EditText) rootView.findViewById(R.id.edt_addressLine1);
        edtAddress1.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtAddress2 = (EditText) rootView.findViewById(R.id.edt_addressLine2);
        edtAddress2.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtPersonalNo = (EditText) rootView.findViewById(R.id.edt_personal_number);
        edtPersonalNo.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        inputLlayoutBankaccountNumber = (TextInputLayout) rootView.findViewById(R.id.input_layout_bankaccount_number);


        tvBirthdate = (TextView) rootView.findViewById(R.id.tv_birthDate);
        tvBirthdate.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvTermsAndConditions = (TextView) rootView.findViewById(R.id.tv_termsAndConditions);
        tvTermsAndConditions.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtBusinessTexId = (EditText) rootView.findViewById(R.id.edt_businessTextId);
        edtBusinessTexId.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        chkTermsAndConditions = (CheckBox) rootView.findViewById(R.id.chk_termsConditions);
        chkTermsAndConditions.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTermsAndConditions.setText(Html.fromHtml(getString(R.string.account_conditions)
                    + getString(R.string.link), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvTermsAndConditions.setText(Html.fromHtml(getString(R.string.account_conditions)
                    + getString(R.string.link)));
        }
        tvTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        btnSubmit = (Button) rootView.findViewById(R.id.btn_update);
        btnSubmit.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);


        linSelectDate = (LinearLayout) rootView.findViewById(R.id.lin_payment_id);
        edtBankAccountNumber = (EditText) rootView.findViewById(R.id.edt_bankaccount_number);
        edtBankAccountNumber.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);


        //spgender = (Spinner) rootView.findViewById(R.id.sp_gender);
        //spgender.setAdapter(new SpinnerAdapter(profileActivity, R.layout.spinner_row_list, genderList));

        prgBanner = (ProgressBar) rootView.findViewById(R.id.prg_banner);
        prgBanner.setVisibility(View.GONE);
        prgProfile = (ProgressBar) rootView.findViewById(R.id.prg_profile);
        prgProfile.setVisibility(View.GONE);
        prgLegalDocument = (ProgressBar) rootView.findViewById(R.id.prg_LegaleDocument);
        prgLegalDocument.setVisibility(View.GONE);

        // admob
        if (!getResources().getString(R.string.ad_banner_id).equals("")) {
            // Look up the AdView as a resource and load a request.
            AdView adView = (AdView) rootView.findViewById(R.id.profile_adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } else {
            AdView adView = (AdView) rootView.findViewById(R.id.profile_adView);
            adView.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.setupOutSideTouchHideKeyboard(rootView);
        if (fromSignUp == 1) {
            imgBackHeader.setVisibility(View.GONE);
            ((HomeActivity) getActivity()).linFooterView.setVisibility(View.GONE);
        }
        GetClientInfo();
    }

    @Override
    public void onBackPressed() {

        if (fromSignUp == 0) {
            ((ProfileActivity) getActivity()).popBackFragment();
        } else {
//            ((HomeActivity) getActivity()).removeAllFragment();
//            ((HomeActivity) getActivity()).pushFragment(new HomeFragment(), true, false, null);
        }
    }

    @Override
    public void onClick(View view) {
        String myData = "";
        switch (view.getId()) {

            case R.id.img_helpMe:
                Utils.buttonClickEffect(view);

                break;

            case R.id.btn_update:
                Utils.buttonClickEffect(view);
                if (validateForm()) {

                    clientUpdate();
                }

                break;

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                if (fromSignUp == 0) {
                    ((ProfileActivity) getActivity()).popBackFragment();
                } else {
                    ((HomeActivity) getActivity()).removeAllFragment();
                    ((HomeActivity) getActivity()).pushFragment(new HomeFragment(), true, false, null);
                }
                break;

            case R.id.edt_country:
                Utils.buttonClickEffect(view);
                getCountryInfo(true);
                break;

            case R.id.edt_state:
                Utils.buttonClickEffect(view);
                if (!countryId.equals("")) {
                    getStateInfo(countryId);
                } else {
                    if (loginUserModel.getCountryId() != 0) {
                        getStateInfo(String.valueOf(loginUserModel.getCountryId()));
                    } else {
                        ToastHelper.getInstance().showMessage(getString(R.string.select_country));
                    }
                }
                break;

            case R.id.edt_city:
                Utils.buttonClickEffect(view);
                if (!stateId.equals("")) {
                    getCityInfo(stateId);
                } else {

                    if (loginUserModel.getStateId() != 0) {
                        getCityInfo(String.valueOf(loginUserModel.getStateId()));
                    } else {
                        ToastHelper.getInstance().showMessage(getString(R.string.select_state));
                    }
                }
                break;

            case R.id.img_profilePhoto:
                Utils.buttonClickEffect(view);
                //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), false);
                PageProfilepicUPadte = true;
                if (Build.VERSION.SDK_INT > 22) {
                    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    //if (shouldShowRequestPermissionRationale(permissions))
                    requestPermissions(permissions, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA);
                } else {
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setRequestedSize(400, 400)
                            .start(getContext(), this);
                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                }
                break;
            case R.id.img_file_upload:
                Utils.buttonClickEffect(view);
                PageProfilepicUPadte = false;
                if (Build.VERSION.SDK_INT > 22) {
                    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    //if (shouldShowRequestPermissionRationale(permissions))
                    requestPermissions(permissions, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA);
                } else {
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setRequestedSize(400, 400)
                            .start(getContext(), this);
                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                }
                break;

            case R.id.tv_gender:
                Utils.buttonClickEffect(view);
                showLocationListDialog(getActivity(), genderModelList, tvGender, "Select Gender");
                break;
          /*  case R.id.tv_Pyment_received_by:
                Utils.buttonClickEffect(view);
                showLocationListDialog(profileActivity, paymentTypeModelsList, tvPaymentType, "Select Payment TYpe");
                break;*/

            case R.id.tv_birthDate:
                Utils.buttonClickEffect(view);
                final Calendar mCurrentDate = Calendar.getInstance();
                int mYear = mCurrentDate.get(Calendar.YEAR);
                int mMonth = mCurrentDate.get(Calendar.MONTH);
                int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {


                                mCurrentDate.set(selectedyear, selectedmonth, selectedday);
                                tvBirthdate.setText(Utils.dateFormat(mCurrentDate.getTimeInMillis(), Constants.DATE_MM_DD_YYYY));
                /* it is used to pass selected Date in millisecond*/
                                tvBirthdate.setTag(mCurrentDate.getTimeInMillis());
                            }
                        }, mYear, mMonth, mDay);

                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                try {
                    if (!mDatePicker.isShowing()) {
                        mDatePicker.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_payment_type:
                Utils.buttonClickEffect(view);
                //showLocationListDialog(profileActivity,paymentTypeModelsList , tvGender, "Select Gender");
                showPaymentTypeListDialog(getActivity(), paymentTypeModelsList, tvPaymentType, getString(R.string.str_select_payment_type));
                break;
        }

    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientInfo:

                if (mObject instanceof LoginUserModel) {

                    loginUserModel = (LoginUserModel) mObject;
                    edtFirstName.setText(loginUserModel.getFirstName());
                    edtLastName.setText(loginUserModel.getLastName());
                    edtEmail.setText(loginUserModel.getEmailId());
                    // edtPassword.setText(loginUserModel.getPassword());
                    edtTelephone.setText(loginUserModel.getPhoneNo());
                    edtCity.setText(loginUserModel.getCity());
                    edtState.setText(loginUserModel.getState());
                    edtCountry.setText(loginUserModel.getCountry());
                    tvGender.setText(loginUserModel.getGenderDisp());

                    genderId = loginUserModel.getGender();
                    //spPyamentType.setSelection(loginUserModel.getPaymentTypeId());
                    tvPaymentType.setText(loginUserModel.getPaymentMethodDisp());
                    paymentTypeId = loginUserModel.getPaymentMethod();
                    edtPostalCode.setText(loginUserModel.getpOBox());
                    switch (paymentTypeId) {
                        case 1:
                            edtBankAccountNumber.setText(loginUserModel.getBankAccountNumber());
                            inputLlayoutBankaccountNumber.setVisibility(View.VISIBLE);

                            break;
                        case 2:
                            edtBankAccountNumber.setText(loginUserModel.getBankAccountNumber());
                            inputLlayoutBankaccountNumber.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            edtBankAccountNumber.setText(loginUserModel.getBankAccountNumber());
                            inputLlayoutBankaccountNumber.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            inputLlayoutBankaccountNumber.setVisibility(View.GONE);
                            break;
                    }

                    //  inputLlayoutBankaccountNumber.setVisibility(View.VISIBLE);

                    edtAddress1.setText(loginUserModel.getAddress1());
                    edtAddress2.setText(loginUserModel.getAddress2());
                    String subString = loginUserModel.getBirthDate();
                    if (!subString.isEmpty()) {
                        String birthday = subString.substring(0, subString.length() - 3);
                        tvBirthdate.setText(Utils.oneFormatToAnotherFormat(Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY, birthday));
                    }
                    edtPersonalNo.setText(loginUserModel.getPersonalIdNumber());
                    edtBankAccountNumber.setText(loginUserModel.getBankAccountNumber());
                    edtBusinessTexId.setText(loginUserModel.getBusinessTaxId());


                    if (loginUserModel.getIsBankInformation() == 0) {
                        chkTermsAndConditions.setChecked(false);
                    } else {
                        chkTermsAndConditions.setChecked(true);
                    }

                    Utils.setImage(loginUserModel.getProfilePic(), R.drawable.img_user_placeholder,
                            imgProfilePhoto, prgProfile);
                    prgBanner.setVisibility(View.VISIBLE);

                    /*Glide.with(profileActivity)
                            .load(loginUserModel.getProfilePic())
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.img_user_placeholder)
                                    .error(R.drawable.img_user_placeholder)
                                    .transform(new BlurTransformation(profileActivity))
                                    .signature(new ObjectKey(String.valueOf(PrefHelper.getInstance().getLong(PrefHelper.IMAGE_CACHE_FLAG, 0)))))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    prgBanner.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    prgBanner.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(imgBannerPhoto);*/

                    Picasso.with(getActivity())
                            .load(loginUserModel.getProfilePic())
                            .placeholder(R.drawable.img_user_placeholder)
                            .error(R.drawable.img_user_placeholder)
                            .transform(new BlurTransformation(getActivity()))
                            .into(imgBannerPhoto, new Callback() {
                                @Override
                                public void onSuccess() {
                                    prgBanner.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    prgBanner.setVisibility(View.GONE);
                                }
                            });

                    Utils.setImage(loginUserModel.getLegalDocument(), R.drawable.img_legal_document_upload,
                            imgLegaleDocument, prgLegalDocument, false);
                }
                break;

            case ClientUpdate:

                ToastHelper.getInstance().showMessage(getString(R.string.profile_updated));
                if (fromSignUp == 0) {
                    ((ProfileActivity) getActivity()).popBackFragment();
                } else {
                    ((HomeActivity) getActivity()).removeAllFragment();
                    ((HomeActivity) getActivity()).pushFragment(new HomeFragment(), true, false, null);
                }
                break;

            case GetCountry:

                try {
                    countryList = (ArrayList<CountryModel>) mObject;
                    showLocationListDialog(getActivity(), countryList, edtCountry, getString(R.string.select_country_title));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GetState:
                try {
                    stateList = (ArrayList<StateModel>) mObject;
                    showLocationListDialog(getActivity(), stateList, edtState, getString(R.string.select_state_title));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GetCity:
                try {
                    cityList = (ArrayList<CityModel>) mObject;
                    showLocationListDialog(getActivity(), cityList, edtCity, getString(R.string.select_city_title));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ClientProfilePicUpdate:

                PrefHelper.getInstance().setLong(PrefHelper.IMAGE_CACHE_FLAG, System.currentTimeMillis());
                LoginUserModel loginUserModel = (LoginUserModel) mObject;
                Utils.setImage(loginUserModel.getProfilePic(), R.drawable.img_user_placeholder, imgProfilePhoto, prgProfile);
                prgBanner.setVisibility(View.VISIBLE);

               /* Glide.with(profileActivity)
                        .load(this.loginUserModel.getProfilePic())
                        .apply(new RequestOptions()
                                .transform(new BlurTransformation(profileActivity))
                                .signature(new ObjectKey(String.valueOf(PrefHelper.getInstance().getLong(PrefHelper.IMAGE_CACHE_FLAG, 0)))))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                prgBanner.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                prgBanner.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(imgBannerPhoto);*/

                Picasso.with(getActivity())
                        .load(loginUserModel.getProfilePic())
                        .placeholder(R.drawable.img_user_placeholder)
                        .error(R.drawable.img_user_placeholder)
                        .transform(new BlurTransformation(getActivity()))
                        .into(imgBannerPhoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                prgBanner.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                prgBanner.setVisibility(View.GONE);
                            }
                        });

                LoginUserModel.setLoginCredentials(RestClient.getGsonInstance().toJson(loginUserModel));

                //new DownloadFilesTask().execute(loginUserModel.getProfilePic());
                //PrefHelper.getInstance().se
                // tString(PrefHelper.CLIENT_CREDENTIALS, Utils.objectToString((Serializable) mObject));
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    private boolean validateForm() {

        if (edtFirstName.getText().toString().trim().isEmpty()) {
            edtFirstName.setError(getString(R.string.enter_first_name));
            return false;
        } else if (edtLastName.getText().toString().trim().isEmpty()) {
            edtLastName.setError(getString(R.string.enter_last_name));
            return false;
        } else if (edtEmail.getText().toString().trim().isEmpty()) {
            edtEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!edtEmail.getText().toString().trim().matches(Patterns.EMAIL_ADDRESS.pattern())) {
            edtEmail.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (chkTermsAndConditions.isChecked() || fromSignUp == 1) {
            if (edtFirstName.getText().toString().trim().isEmpty()
                    || edtLastName.getText().toString().trim().isEmpty()
                    || edtEmail.getText().toString().trim().isEmpty()
                    || edtAddress1.getText().toString().trim().isEmpty()
                    || edtAddress2.getText().toString().trim().isEmpty()
                    || edtTelephone.getText().toString().trim().isEmpty()
                    || tvGender.getText().toString().trim().isEmpty()
                    || tvPaymentType.getText().toString().trim().isEmpty()
                    || edtCountry.getText().toString().trim().isEmpty()
                    || edtState.getText().toString().trim().isEmpty()
                    || edtCity.getText().toString().trim().isEmpty()
                    || edtPostalCode.getText().toString().trim().isEmpty()
                    || tvBirthdate.getText().toString().trim().isEmpty()
                    ) {
                ToastHelper.getInstance().showMessage(getString(R.string.str_businessTermsAndConditions));
                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    private void getCountryInfo(boolean requireDialog) {

        Map<String, String> params1 = new HashMap<>();
        params1.put("op", ApiList.GET_COUNTRY_INFO);
        params1.put("AuthKey", ApiList.AUTH_KEY);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params1,
                ApiList.GET_COUNTRY_INFO, requireDialog, RequestCode.GetCountry, this);

    }

    private void getStateInfo(String countryId) {

        Map<String, String> params1 = new HashMap<>();
        params1.put("AuthKey", ApiList.AUTH_KEY);
        params1.put("op", ApiList.GET_STATE_INFO);
        params1.put("CountryId", countryId);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params1, ApiList.GET_STATE_INFO,
                true, RequestCode.GetState,
                this);
    }

    private void getCityInfo(String stateId) {

        Map<String, String> params1 = new HashMap<>();
        params1.put("op", ApiList.GET_CITY_INFO);
        params1.put("AuthKey", ApiList.AUTH_KEY);
        params1.put("StateId", stateId);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params1, ApiList.GET_CITY_INFO,
                true, RequestCode.GetCity,
                this);
    }

    /**
     * This method show the dialog with list of locations and having functionality of search
     *
     * @param context(Context)        : context
     * @param listLocation(ArrayList) : list of locations with countries, states and cities
     * @param textView(TextView)      : to show selected location e.g country-United Kingdom,
     *                                state- new york, city- london
     */
    public void showLocationListDialog(final Context context, final ArrayList<?> listLocation,
                                       final TextView textView, String dialogTitle) {

        try {

            mDialog = new Dialog(context, R.style.dialogStyle);
            // View dataView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_location_list, null);
            mDialog.setContentView(R.layout.custom_dialog_location_list);
            /* Set Dialog width match parent */
            mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            /*Set out side touch hide keyboard*/
            Utils.setupOutSideTouchHideKeyboard(mDialog.findViewById(R.id.parentDialog));

            TextView txtTitleDialog = (TextView) mDialog.findViewById(R.id.tv_titleDialog);
            txtTitleDialog.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
            // Set Dialog title
            txtTitleDialog.setText(dialogTitle);
            final ListView listViewLocation = (ListView) mDialog.findViewById(R.id.lv_location);
            final TextView txtNoRecord = (TextView) mDialog.findViewById(R.id.tv_noRecord);
            txtNoRecord.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
            // txtNoRecord.setText("No Records Found");
            LinearLayout linSearch = (LinearLayout) mDialog.findViewById(R.id.lin_fab_search);
            Button btnCancel = (Button) mDialog.findViewById(R.id.btn_cancel);
            btnCancel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
            btnCancel.setText(getString(R.string.cancel));
            final EditText edtSearchLocation = (EditText) mDialog.findViewById(R.id.edt_searchLocation);
            edtSearchLocation.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            if (listLocation != null && listLocation.size() > 0) {

                listViewLocation.setVisibility(View.VISIBLE);
                AdpLocation adpLocationList = new AdpLocation(context, listLocation, "");
                listViewLocation.setAdapter(adpLocationList);
            } else {

                listViewLocation.setVisibility(View.GONE);
                txtNoRecord.setVisibility(View.VISIBLE);
            }

            listViewLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mDialog.dismiss();
                    TextView txtLocationName = (TextView) view.findViewById(R.id.txtLocationName);
                    Object object = txtLocationName.getTag();

                    if (object != null) {

                        if (object instanceof CountryModel) {
                            CountryModel countryModel = (CountryModel) object;
                            countryId = String.valueOf(countryModel.getCountryId());
                            textView.setText(countryModel.getCountryName());
                            textView.setTag(countryModel);
                            edtState.setText("");
                            edtState.performClick();

                        } else if (object instanceof StateModel) {

                            StateModel stateModel = (StateModel) object;
                            stateId = String.valueOf(stateModel.getStateId());
                            textView.setText(stateModel.getStateName());
                            textView.setTag(stateModel);
                            edtCity.setText("");
                            edtCity.performClick();

                        } else if (object instanceof CityModel) {

                            CityModel cityModel = (CityModel) object;
                            textView.setText(cityModel.getCityName());
                            textView.setTag(cityModel);
                            cityId = String.valueOf(cityModel.getCityId());
                        } else if (object instanceof GenderModel) {
                            GenderModel genderModel = (GenderModel) object;
                            textView.setText(genderModel.getGender());
                            genderId = genderModel.getId();
                        }

                    }

                }
            });

            edtSearchLocation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    /* Show clear icon on editext length greater than zero */
                    String searchStr = edtSearchLocation.getText().toString().trim();

                    ArrayList<Object> filterList = new ArrayList<>();

                    assert listLocation != null;
                    for (int i = 0; i < listLocation.size(); i++) {
                        try {

                            Object object = listLocation.get(i);

                            if (object instanceof CountryModel) {

                                CountryModel countryModel = (CountryModel) object;

                                if (countryModel.getCountryName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }

                                textView.setText(countryModel.getCountryName());
                                textView.setTag(countryModel);

                            } else if (object instanceof StateModel) {

                                StateModel stateModel = (StateModel) object;

                                if (stateModel.getStateName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }

                            } else if (object instanceof CityModel) {

                                CityModel cityModel = (CityModel) object;

                                if (cityModel.getCityName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }
                            } else if (object instanceof GenderModel) {

                                GenderModel genderModel = (GenderModel) object;

                                if (genderModel.getGender().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (filterList.size() > 0) {
                        txtNoRecord.setVisibility(View.GONE);
                        listViewLocation.setVisibility(View.VISIBLE);

                        AdpLocation adpLocationList = new AdpLocation(context, filterList, searchStr/*, locationType*/);
                        listViewLocation.setAdapter(adpLocationList);

                    } else {
                        txtNoRecord.setVisibility(View.VISIBLE);
                        listViewLocation.setVisibility(View.GONE);
                    }

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPaymentTypeListDialog(final Context context, final ArrayList<PaymentTypeModel> listLocation,
                                          final TextView textView, String dialogTitle) {

        try {

            mDialog = new Dialog(context, R.style.dialogStyle);
            // View dataView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_location_list, null);
            mDialog.setContentView(R.layout.custom_dialog_location_list);
            /* Set Dialog width match parent */
            mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            /*Set out side touch hide keyboard*/
            Utils.setupOutSideTouchHideKeyboard(mDialog.findViewById(R.id.parentDialog));

            TextView txtTitleDialog = (TextView) mDialog.findViewById(R.id.tv_titleDialog);
            txtTitleDialog.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
            // Set Dialog title
            txtTitleDialog.setText(dialogTitle);
            final ListView listViewLocation = (ListView) mDialog.findViewById(R.id.lv_location);
            final TextView txtNoRecord = (TextView) mDialog.findViewById(R.id.tv_noRecord);
            txtNoRecord.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
            // txtNoRecord.setText("No Records Found");
            LinearLayout linSearch = (LinearLayout) mDialog.findViewById(R.id.lin_fab_search);
            Button btnCancel = (Button) mDialog.findViewById(R.id.btn_cancel);
            btnCancel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
            btnCancel.setText(getString(R.string.cancel));
            final EditText edtSearchLocation = (EditText) mDialog.findViewById(R.id.edt_searchLocation);
            edtSearchLocation.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            if (listLocation != null && listLocation.size() > 0) {

                listViewLocation.setVisibility(View.VISIBLE);
                AdpPaymentTypeList adpPaymentTypeList = new AdpPaymentTypeList(context, listLocation, "");
                listViewLocation.setAdapter(adpPaymentTypeList);
            } else {

                listViewLocation.setVisibility(View.GONE);
                txtNoRecord.setVisibility(View.VISIBLE);
            }

            listViewLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    mDialog.dismiss();
                    TextView txtLocationName = (TextView) view.findViewById(R.id.txtLocationName);
                    Object object = txtLocationName.getTag();

                    if (object != null) {
                        PaymentTypeModel paymentTypeModel = (PaymentTypeModel) object;
                        paymentTypeId = paymentTypeModel.getPaymentTypeId();
                        textView.setText(paymentTypeModel.getPaymentTypeName());
                        textView.setTag(paymentTypeModel);
                        switch (paymentTypeId) {
                            case 1:
                                edtBankAccountNumber.setHint(getString(R.string.str_email_id));
                                inputLlayoutBankaccountNumber.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                edtBankAccountNumber.setHint(getString(R.string.str_paypal_id));
                                inputLlayoutBankaccountNumber.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                edtBankAccountNumber.setHint(getString(R.string.str_stripe_id));
                                inputLlayoutBankaccountNumber.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                inputLlayoutBankaccountNumber.setVisibility(View.GONE);
                                break;
                        }


                    }

                }
            });

            edtSearchLocation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    /* Show clear icon on editext length greater than zero */
                    String searchStr = edtSearchLocation.getText().toString().trim();

                    ArrayList<Object> filterList = new ArrayList<>();

                    assert listLocation != null;
                    for (int i = 0; i < listLocation.size(); i++) {
                        try {

                            Object object = listLocation.get(i);

                            if (object instanceof CountryModel) {

                                CountryModel countryModel = (CountryModel) object;

                                if (countryModel.getCountryName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }

                                textView.setText(countryModel.getCountryName());
                                textView.setTag(countryModel);

                            } else if (object instanceof StateModel) {

                                StateModel stateModel = (StateModel) object;

                                if (stateModel.getStateName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }

                            } else if (object instanceof CityModel) {

                                CityModel cityModel = (CityModel) object;

                                if (cityModel.getCityName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }
                            } else if (object instanceof GenderModel) {

                                GenderModel genderModel = (GenderModel) object;

                                if (genderModel.getGender().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (filterList.size() > 0) {
                        txtNoRecord.setVisibility(View.GONE);
                        listViewLocation.setVisibility(View.VISIBLE);

                        AdpLocation adpLocationList = new AdpLocation(context, filterList, searchStr/*, locationType*/);
                        listViewLocation.setAdapter(adpLocationList);

                    } else {
                        txtNoRecord.setVisibility(View.VISIBLE);
                        listViewLocation.setVisibility(View.GONE);
                    }

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }

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

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        Constants.REQUEST_FILE_PROFILE);
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
                    Uri selectedImageUri = Utils.getImageUri(getActivity(), thumbnail);
                    beginCrop(selectedImageUri);
                    break;

                case Constants.REQUEST_FILE_PROFILE:

                    selectedImageUri = data.getData();
                    beginCrop(selectedImageUri);
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    handleCrop(0, result.getUri().getPath());
                    break;

            }
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));

    }

    private void handleCrop(int resultCode, String data) {
        if (PageProfilepicUPadte) {
            image64Base = Utils.getStringImage(data, ImageUpload.ClientProfile);
            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.CLIENT_PROFILE_PIC_UPDATE);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("ProfilePic", image64Base);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.CLIENT_PROFILE_PIC_UPDATE, true, RequestCode.ClientProfilePicUpdate, this);
        } else {
            image64Document = Utils.getStringImage(data, ImageUpload.LegalDocument);
            imgLegaleDocument.setImageBitmap(BitmapFactory.decodeFile(data));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // List<String> shouldPermit = new ArrayList<>();

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA) {

            if (grantResults.length > 0 || grantResults.length != 0) {

                if (PermissionClass.verifyPermission(grantResults)) {
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setRequestedSize(400, 400)
                            .start(getContext(), this);
                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                } else {
                    permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    //if (shouldShowRequestPermissionRationale(permissions))
                    requestPermissions(permissions, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA);
                    // PermissionClass.checkPermission(getActivity(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList);
                }
            }
        }
    }

    private void GetClientInfo() {

        try {

            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.GET_CLIENT_INFO);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CLIENT_INFO,
                    true, RequestCode.GetClientInfo, this);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void clientUpdate() {

        try {

            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.CLIENT_UPDATE);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("FirstName", edtFirstName.getText().toString().trim());
            params.put("LastName", edtLastName.getText().toString().trim());
            params.put("Gender", String.valueOf(genderId));
            // params.put("PaymentMethod", "");
            params.put("PaymentMethod", String.valueOf(paymentTypeId));
            params.put("Address1", "");
            params.put("Address2", "");
            //params.put("City", edtCity.getText().toString());
            params.put("POBox", edtPostalCode.getText().toString().trim());
            if (cityId.equals("")) {
                params.put("City", String.valueOf(loginUserModel.getCityId()));
            } else {
                params.put("City", cityId);
            }

            if (stateId.equals("")) {
                params.put("State", String.valueOf(loginUserModel.getStateId()));
            } else {
                params.put("State", stateId);
            }

            if (countryId.equals("")) {
                params.put("Country", String.valueOf(loginUserModel.getCountryId()));
            } else {
                params.put("Country", countryId);
            }

            params.put("PhoneNo", edtTelephone.getText().toString().trim());
            params.put("EmailId", edtEmail.getText().toString().trim());
            params.put("Address1", edtAddress1.getText().toString().trim());
            params.put("Address2", edtAddress2.getText().toString().trim());
            params.put("PersonalIdNumber", "");
            params.put("BankAccountNumber", edtBankAccountNumber.getText().toString().trim());

            if (chkTermsAndConditions.isChecked()) {
                params.put("IsBankInformation", String.valueOf(1).trim());
            } else {
                params.put("IsBankInformation", String.valueOf(0).trim());
            }

            params.put("BusinessTaxId", "");
            params.put("RoutingNumber", "");
            params.put("LegalDocument", image64Document);
            params.put("BirthDate", tvBirthdate.getText().toString().trim());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_UPDATE,
                    true, RequestCode.ClientUpdate, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
