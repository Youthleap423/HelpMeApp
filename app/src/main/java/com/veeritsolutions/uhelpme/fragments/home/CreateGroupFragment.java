package com.veeritsolutions.uhelpme.fragments.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.adapters.AdpContacts;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.enums.ImageUpload;
import com.veeritsolutions.uhelpme.fragments.profile.OtherPersonProfileFragment;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.ChatGroupModel;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VEER7 on 7/1/2017.
 */

public class CreateGroupFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerViewContacts;
    private FloatingActionButton fabCreateGroup;
    private EditText edtSearchUser;
    private ImageView imgSearch, imgGroupPhoto;


    private HomeActivity homeActivity;
    private AdpContacts adpContacts;
    private ArrayList<LoginUserModel> contactList;
    // private int totalPages;
    private Map<String, String> params;
    private List<LoginUserModel> loginUserList;
    private LoginUserModel loginUserModel;
    private String searchText = "";
    private Dialog mDialog;
    private List<String> permissionList;
    private String image64Base = "";
    private ChatUsersListModel chatUsersListModel;
    private ArrayList<LoginUserModel> groupMemberList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            chatUsersListModel = (ChatUsersListModel) bundle.getSerializable(Constants.CHAT_GROUP_DATA);
            groupMemberList = (ArrayList<LoginUserModel>) bundle.getSerializable(Constants.CHAT_GROUP_MEMBER_DATA);
        }

        homeActivity = (HomeActivity) getActivity();
        permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.CAMERA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_create_group, container, false);
        recyclerViewContacts = (RecyclerView) rootView.findViewById(R.id.recyclerView_contacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(homeActivity, LinearLayoutManager.VERTICAL, false));
        fabCreateGroup = (FloatingActionButton) rootView.findViewById(R.id.fab_createGroup);

        imgSearch = (ImageView) rootView.findViewById(R.id.img_search);

        edtSearchUser = (EditText) rootView.findViewById(R.id.edt_searchUser);
        edtSearchUser.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        edtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchText = edtSearchUser.getText().toString().trim();

                if (searchText.isEmpty()) {
                    imgSearch.setVisibility(View.GONE);
                    /*contactList.clear();
                    adpContacts.refreshList(contactList);
                    getNormalUserData("", false);*/
                } else {
                    imgSearch.setVisibility(View.VISIBLE);
                }
            }
        });
        contactList = new ArrayList<>();
        loginUserList = new ArrayList<>();
        loginUserModel = LoginUserModel.getLoginUserModel();
        getNormalUserData("", true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                LoginUserModel loginUser = (LoginUserModel) view.getTag();

                if (loginUser != null) {

                    if (loginUser.isSelected()) {
                        loginUser.setSelected(false);
                        if (loginUserList.size() > 0) {
                            loginUserList.remove(loginUser);
                        }
                    } else {
                        loginUser.setSelected(true);
                        loginUserList.add(loginUser);
                    }

                    contactList.set(loginUser.getPosition(), loginUser);
                    adpContacts.refreshList(contactList);

                    if (loginUserList.size() > 0) {
                        fabCreateGroup.setVisibility(View.VISIBLE);
                    } else {
                        fabCreateGroup.setVisibility(View.GONE);
                    }
                }
                break;

            case R.id.fab_createGroup:
                Utils.buttonClickEffect(view);
                if (chatUsersListModel == null) {
                    showCreateGroupDialog(homeActivity, true);
                } else {
                    insertExistGroupMember();
                }

                break;

            case R.id.img_profilePhoto:
                Utils.buttonClickEffect(view);
                Object o = view.getTag(R.id.img_profilePhoto);

                if (o != null) {
                    loginUserModel = (LoginUserModel) o;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.USER_DATA, loginUserModel);
                    homeActivity.pushFragment(new OtherPersonProfileFragment(), true, false, bundle);
                }
                break;

            case R.id.img_search:
                Utils.buttonClickEffect(view);
                contactList.clear();
                adpContacts.refreshList(contactList);
                getNormalUserData(searchText, true);
                break;
        }
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetAllUsers:

                ArrayList<LoginUserModel> list = (ArrayList<LoginUserModel>) mObject;

                if (list != null && !list.isEmpty()) {

                    contactList.addAll(list);
                    LoginUserModel loginUser = contactList.get(0);
                    // totalPages = loginUser.getTotalPage();
                    adpContacts = (AdpContacts) recyclerViewContacts.getAdapter();
                    if (adpContacts != null && adpContacts.getItemCount() > 0) {
                        adpContacts.refreshList(contactList);
                    } else {
                        adpContacts = new AdpContacts(homeActivity, contactList, true);
                        recyclerViewContacts.setAdapter(adpContacts);
                    }
                    //adpContacts.refreshList(postedJobList);
                }
                break;

            case CreateGroup:

                ChatGroupModel chatGroupModel = (ChatGroupModel) mObject;
                insertGroupMember(chatGroupModel);
                break;

            case ChatGroupMemberInsert:

                CustomDialog.getInstance().dismiss();
                // ChatGroupMemberModel chatGroupMemberModel = (ChatGroupMemberModel) mObject;
                homeActivity.popBackFragment();
                break;
        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }


    private void getNormalUserData(String searchUser, boolean isDialogRequired) {

        params = new HashMap<>();
        params.put("op", ApiList.GET_ALL_USERS);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("SearchBy", searchUser);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_ALL_USERS,
                isDialogRequired, RequestCode.GetAllUsers, this);
    }


    private void insertExistGroupMember() {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < loginUserList.size(); i++) {

            for (int j = 0; j < groupMemberList.size(); j++) {
                if (groupMemberList.get(j).getClientId() != loginUserList.get(i).getClientId()) {
                    if (i == loginUserList.size() - 1) {
                        stringBuilder.append(loginUserList.get(i).getClientId());
                    } else {
                        stringBuilder.append(loginUserList.get(i).getClientId()).append(",");
                    }
                    break;
                }
            }
            // stringBuilder.append(loginUserList.get(i).getClientId()).append(",");
        }
        //  stringBuilder.append(loginUserModel.getClientId());
        Debug.trace("userList", stringBuilder.toString());

        params = new HashMap<>();
        params.put("op", ApiList.CHAT_GROUP_MEMBER_INSERT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ChatGroupId", String.valueOf(chatUsersListModel.getId()));
        params.put("ClientId", stringBuilder.toString());
        params.put("AdminClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.CHAT_GROUP_MEMBER_INSERT, false, RequestCode.ChatGroupMemberInsert, this);
    }

    private void insertGroup(String groupName, Dialog mDialog) {

        if (groupName.isEmpty()) {
            ToastHelper.getInstance().showMessage(getString(R.string.type_group_subject));
            return;
        }
        mDialog.dismiss();
        CustomDialog.getInstance().showProgress(homeActivity, "", false);
        params = new HashMap<>();
        params.put("op", ApiList.CHAT_GROUP_INSERT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ChatGroupId", String.valueOf(0));
        params.put("ChatGroupName", groupName);
        params.put("ChatGroupPic", image64Base);

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.CHAT_GROUP_INSERT, false, RequestCode.CreateGroup, this);
    }


    private void insertGroupMember(ChatGroupModel chatGroupModel) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < loginUserList.size(); i++) {

            if (i == loginUserList.size() - 1) {
                stringBuilder.append(loginUserList.get(i).getClientId());
            } else {
                stringBuilder.append(loginUserList.get(i).getClientId()).append(",");
            }
            //stringBuilder.append(loginUserList.get(i).getClientId()).append(",");
        }
        // stringBuilder.append(loginUserModel.getClientId());

        Debug.trace("userList", stringBuilder.toString());

        params = new HashMap<>();
        params.put("op", ApiList.CHAT_GROUP_MEMBER_INSERT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ChatGroupId", String.valueOf(chatGroupModel.getChatGroupId()));
        params.put("ClientId", stringBuilder.toString());
        params.put("AdminClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.CHAT_GROUP_MEMBER_INSERT, false, RequestCode.ChatGroupMemberInsert, this);
    }

    public void showImageSelect(Context mContext, String mTitle, boolean mIsCancelable) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View dataView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_select_image, null, false);
        mDialog.setContentView(R.layout.custom_dialog_select_image);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);

        TextView tvTitle, tvCamera, tvGallery;

        tvTitle = (TextView) mDialog.findViewById(R.id.tv_selectImageTitle);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        tvTitle.setText(mTitle);

        tvCamera = (TextView) mDialog.findViewById(R.id.tv_camera);
        tvCamera.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvGallery = (TextView) mDialog.findViewById(R.id.tv_gallery);
        tvGallery.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        LinearLayout linCamera, linGallery;

        linCamera = (LinearLayout) mDialog.findViewById(R.id.lin_camera);
        linGallery = (LinearLayout) mDialog.findViewById(R.id.lin_gallery);

        linCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constants.REQUEST_CAMERA_PROFILE);
            }
        });

        linGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        Constants.REQUEST_FILE_PROFILE);
            }
        });
        ImageView imgCancel = (ImageView) mDialog.findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        try {
            if (mDialog != null) {
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (mDialog != null) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCreateGroupDialog(Context mContext, boolean mIsCancelable) {

        final Dialog mDialog = new Dialog(mContext, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View dataView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_alert, null, false);
        mDialog.setContentView(R.layout.custom_dialog_create_group);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        //mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView mDialogTitle = (TextView) mDialog.findViewById(R.id.tv_dialogHeader);
        mDialogTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        final EditText edtEnterOffer = (EditText) mDialog.findViewById(R.id.edt_groupName);
        //TextView tvOk = (TextView) mDialog.findViewById(R.id.tv_ok);
        edtEnterOffer.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        imgGroupPhoto = (ImageView) mDialog.findViewById(R.id.img_groupPhoto);

        imgGroupPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.buttonClickEffect(view);
                //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), false);

                if (Build.VERSION.SDK_INT > 22) {
                    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    //if (shouldShowRequestPermissionRationale(permissions))
                    requestPermissions(permissions, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA);
                } else {
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setRequestedSize(400, 400)
                            .start(getContext(), CreateGroupFragment.this);
                    // showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                }
            }
        });
//        Button btnCancel = (Button) mDialog.findViewById(R.id.btn_actionCancel);
//        btnCancel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        final Button btnOk = (Button) mDialog.findViewById(R.id.btn_createGroup);
        btnOk.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        //btnOk.setTag(edtEnterOffer.getText().toString().trim());
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertGroup(edtEnterOffer.getText().toString().trim(), mDialog);
            }
        });
        mDialog.setCancelable(mIsCancelable);

        //if (!isDialogShowing()) {
        mDialog.show();
        // }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case Constants.REQUEST_CAMERA_PROFILE:

                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    Uri selectedImageUri = Utils.getImageUri(getActivity(), thumbnail);
                    beginCrop(selectedImageUri);
                    break;

                case Constants.REQUEST_FILE_PROFILE:

                    selectedImageUri = data.getData();
                    beginCrop(selectedImageUri);
                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    handleCrop(0, result.getUri().getPath());
                    break;

            }
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
       /* Crop.of(source, destination)
                .withAspect(200, 200)
                .start(getActivity(), this);*/
    }

    private void handleCrop(int resultCode, String data) {

        image64Base = Utils.getStringImage(data, ImageUpload.ClientProfile);
        imgGroupPhoto.setVisibility(View.VISIBLE);
        //imgGroupPhoto.setImageURI(null);
        //imgGroupPhoto.setImageURI(data);
        Utils.setImage(data, R.drawable.img_user_placeholder, imgGroupPhoto);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // List<String> shouldPermit = new ArrayList<>();

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA) {

            if (grantResults.length > 0 || grantResults.length != 0) {

                if (PermissionClass.verifyPermission(grantResults)) {
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setRequestedSize(400, 400)
                            .start(getContext(), CreateGroupFragment.this);
                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                } else {
                    ToastHelper.getInstance().showMessage(getString(R.string.str_allow_required_permission));
                }
            }
        }
    }
}
