package com.veeritsolutions.uhelpme.api;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veeritsolutions.uhelpme.models.ARViewModel;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;
import com.veeritsolutions.uhelpme.models.CategoryModel;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.CityModel;
import com.veeritsolutions.uhelpme.models.CountryModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.Packages;
import com.veeritsolutions.uhelpme.models.PaymentModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.models.ProductRedeem;
import com.veeritsolutions.uhelpme.models.ReviewModel;
import com.veeritsolutions.uhelpme.models.StateModel;
import com.veeritsolutions.uhelpme.models.SubscriptionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ${hitesh} on 12/8/2016.
 */

public class ResponseManager {

    public static <T> Object parseResponse(String mResponse, RequestCode mRequestCode, Gson mGson) {

        Object object = null;

        try {

            JSONObject jsonObject = new JSONObject(mResponse);

            switch (mRequestCode) {

                case clientInsert:

                    object = mGson.fromJson(jsonObject.getJSONObject("ClientInsert").toString(), mRequestCode.getLocalClass());

                    LoginUserModel.setLoginCredentials(jsonObject.getJSONObject("ClientInsert").toString());
                    //PrefHelper.getInstance().setString(PrefHelper.CLIENT_CREDENTIALS, jsonObject.getJSONObject("ClientInsert").toString());
                    break;

                case GetUser:
                    object = mGson.fromJson(jsonObject.getJSONObject("GetUser").toString(), mRequestCode.getLocalClass());
                    LoginUserModel.setLoginCredentials(jsonObject.getJSONObject("GetUser").toString());
                    //  PrefHelper.getInstance().setString(PrefHelper.CLIENT_CREDENTIALS, jsonObject.getJSONObject("GetUser").toString());
                    break;

                case GetCategory:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetCategory").toString(), new TypeToken<ArrayList<CategoryModel>>() {
                    }.getType());

                    //object = mGson.fromJson(jsonObject.getJSONArray("GetCategory").toString(), mRequestCode.getLocalClass());
                    break;

                case JobPostInsert:

                    object = mGson.fromJson(jsonObject.getJSONObject("JobPostInsert").toString(), mRequestCode.getLocalClass());
                    break;

                case GetPostedJob:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetJobPost_Helper").toString(), new TypeToken<ArrayList<PostedJobModel>>() {
                    }.getType());

                    break;

                case GetPostedJobDetail:

                    object = mGson.fromJson(jsonObject.getJSONObject("GetJobPostDetail").toString(), mRequestCode.getLocalClass());

                    break;

                case GetClientInfo:

                    object = mGson.fromJson(jsonObject.getJSONObject("GetClientInfo").toString(), mRequestCode.getLocalClass());
                    break;

                case ClientUpdate:

                    object = mGson.fromJson(jsonObject.getJSONObject("ClientUpdate").toString(), mRequestCode.getLocalClass());
                    LoginUserModel.setLoginCredentials(jsonObject.getJSONObject("ClientUpdate").toString());
                    // PrefHelper.getInstance().setString(PrefHelper.CLIENT_CREDENTIALS, jsonObject.getJSONObject("ClientUpdate").toString());
                    break;

                case JobPostViewInsert:

                    object = jsonObject.getJSONObject("JobPostViewInsert").toString();/*mGson.fromJson(jsonObject.getJSONObject("JobPostViewInsert").toString(), mRequestCode.getLocalClass());*/
                    break;

                case JobPostOfferInsert:

                    object = jsonObject.getJSONObject("JobPostOfferInsert").toString();/* mGson.fromJson(jsonObject.getJSONObject("JobPostOfferInsert").toString(), mRequestCode.getLocalClass());*/
                    break;

                case GetAllUsers:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetUser").toString(), new TypeToken<ArrayList<LoginUserModel>>() {
                    }.getType());
                    break;

                case GetJobPostHelpSeeker:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetJobPost_HelpSeeker").toString(), new TypeToken<ArrayList<PostedJobModel>>() {
                    }.getType());
                    break;

                case CreateGroup:

                    object = mGson.fromJson(jsonObject.getJSONObject("ChatGroupInsert").toString(), mRequestCode.getLocalClass());
                    break;

                case GetChatUserList:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetChatUser").toString(), new TypeToken<ArrayList<ChatUsersListModel>>() {
                    }.getType());
                    break;

                case GetJobPostMyOffers:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetJobPost_MyOffers").toString(), new TypeToken<ArrayList<PostedJobModel>>() {
                    }.getType());
                    break;

                case GetSpecificCategoryChat:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetJobPost_AllPostOffers").toString(), new TypeToken<ArrayList<AllHelpOfferModel>>() {
                    }.getType());
                    break;

                case GetAllHelpOffer:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetJobPost_AllPostOffers").toString(), new TypeToken<ArrayList<AllHelpOfferModel>>() {
                    }.getType());
                    break;

                case GetGroupMember:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetChatGroupMember").toString(), new TypeToken<ArrayList<LoginUserModel>>() {
                    }.getType());
                    break;

                case GetCountry:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetCountry").toString(), new TypeToken<ArrayList<CountryModel>>() {
                    }.getType());
                    break;

                case GetState:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetState").toString(), new TypeToken<ArrayList<StateModel>>() {
                    }.getType());
                    break;

                case GetCity:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetCity").toString(), new TypeToken<ArrayList<CityModel>>() {
                    }.getType());
                    break;

                case ClientProfilePicUpdate:

                    object = mGson.fromJson(jsonObject.getJSONObject("ClientProfilePicUpdate").toString(), mRequestCode.getLocalClass());
                    break;

                case JobPostDecline:
                    object = jsonObject.getJSONObject("JobPostDeclineInsert");
                    break;

                case JobpostRejected:
                    object = jsonObject.getJSONObject("JobPost_RejectOffer");
                    break;

                case GetPackage:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetPackage").toString(), new TypeToken<ArrayList<Packages>>() {
                    }.getType());
                    break;

                case GetReview:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetReview").toString(), new TypeToken<ArrayList<ReviewModel>>() {
                    }.getType());
                    break;

                case ChatUserInsert:

                    object = jsonObject.getJSONObject("ChatUserInsert").toString();
                    break;

                case GetClientPayment:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetClientPaymentType").toString(), new TypeToken<ArrayList<PaymentModel>>() {
                    }.getType());
                    break;

                case SetClientPayment:

                    object = mGson.fromJson(jsonObject.getJSONObject("SetClientPaymentType").toString(), mRequestCode.getLocalClass());
                    break;

                case ClientChangePassword:
                    object = jsonObject.getJSONObject("ChatUserInsert").toString();
                    break;

                case SetClientAppFeedback:
                    object = jsonObject.getJSONObject("ChatUserInsert").toString();
                    break;

                case GetSubscription:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetSubscription").toString(), new TypeToken<ArrayList<SubscriptionModel>>() {
                    }.getType());
                    break;

                case SubscriptionInsert:

                    object = mGson.fromJson(jsonObject.getJSONArray("SubscriptionInsert").toString(), mRequestCode.getLocalClass());
                    break;

                case JobPostUpdate:
                    object = jsonObject.getJSONArray("JobPostUpdate").toString();
                    break;

                case JobPostOfferUpdate:
                    object = jsonObject.getJSONArray("JobPostOfferUpdate").toString();
                    break;

                case GetProductRedeem:

                    object = mGson.fromJson(jsonObject.getJSONArray("GetProductRedeem").toString(), new TypeToken<ArrayList<ProductRedeem>>() {
                    }.getType());
                    break;

                case GetProduct:
                    object = mGson.fromJson(jsonObject.getJSONArray("GetProduct").toString(), new TypeToken<ArrayList<ProductRedeem>>() {
                    }.getType());
                    break;

                case ProductRedeemInsert:
                    object = jsonObject.getJSONObject("ProductRedeemInsert").toString();
                    break;

                case SetClientLocation:
                    object = jsonObject.getJSONObject("SetClientLocation").toString();
                    break;

                case GetARView:
                    object = mGson.fromJson(jsonObject.getJSONArray("GetARView").toString(), new TypeToken<ArrayList<ARViewModel>>() {
                    }.getType());
                    break;
                case AcceptOffer:

                    object = jsonObject.getJSONObject("SetClientLocation").toString();
                    //object = mGson.fromJson(jsonObject.getJSONArray("JobPost_AcceptOffer").toString(), mRequestCode.getLocalClass());
                    break;

                case FinishOffer:

                    object = jsonObject.getJSONObject("JobPost_FinishOffer").toString();
                    //object = mGson.fromJson(jsonObject.getJSONArray("JobPost_AcceptOffer").toString(), mRequestCode.getLocalClass());
                    break;

                case ReviewInsert:

                    object = mGson.fromJson(jsonObject.getJSONArray("ReviewInsert").toString(), mRequestCode.getLocalClass());
                    break;

                case SetClientTokenId:

                    object = mGson.fromJson(jsonObject.getJSONObject("SetClientTokenId").toString(), mRequestCode.getLocalClass());
                    break;

                case GetAboutUs:

                    object = mGson.fromJson(jsonObject.getJSONObject("GetAboutUs").toString(), mRequestCode.getLocalClass());
                    break;

                case DoStripePayment:

                    object = mGson.fromJson(jsonObject.getJSONObject("DoStripePayment").toString(), mRequestCode.getLocalClass());
                    break;

                case ClientCategoryInsert:

                    object = jsonObject.getJSONObject("ClientCategoryInsert").toString();
                    break;

                case ClientRadiusUpdate:

                    object = jsonObject.getJSONObject("ClientProfilePicUpdate").toString();
                    break;

                case ClientWatchVideo:

                    object = mGson.fromJson(jsonObject.getJSONObject("ClientProfilePicUpdate").toString(), mRequestCode.getLocalClass());
                    break;

                case ForgotPassword:

                    object = mGson.fromJson(jsonObject.getJSONObject("ForgotPassword").toString(), mRequestCode.getLocalClass());
                    break;

                case GetClientLocation:

                    object = mGson.fromJson(jsonObject.getJSONObject("GetClientLocation").toString(), mRequestCode.getLocalClass());
                    break;

                case JobPostIssue:
                    object = jsonObject;
                    break;

                case JobPostOfferCancel:
                    object = jsonObject;
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
