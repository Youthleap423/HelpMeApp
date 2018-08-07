package com.veeritsolutions.uhelpme.utility;

/**
 * Created by Admin on 5/11/2017.
 */

public class Constants {

    /**
     * for advertise video and payment
     */
    public static final String AD_UNIT_ID = "ca-app-pub-8023119556052962/7659424797";
    public static final String APP_ID = "ca-app-pub-8023119556052962~9500965985";
    public static String PAYPAL_SANDBOX_CLIENT_ID = "AQsGc_G-f7kthCZXQeNii8XiE_0iUYWXqPgAVMwc0bfJYS69WtYVMGOOD52aGNH8sKrXaqTgRHcrF_s-";
    public static String PAYPAL_LIVE_CLIENT_ID = "AfAIE1qZLeFN2_3vMXWdftievpOdq5Ca4ShQ1shGeKTB--LW-ClHChMPj2WlXyxPsFExhdzXVtvV31Zs";
//    public static String STRIPE_TEST_API_KEY = "pk_test_uogRcFSQqW34ROnD3I26k35d";
//    public static final String STRIPE_LIVE_API_KEY = "pk_live_2DfxSnkktJZRE2ZyvUKtcQD8";
    public static final String STRIPE_LIVE_API_KEY = "pk_test_uogRcFSQqW34ROnD3I26k35d";
    /**
     * Taking photos from gallery and camera with different request code
     */
    public static final int REQUEST_CAMERA_PROFILE = 1;
    // public static final int REQUEST_CAMERA_BANNER = 2;
    public static final int REQUEST_FILE_PROFILE = 3;
    /**
     * Constant used in the location settings dialog.
     */
    public static final int REQUEST_CHECK_SETTINGS = 1;
    /*The desired interval for location updates. Inexact. Updates may be more or less frequent.*/
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000 * 20;
    /*The fastest rate for active location updates. Exact. Updates will never be more frequent than this value.*/
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // public static final int REQUEST_FILE_BANNER = 4;
    /*for checking location permission*/
    public static final int REQUEST_LOCATION = 9;
    /**
     * social login keys for getting data
     */
    public static final String USERNAME = "UserName";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String REGISTER_BY = "registerBy";
    public static final String IS_FROM_SIGN_UP = "IsFromSignUp";
    public static final String IS_FROM_HOME_ACTIVITY = "homeActivity";
    /*Type face constants in whole project */
    public static final String FONT_WORKSANS_MEDIUM = "fonts/WorkSans-Medium.otf";
    public static final String FONT_WORKSANS_REGULAR = "fonts/WorkSans-Regular.otf";
    public static final String FONT_WORKSANS_LIGHT = "fonts/WorkSans-Light.otf";
    /**
     * date and time formats
     */
    public static final String MM_DD_YYYY_HH_MM_SS_A = "MM/dd/yyyy HH:mm:ss";
    public static final String DATE_YYYY = "yyyy";
    public static final String DATE_MM_DD_YYYY = "MM/dd/yyyy";
    public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int PHONE_LENGTH = 10;
    /**
     * notification related parameters
     */
    public static final String MESSAGE = "message";
    //    public static final String KEY_NOTIFICATION_MESSAGE = "message";
    public static final String KEY_NOTIFICATION_MESSAGE_DATA = "notificationMessageData";
    //    public static final String HH_MM_SS = "HH:mm:ss";
//    public static final String DATE_DD_MM_YYYY = "dd/MM/yyyy";
//    public static final String DATE_FORMAT = "dd/MM/yyyy";
//    public static final int DEFAULT_CALENDER_YEAR = 1970;
//    public static final int DEFAULT_CALENDER_MONTH = 1;
//    public static final int DEFAULT_CALENDER_DATE = 1;
    public static final String KEY_SHOW_POP_UP = "showPopUp";
    public static final String KEY_VET_APPOINTMENT_ID = "VetAppointmentId";
    public static final String KEY_APP_ICON_PATH = "AppIconPath";
    public static final String KEY_IMAGEPATH = "ImagePath";
    public static final String KEY_NOTIFICATION_TYPE = "NotificationType";
    public static final String KEY_FROM_NOTIFICATION = "fromNotification";
    public static final int NOTIFICATION_REQUEST_CODE = 1;
    public static final String DEBUG_KEY_FIREBASE_DEVICE_TOKEN = "firebaseTokenKey";
    public static final String AR_VIEW_DATA = "arViewData";
    public static final int CROP_REQUEST_CODE = 5;

    public static String CATEGORY_DATA = "categoryData";
    public static String HELP_DATA = "helpData";
    public static String CHAT_DATA = "chatData";
    public static String CHAT_GROUP_DATA = "chatGroup";
    public static int CHAT_GROUP_INSTANT = 1;
    public static String BASE_64_IMAGE = "base64image";
    public static String PACKAGE_DATA = "packages";
    public static String USER_DATA = "userData";
    public static String CHAT_GROUP_MEMBER_DATA = "chatGroupMemberData";
    public static String HH_MM_SS_24 = "HH:mm:ss";
    public static String HH_MM_SS_12 = "hh:mm:ss";

    public static int JOB_AWARDED = 1;
    public static int JOB_FINISHED = 2;
    public static int JOB_REJECTED = -1;
    public static int JOB_CANCELLED = 3;
    public static String IS_FROM_TRACK = "isFromTrack";
    public static String IS_FROM_AR_VIEW = "fromARview";
}
