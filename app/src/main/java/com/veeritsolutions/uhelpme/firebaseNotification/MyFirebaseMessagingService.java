package com.veeritsolutions.uhelpme.firebaseNotification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.activity.SignInActivity;
import com.veeritsolutions.uhelpme.activity.SplashActivity;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * TODO stub is generated but developer or programmer need to add code as required.
 * This class handle all messaging service from firebase
 * in onMessageReceived method give the push notification message
 * from firebase server
 *
 * To use FirebaseMessagingService you need to add the following in your app manifest
 *
 *         <service
 *           android:name=".MyFirebaseMessagingService">
 *              <intent-filter>
 *                   <action android:name="com.google.firebase.MESSAGING_EVENT"/>
 *              </intent-filter>
 *        </service>
 *
 *  refer below link for more information
 *  @link : https://firebase.google.com/docs/notifications/android/console-audience
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //variable declaration
    private static final String TAG = MyFirebaseMessagingService.class.getName();
    private RemoteViews notificationView;
    // TODO stub is generated but developer or programmer need to add code as required.
    private String message = "";
    private Map<String, String> messageBody = null;
    private String title = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (!TextUtils.isEmpty(remoteMessage.getNotification().getBody())) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
            messageBody = remoteMessage.getData();
            Debug.trace("messageBody", messageBody.toString());
            //generateNotification(message, messageBody);

            handleNotification(title, message, messageBody);
        }
    }

    /**
     * This method manage the flow of notification based on application lifecycle like
     * Notification should be generate or do other action (e.g. show popUp, Update data)
     *
     * @param title       (String) : title of the notification
     * @param message     (String) : message or data that comes in notification
     * @param messageBody (String) : for key and value data pair
     */

    private void handleNotification(String title, String message, Map<String, String> messageBody) {

        Bundle bundle = new Bundle();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.putAll(messageBody);

        bundle.putSerializable(Constants.KEY_NOTIFICATION_MESSAGE_DATA, hashMap);
        Intent intent;
        if (MyApplication.isApplicationVisible()) {
            if (PrefHelper.getInstance().getBoolean(PrefHelper.IS_LOGIN, false)) {

                intent = new Intent();
                intent.setAction(Constants.KEY_SHOW_POP_UP);
                intent.putExtra(Constants.KEY_FROM_NOTIFICATION, message);

                sendBroadcast(intent);
            } else {
                intent = new Intent(this, SignInActivity.class);
                intent.putExtra(Constants.KEY_FROM_NOTIFICATION, bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
            }
        } else {
            intent = new Intent(this, SplashActivity.class);
            intent.putExtra(Constants.KEY_FROM_NOTIFICATION, bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
        }
        generateNotification(title, message, messageBody, intent);
    }

    /**
     * This method creates notification and display it in notification bar.
     * In notification click action it opens relevant activity
     *
     * @param title       (String) : title of the notification
     * @param message     (String) : message or data that to be shown or update
     * @param messageBody (MAP<String,String>) : for getting actual data
     */
    private void generateNotification(String title, final String message, Map<String, String> messageBody, Intent intent) {


        NotificationWithText.notify(getApplicationContext(), title, message, Constants.NOTIFICATION_REQUEST_CODE, intent);

        /*PendingIntent pendingIntent = PendingIntent.getActivity(this, Constants.NOTIFICATION_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.img_notification_icon);
        //notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img_notification_icon));
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.FLAG_NO_CLEAR);
        //notificationBuilder.setCustomBigContentView(getComplexNotificationView(messageBody, body, pendingIntent));
        String picPath = messageBody.get(Constants.KEY_IMAGEPATH);
        if (picPath != null && picPath.length() != 0) {
            notificationBuilder.setStyle(getStyle(picPath));
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Constants.NOTIFICATION_REQUEST_CODE, notificationBuilder.build());*/
    }


    private android.support.v4.app.NotificationCompat.BigPictureStyle getStyle(String picPath) {

        //String picPath = body.get(Constants.KEY_IMAGEPATH);
        Bitmap theBitmap = null;
        //if (picPath != null && picPath.length() != 0) {
        try {
            theBitmap = Picasso.
                    with(MyApplication.getInstance())
                    .load(picPath)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // notificationView.setImageViewBitmap(R.id.img_notification, theBitmap);

        //  }
        // Create the style object with BigPictureStyle subclass.
        NotificationCompat.BigPictureStyle notiStyle = new
                NotificationCompat.BigPictureStyle();
        if (theBitmap != null)
            notiStyle.bigPicture(theBitmap);

        return notiStyle;
    }

    /*private RemoteViews getComplexNotificationView(String message, Map<String, String> messageBody, PendingIntent pendingIntent) {
        // Using RemoteViews to bind custom layouts into Notification
        notificationView = new RemoteViews(
                getPackageName(),
                R.layout.activity_custom_notification
        );

        notificationView.setOnClickPendingIntent(R.id.btn_actionOk, pendingIntent);
        notificationView.setOnClickPendingIntent(R.id.btn_actionCancel, pendingIntent);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        notificationView.setImageViewResource(R.id.img_noti_left, R.drawable.img_launcher_icon);
        //notificationView.setImageViewResource(R.id.img_notification, R.drawable.img_notification_birthday);
        // notificationView.setImageViewBitmap(R.id.tv_notification_details, buildUpdate(messageBody, 50, R.color.black));

        notificationView.setTextViewText(R.id.tv_notification_title, getString(R.string.app_name));
        notificationView.setTextViewTextSize(R.id.tv_notification_title, TypedValue.COMPLEX_UNIT_DIP, 15);
        // notificationView.setTextColor(R.id.tv_notification_title,
        //         ResourcesCompat.getColor(MyApplication.getInstance().getResources(), R.color.homeCategory, null));

        notificationView.setTextViewText(R.id.tv_notification_details, message);
        //  notificationView.setTextColor(R.id.tv_notification_details,
        //          ResourcesCompat.getColor(MyApplication.getInstance().getResources(), R.color.hint, null));

        String picPath = messageBody.get(Constants.KEY_IMAGEPATH);
        if (picPath != null && picPath.length() != 0) {
            try {
                Bitmap theBitmap = Glide.
                        with(MyApplication.getInstance()).
                        load(picPath).
                        asBitmap().
                        into(200, 200). // Width and height
                        get();
                notificationView.setImageViewBitmap(R.id.img_notification, theBitmap);

            } catch (InterruptedException | ExecutionException e) {
                notificationView.setViewVisibility(R.id.img_notification, View.GONE);
                e.printStackTrace();
            }
        } else {
            notificationView.setViewVisibility(R.id.img_notification, View.GONE);
        }

        // new NotificationImageAsyncTask().execute(messageBody.get("Url"));
        return notificationView;
    }*/

/*
    public class NotificationImageAsyncTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(params[0]);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Debug.trace(TAG, "Error getting bitmap" + e);
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            notificationView.setImageViewBitmap(R.id.img_notification, result);
        }
    }
*/

    /**
     * Code for converting message to image
     */
   /* public Bitmap buildUpdate(String message, float textSize, int textColor) {
        Bitmap myBitmap = Bitmap.createBitmap(160, 84, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface clock = MyApplication.getInstance().FONT_WORKSANS_REGULAR;
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(clock);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        myCanvas.drawText(message, 80, 60, paint);
        return myBitmap;
    }*/
}
