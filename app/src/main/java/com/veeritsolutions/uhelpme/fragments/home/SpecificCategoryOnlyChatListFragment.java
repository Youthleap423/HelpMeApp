package com.veeritsolutions.uhelpme.fragments.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.adapters.AdpSpecificCategoryOnlyChatList;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.models.SubscriptionModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ABC on 10/2/2017.
 */

public class SpecificCategoryOnlyChatListFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerView;

    private HomeActivity homeActivity;
    private AdpSpecificCategoryOnlyChatList adpChatList;
    private ArrayList<AllHelpOfferModel> chatListModels;
    private Bundle bundle;
    private PostedJobModel postedJobModel;
    private Map<String, String> params;
    private LoginUserModel loginUserModel;
    private AllHelpOfferModel allHelpOfferModel;
    public SubscriptionModel subscriptionModel;
    private int actualJobHours = 0;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_specific_category_only_chat_list, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView_categoryChat);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chatListModels = new ArrayList<>();

        getCategoryChatData();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetSpecificCategoryChat:

                ArrayList<AllHelpOfferModel> list = (ArrayList<AllHelpOfferModel>) mObject;

                if (!list.isEmpty()) {
                    chatListModels.addAll(list);
                    if (!chatListModels.isEmpty()) {

                        adpChatList = (AdpSpecificCategoryOnlyChatList) recyclerView.getAdapter();

                        if (adpChatList != null && adpChatList.getItemCount() > 0) {
                            adpChatList.refreshList(chatListModels);

                        } else {
                            adpChatList = new AdpSpecificCategoryOnlyChatList(homeActivity, chatListModels, false);
                            recyclerView.setAdapter(adpChatList);
                        }
                    }
                }
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

            case R.id.lin_contacts:
                Utils.buttonClickEffect(view);
                allHelpOfferModel = (AllHelpOfferModel) view.getTag();
                ChatUsersListModel chatUsersListModel = new ChatUsersListModel();
                chatUsersListModel.setId(allHelpOfferModel.getClientId());
                chatUsersListModel.setName(allHelpOfferModel.getFirstName() + " " + allHelpOfferModel.getLastName());

                bundle = new Bundle();
                bundle.putSerializable(Constants.CHAT_DATA, chatUsersListModel);

                homeActivity.pushFragment(new OneToOneChatFragment(), true, false, bundle);
                break;

           /* case R.id.tv_acceptOffer:
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

            case R.id.img_mapIcon:
                Utils.buttonClickEffect(view);
                allHelpOfferModel = (AllHelpOfferModel) view.getTag();
                if (allHelpOfferModel != null) {
                    Intent intent = new Intent(getActivity(), TrackUserActivity.class);
                    intent.putExtra(Constants.USER_DATA, allHelpOfferModel.getClientId());
                    startActivity(intent);
                }
                break;*/
        }
    }

    private void showFinishJobDialog() {

        final AlertDialog.Builder[] builder = new AlertDialog.Builder[1];
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder[0] = new AlertDialog.Builder(homeActivity, R.style.dialogStyle);
        //  } else {
        //      builder = new AlertDialog.Builder(profileActivity);
        //  }
        // builder.create();
        builder[0].setTitle(getString(R.string.finish_job));
        builder[0].setMessage(R.string.are_you_want_to_finish_help_post);
        builder[0].setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
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
        builder[0].setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder[0].show();

    }

    private void showEnterAmountDialog() {

        final Dialog dialog = new Dialog(getActivity(), R.style.dialogStyle);
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
                        tvAmount.setText("We will pay you back the extra amount of money very shortly");
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

    private void showPaymentDialog(final int amount, final boolean isFirstTime) {

        final Dialog dialog = new Dialog(getActivity(), R.style.dialogStyle);

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
            tvTitle.setText("This help is hourly base. Total amount : " + amount);
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
                    //cardToSave.setName(loginUserModel.getFirstName() + " " + loginUserModel.getLastName());
                    //cardToSave.setAddressZip("12345");
                    //cardToSave.setCurrency("USD");
                    //cardToSave.setAddressCountry("U.S");

                    CustomDialog.getInstance().showProgress(getActivity(), "", false);
                    Stripe stripe = new Stripe(getActivity(), Constants.STRIPE_LIVE_API_KEY);

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
        final AlertDialog.Builder[] builder = new AlertDialog.Builder[1];
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder[0] = new AlertDialog.Builder(homeActivity, R.style.dialogStyle);
        //  } else {
        //      builder = new AlertDialog.Builder(profileActivity);
        //  }
        // builder.create();
        builder[0].setTitle(getString(R.string.accept_post_offer_for_help));
        builder[0].setMessage(R.string.are_you_want_to_accept_offer);
        builder[0].setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
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
        builder[0].setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder[0].show();
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

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.REVIEW_INSERT, true, RequestCode.ReviewInsert, this);

    }
}

