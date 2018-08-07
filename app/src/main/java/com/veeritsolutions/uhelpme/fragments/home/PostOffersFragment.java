package com.veeritsolutions.uhelpme.fragments.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.activity.TrackUserActivity;
import com.veeritsolutions.uhelpme.adapters.AdpPostOffers;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.enums.ImageUpload;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/7/2017.
 */

public class PostOffersFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerView;
    private ImageView imgIssuePhoto;

    private HomeActivity homeActivity;
    private AdpPostOffers adpChatList;
    private ArrayList<AllHelpOfferModel> chatListModels;
    private Bundle bundle;
    private PostedJobModel postedJobModel;
    private Map<String, String> params;
    private LoginUserModel loginUserModel;
    private AllHelpOfferModel allHelpOfferModel;
    // public SubscriptionModel subscriptionModel;
    private int actualJobHours = 0;
    private String image64BaseIssue = "";
    // private AdpHelpNeeded adpHelpNeeded;
    // private View itemView;
    // private ArrayList<PostedJobModel> postedJobList;
    // private TextView tvHelpPostHours;
    //private boolean isFirstTime = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();

        loginUserModel = LoginUserModel.getLoginUserModel();
        if (bundle != null) {
            postedJobModel = (PostedJobModel) bundle.getSerializable(Constants.HELP_DATA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        chatListModels = new ArrayList<>();
        getCategoryChatData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_post_offers, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_categoryChat);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity, LinearLayoutManager.VERTICAL, false);


        recyclerView.setLayoutManager(linearLayoutManager);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // getCategoryChatData();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetSpecificCategoryChat:

                ArrayList<AllHelpOfferModel> list = (ArrayList<AllHelpOfferModel>) mObject;

                if (!list.isEmpty()) {
                    chatListModels.addAll(list);
                    if (!chatListModels.isEmpty()) {

                        adpChatList = (AdpPostOffers) recyclerView.getAdapter();

                        if (adpChatList != null && adpChatList.getItemCount() > 0) {
                            adpChatList.refreshList(chatListModels);

                        } else {
                            adpChatList = new AdpPostOffers(homeActivity, chatListModels, false);
                            recyclerView.setAdapter(adpChatList);
                        }
                    }
                }
                break;

            case JobpostRejected:
                // chatListModels.remove(allHelpOfferModel);
                int position = allHelpOfferModel.getPosition();
                allHelpOfferModel.setIsHire(-1);
                chatListModels.set(position, allHelpOfferModel);
                adpChatList.refreshList(chatListModels);
                break;

            case AcceptOffer:

                String s = (String) mObject;
                chatListModels.clear();
                getCategoryChatData();
                break;

            case FinishOffer:
                s = (String) mObject;
                chatListModels.clear();
                getCategoryChatData();
                showReviewRatingDialog(allHelpOfferModel);
                break;

            case DoStripePayment:
                insertAcceptOffer(allHelpOfferModel);
                break;

            case JobPostIssue:
                showReviewRatingDialog(allHelpOfferModel);
                break;
        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                homeActivity.popBackFragment();
                break;

//            case R.id.lin_contacts:
//                Utils.buttonClickEffect(view);
//                allHelpOfferModel = (AllHelpOfferModel) view.getTag();
//                if (allHelpOfferModel != null) {
//                    if (allHelpOfferModel.getIsHire() != -1) {
//                        ChatUsersListModel chatUsersListModel = new ChatUsersListModel();
//                        chatUsersListModel.setId(allHelpOfferModel.getClientId());
//                        chatUsersListModel.setName(allHelpOfferModel.getFirstName() + " " + allHelpOfferModel.getLastName());
//
//                        bundle = new Bundle();
//                        bundle.putSerializable(Constants.CHAT_DATA, chatUsersListModel);
//
//                        homeActivity.pushFragment(new OneToOneChatFragment(), true, false, bundle);
//                    }
//                }
//
//                break;

            case R.id.tv_acceptOffer:
                // showReviewRatingDialog();
                Utils.buttonClickEffect(view);
                allHelpOfferModel = (AllHelpOfferModel) view.getTag();

                if (allHelpOfferModel != null) {
                    if (allHelpOfferModel.getIsHire() == 0) {
                        showConfirmationDialog(allHelpOfferModel);
                    } else if (allHelpOfferModel.getIsHire() == 1) {
                        showFinishJobDialog();
                    }
                }
                break;

            case R.id.tv_rejectOffer:
                Utils.buttonClickEffect(view);
                allHelpOfferModel = (AllHelpOfferModel) view.getTag();
                if (allHelpOfferModel != null) {
                    declineHelpPost(allHelpOfferModel);
                }
                break;

            case R.id.img_mapIcon:
                Utils.buttonClickEffect(view);
                allHelpOfferModel = (AllHelpOfferModel) view.getTag();
                if (allHelpOfferModel != null) {
                    Intent intent = new Intent(getActivity(), TrackUserActivity.class);
                    intent.putExtra(Constants.USER_DATA, allHelpOfferModel);
                    startActivity(intent);
                }
                break;
        }
    }

    private void declineHelpPost(AllHelpOfferModel allHelpOfferModel) {

        params = new HashMap<>();
        params.put("op", ApiList.JOB_POST_REJECTED);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.JOB_POST_REJECTED,
                true, RequestCode.JobpostRejected, this);
    }

    private void showFinishJobDialog() {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(homeActivity, R.style.dialogStyle);
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
        builder = new AlertDialog.Builder(homeActivity, R.style.dialogStyle);
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

        final Dialog dialog = new Dialog(getActivity(), R.style.dialogStyle);
        dialog.setContentView(R.layout.custom_dialog_help_finish_issue);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_dialogHeader);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        final EditText edtCancelReason = (EditText) dialog.findViewById(R.id.edt_cancelHelpReason);
        edtCancelReason.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        TextView tvSubmit = (TextView) dialog.findViewById(R.id.tv_issueSubmit);
        tvSubmit.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tvCancel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imgIssuePhoto = (ImageView) dialog.findViewById(R.id.img_issuePhoto);
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
                            .start(getContext(), PostOffersFragment.this);
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

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.JOB_POST_ISSUE,
                true, RequestCode.JobPostIssue, this);
    }

    private void showFinishConfirmationDialog() {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(homeActivity, R.style.dialogStyle);
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

    private void showEnterAmountDialog() {

        final Dialog dialog = new Dialog(getActivity(), R.style.dialogStyle);
        dialog.setContentView(R.layout.custom_dialog_enter_amount);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tvAmount = (TextView) dialog.findViewById(R.id.tv_amount);
        tvAmount.setText("");
        final EditText edtWorkHours = (EditText) dialog.findViewById(R.id.edt_workHours);

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

        Button btnPayment = (Button) dialog.findViewById(R.id.btn_submit);

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

    private void showPaymentDialog(final int amount, final boolean isFirstTime) {

        final Dialog dialog = new Dialog(getActivity(), R.style.dialogStyle);

        dialog.setContentView(R.layout.custom_dialog_payment_selection);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //final CardInputWidget mCardInputWidget = (CardInputWidget) dialog.findViewById(R.id.card_input_widget);
        //final float finalAmount = amount;
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_titleDialog);
        if (allHelpOfferModel.getJobAmountFlag() == 0) {
            //finalAmount = amount;
            tvTitle.setText(getString(R.string.str_payment) + " $" + amount);

        } else {
            //finalAmount = allHelpOfferModel.getJobHour() * allHelpOfferModel.getOfferAmount();
            tvTitle.setText(getString(R.string.str_hourly_bases) + amount);
        }

        final EditText edtCardNo, edtCardYear, edtCardMonth, edtCardCVV;

        edtCardNo = (EditText) dialog.findViewById(R.id.edt_cardNo);
        edtCardYear = (EditText) dialog.findViewById(R.id.edt_cardYear);
        edtCardMonth = (EditText) dialog.findViewById(R.id.edt_cardMonth);
        edtCardCVV = (EditText) dialog.findViewById(R.id.edt_cardCVV);

        final Card[] cardToSave = new Card[1];
        Button btnSaveCard = (Button) dialog.findViewById(R.id.btn_submit);

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
                    //cardToSave.setName(loginUserModel.getFirstName() + " " + loginUserModel.getLastName());
                    //cardToSave.setAddressZip("12345");
                    //cardToSave.setCurrency("USD");
                    //cardToSave.setAddressCountry("U.S");
                    Stripe stripe;
                    CustomDialog.getInstance().showProgress(getActivity(), "", false);
                    stripe = new Stripe(getActivity(), Constants.STRIPE_LIVE_API_KEY);

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


                                            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.DO_STRIPE_PAYMENT,
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


        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.DO_STRIPE_PAYMENT,
                true, RequestCode.DoStripePayment, this);
    }

    private void insertFinishJobOffer(AllHelpOfferModel allHelpOfferModel, int jobActualHours) {

        params = new HashMap<>();
        params.put("op", ApiList.FINISH_OFFER);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));
        params.put("JobActualHour", String.valueOf(jobActualHours));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.FINISH_OFFER,
                true, RequestCode.FinishOffer, this);
    }

    private void getCategoryChatData() {

        params = new HashMap<>();
        params.put("op", ApiList.GET_CATEGORY_CHAT_DATA);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(postedJobModel.getJobPostId()));
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_CATEGORY_CHAT_DATA,
                true, RequestCode.GetSpecificCategoryChat, this);
    }

    private void showConfirmationDialog(final AllHelpOfferModel allHelpOfferModel) {
        final AlertDialog.Builder builder;
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder = new AlertDialog.Builder(homeActivity, R.style.dialogStyle);
        //  } else {
        //      builder = new AlertDialog.Builder(profileActivity);
        //  }
        // builder.create();
        builder.setTitle(getString(R.string.accept_post_offer_for_help));
        builder.setMessage(R.string.are_you_want_to_accept_offer);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                int amount;
                if (allHelpOfferModel.getJobAmountFlag() == 0) {

                    amount = allHelpOfferModel.getOfferAmount();

                } else {

                    amount = allHelpOfferModel.getJobHour() * allHelpOfferModel.getOfferAmount();
                }
                showPaymentDialog(amount, true);
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


    private void insertAcceptOffer(AllHelpOfferModel allHelpOfferModel) {

        params = new HashMap<>();
        params.put("op", ApiList.JOB_POST_ACCEPT_OFFER);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.JOB_POST_ACCEPT_OFFER, true, RequestCode.AcceptOffer, this);

    }

    private void showReviewRatingDialog(final AllHelpOfferModel allHelpOfferModel) {

        final Dialog mDialog;
        mDialog = new Dialog(homeActivity, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View dataView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_alert, null, false);
        mDialog.setContentView(R.layout.custom_dialog_review_rating);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        //mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView mDialogTitle = (TextView) mDialog.findViewById(R.id.tv_dialogHeader);
        mDialogTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        // mDialogTitle.setText(mTitle);
        TextView tvUhelpMe = (TextView) mDialog.findViewById(R.id.txv_show);
        tvUhelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        final float[] ratings = new float[1];
        RatingBar ratingBar = (RatingBar) mDialog.findViewById(R.id.rb_rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratings[0] = rating;
            }
        });
        final EditText editText = (EditText) mDialog.findViewById(R.id.edt_review);

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

        ImageView tvOk = (ImageView) mDialog.findViewById(R.id.img_close);
        //  tvOk.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        Button btnSubmit = (Button) mDialog.findViewById(R.id.btn_submit);
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

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.REVIEW_INSERT, true, RequestCode.ReviewInsert, this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case Constants.REQUEST_CAMERA_PROFILE:

                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    Uri selectedImageUri = Utils.getImageUri(getActivity(), thumbnail);
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
                            .start(getContext(), PostOffersFragment.this);
                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                } else {
                    ToastHelper.getInstance().showMessage(getString(R.string.str_allow_required_permission));
                }
            }
        }
    }

    private void handleCrop(int resultCode, String data) {

        image64BaseIssue = Utils.getStringImage(data, ImageUpload.ClientProfile);
        imgIssuePhoto.setVisibility(View.VISIBLE);
        //imgGroupPhoto.setImageURI(null);
        //imgGroupPhoto.setImageURI(data);
        //Glide.with(getActivity()).load(data).into(imgIssuePhoto);
        Utils.setImage(data, android.R.drawable.ic_menu_camera, imgIssuePhoto);
    }
}
