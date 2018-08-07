package com.veeritsolutions.uhelpme.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.adapters.AdpGroupMember;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.fragments.profile.OtherPersonProfileFragment;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/10/2017.
 */

public class GroupMemberFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerViewContact;
    private FloatingActionButton fabAddMember;
    private TextView tvHeader;

    private HomeActivity homeActivity;
    private Map<String, String> params;
    private Bundle bundle;
    private ChatUsersListModel chatUsersListModel;
    private ArrayList<LoginUserModel> groupMemberList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);

        bundle = getArguments();
        if (bundle != null) {
            chatUsersListModel = (ChatUsersListModel) bundle.getSerializable(Constants.CHAT_GROUP_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_group_member, container, false);

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        recyclerViewContact = (RecyclerView) rootView.findViewById(R.id.recyclerView_groupMember);
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity, LinearLayoutManager.VERTICAL, false);
        recyclerViewContact.setLayoutManager(layoutManager);

        fabAddMember = (FloatingActionButton) rootView.findViewById(R.id.fab_addMember);
        fabAddMember.setColorFilter(R.color.colorAccent);
        if (chatUsersListModel.getIsAdmin() == 1) {
            fabAddMember.setVisibility(View.VISIBLE);
        } else {
            fabAddMember.setVisibility(View.GONE);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getGroupMemberData();
    }

    private void getGroupMemberData() {

        params = new HashMap<>();
        params.put("op", ApiList.GET_GROUP_CHAT_MEMBERS);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ChatGroupId", String.valueOf(chatUsersListModel.getId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_GROUP_CHAT_MEMBERS,
                true, RequestCode.GetGroupMember, this);

    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetGroupMember:

                groupMemberList = (ArrayList<LoginUserModel>) mObject;
                if (groupMemberList != null && !groupMemberList.isEmpty()) {
                    AdpGroupMember adpGroupMember = (AdpGroupMember) recyclerViewContact.getAdapter();

                    if (adpGroupMember != null && adpGroupMember.getItemCount() > 0) {
                        adpGroupMember.refreshList(groupMemberList);
                    } else {
                        adpGroupMember = new AdpGroupMember(homeActivity, groupMemberList, false);
                        recyclerViewContact.setAdapter(adpGroupMember);
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

            case R.id.fab_addMember:
                Utils.buttonClickEffect(view);
                bundle = new Bundle();
                bundle.putSerializable(Constants.CHAT_GROUP_DATA, chatUsersListModel);
                bundle.putSerializable(Constants.CHAT_GROUP_MEMBER_DATA, groupMemberList);
                homeActivity.pushFragment(new CreateGroupFragment(), true, false, bundle);
                break;

            case R.id.img_profilePhoto:

                LoginUserModel loginUserModel = (LoginUserModel) view.getTag();

                if (loginUserModel != null) {
                    bundle = new Bundle();
                    bundle.putSerializable(Constants.USER_DATA, loginUserModel);
                    homeActivity.pushFragment(new OtherPersonProfileFragment(), true, true, bundle);
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.group_chat_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
