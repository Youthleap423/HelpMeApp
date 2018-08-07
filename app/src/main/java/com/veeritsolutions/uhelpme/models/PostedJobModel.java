package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by VEER7 on 6/30/2017.
 */

public class PostedJobModel implements Serializable {

    private int position = 0;
    @SerializedName("DataId")
    @Expose
    private int dataId = 0;
    @SerializedName("JobPostId")
    @Expose
    private int jobPostId = 0;
    @SerializedName("ClientId")
    @Expose
    private int clientId = 0;
    @SerializedName("FirstName")
    @Expose
    private String firstName = "";
    @SerializedName("LastName")
    @Expose
    private String lastName = "";
    @SerializedName("JobTitle")
    @Expose
    private String jobTitle = "";
    @SerializedName("JobDescription")
    @Expose
    private String jobDescription = "";
    @SerializedName("JobPhoto")
    @Expose
    private String jobPhoto = "";
    @SerializedName("IsFree")
    @Expose
    private int isFree = 0;
    @SerializedName("IsHire")
    @Expose
    private int IsHire = 0;
    @SerializedName("CategoryId")
    @Expose
    private int categoryId = 0;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName = "";
    @SerializedName("CategoryIcon1")
    @Expose
    private String categoryIcon1 = "";
    @SerializedName("CategoryIcon2")
    @Expose
    private String categoryIcon2 = "";
    @SerializedName("CategoryColorCode")
    @Expose
    private String colorCode = "";
    @SerializedName("JobPostingPoints")
    @Expose
    private int jobPostingPoints = 0;
    @SerializedName("JobPostingAmount")
    @Expose
    private int jobPostingAmount = 0;
    @SerializedName("Latitude")
    @Expose
    private float latitude = 0;
    @SerializedName("Longitude")
    @Expose
    private float longitude = 0;
    @SerializedName("Altitude")
    @Expose
    private float altitude = 0;
    @SerializedName("Latitude_1")
    @Expose
    private float latitude1 = 0;
    @SerializedName("Longitude_1")
    @Expose
    private float longitude1 = 0;
    @SerializedName("Altitude_1")
    @Expose
    private float altitude1 = 0;
    @SerializedName("JobHour")
    @Expose
    private int jobHour = 0;
    @SerializedName("JobDoneTime")
    @Expose
    private String jobDoneTime = "";
    @SerializedName("JobAmount")
    @Expose
    private long jobAmount = 0;
    @SerializedName("PaymentTime")
    @Expose
    private String paymentTime = "";
    @SerializedName("PaymentId")
    @Expose
    private String paymentId = "";
    @SerializedName("PaymentStatus")
    @Expose
    private String paymentStatus = "";
    @SerializedName("PaymentResponse")
    @Expose
    private String paymentResponse = "";
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn = "";
    @SerializedName("EndDate")
    @Expose
    private String endDate = "";
    @SerializedName("RowNo")
    @Expose
    private int rowNo = 0;
    @SerializedName("TotalRowNo")
    @Expose
    private int totalRowNo = 0;
    @SerializedName("TotalPage")
    @Expose
    private int totalPage = 0;
    @SerializedName("BestOffer")
    @Expose
    private int bestOffer = 0;
    @SerializedName("JobPostOfferModel")
    @Expose
    private List<JobPostOfferModel> jobPostOffer;
    @SerializedName("JobPostViewModel")
    @Expose
    private List<JobPostViewModel> jobPostView;
    @SerializedName("JobPostTimeDiff")
    @Expose
    private String jobPostTimeDiff = "";
    @SerializedName("MyOfferAmount")
    @Expose
    private int myOfferAmount = 0;
    @SerializedName("ProfilePic")
    @Expose
    private String profilePic = "";
    @SerializedName("JobPostOfferId")
    @Expose
    private int jobPostOfferId = 0;
    @SerializedName("ChatGroupId")
    @Expose
    private String chatGroupId = "";
    @SerializedName("JobTimeFlag")
    @Expose
    private int JobTimeFlag = 0;
    @SerializedName("JobAmountFlag")
    @Expose
    private int JobAmountFlag = 1;
    @SerializedName("JobPhoto1")
    @Expose
    private String JobPhoto1 = "";
    @SerializedName("JobPhoto2")
    @Expose
    private String JobPhoto2 = "";
    @SerializedName("JobPhoto3")
    @Expose
    private String JobPhoto3 = "";

    public int getIsHire() {
        return IsHire;
    }

    public void setIsHire(int isHire) {
        IsHire = isHire;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getJobPostOfferId() {
        return jobPostOfferId;
    }

    public void setJobPostOfferId(int jobPostOfferId) {
        this.jobPostOfferId = jobPostOfferId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(int jobPostId) {
        this.jobPostId = jobPostId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobPhoto() {
        return jobPhoto;
    }

    public void setJobPhoto(String jobPhoto) {
        this.jobPhoto = jobPhoto;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public int getJobPostingPoints() {
        return jobPostingPoints;
    }

    public void setJobPostingPoints(int jobPostingPoints) {
        this.jobPostingPoints = jobPostingPoints;
    }

    public int getJobPostingAmount() {
        return jobPostingAmount;
    }

    public void setJobPostingAmount(int jobPostingAmount) {
        this.jobPostingAmount = jobPostingAmount;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getJobHour() {
        return jobHour;
    }

    public void setJobHour(int jobHour) {
        this.jobHour = jobHour;
    }

    public String getJobDoneTime() {
        return jobDoneTime;
    }

    public void setJobDoneTime(String jobDoneTime) {
        this.jobDoneTime = jobDoneTime;
    }

    public long getJobAmount() {
        return jobAmount;
    }

    public void setJobAmount(long jobAmount) {
        this.jobAmount = jobAmount;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(String paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getTotalRowNo() {
        return totalRowNo;
    }

    public void setTotalRowNo(int totalRowNo) {
        this.totalRowNo = totalRowNo;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getBestOffer() {
        return bestOffer;
    }

    public void setBestOffer(int bestOffer) {
        this.bestOffer = bestOffer;
    }

    public String getCategoryIcon1() {
        return categoryIcon1;
    }

    public void setCategoryIcon1(String categoryIcon1) {
        this.categoryIcon1 = categoryIcon1;
    }

    public String getCategoryIcon2() {
        return categoryIcon2;
    }

    public void setCategoryIcon2(String categoryIcon2) {
        this.categoryIcon2 = categoryIcon2;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public float getLatitude1() {
        return latitude1;
    }

    public void setLatitude1(float latitude1) {
        this.latitude1 = latitude1;
    }

    public float getLongitude1() {
        return longitude1;
    }

    public void setLongitude1(float longitude1) {
        this.longitude1 = longitude1;
    }

    public float getAltitude1() {
        return altitude1;
    }

    public void setAltitude1(float altitude1) {
        this.altitude1 = altitude1;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public int getMyOfferAmount() {
        return myOfferAmount;
    }

    public void setMyOfferAmount(int myOfferAmount) {
        this.myOfferAmount = myOfferAmount;
    }

    public String getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(String chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public List<JobPostOfferModel> getJobPostOffer() {
        return jobPostOffer;
    }

    public void setJobPostOffer(List<JobPostOfferModel> jobPostOffer) {
        this.jobPostOffer = jobPostOffer;
    }

    public List<JobPostViewModel> getJobPostView() {
        return jobPostView;
    }

    public void setJobPostView(List<JobPostViewModel> jobPostView) {
        this.jobPostView = jobPostView;
    }

    public String getJobPostTimeDiff() {
        return jobPostTimeDiff;
    }

    public void setJobPostTimeDiff(String jobPostTimeDiff) {
        this.jobPostTimeDiff = jobPostTimeDiff;
    }

    public int getJobTimeFlag() {
        return JobTimeFlag;
    }

    public void setJobTimeFlag(int jobTimeFlag) {
        JobTimeFlag = jobTimeFlag;
    }

    public int getJobAmountFlag() {
        return JobAmountFlag;
    }

    public void setJobAmountFlag(int jobAmountFlag) {
        JobAmountFlag = jobAmountFlag;
    }

    public String getJobPhoto1() {
        return JobPhoto1;
    }

    public void setJobPhoto1(String jobPhoto1) {
        JobPhoto1 = jobPhoto1;
    }

    public String getJobPhoto2() {
        return JobPhoto2;
    }

    public void setJobPhoto2(String jobPhoto2) {
        JobPhoto2 = jobPhoto2;
    }

    public String getJobPhoto3() {
        return JobPhoto3;
    }

    public void setJobPhoto3(String jobPhoto3) {
        JobPhoto3 = jobPhoto3;
    }
}
