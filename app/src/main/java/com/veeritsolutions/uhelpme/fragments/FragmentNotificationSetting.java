package com.veeritsolutions.uhelpme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.maps.SupportMapFragment;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.adapters.AdpCategory;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.CategoryModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/29/2017.
 */

public class FragmentNotificationSetting extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerViewCategory;
    private ProfileActivity homeActivity;

    //  private Spinner spcategory;

    private Map<String, String> params;
    //  private Bundle bundle;
    private CategoryModel categoryModel;

    // private List<String> CategoryList;
    //  private StringBuilder stringBuilder;

    // private SpinnerAdapter adpCategory;

    private String categoryId = "";

    private SupportMapFragment spFragment;
    private LoginUserModel loginUserModel;
    // private View view;
    // private ArrayList<CategoryModel> categoryModelsContent;
    private SeekBar seekBar;
    private TextView tvDistance;

    private AdpCategory adpCategory;
    private ArrayList<CategoryModel> categoryModelsList;
    private ArrayList<String> category = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (ProfileActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_notification_setting, container, false);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekBar.setProgress((int) loginUserModel.getRadious());
        //  spcategory = (Spinner) rootView.findViewById(R.id.sp_category);
        //  CategoryList = new ArrayList<>();
        tvDistance = (TextView) rootView.findViewById(R.id.tv_distance);
        tvDistance.setText(loginUserModel.getRadious() + " km");
        recyclerViewCategory = (RecyclerView) rootView.findViewById(R.id.recyclerView_category);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                tvDistance.setText("");
                tvDistance.setText(tvDistance.getText().toString() + " " + progress + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // stringBuilder = new StringBuilder();
        GetCategory();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case GetCategory:
                rootView.setVisibility(View.VISIBLE);
                categoryModelsList = (ArrayList<CategoryModel>) mObject;


                if (categoryModelsList != null && !categoryModelsList.isEmpty()) {

                    if (PrefHelper.getInstance().containKey(PrefHelper.CATEGORY_ID)) {
                        for (int i = 0; i < categoryModelsList.size(); i++) {
                            CategoryModel categoryModel = categoryModelsList.get(i);

                            // if (PrefHelper.getInstance().containKey(PrefHelper.CATEGORY_ID)) {
                            String catId = PrefHelper.getInstance().getString(PrefHelper.CATEGORY_ID, "");
                            if (catId.length() > 0) {
                                String[] cateId = catId.split(",");
                                for (String aCateId : cateId) {
                                    if (aCateId.equals(String.valueOf(categoryModel.getCategoryId()))) {
                                        category.add(String.valueOf(categoryModel.getCategoryId()));
                                        categoryModel.setSelected(true);
                                        categoryModelsList.set(i, categoryModel);
                                    }
                                }
                            }
                            //}
                        }
                    }
                    adpCategory = (AdpCategory) recyclerViewCategory.getAdapter();
                    if (adpCategory != null && adpCategory.getItemCount() > 0) {
                        adpCategory.notifyDataSetChanged();

                    } else {
                        adpCategory = new AdpCategory(homeActivity, categoryModelsList);
                        recyclerViewCategory.setAdapter(adpCategory);
                    }
                    categoryId = Utils.mytoString(category, ",");
                }
                //  setSpinnerData(mObject);
                break;

            case ClientRadiusUpdate:
                insertClientCategory();
                break;

            case ClientCategoryInsert:
                ToastHelper.getInstance().showMessage("Updated");
                loginUserModel.setRadious(seekBar.getProgress());
                PrefHelper.getInstance().setString(PrefHelper.CATEGORY_ID, categoryId);
                LoginUserModel.setLoginCredentials(RestClient.getGsonInstance().toJson(loginUserModel));
                homeActivity.popBackFragment();
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

            case R.id.btn_update:
                Utils.buttonClickEffect(view);
              /*  if (tvDistance.getText().toString().trim().equals("0")) {
                    ToastHelper.getInstance().showMessage("Select valid radius distance");
                    return;
                }*/
                if (categoryId.length() == 0) {

                    StringBuilder sb = new StringBuilder();

                    for (int i = 1; i <= categoryModelsList.size(); i++) {
                        if (i > 1) {
                            sb.append(",");
                        }
                        String item = String.valueOf(i);
                        sb.append(item);
                    }
                    categoryId = sb.toString();
                }
                insertClientRadius();
                break;

            case R.id.lin_categoryList:

                Utils.buttonClickEffect(view);
                categoryModel = (CategoryModel) view.getTag();

                if (!categoryModel.isSelected()) {

                    categoryModel.setSelected(true);
                    category.add(String.valueOf(categoryModel.getCategoryId()));

                } else {
                    categoryModel.setSelected(false);
                    category.remove(String.valueOf(categoryModel.getCategoryId()));
                }

                categoryModelsList.set(categoryModel.getPosition(), categoryModel);
                adpCategory.refreshList(categoryModelsList);
                categoryId = Utils.mytoString(category, ",");
                break;
        }
    }

    private void insertClientRadius() {
        params = new HashMap<>();
        params.put("op", ApiList.CLIENT_RADIUS_UPDATE);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("Radious", String.valueOf(seekBar.getProgress()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.CLIENT_RADIUS_UPDATE,
                true, RequestCode.ClientRadiusUpdate, this);
    }

    private void insertClientCategory() {

        params = new HashMap<>();
        params.put("op", ApiList.CLIENT_CATEGORY_INSERT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("CategoryId", categoryId);

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.CLIENT_CATEGORY_INSERT,
                true, RequestCode.ClientCategoryInsert, this);
    }

    private void GetCategory() {

        try {
            //params = new JSONObject();
            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.GET_CATEGORY);
            params.put("AuthKey", ApiList.AUTH_KEY);


            RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_CATEGORY,
                    true, RequestCode.GetCategory, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

