package com.veeritsolutions.uhelpme.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.veeritsolutions.uhelpme.adapters.AdpOneToOneChat;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
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
 * Created by Admin on 8/3/2017.
 */

public class OneToOneChatActivity extends AppCompatActivity implements OnClickEvent, OnBackPressedEvent, DataObserver {
    // private View rootView;
    private RecyclerView recyclerViewChat;
    private EditText inputText;
    private Button btnSend;
    private TextView tvHeader;
    private ImageView imgProfilePic;

    // private HomeActivity homeActivity;
    private AdpOneToOneChat adpChat;
    private ChatUsersListModel specificCategoryChatListModel;
    // private Bundle bundle;
    private LoginUserModel loginUserModel;

    // Firebase variables and objects
    // private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceOne, databaseReferenceTwo;
    private View rootView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        loginUserModel = LoginUserModel.getLoginUserModel();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            specificCategoryChatListModel = (ChatUsersListModel) getIntent().getSerializableExtra(Constants.CHAT_DATA);
        }

        // firebaseDatabase = FirebaseDatabase.getInstance(ServerConfig.FCM_APP_URL);
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReferenceOne = MyApplication.getInstance().getFirebaseDatabase().getReference().child(String.valueOf(loginUserModel.getClientId() + "_" + specificCategoryChatListModel.getId()));
        databaseReferenceTwo = MyApplication.getInstance().getFirebaseDatabase().getReference().child(String.valueOf(specificCategoryChatListModel.getId() + "_" + loginUserModel.getClientId()));

        setContentView(R.layout.fragment_one_to_one_chat);
        init();

    }

    private void init() {

        tvHeader = (TextView) findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
        tvHeader.setText(specificCategoryChatListModel.getName());

        imgProfilePic = (ImageView) findViewById(R.id.img_profilePhoto);
        Utils.setImage(specificCategoryChatListModel.getProfilePic(), R.drawable.img_user_placeholder, imgProfilePic);

        recyclerViewChat = (RecyclerView) findViewById(R.id.recyclerView_chat);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewChat.setLayoutManager(linearLayoutManager);
        // Setup our input methods. Enter key on the keyboard or pushing the send button
        inputText = (EditText) findViewById(R.id.edt_sendMsg);
        btnSend = (Button) findViewById(R.id.img_send);

        inputText.requestFocus();
        rootView = LayoutInflater.from(this).inflate(R.layout.fragment_one_to_one_chat, null, false);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > Utils.dpToPx(200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    if (adpChat.getItemCount() > 0)
                        linearLayoutManager.scrollToPosition(adpChat.getItemCount() - 1);
                    // homeActivity.linFooterView.setVisibility(View.GONE);
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

        adpChat = new AdpOneToOneChat(databaseReferenceOne, this);
        recyclerViewChat.setAdapter(adpChat);
        recyclerViewChat.setAddStatesFromChildren(true);

        adpChat.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adpChat.getItemCount() > 0)
                linearLayoutManager.scrollToPosition(adpChat.getItemCount() - 1);
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case ChatUserInsert:
               /* PrefHelper.getInstance().setString(
                        loginUserModel.getClientId() + "_" + specificCategoryChatListModel.getId(),
                        loginUserModel.getClientId() + "_" + specificCategoryChatListModel.getId());*/
                break;

            case ChatUserDelete:
                finish();
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                // homeActivity.popBackFragment();
                finish();
                break;

            case R.id.img_send:
                Utils.buttonClickEffect(view);
                sendMsg();
                break;
        }
    }

    private void sendMsg() {

        EditText inputText = (EditText) this.findViewById(R.id.edt_sendMsg);
        String msg = inputText.getText().toString().trim();

        if (!msg.equals("")) {
            // Create our 'model', a Chat object
            ChatModel userChat = new ChatModel(loginUserModel.getClientId(), loginUserModel.getFirstName(),
                    msg, Utils.dateFormat(System.currentTimeMillis(), Constants.MM_DD_YYYY_HH_MM_SS_A), 0);
            // ChatModel otherChat = new ChatModel(specificCategoryChatListModel.getClientId(), specificCategoryChatListModel.getFirstName(), input, Utils.dateFormat(System.currentTimeMillis(), Constants.MM_DD_YYYY_HH_MM_SS_A));
            // Create a new, auto-generated child of that chat location, and save our chat data there
            databaseReferenceOne.push().setValue(userChat);
            databaseReferenceTwo.push().setValue(userChat);
            inputText.setText("");


            String str = loginUserModel.getClientId() + "_" + specificCategoryChatListModel.getId();

            // if (!PrefHelper.getInstance().containKey(str)) {

            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.CHAT_USER_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("ToClientId", String.valueOf(specificCategoryChatListModel.getId()));

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.CHAT_USER_INSERT,
                    false, RequestCode.ChatUserInsert, this);
            // }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.one_to_one_chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_clearChat:
                databaseReferenceOne.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            adpChat = new AdpOneToOneChat(databaseReferenceOne, OneToOneChatActivity.this);
                            //databaseReferenceOne.removeValue();
                            recyclerViewChat.setAdapter(adpChat);
                        }
                    }
                });
                break;

            case R.id.menu_deleteChat:

                databaseReferenceOne.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            deleteChat();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteChat() {

        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.CHAT_USER_DELETE);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("ToClientId", String.valueOf(specificCategoryChatListModel.getId()));

        RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.CHAT_USER_DELETE,
                true, RequestCode.ChatUserDelete, this);
    }
}
