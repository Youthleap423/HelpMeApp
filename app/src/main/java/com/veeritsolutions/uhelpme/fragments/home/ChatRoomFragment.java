package com.veeritsolutions.uhelpme.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.adapters.AdpChatUserList;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
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
 * Created by VEER7 on 7/1/2017.
 */

public class ChatRoomFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private LinearLayout linCreateGroup;
    private ImageView imgBackHeader;
    private RecyclerView recyclerViewChatUsers;

    private HomeActivity homeActivity;
    private ArrayList<ChatUsersListModel> chatUserList;
    private Map<String, String> params;
    private LoginUserModel loginUserModel;
    private AdpChatUserList adpChatUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        homeActivity = (HomeActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();

        homeActivity.imgHome.setImageResource(R.drawable.img_home_inactive);
        homeActivity.imgSearch.setImageResource(R.drawable.img_search_tabbar_inactive);
        homeActivity.imgDashbord.setImageResource(R.drawable.img_dashboard_inactive);
        homeActivity.imgChatRoom.setImageResource(R.drawable.img_chat_room_active);
        homeActivity.imgHelpMe.setImageResource(R.drawable.img_helpme);

        homeActivity.tvHome.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvSearch.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvDashBoard.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvChatRoom.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));

        rootView = inflater.inflate(R.layout.fragment_chatroom, container, false);

        imgBackHeader = (ImageView) rootView.findViewById(R.id.img_back_header);
        imgBackHeader.setVisibility(View.GONE);

        linCreateGroup = (LinearLayout) rootView.findViewById(R.id.lin_createGroup);
        recyclerViewChatUsers = (RecyclerView) rootView.findViewById(R.id.recyclerView_chatUsers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity, LinearLayoutManager.VERTICAL, false);
        recyclerViewChatUsers.setLayoutManager(linearLayoutManager);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chatUserList = new ArrayList<>();
        getChatUserData("", 1, true);
    }

    private void getChatUserData(String searchText, int pageNo, boolean isDialogRequired) {

        params = new HashMap<>();
        params.put("op", ApiList.GET_CHAT_USER);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.GET_CHAT_USER, isDialogRequired, RequestCode.GetChatUserList, this);
    }


    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_createGroup:
                Utils.buttonClickEffect(view);
                homeActivity.pushFragment(new CreateGroupFragment(), true, false, null);
                break;

            case R.id.lin_contacts:
                Utils.buttonClickEffect(view);
                ChatUsersListModel chatUsersListModel = (ChatUsersListModel) view.getTag();

                if (chatUsersListModel.getIsGroup() == Constants.CHAT_GROUP_INSTANT) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.CHAT_DATA, chatUsersListModel);
                    homeActivity.pushFragment(new GroupChatFragment(), true, false, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.CHAT_DATA, chatUsersListModel);
                    homeActivity.pushFragment(new OneToOneChatFragment(), true, false, bundle);
                }
                break;
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetChatUserList:

                ArrayList<ChatUsersListModel> list = (ArrayList<ChatUsersListModel>) mObject;
                if (!list.isEmpty()) {
                    chatUserList.addAll(list);

                    if (!chatUserList.isEmpty()) {
                        ChatUsersListModel chatUsersListModel = chatUserList.get(0);
                        // totalPages = chatUsersListModel.getTotalPage();
                        adpChatUser = (AdpChatUserList) recyclerViewChatUsers.getAdapter();
                        if (adpChatUser != null && adpChatUser.getItemCount() > 0) {
                            adpChatUser.refreshList(chatUserList);
                        } else {
                            adpChatUser = new AdpChatUserList(homeActivity, chatUserList, false);
                            recyclerViewChatUsers.setAdapter(adpChatUser);
                        }
                        // adpHelpNeeded.refreshList(postedJobList);
                    }

                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }
}
