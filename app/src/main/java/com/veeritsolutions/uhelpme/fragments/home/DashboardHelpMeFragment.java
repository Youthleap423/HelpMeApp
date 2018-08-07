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
import com.veeritsolutions.uhelpme.adapters.AdpMyHelpMe;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;
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

public class DashboardHelpMeFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    private View rootView;
    // private String mTag;
    private HomeActivity homeActivity;
    private RecyclerView recyclerViewDashboard;
    private AdpMyHelpMe adapter;
    private ArrayList<PostedJobModel> jobPostSeekerArrayList;
    private LoginUserModel loginUserModel;
    private int totalPages;
    private Map<String, String> params;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();
        jobPostSeekerArrayList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_dashboard_myhelpme, container, false);

        recyclerViewDashboard = (RecyclerView) rootView.findViewById(R.id.recyclerView_dashboard);
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
                    //  Log.d("tottalrecords1", String.valueOf(totalRecords));
                    // if (Constants.netCheckin(VerticalViewActivity.this)) {
                    adapter.showLoading(true);
                    GetJobPost_Seeker(page, false);
                    // } else {
                    //     Toast.makeText(VerticalViewActivity.this, "Start your mobile data or wifi connection", Toast.LENGTH_SHORT).show();
                    // }

                } else {
                    adapter.showLoading(false);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GetJobPost_Seeker(1, true);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_OfferIcon:
                Utils.buttonClickEffect(view);
                PostedJobModel postedJobModel = (PostedJobModel) view.getTag();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HELP_DATA, postedJobModel);

                //new DashboardFragment().getChildFragmentManager().popBackStack();
                homeActivity.pushFragment(new PostOffersFragment(), true, false, bundle);
                break;

            case R.id.img_InfoIcon:
                Utils.buttonClickEffect(view);
                PostedJobModel postedJobModel1 = (PostedJobModel) view.getTag();
                getAllHelpOfferData(postedJobModel1);
                break;

            case R.id.img_EditIcon:
                Utils.buttonClickEffect(view);
                postedJobModel = (PostedJobModel) view.getTag();
                if (postedJobModel != null) {
                    if (postedJobModel.getIsHire() == 0) {
                        bundle = new Bundle();
                        bundle.putSerializable(Constants.HELP_DATA, postedJobModel);
                        homeActivity.pushFragment(new ModifyMyHelpFragment(), true, false, bundle);
                    } else {
                        ToastHelper.getInstance().showMessage(getString(R.string.str_can_not_edit_job));
                    }
                }
                break;

            case R.id.img_ChatIcon:
                Utils.buttonClickEffect(view);
                PostedJobModel postedJobMode2 = (PostedJobModel) view.getTag();
                if (postedJobMode2 != null) {
                    if (postedJobMode2.getIsHire() == Constants.JOB_AWARDED) {
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable(Constants.HELP_DATA, postedJobMode2);
                        //new DashboardFragment().getChildFragmentManager().popBackStack();
                        homeActivity.pushFragment(new SpecificCategoryOnlyChatListFragment(), true, false, bundle1);
                    } else {
                        if (postedJobMode2.getIsHire() == Constants.JOB_FINISHED) {
                            ToastHelper.getInstance().showMessage(getString(R.string.str_job_finished));
                        } else if (postedJobMode2.getIsHire() == Constants.JOB_REJECTED) {
                            ToastHelper.getInstance().showMessage(getString(R.string.str_job_rejected));
                        } else if (postedJobMode2.getIsHire() == Constants.JOB_CANCELLED) {
                            ToastHelper.getInstance().showMessage(getString(R.string.str_job_cancelled));
                        } else if (postedJobMode2.getIsHire() == 0) {
                            ToastHelper.getInstance().showMessage(getString(R.string.str_job_not_accepted));
                        }
                    }
                }
                break;

        }
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetJobPostHelpSeeker:

                //ToastHelper.getInstance().showMessage("Success");
                ArrayList<PostedJobModel> list = (ArrayList<PostedJobModel>) mObject;

                if (list != null && !list.isEmpty()) {
                    jobPostSeekerArrayList.addAll(list);

                    if (!jobPostSeekerArrayList.isEmpty()) {

                        totalPages = jobPostSeekerArrayList.get(0).getTotalPage();
                        adapter = (AdpMyHelpMe) recyclerViewDashboard.getAdapter();

                        if (adapter != null && adapter.getItemCount() > 0) {

                            adapter.refreshList(jobPostSeekerArrayList);
                        } else {
                            adapter = new AdpMyHelpMe(getActivity(), jobPostSeekerArrayList, false);
                            recyclerViewDashboard.setAdapter(adapter);
                        }
                    }
                }
                break;

            case GetAllHelpOffer:

                ArrayList<AllHelpOfferModel> list1 = (ArrayList<AllHelpOfferModel>) mObject;

                if (list1 != null && !list1.isEmpty()) {

                    CustomDialog.getInstance().showAllHelpOffers(homeActivity, false, list1);
                }
                break;

        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        if (adapter != null) {
            adapter.showLoading(false);
        }
        ToastHelper.getInstance().showMessage(mError);
    }

    private void GetJobPost_Seeker(int page, boolean isDialogRequired) {

        try {
            params = new HashMap<>();
            params.put("op", ApiList.GET_JOB_POST_HELP_SEEKER);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("CategoryId", "");
            //params.put("CategoryId",String.valueOf(categoryModel.getCategoryId()));
            params.put("Keyword", "");
            params.put("PageNumber", String.valueOf(page));
            params.put("Records", String.valueOf(10));

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.GET_JOB_POST_HELP_SEEKER, isDialogRequired, RequestCode.GetJobPostHelpSeeker, this);

        } catch (Exception e) {

            e.printStackTrace();

        }
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

}
