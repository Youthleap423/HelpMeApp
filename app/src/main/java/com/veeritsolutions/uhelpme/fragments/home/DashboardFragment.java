package com.veeritsolutions.uhelpme.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;

import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.utility.Utils;

/**
 * Created by Admin on 7/1/2017.
 */

public class DashboardFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private ImageView imgBackHeader;

    private HomeActivity homeActivity;
    private TabHost tabHost;
    public final String TAB_ONE = "MyHelpMe";
    public final String TAB_TWO = "MyOffers";

    private FragmentManager fragmentManager;
    private OnClickEvent onClickEvent;
    //private OnBackPressedEvent onBackPressedEvent;
    private Fragment currentFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        fragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        homeActivity.imgHome.setImageResource(R.drawable.img_home_inactive);
        homeActivity.imgSearch.setImageResource(R.drawable.img_search_tabbar_inactive);
        homeActivity.imgDashbord.setImageResource(R.drawable.img_dashboard_active);
        homeActivity.imgChatRoom.setImageResource(R.drawable.img_chat_room_inactive);
        homeActivity.imgHelpMe.setImageResource(R.drawable.img_helpme);

        homeActivity.tvHome.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvSearch.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvDashBoard.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        homeActivity.tvChatRoom.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));

        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        imgBackHeader = (ImageView) rootView.findViewById(R.id.img_back_header);
        imgBackHeader.setVisibility(View.GONE);

        tabHost = (TabHost) rootView.findViewById(R.id.tab_host);
        tabHost.setup();


        TabHost.TabSpec spec = tabHost.newTabSpec(TAB_ONE);
        spec.setContent(R.id.tab_container);
        spec.setIndicator(getString(R.string.my_helpme));

        TabHost.TabSpec spec1 = tabHost.newTabSpec(TAB_TWO);
        spec1.setContent(R.id.tab_container);
        spec1.setIndicator(getString(R.string.my_offers));

        tabHost.addTab(spec);
        tabHost.addTab(spec1);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                //ToastHelper.getInstance().showMessage("tab id:",Integer.parseInt(tabId));

                if (TAB_ONE.equals(tabId)) {

                    updateTab(tabId, new DashboardHelpMeFragment());
                    // mCurrentTab = 0;
                    // return;
                } else if (TAB_TWO.equals(tabId)) {
                    updateTab(tabId, new DashboardMyOfferFragment());
                    //  mCurrentTab = 1;
                    //  return;
                }

            }
        });
        updateTab(TAB_ONE, new DashboardHelpMeFragment());
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onBackPressed() {

        homeActivity.popBackFragment();
//        if (onBackPressedEvent != null) {
//            onBackPressedEvent.onBackPressed();
//        }
    }

    @Override
    public void onClick(View view) {

        onClickEvent.onClick(view);

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                homeActivity.popBackFragment();
                break;
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {


        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

    }

    private void updateTab(String tabId, Fragment fragment) {

        currentFragment = fragment;
        onClickEvent = (OnClickEvent) currentFragment;

        // if (fragmentManager.findFragmentByTag(tabId) == null) {
        fragmentManager.beginTransaction()
                .replace(R.id.tab_container, currentFragment, tabId)
                // .addToBackStack(tabId)
                .commit();
        // }
    }

}
