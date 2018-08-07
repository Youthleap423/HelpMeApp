package com.veeritsolutions.uhelpme.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.adapters.AdpMyOffers;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.fragments.profile.OtherPersonProfileFragment;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.EndlessRecyclerViewScrollListener;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 7/4/2017.
 */

public class DashboardMyOfferFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerViewDashboard;

    private ArrayList<PostedJobModel> myOfferList;
    private AdpMyOffers adpMyOffer;
    private HomeActivity homeActivity;
    private Map<String, String> params;
    private LoginUserModel loginUserModel;
    private int totalPages;
    private PostedJobModel postedJobModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        homeActivity = (HomeActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();

        rootView = inflater.inflate(R.layout.fragment_dashboard_myoffer, container, false);

        recyclerViewDashboard = (RecyclerView) rootView.findViewById(R.id.recyclerView_dash_myoffer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDashboard.setLayoutManager(linearLayoutManager);

        recyclerViewDashboard.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager, 1) {
            @Override
            protected void onShow() {

            }

            @Override
            protected void onHide() {

            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page <= totalPages) {

                    adpMyOffer.showLoading(true);
                    getMyOfferData(page, false);


                } else {
                    adpMyOffer.showLoading(false);
                }
            }
        });

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myOfferList = new ArrayList<>();

        getMyOfferData(1, true);
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_ChatIcon:
                Utils.buttonClickEffect(view);
                postedJobModel = (PostedJobModel) view.getTag();

                if (postedJobModel != null) {
                    if (postedJobModel.getIsHire() == Constants.JOB_AWARDED) {

                        ChatUsersListModel chatUsersListModel = new ChatUsersListModel();
                        chatUsersListModel.setId(postedJobModel.getClientId());
                        chatUsersListModel.setName(postedJobModel.getFirstName() + " " + postedJobModel.getLastName());
                        chatUsersListModel.setProfilePic(postedJobModel.getProfilePic());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.CHAT_DATA, chatUsersListModel);
                        homeActivity.pushFragment(new OneToOneChatFragment(), true, false, bundle);
                    } else {
                        if (postedJobModel.getIsHire() == Constants.JOB_FINISHED) {
                            ToastHelper.getInstance().showMessage(getString(R.string.str_job_finished));
                        } else if (postedJobModel.getIsHire() == Constants.JOB_REJECTED) {
                            ToastHelper.getInstance().showMessage(getString(R.string.str_job_rejected));
                        } else if (postedJobModel.getIsHire() == Constants.JOB_CANCELLED) {
                            ToastHelper.getInstance().showMessage(getString(R.string.str_job_cancelled));
                        } else if (postedJobModel.getIsHire() == 0) {
                            ToastHelper.getInstance().showMessage(getString(R.string.str_offer_not_accepted));
                        }
                    }
                }
                break;

            case R.id.img_InfoIcon:
                Utils.buttonClickEffect(view);
                postedJobModel = (PostedJobModel) view.getTag();
                if (postedJobModel != null)
                    getAllHelpOfferData(postedJobModel);
                break;

            case R.id.img_EditIcon:
                Utils.buttonClickEffect(view);
                postedJobModel = (PostedJobModel) view.getTag();
                if (postedJobModel != null) {
                    if (postedJobModel.getIsHire() == 0) {
                        CustomDialog.getInstance().showEnterOffer(homeActivity, false, getString(R.string.update_your_offer));
                    } else {
                        ToastHelper.getInstance().showMessage(getString(R.string.str_can_not_edit_offer));
                    }
                }
                break;

            case R.id.btn_actionOk:
                Utils.buttonClickEffect(view);
                Object o = view.getTag();

                if (o != null) {
                    String offerAmount = (String) o;

                    if (offerAmount.isEmpty()) {
                        ToastHelper.getInstance().showMessage(getString(R.string.enter_amount));
                        return;
                    }
                   /* if (offerAmount.equals("0")) {
                        ToastHelper.getInstance().showMessage(getString(R.string.can_not_put_zero_offer));
                        return;
                    }*/

                    CustomDialog.getInstance().dismiss();
                    params = new HashMap<>();
                    params.put("op", "JobPostOfferUpdate");
                    params.put("AuthKey", ApiList.AUTH_KEY);
                    params.put("JobPostOfferId", String.valueOf(postedJobModel.getJobPostOfferId()));
                    params.put("JobPostId", String.valueOf(postedJobModel.getJobPostId()));
                    params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
                    params.put("OfferAmount", offerAmount);
                    params.put("Latitude", String.valueOf(PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0)));
                    params.put("Longitude", String.valueOf(PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0)));
                    params.put("Altitude", String.valueOf(PrefHelper.getInstance().getFloat(PrefHelper.ALTITUDE, 0)));

                    RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.JOB_POST_OFFER_UPDATE,
                            true, RequestCode.JobPostOfferUpdate, this);
                }
                break;

            case R.id.img_HeaderProfilePhoto:

                postedJobModel = (PostedJobModel) view.getTag(R.id.img_HeaderProfilePhoto);
                if (postedJobModel != null) {
                    LoginUserModel loginUserModel = new LoginUserModel();
                    loginUserModel.setClientId(postedJobModel.getClientId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.USER_DATA, loginUserModel);
                    homeActivity.pushFragment(new OtherPersonProfileFragment(), true, false, bundle);
                }
                break;
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetJobPostMyOffers:

                ArrayList<PostedJobModel> list = (ArrayList<PostedJobModel>) mObject;
                if (!list.isEmpty()) {

                    myOfferList.addAll(list);

                    if (!myOfferList.isEmpty()) {

                        totalPages = myOfferList.get(0).getTotalPage();

                        adpMyOffer = (AdpMyOffers) recyclerViewDashboard.getAdapter();

                        if (adpMyOffer != null && adpMyOffer.getItemCount() > 0) {
                            adpMyOffer.refreshList(myOfferList);
                        } else {
                            adpMyOffer = new AdpMyOffers(homeActivity, myOfferList, false);
                            recyclerViewDashboard.setAdapter(adpMyOffer);
                        }
                    }
                }

                break;

            case GetAllHelpOffer:

                ArrayList<AllHelpOfferModel> list1 = (ArrayList<AllHelpOfferModel>) mObject;

                if (list1 != null && !list1.isEmpty()) {
                    // chatListModels.addAll(list);
                    // if (!chatListModels.isEmpty()) {

                    CustomDialog.getInstance().showAllHelpOffers(homeActivity, false, list1);
                    // }
                }
                break;
            case JobPostOfferUpdate:
                myOfferList = new ArrayList<>();
                getMyOfferData(1, true);
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        if (adpMyOffer != null) {
            adpMyOffer.showLoading(false);
        }
        ToastHelper.getInstance().showMessage(mError);
    }

    private void getAllHelpOfferData(PostedJobModel postedJobModel) {

        params = new HashMap<>();
        params.put("op", ApiList.GET_CATEGORY_CHAT_DATA);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(postedJobModel.getJobPostId()));
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_CATEGORY_CHAT_DATA,
                true, RequestCode.GetAllHelpOffer, this);
    }

    private void getMyOfferData(int pageNo, boolean isDialogRequired) {

        params = new HashMap<>();
        params.put("op", ApiList.GET_JOB_POST_MY_OFFERS);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("PageNumber", String.valueOf(pageNo));
        params.put("Records", String.valueOf(10));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_JOB_POST_MY_OFFERS,
                isDialogRequired, RequestCode.GetJobPostMyOffers, this);

    }

}
