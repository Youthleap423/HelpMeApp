package com.veeritsolutions.uhelpme.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.fragments.OtherPersonHelpMeFragment;
import com.veeritsolutions.uhelpme.fragments.OtherPersonOffersFragment;
import com.veeritsolutions.uhelpme.fragments.OtherPersonReviewRatingFragment;
import com.veeritsolutions.uhelpme.fragments.home.OneToOneChatFragment;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;
import com.veeritsolutions.uhelpme.utility.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VEER7 on 7/17/2017.
 */

public class OtherPersonProfileFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    //  private RecyclerView recyclerViewReward;
    private TextView tvUserName, tvLocation, tvRatingLabel, tvRating, tvPointlabel, tvPoint, tvHelpMeLabel,
            tvHelpMe, tvOfferedLabel, tvOffered/*, tvReview*/;
    private ImageView imgProfilePhoto;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private RatingBar rbRating;

    private HomeActivity homeActivity;
    private LoginUserModel loginUserModel;
    //  private ArrayList<ReviewModel> reviewModelsList;
    //  private AdpReviewRating adapterReward;


    public OtherPersonProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();

        if (bundle != null) {
            loginUserModel = (LoginUserModel) bundle.getSerializable(Constants.USER_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // rootView = inflater.inflate(R.layout.fragment_modify_help_offer, container, false);
        rootView = inflater.inflate(R.layout.fragment_other_user_profile, container, false);

        imgProfilePhoto = (ImageView) rootView.findViewById(R.id.img_navHeaderProfilePhoto);
        rbRating = (RatingBar) rootView.findViewById(R.id.rb_rating);

        tvUserName = (TextView) rootView.findViewById(R.id.tv_navHeaderName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvLocation = (TextView) rootView.findViewById(R.id.tv_navHeaderLocation);
        tvLocation.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

        //tvRatingLabel = (TextView) rootView.findViewById(R.id.tv_nav_header_rating_text);
        //tvRatingLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

        tvPointlabel = (TextView) rootView.findViewById(R.id.tv_nav_header_points_text);
        tvPointlabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

        tvHelpMeLabel = (TextView) rootView.findViewById(R.id.tv_nav_header_help_me_text);
        tvHelpMeLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

        tvOfferedLabel = (TextView) rootView.findViewById(R.id.tv_nav_header_offered_text);
        tvOfferedLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

        //tvRating = (TextView) rootView.findViewById(R.id.tv_nav_header_rating);
        //tvRating.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvPoint = (TextView) rootView.findViewById(R.id.tv_nav_header_points);
        tvPoint.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpMe = (TextView) rootView.findViewById(R.id.tv_nav_header_helpme);
        tvHelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvOffered = (TextView) rootView.findViewById(R.id.tv_nav_header_offered);
        tvOffered.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        // tvReview = (TextView) rootView.findViewById(R.id.tv_review);
        // tvReview.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        //setupViewpager(mViewPager);

        // setupTabIcons();

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        //recyclerViewReward = (RecyclerView) rootView.findViewById(R.id.recyclerView_other_user_profile);
        //recyclerViewReward.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getClientInfo();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientInfo:

                loginUserModel = (LoginUserModel) mObject;

                Utils.setImage(loginUserModel.getProfilePic(), R.drawable.img_user_placeholder, imgProfilePhoto);
                rbRating.setRating(loginUserModel.getRating());
                tvUserName.setText(loginUserModel.getFirstName() + " " + loginUserModel.getLastName());
                if (loginUserModel.getState().length() == 0) {
                    tvLocation.setText(loginUserModel.getCountry());
                } else {
                    tvLocation.setText(loginUserModel.getState() + ", " + loginUserModel.getCountry());
                }
               // tvRatingLabel.setText(String.valueOf(loginUserModel.getRating()));
                tvPointlabel.setText(String.valueOf(loginUserModel.getPoints()));
                tvHelpMeLabel.setText(String.valueOf(loginUserModel.getHelpMe()));
                tvOfferedLabel.setText(String.valueOf(loginUserModel.getOffered()));
                setupViewpager(mViewPager, loginUserModel);
                if (tabLayout != null) {
                    tabLayout.setupWithViewPager(mViewPager);
                }
                //  GetReview(loginUserModel);
                break;

            case GetReview:

              /*  reviewModelsList = (ArrayList<ReviewModel>) mObject;

                if (!reviewModelsList.isEmpty()) {
                    if (adapterReward == null) {

                        adapterReward = new AdpReviewRating(reviewModelsList, getActivity());
                        recyclerViewReward.setAdapter(adapterReward);
                    } else {
                        adapterReward.notifyDataSetChanged();
                    }
                }*/
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

            case R.id.fab_chat:
                Utils.buttonClickEffect(view);
                ChatUsersListModel chatUsersListModel = new ChatUsersListModel();
                chatUsersListModel.setId(loginUserModel.getClientId());
                chatUsersListModel.setName(loginUserModel.getFirstName() + " " + loginUserModel.getLastName());
                chatUsersListModel.setProfilePic(loginUserModel.getProfilePic());

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.CHAT_DATA, chatUsersListModel);

                homeActivity.pushFragment(new OneToOneChatFragment(), true, false, bundle);
                break;
        }
    }

    private void GetReview(LoginUserModel loginUserModel) {

        try {

            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.GET_REVIEW);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_REVIEW,
                    true, RequestCode.GetReview, this);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void getClientInfo() {

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

    /**
     * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = mFragmentList.get(position);
            //onClickEvent = (OnClickEvent) fragment;
            return fragment;
        }

        @Override
        public int getCount() {
            // Show total pages.
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title, LoginUserModel loginUserModel) {

            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.USER_DATA, loginUserModel);
            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupViewpager(ViewPager viewPager, LoginUserModel loginUserModel) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new OtherPersonReviewRatingFragment(), "Reviews", loginUserModel);
        adapter.addFragment(new OtherPersonHelpMeFragment(), "HelpMe", loginUserModel);
        adapter.addFragment(new OtherPersonOffersFragment(), "Offers", loginUserModel);

        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }
}

