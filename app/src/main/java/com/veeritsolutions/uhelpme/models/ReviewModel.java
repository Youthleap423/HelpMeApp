package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 7/10/2017.
 */

public class ReviewModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private long dataId = 0;
    @SerializedName("ReviewId")
    @Expose
    private long reviewId = 0;
    @SerializedName("ClientId")
    @Expose
    private long clientId = 0;
    @SerializedName("FirstName")
    @Expose
    private String firstName = "";
    @SerializedName("LastName")
    @Expose
    private String lastName = "";
    @SerializedName("JobPostId")
    @Expose
    private long jobPostId = 0;
    @SerializedName("Rating")
    @Expose
    private long rating = 0;
    @SerializedName("ReviewData")
    @Expose
    private String reviewData = "";
    @SerializedName("JobPhoto")
    @Expose
    private String jobPhoto = "";
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn = "";
    @SerializedName("EndDate")
    @Expose
    private String endDate = "";
    @SerializedName("JobTitle")
    @Expose
    private String jobTitle = "";
    @SerializedName("OfferAmount")
    @Expose
    private long offerAmount = 0;
    @SerializedName("HelpSeeker_ProfilePic")
    @Expose
    private String helpSeekerProfilePic = "";
    @SerializedName("ReviewTimeDiff")
    @Expose
    private String reviewTimeDiff = "";

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
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

    public long getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(long jobPostId) {
        this.jobPostId = jobPostId;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getReviewData() {
        return reviewData;
    }

    public void setReviewData(String reviewData) {
        this.reviewData = reviewData;
    }

    public String getJobPhoto() {
        return jobPhoto;
    }

    public void setJobPhoto(String jobPhoto) {
        this.jobPhoto = jobPhoto;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public long getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(long offerAmount) {
        this.offerAmount = offerAmount;
    }

    public String getHelpSeekerProfilePic() {
        return helpSeekerProfilePic;
    }

    public void setHelpSeekerProfilePic(String helpSeekerProfilePic) {
        this.helpSeekerProfilePic = helpSeekerProfilePic;
    }

    public String getReviewTimeDiff() {
        return reviewTimeDiff;
    }

    public void setReviewTimeDiff(String reviewTimeDiff) {
        this.reviewTimeDiff = reviewTimeDiff;
    }
}
