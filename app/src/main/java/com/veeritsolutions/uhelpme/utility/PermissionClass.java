package com.veeritsolutions.uhelpme.utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.veeritsolutions.uhelpme.MyApplication;

import java.util.ArrayList;
import java.util.List;

/*
 * TODO stub is generated but developer or programmer need to add code as required.
 * This class use to check runtime permission for android version more than 5(lollipop).
 * If your app needs a dangerous permission that was listed in the app manifest,
 * it must ask the user to grant the permission. Android provides several methods you can use
 * to request a permission. Calling these methods brings up a standard Android dialog,
 * which you cannot customize.
 *
 * refer below link for more details
 * @link:https://developer.android.com/training/permissions/requesting.html
 *
 * TODO steps to access method
 *
 * 1. Import permissionUtils package or Required class (PermissionClass.java)
 *
 * 2. Access "checkPermission" method
 *    @Note:- In this method
 *            check permission granted or not
 *
 *              case 1: Granted : do your stuff
 *              case 2: Not granted : than it will show dialog to user to do action
 *
 *    copy below code where you want to check permission
 *
 *    PermissionClass.checkPermission(this, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION,
 *      Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
 *      Manifest.permission.CAMERA))
 *
 * 3. Implement override method "onRequestPermissionsResult()"
 *    implement override method in activity Or fragment where you want to check and get granted permission list
 *    @Note:- In this method we can get granted permission result
 *            You can do relevant action in this method
 *
 *            Refer below link for further details
 *            @link : https://developer.android.com/training/permissions/requesting.html
 *            Refer "Handle the permissions request response" point
 *    e.g.
 *    @Override
 *    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 *        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 *        List<String> shouldPermit = new ArrayList<>();
 *
 *         if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION) {
 *
 *             if (grantResults.length > 0 || grantResults.length != PackageManager.PERMISSION_GRANTED) {
 *
 *               for (int i = 0; i < grantResults.length; i++) {
 *                //  permissions[i] = Manifest.permission.CAMERA; //for specific permission check
 *                 grantResults[i] = PackageManager.PERMISSION_DENIED;
 *                shouldPermit.add(permissions[i]);
 *                       }
 *             }
 *         }
 *    }
 *
 * 4. Access "verifyPermission" method to check or any further action.
 *
 *    Copy below code to override method "onRequestPermissionsResult()"
 *    if (PermissionUtil.verifyPermissions(grantResults)) {
 *          // permission granted
 *          // perform action
 *    }else{
 *          // permission denied
 *          // perform action
 *    }
 *    case 1: we can recall the dialog if user denied to accept the permission
 *    case 2: Perform relevant action
 */

public class PermissionClass {

    /* Variable declaration*/
    public static final int REQUEST_CODE_RUNTIME_PERMISSION = 151;
    public static final int REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA = 156;

    public static final int Request_code_Read_Storage = 153;
    public static final int Request_code_Write_Storage = 152;
    public static final int Request_code_Location_Storage = 154;
    public static final int Request_code_Camera = 155;


    /**
     * This method check that required permission user granted or denied.
     * if denied than it pass permission list to show pop up to allow the permission
     * <p>
     * case 1: Granted : do your stuff
     * case 2: Not granted : than it will show dialog to user to accept permission
     *
     * @param context        (Context)             : application context
     * @param requestCode    (int)             : request code to be identify the request
     *                       e.g. 151
     * @param permissionList (List<String>) : list of permission that need to be granted by user
     *                       <p> for single permission
     *                       e.g. Collections.singletonList(Manifest.permission.ACCESS_FINE_LOCATION)
     *                       <p> for multiple permission
     *                       e.g. Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE,
     *                       Manifest.permission.WRITE_EXTERNAL_STORAGE,
     *                       Manifest.permission.CAMERA)
     * @return (boolean) : it return true, if permission already granted Or
     * return false and show permission relevant dialog
     */
    public static boolean checkPermission(Context context, int requestCode, List<String> permissionList) {

        List<String> deniedPermissionList = new ArrayList<>();

        for (String permission : permissionList) {
            if (ActivityCompat.checkSelfPermission(MyApplication.getInstance(), permission)
                    != PackageManager.PERMISSION_GRANTED) {

                deniedPermissionList.add(permission);

            }
        }

        if (deniedPermissionList.size() > 0) {
            // parse list as an array
            // In requestPermission method we must pass array
            String[] deniedPermissionArray = new String[deniedPermissionList.size()];
            deniedPermissionArray = deniedPermissionList.toArray(deniedPermissionArray);

            makeRequest(context, requestCode, deniedPermissionArray);
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method shows permission dialog whose permission is not granted.
     * Calling this method brings up a standard Android dialog,
     * which you cannot customize.
     *
     * @param context     (Context)     : application context
     * @param requestCode (int)     : request code to be identify the request e.g. 151
     * @param permission  (String[]) : list of permission that need to be granted by user
     * @see android.Manifest.permission
     * @see #REQUEST_CODE_RUNTIME_PERMISSION
     */
    public static void makeRequest(Context context, int requestCode, String[] permission) {

        ActivityCompat.requestPermissions((Activity) context,
                permission,
                requestCode);

    }

    /**
     * TODO stub is generated but developer need to test
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @param grantResults (int[]) : The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}.
     * @return (boolean) : return true or false,if permission is granted or not, respectively
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermission(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


}
