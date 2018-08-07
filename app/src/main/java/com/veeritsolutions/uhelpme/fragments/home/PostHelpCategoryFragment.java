package com.veeritsolutions.uhelpme.fragments.home;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.adapters.AdpCategory;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.fragments.profile.PackagesFragment;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.CategoryModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Admin on 6/23/2017.
 */

public class PostHelpCategoryFragment extends Fragment implements DataObserver, OnBackPressedEvent, OnClickEvent {

    private View rootView;
    private TextView tvUhelpMe, tvCategory;
    private RecyclerView recyclerViewCategory;
    private Button btnNextStep;

    private Bundle bundle;
    private HomeActivity homeActivity;
    private Map<String, String> params;
    private AdpCategory adpCategory;
    private ArrayList<CategoryModel> categoryModelsList;
    private CategoryModel categoryModel;
    // private LoginUserModel loginUserModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        //  loginUserModel = LoginUserModel.getLoginUserModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_post_help_category, container, false);

        tvUhelpMe = (TextView) rootView.findViewById(R.id.txv_show);
        tvUhelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        String str = getString(R.string.view_a_u) + getString(R.string.font_color) + getString(R.string.helpme) + getString(R.string.font);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            tvUhelpMe.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));

        } else {
            tvUhelpMe.setText(Html.fromHtml(str));
        }

        tvCategory = (TextView) rootView.findViewById(R.id.tv_category);
        tvCategory.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        recyclerViewCategory = (RecyclerView) rootView.findViewById(R.id.recyclerView_category);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        btnNextStep = (Button) rootView.findViewById(R.id.btn_next_help);
        btnNextStep.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        return rootView;
        //  return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getCategoryData();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetCategory:

                categoryModelsList = (ArrayList<CategoryModel>) mObject;

                if (!categoryModelsList.isEmpty()) {

                    adpCategory = (AdpCategory) recyclerViewCategory.getAdapter();
                    if (adpCategory != null && adpCategory.getItemCount() > 0) {
                        adpCategory.notifyDataSetChanged();

                    } else {
                        adpCategory = new AdpCategory(homeActivity, categoryModelsList);
                        recyclerViewCategory.setAdapter(adpCategory);
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

            case R.id.btn_next_help:
                Utils.buttonClickEffect(view);
                if (categoryModel == null) {
                    ToastHelper.getInstance().showMessage(getString(R.string.select_category_for_help_post));
                } else {
                    bundle.putSerializable(Constants.CATEGORY_DATA, categoryModel);
                    homeActivity.pushFragment(new PostHelpMapFragment(), true, false, bundle);

                   /* if (categoryModel.getCategoryPoints() > loginUserModel.getPoints()) {
                        ToastHelper.getInstance().showMessage(getString(R.string.you_dont_have_enough_points_to_post_help));
                        showConfirmationDialog();
                    } else {
                        bundle.putSerializable(Constants.CATEGORY_DATA, categoryModel);
                        homeActivity.pushFragment(new PostHelpMapFragment(), true, false, bundle);
                    }*/
                }
                break;

            case R.id.lin_categoryList:
                Utils.buttonClickEffect(view);
                categoryModel = (CategoryModel) view.getTag();
                for (int i = 0; i < categoryModelsList.size(); i++) {
                    categoryModelsList.get(i).setSelected(false);
                    categoryModelsList.set(i, categoryModelsList.get(i));
                }
                if (categoryModel.isSelected()) {


                    categoryModel.setSelected(false);
                } else {

                    categoryModel.setSelected(true);
                }
                categoryModelsList.set(categoryModel.getPosition(), categoryModel);
                adpCategory.refreshList(categoryModelsList);
                break;
        }
    }

    private void showConfirmationDialog() {
        final AlertDialog.Builder[] builder = new AlertDialog.Builder[1];
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder[0] = new AlertDialog.Builder(homeActivity, R.style.dialogStyle);
        //  } else {
        //      builder = new AlertDialog.Builder(profileActivity);
        //  }
        // builder.create();
        builder[0].setTitle(getString(R.string.purchase_credit_points));
        builder[0].setMessage(R.string.are_you_want_to_but_credit_points);
        builder[0].setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                homeActivity.pushFragment(new PackagesFragment(), true, false, null);
            }
        });
        builder[0].setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder[0].show();
    }

    private void getCategoryData() {

        params = new HashMap<>();
        params.put("op", "GetCategory");
        params.put("AuthKey", ApiList.AUTH_KEY);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CATEGORY,
                true, RequestCode.GetCategory, this);
    }

}
