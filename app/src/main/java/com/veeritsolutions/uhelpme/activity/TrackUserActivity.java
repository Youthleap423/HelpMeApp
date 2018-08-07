package com.veeritsolutions.uhelpme.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.enums.ImageUpload;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.models.ARViewModel;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.LocationModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class TrackUserActivity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback, DataObserver {

    private MapView mapView;
    private GifImageView imgEarth;
    private TextView tvHeader;
    private Button btnStatus;
    private GoogleMap googleMap;
    private int clientId = 0;
    private Timer mTimer;
    private UpdateLocationTask mDurationTask;
    private boolean enableCamera = true, userBitmap = true;

    private AllHelpOfferModel allHelpOfferModel;
    private Dialog dialog;

    private Map<String, String> params;
    private int actualJobHours = 0;
    private ImageView imgIssuePhoto;
    private String image64BaseIssue = "";
    private Bitmap loginUserBitmap, helperBitmap;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_user);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();// needed to get the map to display immediately
        mapView.getMapAsync(this);
        if (getIntent() != null) {
            allHelpOfferModel = (AllHelpOfferModel) getIntent().getSerializableExtra(Constants.USER_DATA);
            clientId = allHelpOfferModel.getClientId();
        }
        new Location().execute(LoginUserModel.getLoginUserModel().getProfilePic());
        new Location().execute(allHelpOfferModel.getProfilePic());
        tvHeader = findViewById(R.id.tv_headerTitle);
        tvHeader.setText(allHelpOfferModel.getFirstName() + " " + allHelpOfferModel.getLastName());
        btnStatus = findViewById(R.id.btn_status);
        if (allHelpOfferModel.getIsHire() == 0) {
            btnStatus.setText("Accept");
        } else if (allHelpOfferModel.getIsHire() == Constants.JOB_AWARDED) {
            btnStatus.setText("Awarded");
        } else if (allHelpOfferModel.getIsHire() == Constants.JOB_FINISHED) {
            btnStatus.setText("Finished");
        } else if (allHelpOfferModel.getIsHire() == Constants.JOB_REJECTED) {
            btnStatus.setText("Rejected");
        }

        imgEarth = findViewById(R.id.img_earth);
        imgEarth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.buttonClickEffect(view);
                showMenuDialog();
            }
        });

        getLocationData();
        mTimer = new Timer();
        mDurationTask = new UpdateLocationTask();
        mTimer.schedule(mDurationTask, new Date(), 10000);

    }

    private void getLocationData() {

        Map<String, String> params = new HashMap<>();
        params.put("op", "GetClientLocation");
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(clientId));

        RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.GET_CLIENT_LOCATION,
                false, RequestCode.GetClientLocation, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                finish();
                break;

            case R.id.img_earth:
                Utils.buttonClickEffect(view);
                showMenuDialog();
                break;

            case R.id.tv_backToMenu:
                Utils.buttonClickEffect(view);
                dismiss();
                Intent intent = new Intent(TrackUserActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                break;

            case R.id.tv_makeNewHelp:
                Utils.buttonClickEffect(view);
                dismiss();
                intent = new Intent(TrackUserActivity.this, HomeActivity.class);
                intent.putExtra(Constants.IS_FROM_TRACK, true);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                break;

            case R.id.tv_cancelHelp:
                Utils.buttonClickEffect(view);
                dismiss();
                showCancelHelpDialog();
                break;

            case R.id.tv_cancelYes:
                Utils.buttonClickEffect(view);
                dismiss();

                showCancelHelpReasonDialog();
                break;

            case R.id.tv_cancelNo:
                Utils.buttonClickEffect(view);
                dismiss();
                break;

            case R.id.tv_backToHelpProgress:
                Utils.buttonClickEffect(view);
                dismiss();
                break;

            case R.id.img_ChatIcon:
                Utils.buttonClickEffect(view);
                // if (allHelpOfferModel.getIsHire() != -1) {
                ChatUsersListModel chatUsersListModel = new ChatUsersListModel();
                chatUsersListModel.setId(allHelpOfferModel.getClientId());
                chatUsersListModel.setName(allHelpOfferModel.getFirstName() + " " + allHelpOfferModel.getLastName());
                chatUsersListModel.setProfilePic(allHelpOfferModel.getProfilePic());
                intent = new Intent(TrackUserActivity.this, OneToOneChatActivity.class);
                intent.putExtra(Constants.CHAT_DATA, chatUsersListModel);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                // } else {
                //     ToastHelper.getInstance().showMessage();
                // }
                break;

            case R.id.btn_status:
                Utils.buttonClickEffect(view);
                showFinishJobDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientLocation:

                LocationModel locationModel = (LocationModel) mObject;

                if (googleMap != null) {
                    googleMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(locationModel.getLatitude(), locationModel.getLongitude()));
                    if (helperBitmap != null) {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(helperBitmap)));
                    }
                    googleMap.addMarker(markerOptions);

                    float lat = PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0);
                    float lang = PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0);
                    markerOptions.position(new LatLng(lat, lang));
                    if (loginUserBitmap != null) {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(loginUserBitmap)));
                    }
                    googleMap.addMarker(markerOptions);

                    googleMap.setTrafficEnabled(true);
                    googleMap.setIndoorEnabled(true);
                    googleMap.setBuildingsEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);


                    String[] permission = new String[3];
                    permission[0] = android.Manifest.permission.ACCESS_COARSE_LOCATION;
                    permission[1] = android.Manifest.permission.ACCESS_FINE_LOCATION;
                    permission[2] = android.Manifest.permission.ACCESS_NETWORK_STATE;

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

                        requestPermissions(permission, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION);

                    } else {
                        googleMap.setMyLocationEnabled(true);
                    }

                    if (enableCamera) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                new LatLng(locationModel.getLatitude(), locationModel.getLongitude()), 15);
                        googleMap.animateCamera(cameraUpdate);
                        enableCamera = false;
                    }
                }
                break;

            case JobPostOfferCancel:
                finish();
                break;
            case FinishOffer:
                String s = (String) mObject;
                showReviewRatingDialog(allHelpOfferModel);

                break;

            case DoStripePayment:
                insertAcceptOffer(allHelpOfferModel);
                break;

            case JobPostIssue:
                showReviewRatingDialog(allHelpOfferModel);
                break;

            case ReviewInsert:
                finish();
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    private void showFinishJobDialog() {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(TrackUserActivity.this, R.style.dialogStyle);
        builder.setTitle(getString(R.string.finish_job));
        builder.setMessage(R.string.are_you_want_to_finish_help_post);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if (allHelpOfferModel.getJobAmountFlag() == 0) {
                    int jobActualHours = allHelpOfferModel.getJobHour();
                    insertFinishJobOffer(allHelpOfferModel, jobActualHours);
                } else {
                    showEnterAmountDialog();
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showFinishConfirmationDialog();
            }
        });
        builder.setNeutralButton("Issue?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                showIssueConfirmationDialog();
            }
        });
        builder.show();
    }

    private void showIssueConfirmationDialog() {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, R.style.dialogStyle);
        builder.setTitle(getString(R.string.str_issue_confirm));
        //builder.setMessage(R.string.are_you_want_to_finish_help_post);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                showIssueDialog();
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

    private void showIssueDialog() {

        final Dialog dialog = new Dialog(this, R.style.dialogStyle);
        dialog.setContentView(R.layout.custom_dialog_help_finish_issue);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        TextView tvTitle = dialog.findViewById(R.id.tv_dialogHeader);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        final EditText edtCancelReason = dialog.findViewById(R.id.edt_cancelHelpReason);
        edtCancelReason.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        TextView tvSubmit = dialog.findViewById(R.id.tv_issueSubmit);
        tvSubmit.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvCancel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imgIssuePhoto = dialog.findViewById(R.id.img_issuePhoto);
        imgIssuePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.buttonClickEffect(view);
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
                            .start(TrackUserActivity.this);
                    // showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                }
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                submitHelpIssue(edtCancelReason);
            }
        });

    }

    private void submitHelpIssue(EditText edtCancelReason) {

        params = new HashMap<>();
        params.put("op", ApiList.JOB_POST_ISSUE);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));
        params.put("JobActualHour", String.valueOf(0));
        params.put("IssueRemarks", edtCancelReason.getText().toString());
        params.put("IssuePic", image64BaseIssue);

        RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.JOB_POST_ISSUE,
                true, RequestCode.JobPostIssue, this);
    }

    private void insertFinishJobOffer(AllHelpOfferModel allHelpOfferModel, int jobActualHours) {

        params = new HashMap<>();
        params.put("op", ApiList.FINISH_OFFER);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));
        params.put("JobActualHour", String.valueOf(jobActualHours));

        RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.FINISH_OFFER,
                true, RequestCode.FinishOffer, this);
    }

    private void showEnterAmountDialog() {

        final Dialog dialog = new Dialog(TrackUserActivity.this, R.style.dialogStyle);
        dialog.setContentView(R.layout.custom_dialog_enter_amount);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tvAmount = dialog.findViewById(R.id.tv_amount);
        tvAmount.setText("");
        final EditText edtWorkHours = dialog.findViewById(R.id.edt_workHours);

        edtWorkHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String workHours = edtWorkHours.getText().toString().trim();
                if (!workHours.isEmpty() && TextUtils.isDigitsOnly(workHours)) {
                    int hours = Integer.parseInt(workHours);
                    actualJobHours = hours;
                    if (hours > allHelpOfferModel.getJobHour()) {
                        int totalAmount = (hours - allHelpOfferModel.getJobHour()) * allHelpOfferModel.getOfferAmount();

                        tvAmount.setText(String.valueOf(totalAmount));
                    } else {
                        tvAmount.setText(getString(R.string.str_extra_money_payback));
                    }

                } else if (!TextUtils.isDigitsOnly(workHours)) {
                    ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_work_hours));
                    tvAmount.setText("");
                } else {
                    tvAmount.setText("");
                }
            }
        });

        Button btnPayment = dialog.findViewById(R.id.btn_submit);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvAmount.getText().toString().trim().isEmpty()) {
                    ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_work_hours));
                } else {
                    dialog.dismiss();
                    //allHelpOfferModel.setOfferAmount(Integer.parseInt(tvAmount.getText().toString().trim()));
                    int hours = Integer.parseInt(edtWorkHours.getText().toString().trim());
                    if (hours > allHelpOfferModel.getJobHour()) {
                        //isFirstTime = false;
                        showPaymentDialog(Integer.parseInt(tvAmount.getText().toString().trim()), false);
                    } else {
                        insertFinishJobOffer(allHelpOfferModel, actualJobHours);
                    }
                    tvAmount.setText("");
                }
            }
        });
        dialog.show();
    }

    private void showFinishConfirmationDialog() {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, R.style.dialogStyle);
        builder.setTitle(getString(R.string.not_finish));
        //builder.setMessage(R.string.are_you_want_to_finish_help_post);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                ToastHelper.getInstance().showMessage(getString(R.string.str_we_will_wait));
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showPaymentDialog(final int amount, final boolean isFirstTime) {

        final Dialog dialog = new Dialog(TrackUserActivity.this, R.style.dialogStyle);

        dialog.setContentView(R.layout.custom_dialog_payment_selection);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //final CardInputWidget mCardInputWidget = (CardInputWidget) dialog.findViewById(R.id.card_input_widget);
        //final float finalAmount = amount;
        TextView tvTitle = dialog.findViewById(R.id.tv_titleDialog);
        if (allHelpOfferModel.getJobAmountFlag() == 0) {
            //finalAmount = amount;
            tvTitle.setText(getString(R.string.str_payment) + " $" + amount);

        } else {
            //finalAmount = allHelpOfferModel.getJobHour() * allHelpOfferModel.getOfferAmount();
            tvTitle.setText(getString(R.string.str_hourly_bases) + amount);
        }

        final EditText edtCardNo, edtCardYear, edtCardMonth, edtCardCVV;

        edtCardNo = dialog.findViewById(R.id.edt_cardNo);
        edtCardYear = dialog.findViewById(R.id.edt_cardYear);
        edtCardMonth = dialog.findViewById(R.id.edt_cardMonth);
        edtCardCVV = dialog.findViewById(R.id.edt_cardCVV);

        final Card[] cardToSave = new Card[1];
        Button btnSaveCard = dialog.findViewById(R.id.btn_submit);

        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardNo = edtCardNo.getText().toString().trim();
                if (cardNo.isEmpty()) {
                    ToastHelper.getInstance().showMessage(getString(R.string.str_enter_card_number));
                    return;
                }

                if (edtCardMonth.getText().toString().trim().isEmpty() || !TextUtils.isDigitsOnly(edtCardMonth.getText().toString().trim())) {
                    ToastHelper.getInstance().showMessage(getString(R.string.valid_card_month));
                    return;
                }

                int cardMonth = Integer.parseInt(edtCardMonth.getText().toString().trim());

                if (edtCardYear.getText().toString().trim().isEmpty() || !TextUtils.isDigitsOnly(edtCardYear.getText().toString().trim())) {
                    ToastHelper.getInstance().showMessage(getString(R.string.valid_card_year));
                    return;
                }
                int cardYear = Integer.parseInt(edtCardYear.getText().toString().trim());


                String cardCVV = edtCardCVV.getText().toString().trim();

                if (cardCVV.isEmpty()) {
                    ToastHelper.getInstance().showMessage(getString(R.string.enter_card_cvv));
                    return;
                }
                cardToSave[0] = new Card(cardNo, cardMonth, cardYear, cardCVV);

                if (!cardToSave[0].validateCard()) {
                    ToastHelper.getInstance().showMessage(getString(R.string.invalid_card));
                } else {

                    CustomDialog.getInstance().showProgress(TrackUserActivity.this, "", false);
//                    Stripe stripe = new Stripe(TrackUserActivity.this, Constants.STRIPE_LIVE_API_KEY);
                    Stripe stripe = new Stripe(TrackUserActivity.this, Constants.STRIPE_LIVE_API_KEY);
                    stripe.createToken(cardToSave[0], new TokenCallback() {
                                public void onSuccess(Token token) {
                                    dialog.dismiss();
                                    CustomDialog.getInstance().dismiss();
                                    // Send token to your server
                                    //ToastHelper.getInstance().showMessage("Payment id : " + token.getId());
                                    Debug.trace("StripeTokenId", token.getId());
                                    //Debug.trace("stripeToken", token.getId());
                                    if (allHelpOfferModel.getJobAmountFlag() == 0) {
                                        insertStripePayment(token.getId(), amount);
                                    } else {
                                        if (isFirstTime) {
                                            insertStripePayment(token.getId(), amount);
                                        } else {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("op", ApiList.DO_STRIPE_PAYMENT);
                                            params.put("AuthKey", ApiList.AUTH_KEY);
                                            params.put("iAmount", String.valueOf(amount));
                                            params.put("sCurrency", "CAD");
                                            params.put("sDescription", "");
                                            params.put("sTokenId", token.getId());
                                            params.put("sJobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));


                                            RestClient.getInstance().post(TrackUserActivity.this, Request.Method.POST, params, ApiList.DO_STRIPE_PAYMENT,
                                                    true, RequestCode.DoStripePayment, new DataObserver() {

                                                        @Override
                                                        public void onSuccess(RequestCode mRequestCode, Object mObject) {

                                                            insertFinishJobOffer(allHelpOfferModel, actualJobHours);
                                                        }

                                                        @Override
                                                        public void onFailure(RequestCode mRequestCode, String mError) {

                                                            ToastHelper.getInstance().showMessage(mError);
                                                        }
                                                    });
                                        }
                                    }

                                }

                                public void onError(Exception error) {
                                    CustomDialog.getInstance().dismiss();
                                    // Show localized error message
                                    ToastHelper.getInstance().showMessage(getString(R.string.payment_failed_stripe));
                                    //ToastHelper.getInstance().showMessage(error.getMessage());
                                }
                            }
                    );
                }
            }
        });
        dialog.show();
    }

    public void insertStripePayment(String tokenId, int amount) {

        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.DO_STRIPE_PAYMENT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("iAmount", String.valueOf(amount));
        params.put("sCurrency", "CAD");
        params.put("sDescription", "");
        params.put("sTokenId", tokenId);
        params.put("sJobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));


        RestClient.getInstance().post(TrackUserActivity.this, Request.Method.POST, params, ApiList.DO_STRIPE_PAYMENT,
                true, RequestCode.DoStripePayment, this);
    }

    public void showCancelHelpReasonDialog() {
        dialog = new Dialog(this, R.style.dialogStyle);
        dialog.setContentView(R.layout.custom_dialog_cancel_help_reason);
        dialog.getWindow().getAttributes().windowAnimations = R.style.slideUpDown;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        TextView tvTitle = dialog.findViewById(R.id.tv_dialogHeader);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        final EditText edtCancelReason = dialog.findViewById(R.id.edt_cancelHelpReason);
        edtCancelReason.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);


        TextView tvSubmit = dialog.findViewById(R.id.tv_submit);
        tvSubmit.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCancelReason.getText().toString().trim().isEmpty()) {
                    ToastHelper.getInstance().showMessage(getString(R.string.str_cancel_help_reason));
                } else {
                    cancelHelp(edtCancelReason);
                }
            }
        });
    }


    private void cancelHelp(EditText edtCancelReason) {

        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.JOB_POST_CANCEL_OFFER);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));
        params.put("CancelReason", edtCancelReason.getText().toString().trim());

        RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.JOB_POST_CANCEL_OFFER,
                true, RequestCode.JobPostOfferCancel, this);
    }


    private void showCancelHelpDialog() {

        dialog = new Dialog(this, R.style.dialogStyle);
        dialog.setContentView(R.layout.custom_dialog_cancel_help);
        dialog.getWindow().getAttributes().windowAnimations = R.style.slideUpDown;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        TextView tvTitle = dialog.findViewById(R.id.tv_dialogHeader);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        tvTitle.setText(getString(R.string.str_cancel_help) + " ?");

        TextView cancelYes = dialog.findViewById(R.id.tv_cancelYes);
        cancelYes.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        TextView tvCancelNo = dialog.findViewById(R.id.tv_cancelNo);
        tvCancelNo.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        TextView tvBackToHelp = dialog.findViewById(R.id.tv_backToHelpProgress);
        tvBackToHelp.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        ImageView imgClose = dialog.findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void showMenuDialog() {
        dialog = new Dialog(this, R.style.dialogStyle);
        dialog.setContentView(R.layout.custom_dialog_track_menu);
        dialog.getWindow().getAttributes().windowAnimations = R.style.slideUpDown;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        TextView tvTitle = dialog.findViewById(R.id.tv_dialogHeader);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        TextView tvBackToMenu = dialog.findViewById(R.id.tv_backToMenu);
        tvBackToMenu.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        TextView tvMakeHelp = dialog.findViewById(R.id.tv_makeNewHelp);
        tvMakeHelp.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        TextView tvCancelHelp = dialog.findViewById(R.id.tv_cancelHelp);
        tvCancelHelp.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        ImageView imgClose = dialog.findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void showReviewRatingDialog(final AllHelpOfferModel allHelpOfferModel) {

        final Dialog mDialog;
        mDialog = new Dialog(this, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View dataView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_alert, null, false);
        mDialog.setContentView(R.layout.custom_dialog_review_rating);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        //mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView mDialogTitle = mDialog.findViewById(R.id.tv_dialogHeader);
        mDialogTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        // mDialogTitle.setText(mTitle);
        TextView tvUhelpMe = mDialog.findViewById(R.id.txv_show);
        tvUhelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        final float[] ratings = new float[1];
        RatingBar ratingBar = mDialog.findViewById(R.id.rb_rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratings[0] = rating;
            }
        });
        final EditText editText = mDialog.findViewById(R.id.edt_review);

        String str = getString(R.string.view_a_u) + getString(R.string.font_color) + getString(R.string.helpme) + getString(R.string.font);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvUhelpMe.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
            //  tvUhelpMe.setText(Html.fromHtml("View a U" + "<a color=\"#0095d7\">HelpMe</a>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvUhelpMe.setText(Html.fromHtml(str));
            //tvUhelpMe.setText(Html.fromHtml("By creating your account, you accept the "
            //        + "<a color=\"#0095d7\"href=\"http://www.anivethub.com/terms-and-conditions\">general conditions</a> of <b>UHelpMe</b>"));
            //  tvUhelpMe.setText(Html.fromHtml("View a U" + "<a color=\"#0095d7\">HelpMe</a>"));
        }

        ImageView tvOk = mDialog.findViewById(R.id.img_close);
        //  tvOk.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        Button btnSubmit = mDialog.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().trim().isEmpty()) {
                    ToastHelper.getInstance().showMessage(getString(R.string.enter_review));
                } else {
                    mDialog.dismiss();
                    insertReviewAndRating(allHelpOfferModel, ratings[0], editText.getText().toString().trim());
                }
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void insertReviewAndRating(AllHelpOfferModel allHelpOfferModel, float rating, String reviews) {

        params = new HashMap<>();
        params.put("op", ApiList.REVIEW_INSERT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("Rating", String.valueOf(rating));
        params.put("ReviewData", reviews);

        RestClient.getInstance().post(this, Request.Method.POST, params,
                ApiList.REVIEW_INSERT, true, RequestCode.ReviewInsert, this);

    }

    private void insertAcceptOffer(AllHelpOfferModel allHelpOfferModel) {

        params = new HashMap<>();
        params.put("op", ApiList.JOB_POST_ACCEPT_OFFER);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));

        RestClient.getInstance().post(this, Request.Method.POST, params,
                ApiList.JOB_POST_ACCEPT_OFFER, true, RequestCode.AcceptOffer, this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDurationTask.cancel();
        mTimer.cancel();
    }

    private class UpdateLocationTask extends TimerTask {

        @Override
        public void run() {
            TrackUserActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getLocationData();
                }
            });
        }
    }

    public void dismiss() {
        try {
            if (dialog != null) {
                if (isDialogShowing()) {
                    dialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return (boolean) : return true or false, if the dialog is showing or not
     */
    public boolean isDialogShowing() {

        return dialog != null && dialog.isShowing();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case Constants.REQUEST_CAMERA_PROFILE:

                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    Uri selectedImageUri = Utils.getImageUri(this, thumbnail);
                    //beginCrop(selectedImageUri);
                    break;

                case Constants.REQUEST_FILE_PROFILE:

                    selectedImageUri = data.getData();
                    //beginCrop(selectedImageUri);
                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    handleCrop(0, result.getUri().getPath());
                    break;

            }
        }
    }

    private void handleCrop(int resultCode, String data) {

        image64BaseIssue = Utils.getStringImage(data, ImageUpload.ClientProfile);
        imgIssuePhoto.setVisibility(View.VISIBLE);
        //imgGroupPhoto.setImageURI(null);
        //imgGroupPhoto.setImageURI(data);
        //Glide.with(this).load(data).into(imgIssuePhoto);
        Utils.setImage(data, android.R.drawable.ic_menu_camera, imgIssuePhoto);
    }

    @SuppressLint("MissingPermission")
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
                            .start(TrackUserActivity.this);
                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                } else {
                    ToastHelper.getInstance().showMessage(getString(R.string.str_allow_required_permission));
                }
            }
        } else if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION) {

            if (grantResults.length > 0 || grantResults.length != PackageManager.PERMISSION_GRANTED) {

                if (PermissionClass.verifyPermission(grantResults)) {
                    googleMap.setMyLocationEnabled(true);

                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Location extends AsyncTask<String, Void, Bitmap> {

        String[] arViewModel;

        @Override
        protected Bitmap doInBackground(String... params) {
            arViewModel = params;
            URL url;
            Bitmap bmp = null;
            try {
                url = new URL(params[0]);

                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                result = Bitmap.createScaledBitmap(result, 50, 50, true);
                result = Utils.getRoundedCornerBitmap(result, 50);
                if (userBitmap) {

                    loginUserBitmap = result;
                    float lat = PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0);
                    float lang = PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(lat, lang));
                    if (loginUserBitmap != null) {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(loginUserBitmap)));
                    }
                    googleMap.addMarker(markerOptions);
                    userBitmap = false;
                } else {

                    helperBitmap = result;
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(allHelpOfferModel.getLatitude(), allHelpOfferModel.getLongitude()));
                    if (helperBitmap != null) {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(helperBitmap)));
                    }
                    googleMap.addMarker(markerOptions);
                }
            }
        }
    }

    private Bitmap getMarkerBitmapFromView(Bitmap bitmap) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_location_view, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageBitmap(bitmap);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }
}

