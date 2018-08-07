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

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Utils;
import com.veeritsolutions.uhelpme.utility.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VEER7 on 7/15/2017.
 */

public class RewardFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // private TabHost tabHost;
    public final String TAB_ONE = "Rewards Points";
    public final String TAB_TWO = "Redeem History";
    private View rootView;
    private ImageView imgProfilePhoto;
    private TextView tvUserName, tvLocation, tvRatingLabel, tvRating, tvPointlabel, tvPoint, tvHelpMeLabel,
            tvHelpMe, tvOfferedLabel, tvOffered;
    private RatingBar rbRating;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ProfileActivity profileActivity;
    //private FragmentManager fragmentManager;
    private OnClickEvent onClickEvent;
    private OnBackPressedEvent onBackPressedEvent;
    private Fragment currentFragment;
    private LoginUserModel loginUserModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginUserModel = LoginUserModel.getLoginUserModel();

        profileActivity = (ProfileActivity) getActivity();

        // fragmentManager = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_rewards, container, false);

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

        // tvRating = (TextView) rootView.findViewById(R.id.tv_nav_header_rating);
        // tvRating.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvPoint = (TextView) rootView.findViewById(R.id.tv_nav_header_points);
        tvPoint.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpMe = (TextView) rootView.findViewById(R.id.tv_nav_header_helpme);
        tvHelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvOffered = (TextView) rootView.findViewById(R.id.tv_nav_header_offered);
        tvOffered.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        setupViewpager(mViewPager);

        // setupTabIcons();

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }

       /* tabHost = (TabHost) rootView.findViewById(R.id.tab_host);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec(TAB_ONE);
        spec.setContent(R.id.tab_container);
        spec.setIndicator(getString(R.string.reward_points));

        TabHost.TabSpec spec1 = tabHost.newTabSpec(TAB_TWO);
        spec1.setContent(R.id.tab_container);
        spec1.setIndicator(getString(R.string.redeem_history));

        tabHost.addTab(spec);
        tabHost.addTab(spec1);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                //ToastHelper.getInstance().showMessage("tab id:",Integer.parseInt(tabId));

                if (TAB_ONE.equals(tabId)) {

                    updateTab(tabId, new RedeemPointsFragment());
                    // mCurrentTab = 0;
                    // return;
                } else if (TAB_TWO.equals(tabId)) {
                    updateTab(tabId, new RedeemHistoryFragment());
                    //  mCurrentTab = 1;
                    //  return;
                }

            }
        });*/
        //  updateTab(TAB_ONE, new RedeemPointsFragment());

        return rootView;
    }

    private void setupViewpager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new RedeemPointsFragment(), TAB_ONE);
        adapter.addFragment(new RedeemHistoryFragment(), TAB_TWO);
        // adapter.addFragment(new OtherPersonOffersFragment(), "Offers", loginUserModel);

        mViewPager.setAdapter(adapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {

        profileActivity.popBackFragment();
        // profileActivity.removeAllFragment();
        // profileActivity.pushFragment(new ProfileHomeFragment(), true, false, null);
//        if (onBackPressedEvent != null) {
//            onBackPressedEvent.onBackPressed();
//        }
        // onBackPressedEvent.onBackPressed();
    }

    @Override
    public void onClick(View view) {

        onClickEvent.onClick(view);

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                profileActivity.popBackFragment();
                //profileActivity.removeFragmentUntil(ProfileHomeFragment.class);
//                profileActivity.removeAllFragment();
//                profileActivity.pushFragment(new ProfileHomeFragment(), true, false, null);
                break;
        }
    }

    private void updateTab(String tabId, Fragment fragment) {

        currentFragment = fragment;
        onClickEvent = (OnClickEvent) currentFragment;
        onBackPressedEvent = (OnBackPressedEvent) currentFragment;
        // if (fragmentManager.findFragmentByTag(tabId) == null) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.tab_container, currentFragment, tabId)
                .addToBackStack(tabId)
                .commit();
        // }
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
            onClickEvent = (OnClickEvent) fragment;
            return fragment;
        }

        @Override
        public int getCount() {
            // Show total pages.
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {

            onClickEvent = (OnClickEvent) fragment;
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(Constants.USER_DATA, loginUserModel);
//            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}


