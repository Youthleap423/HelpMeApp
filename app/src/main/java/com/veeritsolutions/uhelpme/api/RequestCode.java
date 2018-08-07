package com.veeritsolutions.uhelpme.api;


import com.veeritsolutions.uhelpme.models.ARViewModel;
import com.veeritsolutions.uhelpme.models.AboutUsModel;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;
import com.veeritsolutions.uhelpme.models.CategoryModel;
import com.veeritsolutions.uhelpme.models.ChatGroupMemberModel;
import com.veeritsolutions.uhelpme.models.ChatGroupModel;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.CityModel;
import com.veeritsolutions.uhelpme.models.CountryModel;
import com.veeritsolutions.uhelpme.models.DeviceTokenModel;
import com.veeritsolutions.uhelpme.models.LocationModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.Packages;
import com.veeritsolutions.uhelpme.models.PaymentModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.models.ProductModel;
import com.veeritsolutions.uhelpme.models.ProductRedeem;
import com.veeritsolutions.uhelpme.models.ReviewModel;
import com.veeritsolutions.uhelpme.models.StateModel;
import com.veeritsolutions.uhelpme.models.StripeModel;

/**
 * Created by ${hitesh} on 12/7/2016.
 */

public enum RequestCode {

    clientInsert(LoginUserModel.class),
    GetUser(LoginUserModel.class),
    GetCategory(CategoryModel[].class),
    JobPostInsert(PostedJobModel.class),
    GetPostedJob(PostedJobModel[].class),
    GetPostedJobDetail(PostedJobModel.class),
    GetClientInfo(LoginUserModel.class),
    ClientUpdate(LoginUserModel.class),
    JobPostViewInsert(null),
    JobPostOfferInsert(null),
    GetAllUsers(LoginUserModel[].class),
    GetJobPostHelpSeeker(PostedJobModel.class),
    CreateGroup(ChatGroupModel.class),
    ChatGroupMemberInsert(ChatGroupMemberModel.class),
    GetChatUserList(ChatUsersListModel[].class),
    GetJobPostMyOffers(PostedJobModel[].class),
    GetSpecificCategoryChat(AllHelpOfferModel[].class),
    GetAllHelpOffer(AllHelpOfferModel[].class),
    GetGroupMember(null),
    GetCountry(CountryModel[].class),
    GetState(StateModel[].class),
    GetCity(CityModel[].class),
    ClientProfilePicUpdate(LoginUserModel.class),
    ClientLegalDocumentUpdate(LoginUserModel.class),
    JobPostDecline(null),
    JobpostRejected(null),
    GetPackage(Packages[].class),
    GetReview(ReviewModel[].class),
    ChatUserInsert(null),
    GetClientPayment(PaymentModel[].class),
    SetClientPayment(PaymentModel.class),
    ClientChangePassword(null),
    SetClientAppFeedback(null),
    GetSubscription(Packages[].class),
    SubscriptionInsert(null),
    JobPostUpdate(null),
    JobPostOfferUpdate(null),
    GetProductRedeem(ProductRedeem[].class),
    GetProduct(ProductModel[].class),
    ProductRedeemInsert(null),
    SetClientLocation(null),
    GetARView(ARViewModel[].class),
    AcceptOffer(null),
    FinishOffer(null),
    ReviewInsert(ReviewModel.class),
    SetClientTokenId(DeviceTokenModel.class),
    GetAboutUs(AboutUsModel.class),
    DoStripePayment(StripeModel.class),
    ClientCategoryInsert(null),
    ClientRadiusUpdate(null),
    ClientWatchVideo(LoginUserModel.class),
    SendEmail(null),
    ForgotPassword(LoginUserModel.class),
    ChatGroupMemberLeave(null),
    ChatGroupMemberDelete(null),
    ChatUserDelete(null),
    GetClientLocation(LocationModel.class),
    JobPostIssue(null),
    JobPostOfferCancel(null);


    Class mLocalClass;

    RequestCode(Class mLocalClass) {

        this.mLocalClass = mLocalClass;
    }

    public Class getLocalClass() {
        return mLocalClass;
    }

    public void setLocalClass(Class mLocalClass) {
        this.mLocalClass = mLocalClass;
    }
}
