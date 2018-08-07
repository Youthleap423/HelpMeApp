package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.PrefHelper;

import java.io.Serializable;

/**
 * Created by VEER7 on 6/20/2017.
 */

public class LoginUserModel implements Serializable {

    public static LoginUserModel getLoginUserModel() {

        return RestClient.getGsonInstance().fromJson(PrefHelper.getInstance().getString(PrefHelper.CLIENT_CREDENTIALS, ""), LoginUserModel.class);
//        return (LoginUserModel) stringToObject(PrefHelper.getInstance().getString(PrefHelper.CLIENT_CREDENTIALS, ""));
    }

    public static void setLoginCredentials(String loginCredentials) {

        PrefHelper.getInstance().setString(PrefHelper.CLIENT_CREDENTIALS, loginCredentials);
    }

    @SerializedName("DataId")
    @Expose
    private int dataId = 0;
    @SerializedName("ClientId")
    @Expose
    private int clientId = 0;
    @SerializedName("FirstName")
    @Expose
    private String firstName = "";
    @SerializedName("LastName")
    @Expose
    private String lastName = "";
    @SerializedName("Address1")
    @Expose
    private String address1 = "";
    @SerializedName("Address2")
    @Expose
    private String address2 = "";
    @SerializedName("CityId")
    @Expose
    private int cityId = 0;
    @SerializedName("POBox")
    @Expose
    private String pOBox = "";
    @SerializedName("StateId")
    @Expose
    private int stateId = 0;
    @SerializedName("CountryId")
    @Expose
    private int countryId = 0;
    @SerializedName("PhoneNo")
    @Expose
    private String phoneNo = "";
    @SerializedName("EmailId")
    @Expose
    private String emailId = "";
    @SerializedName("Password")
    @Expose
    private String password = "";
    @SerializedName("ProfilePic")
    @Expose
    private String profilePic = "";
    @SerializedName("AcTokenId")
    @Expose
    private String acTokenId = "";
    @SerializedName("IsActive")
    @Expose
    private int isActive;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn = "";
    @SerializedName("EndDate")
    @Expose
    private String endDate = "";
    @SerializedName("RegisteredBy")
    @Expose
    private String registeredBy = "";
    @SerializedName("City")
    @Expose
    private String city = "";
    @SerializedName("State")
    @Expose
    private String state = "";
    @SerializedName("Country")
    @Expose
    private String country = "";
    @SerializedName("Latitude")
    @Expose
    private float latitude = 0;
    @SerializedName("Longitude")
    @Expose
    private float longitude = 0;
    @SerializedName("Altitude")
    @Expose
    private float altitude = 0;
    @SerializedName("Gender")
    @Expose
    private int gender = 0;

    @SerializedName("PaymentMethodDisp")
    @Expose
    private String PaymentMethodDisp= "" ;

    @SerializedName("GenderDisp")
    @Expose
    private String genderDisp = "";

    @SerializedName("PaymentMethod")
    @Expose
    private int PaymentMethod = 0;

    @SerializedName("IsClientProfile")
    @Expose
    private int isClientProfile;

    private boolean isSelected = false;

    private int position = 0;
    @SerializedName("Rating")
    @Expose
    private float rating = 0;
    @SerializedName("Points")
    @Expose
    private float points = 0;
    @SerializedName("HelpMe")
    @Expose
    private float helpMe = 0;
    @SerializedName("Offered")
    @Expose
    private float offered = 0;
    @SerializedName("IsAdmin")
    @Expose
    private int isAdmin;
    @SerializedName("Radious")
    @Expose
    private float Radious = 0;

    @SerializedName("BirthDate")
    @Expose
    private String birthDate="";
    @SerializedName("IsBankInformation")
    @Expose
    private int isBankInformation=0;
    @SerializedName("BusinessTaxId")
    @Expose
    private String businessTaxId="";
    @SerializedName("PersonalIdNumber")
    @Expose
    private String personalIdNumber="";
    @SerializedName("BankAccountNumber")
    @Expose
    private String bankAccountNumber="";
    @SerializedName("RoutingNumber")
    @Expose
    private String routingNumber="";
    @SerializedName("LegalDocument")
    @Expose
    private String legalDocument="";


    public float getRadious() {
        return Radious;
    }

    public void setRadious(float radious) {
        Radious = radious;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAcTokenId() {
        return acTokenId;
    }

    public void setAcTokenId(String acTokenId) {
        this.acTokenId = acTokenId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
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

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getIsClientProfile() {
        return isClientProfile;
    }

    public void setIsClientProfile(int isClientProfile) {
        this.isClientProfile = isClientProfile;
    }

    public String getpOBox() {
        return pOBox;
    }

    public void setpOBox(String pOBox) {
        this.pOBox = pOBox;
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

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getGender() {
        return gender;
    }



    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getGenderDisp() {
        return genderDisp;
    }

    public void setGenderDisp(String paymentType) {
        this.genderDisp = genderDisp;
    }

    public String getPaymentMethodDisp() {
        return PaymentMethodDisp;
    }

    public void setPaymentMethodDisp(String paymentMethodDisp) {
        PaymentMethodDisp = paymentMethodDisp;
    }

    public int getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public float getHelpMe() {
        return helpMe;
    }

    public void setHelpMe(float helpMe) {
        this.helpMe = helpMe;
    }

    public float getOffered() {
        return offered;
    }

    public void setOffered(float offered) {
        this.offered = offered;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getIsBankInformation() {
        return isBankInformation;
    }

    public void setIsBankInformation(int isBankInformation) {
        this.isBankInformation = isBankInformation;
    }

    public String getBusinessTaxId() {
        return businessTaxId;
    }

    public void setBusinessTaxId(String businessTaxId) {
        this.businessTaxId = businessTaxId;
    }

    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    public void setPersonalIdNumber(String personalIdNumber) {
        this.personalIdNumber = personalIdNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getLegalDocument() {
        return legalDocument;
    }

    public void setLegalDocument(String legalDocument) {
        this.legalDocument = legalDocument;
    }
}
