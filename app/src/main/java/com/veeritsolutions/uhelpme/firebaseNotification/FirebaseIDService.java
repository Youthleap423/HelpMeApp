package com.veeritsolutions.uhelpme.firebaseNotification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;


import org.json.JSONObject;

/*
 * TODO stub is generated but developer or programmer need to add code as required.
 * This class extents the FirebaseInstanceIdService
 * It runs in background and gives the fresh device token from firebase
 * in onTokenRefresh method
 *
 * To use FirebaseIDService you need to add the following in your app manifest
 *     <service
 *       android:name=".firebaseNotification.FirebaseIDService">
 *         <intent-filter>
 *               <action android:name="com.google.firebase.INSTANCE_ID_EVENT"/>
 *         </intent-filter>
 *     </service>
 */
public class FirebaseIDService extends FirebaseInstanceIdService {

    //variable declaration
    private static final String TAG = FirebaseIDService.class.getName();
    private JSONObject params;

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Debug.trace(Constants.DEBUG_KEY_FIREBASE_DEVICE_TOKEN, token);
        sendRegistrationToServer(token);
    }

    /**
     * This method to store the token on your server
     *
     * @param token (String) : firebase token
     */
    public void sendRegistrationToServer(String token) {

        PrefHelper.getInstance().setString(PrefHelper.FIREBASE_DEVICE_TOKEN, token);
    }
}
