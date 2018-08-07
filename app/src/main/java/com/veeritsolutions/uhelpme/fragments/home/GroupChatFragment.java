package com.veeritsolutions.uhelpme.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.adapters.AdpOneToOneChat;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.fragments.profile.OtherPersonProfileFragment;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.ChatModel;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/8/2017.
 */

public class GroupChatFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerViewGroupChat;
    private EditText inputText;
    private Button btnSend;
    private TextView tvHeader, tvGroupInfo;
    private ImageView imgProfilePic;
    private Toolbar toolbar;
    private LinearLayoutManager linearLayoutManager;

    private HomeActivity homeActivity;
    private AdpOneToOneChat adpChat;
    // private AllHelpOfferModel specificCategoryChatListModel;
    private Bundle bundle;
    private LoginUserModel loginUserModel;
    private ChatUsersListModel chatGroupModel;

    // Firebase variables and objects
    // private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGroup;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        loginUserModel = LoginUserModel.getLoginUserModel();
        // firebaseDatabase = FirebaseDatabase.getInstance(ServerConfig.FCM_APP_URL);
        // firebaseDatabase.setPersistenceEnabled(true);

        if (bundle != null) {
            chatGroupModel = (ChatUsersListModel) bundle.getSerializable(Constants.CHAT_DATA);
            databaseReferenceGroup = MyApplication.getInstance().getFirebaseDatabase().getReference().child(String.valueOf(chatGroupModel.getId()));
        }
        // databaseReferenceTwo = firebaseDatabase.getReference().child(String.valueOf(specificCategoryChatListModel.getClientId() + loginUserModel.getClientId()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_group_chat, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        homeActivity.setSupportActionBar(toolbar);

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
        tvHeader.setText(chatGroupModel.getName());

        tvGroupInfo = (TextView) rootView.findViewById(R.id.tv_groupInfo);
        tvGroupInfo.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        imgProfilePic = (ImageView) rootView.findViewById(R.id.img_profilePhoto);
        Utils.setImage(chatGroupModel.getProfilePic(), R.drawable.img_user_placeholder, imgProfilePic);

        recyclerViewGroupChat = (RecyclerView) rootView.findViewById(R.id.recyclerView_chat);
        linearLayoutManager = new LinearLayoutManager(homeActivity, LinearLayoutManager.VERTICAL, false);
        recyclerViewGroupChat.setLayoutManager(linearLayoutManager);

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        inputText = (EditText) rootView.findViewById(R.id.edt_sendMsg);
        btnSend = (Button) rootView.findViewById(R.id.img_send);

        inputText.requestFocus();

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > Utils.dpToPx(200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    if (adpChat.getItemCount() > 0)
                        linearLayoutManager.scrollToPosition(adpChat.getItemCount() - 1);
                    //homeActivity.linFooterView.setVisibility(View.GONE);
                } else {
                    //homeActivity.linFooterView.setVisibility(View.VISIBLE);
                }
            }
        });

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (adpChat.getItemCount() > 0)
                    linearLayoutManager.scrollToPosition(adpChat.getItemCount() - 1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adpChat.getItemCount() > 0)
                    linearLayoutManager.scrollToPosition(adpChat.getItemCount() - 1);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adpChat.getItemCount() > 0)
                    linearLayoutManager.scrollToPosition(adpChat.getItemCount() - 1);
                String str = inputText.getText().toString().trim();

                if (str.isEmpty()) {
                    btnSend.setEnabled(false);
                } else {
                    btnSend.setEnabled(true);
                }
            }
        });

        adpChat = new AdpOneToOneChat(databaseReferenceGroup, homeActivity);
        recyclerViewGroupChat.setAdapter(adpChat);

        adpChat.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adpChat.getItemCount() > 0)
                    linearLayoutManager.scrollToPosition(adpChat.getItemCount() - 1);
            }
        });

        tvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.buttonClickEffect(view);
                bundle = new Bundle();
                bundle.putSerializable(Constants.CHAT_GROUP_DATA, chatGroupModel);

                homeActivity.pushFragment(new GroupMemberFragment(), true, false, bundle);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.setupOutSideTouchHideKeyboard(rootView);
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

            case R.id.img_send:
                Utils.buttonClickEffect(view);
                sendMsg();
                break;

            case R.id.tv_author:
                ChatModel chatModel = (ChatModel) view.getTag();
                if (chatModel != null) {
                    LoginUserModel loginUserModel = new LoginUserModel();
                    loginUserModel.setClientId(chatModel.getClientId());
                    bundle = new Bundle();
                    bundle.putSerializable(Constants.USER_DATA, loginUserModel);

                    homeActivity.pushFragment(new OtherPersonProfileFragment(), true, false, bundle);
                }
                break;
        }
    }

    private void sendMsg() {

        EditText inputText = (EditText) getActivity().findViewById(R.id.edt_sendMsg);
        String msg = inputText.getText().toString().trim();

        if (!msg.equals("")) {
            // Create our 'model', a Chat object
            ChatModel userChat = new ChatModel(loginUserModel.getClientId(), loginUserModel.getFirstName(),
                    msg, Utils.dateFormat(System.currentTimeMillis(), Constants.MM_DD_YYYY_HH_MM_SS_A), 1);
            // ChatModel otherChat = new ChatModel(specificCategoryChatListModel.getClientId(), specificCategoryChatListModel.getFirstName(), input, Utils.dateFormat(System.currentTimeMillis(), Constants.MM_DD_YYYY_HH_MM_SS_A));
            // Create a new, auto-generated child of that chat location, and save our chat data there
            databaseReferenceGroup.push().setValue(userChat);
            //databaseReferenceTwo.push().setValue(userChat);
            inputText.setText("");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (chatGroupModel.getIsAdmin() == 1) {
            inflater.inflate(R.menu.group_chat_delete_menu, menu);
        } else {
            inflater.inflate(R.menu.group_chat_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menu_clearChat:
                databaseReferenceGroup.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            adpChat = new AdpOneToOneChat(databaseReferenceGroup, homeActivity);
                            //databaseReferenceOne.removeValue();
                            recyclerViewGroupChat.setAdapter(adpChat);
                        }
                    }
                });
                break;

            case R.id.menu_LeaveGroup:

                leaveGroup();
                break;

            case R.id.menu_deleteGroup:
                databaseReferenceGroup.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            deleteGroup();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteGroup() {

        Map<String, String> params = new HashMap<>();
        params.put("op", "ChatGroupMemberDelete");
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ChatGroupId", String.valueOf(chatGroupModel.getId()));
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.CHAT_GROUP_MEMBER_DELETE,
                true, RequestCode.ChatGroupMemberDelete, this);
    }

    private void leaveGroup() {

        Map<String, String> params = new HashMap<>();
        params.put("op", "ChatGroupMemberLeave");
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ChatGroupId", String.valueOf(chatGroupModel.getId()));
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));


        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.CHAT_GROUP_MEMBER_LEAVE,
                true, RequestCode.ChatGroupMemberLeave, this);

    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case ChatGroupMemberLeave:
                homeActivity.popBackFragment();
                break;

            case ChatGroupMemberDelete:
                homeActivity.popBackFragment();
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }
}
