package com.veeritsolutions.uhelpme.utility;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.enums.ImageUpload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Utils {

    private static final float BLUR_RADIUS = 20f;
    static long cacheProfileFlag;
    static long cacheBannerFlag;
//    public static float round(float d, int decimalPlace) {
//        BigDecimal bd = new BigDecimal(Float.toString(d));
//        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
//        return bd.floatValue();
//    }

    public static Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Debug.trace("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Debug.trace("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    /**
     * This method give the App version
     *
     * @return it return app version name e.g. 1.0,1.1
     */
    public static String getAppVersion() {

        String version = "";
        try {
            PackageInfo pInfo = MyApplication.getInstance().getPackageManager()
                    .getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;
    }

    public static void printFbKeyHash() {
        // Add code to print out the key hash

        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = MyApplication.getInstance().getPackageManager().getPackageInfo(
                    MyApplication.getInstance().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Debug.trace("FBKeyHash:", "" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Debug.trace("error", "Error while printing facebook hash");
        }

    }

    public static boolean isInternetAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void hideKeyBoard(View view) {
        // Check if no dataView has focus:
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }

    public static Bitmap compressImage(String imagePath) {

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        final int REQUIRED_SIZE = 100;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(imagePath, options);

        return bm;
    }

    public static int dpToPx(float valueInDp) {
        DisplayMetrics metrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public static String getStringImage(String imagePath, ImageUpload imageUpload) {

        // File sd = Environment.getExternalStorageDirectory();
        File image = new File(imagePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
//        if (imageUpload == ImageUpload.ClientProfile)
//            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
//        else if (imageUpload == ImageUpload.ClientBanner)
//            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 200, true);
        // Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;

    }

    public static String getStringImage(Bitmap bitmap, ImageUpload imageUpload) {

        // File sd = Environment.getExternalStorageDirectory();
        // File image = new File(imagePath);
        // BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        // Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        if (imageUpload == ImageUpload.ClientProfile)
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        else if (imageUpload == ImageUpload.ClientBanner)
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 200, true);
        // Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;

    }

    /**
     * Return Date in specified format. This method convert date
     * from millisecond to date format e.g. 1478180961448 to 3/11/2016
     *
     * @param milliSeconds (long) : Date in milliseconds e.g. 1478180961448
     * @param dateFormat   (String) :   Date format e.g. dd/MM/yyyy
     * @return (String) : representing Date in specified format e.g. 3/11/2016
     * @see SimpleDateFormat#format(Object)
     */
    public static String dateFormat(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying Date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        return formatter.format(milliSeconds);
    }

    public static int getScreenHeight(Context context) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay()
                .getMetrics(displaymetrics);

        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        return height;
    }

    public static int getScreenWidth(Context context) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay()
                .getMetrics(displaymetrics);

        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        return width;
    }

    public static String formatDate(String inputDate, String InputDateFormat, String outputDateFormat) {

        SimpleDateFormat originalFormat = new SimpleDateFormat(InputDateFormat);
        //   originalFormat.applyPattern("dd/MM/yyyy HH:mm:ss a");
        SimpleDateFormat targetFormat = new SimpleDateFormat(outputDateFormat);
        Date outPutDate = null, toDate = null;
        try {
            outPutDate = originalFormat.parse(inputDate);
            toDate = originalFormat.parse(inputDate);
            //   System.out.println("Old Format :   " + originalFormat.format(fromDate));
            //   System.out.println("New Format :   " + targetFormat.format(fromDate));

        } catch (ParseException ex) {
            // Handle Exception.
        }

        return targetFormat.format(outPutDate);
    }

    public static Date formatDateWithDate(String inputDate) {

        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        //   originalFormat.applyPattern("dd/MM/yyyy HH:mm:ss a");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date outPutDate = null, date1 = null;
        try {
            outPutDate = originalFormat.parse(inputDate);
            String newDate = targetFormat.format(outPutDate);

            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            date1 = formatter1.parse(newDate);

        } catch (ParseException ex) {
            // Handle Exception.
        }

        return date1;
    }

    public static String oneFormatToAnotherFormat(String originalDateFormat, String targetDateFormat, String inputStringDate) {

        String formattedDate = null;
        try {
            DateFormat originalFormat = new SimpleDateFormat(originalDateFormat, Locale.getDefault());
            DateFormat targetFormat = new SimpleDateFormat(targetDateFormat);
            Date date = originalFormat.parse(inputStringDate);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String dateToString(String date, String dateFormat) {

        SimpleDateFormat dateformat = new SimpleDateFormat(dateFormat);
        dateformat.applyLocalizedPattern(dateFormat);
        String returnDate;
        //  Date date = new Date();
        String datetime = dateformat.format(date);
        //  System.out.println("Current Date Time : " + datetime);
        return datetime;
    }

    /**
     * This method identify touch event. If user touches outside of keyboard
     * on screen instead of input control then call hide Keyboard method to hide keyboard.
     *
     * @param view(View) :parent dataView
     */
    @SuppressLint("ClickableViewAccessibility")
    public static void setupOutSideTouchHideKeyboard(final View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(view);
                    return false;
                }

            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                setupOutSideTouchHideKeyboard(innerView);
            }
        }
    }

    /**
     * This method hide keyboard
     *
     * @param view contains dataView, on which touch event has been performed.
     */
    private static void hideKeyboard(View view) {

        InputMethodManager mgr = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static void buttonClickEffect(final View v) {
        // v.setEnabled(false);

        AlphaAnimation obja = new AlphaAnimation(1.0f, 0.3f);
        obja.setDuration(5);
        obja.setFillAfter(false);
        v.startAnimation(obja);

    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Kilo Meters
     */
    public static double getDistance(double lat1, double lat2, double lon1,
                                     double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance) / 1000;
    }

    public static String getMinutes(long milliSeconds) {

        long totalSeconds = milliSeconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.valueOf(minutes);
    }

    public static String getSeconds(long milliSeconds) {

        long totalSeconds = milliSeconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.valueOf(seconds);
    }

    public static String formatTimeSpan(long milliSeconds) {
        long totalSeconds = milliSeconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format(Locale.UK, "%02d:%02d", minutes, seconds);
    }


    /**
     * This method convert object into a string
     *
     * @param obj(Serializable) : object to be converted in string
     */
    public static String objectToString(Serializable obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(
                    new Base64OutputStream(baos, Base64.NO_PADDING
                            | Base64.NO_WRAP));
            oos.writeObject(obj);
            oos.close();
            return baos.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method convert string into object
     *
     * @param str(String) : string to be converted into object
     */
    public static Object stringToObject(String str) {
        try {
            return new ObjectInputStream(new Base64InputStream(
                    new ByteArrayInputStream(str.getBytes()), Base64.NO_PADDING
                    | Base64.NO_WRAP)).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static Bitmap createBlurredImage(Bitmap originalBitmap, Context context) {

        Bitmap blurredBitmap;
        if (!originalBitmap.isRecycled()) {
            blurredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        } else {
            blurredBitmap = originalBitmap;
            return blurredBitmap;
        }


        RenderScript rs;
        rs = RenderScript.create(context);
        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(
                rs,
                blurredBitmap,
                Allocation.MipmapControl.MIPMAP_FULL,
                Allocation.USAGE_SHARED
        );
        Allocation output = Allocation.createTyped(rs, input.getType());

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);

        // Set the blur radius
        script.setRadius(24);

        // Start the ScriptIntrinisicBlur
        script.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(blurredBitmap);

        originalBitmap.recycle();

        return blurredBitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void setTypeFace(MenuItem menuItem, Typeface typeface) {

        SpannableString mNewTitle = new SpannableString(menuItem.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", typeface), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitle(mNewTitle);
    }


    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }

    public static void setImage(String imagePath, int placeholder, ImageView imgProfilePhoto) {

       /* Glide.with(MyApplication.getInstance()).load(imagePath)
                .apply(new RequestOptions()
                        .placeholder(placeholder)
                        .error(placeholder)
                        .signature(new ObjectKey(String.valueOf(cacheProfileFlag)))
                        .transform(new CropCircleTransformation(MyApplication.getInstance())))
                .into(imgProfilePhoto);*/

        if (imagePath != null && !imagePath.isEmpty()) {
            Picasso.with(MyApplication.getInstance())
                    .load(imagePath)
                    .placeholder(placeholder)
                    //.stableKey(String.valueOf(cacheProfileFlag))
                    .error(placeholder)
                    .transform(new CropCircleTransformation())
                    .into(imgProfilePhoto);
        } else {
            Picasso.with(MyApplication.getInstance())
                    .load(placeholder)
                    .placeholder(placeholder)
                    //.stableKey(String.valueOf(cacheProfileFlag))
                    .error(placeholder)
                    .transform(new CropCircleTransformation())
                    .into(imgProfilePhoto);
        }

    }

    public static void setImage(String imagePath, int placeholder, ImageView imgProfilePhoto,
                                final ProgressBar progressBar) {

        progressBar.setVisibility(View.VISIBLE);
       /* Glide.with(MyApplication.getInstance()).load(imagePath)
                .apply(new RequestOptions()
                        .signature(new ObjectKey(String.valueOf(cacheProfileFlag)))
                        .centerCrop()
                        .placeholder(placeholder)
                        .error(placeholder)
                        .transform(new CropCircleTransformation(MyApplication.getInstance())))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imgProfilePhoto);*/

        if (imagePath != null && !imagePath.isEmpty()) {
            Picasso.with(MyApplication.getInstance())
                    .load(imagePath)
                    .placeholder(placeholder)
                    //.stableKey(String.valueOf(cacheProfileFlag))
                    .error(placeholder)
                    .transform(new CropCircleTransformation())
                    .into(imgProfilePhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            Picasso.with(MyApplication.getInstance())
                    .load(placeholder)
                    .placeholder(placeholder)
                    //.stableKey(String.valueOf(cacheProfileFlag))
                    .error(placeholder)
                    .transform(new CropCircleTransformation())
                    .into(imgProfilePhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }


    }

    public static void setImage(String imagePath, int placeholder, ImageView imgProfilePhoto,
                                int overrideWidth, int overrideHeight, boolean isCircle) {

        if (isCircle) {


           /* Glide.with(MyApplication.getInstance()).load(imagePath)
                    .apply(new RequestOptions()
                            .signature(new ObjectKey(String.valueOf(cacheProfileFlag)))
                            .centerCrop()
                            .placeholder(placeholder)
                            .error(placeholder)
                            .transform(new CropCircleTransformation(MyApplication.getInstance())))
                    .into(imgProfilePhoto);*/
            if (imagePath != null && !imagePath.isEmpty()) {
                Picasso.with(MyApplication.getInstance())
                        .load(imagePath)
                        .placeholder(placeholder)
                        //.stableKey(String.valueOf(cacheProfileFlag))
                        .error(placeholder)
                        //.transform(new CropCircleTransformation(MyApplication.getInstance()))
                        .into(imgProfilePhoto);
            } else {
                Picasso.with(MyApplication.getInstance())
                        .load(placeholder)
                        .placeholder(placeholder)
                        //.stableKey(String.valueOf(cacheProfileFlag))
                        .error(placeholder)
                        //.transform(new CropCircleTransformation(MyApplication.getInstance()))
                        .into(imgProfilePhoto);
            }

        } else {

           /* Glide.with(MyApplication.getInstance()).load(imagePath)
                    .apply(new RequestOptions()
                            .signature(new ObjectKey(String.valueOf(cacheProfileFlag)))
                            .centerCrop()
                            .placeholder(placeholder)
                            .error(placeholder))
                    .into(imgProfilePhoto);*/

            if (imagePath != null && !imagePath.isEmpty()) {
                Picasso.with(MyApplication.getInstance())
                        .load(imagePath)
                        .placeholder(placeholder)
                        //.stableKey(String.valueOf(cacheProfileFlag))
                        .error(placeholder)
                        .transform(new CropCircleTransformation())
                        .into(imgProfilePhoto);
            } else {
                Picasso.with(MyApplication.getInstance())
                        .load(placeholder)
                        .placeholder(placeholder)
                        //.stableKey(String.valueOf(cacheProfileFlag))
                        .error(placeholder)
                        .transform(new CropCircleTransformation())
                        .into(imgProfilePhoto);
            }
        }
    }

    public static void setImage(String imagePath, int placeholder, ImageView imgProfilePhoto,
                                final ProgressBar progressBar, boolean isCircular) {

        progressBar.setVisibility(View.VISIBLE);
        if (isCircular) {
           /* Glide.with(MyApplication.getInstance()).load(imagePath)
                    .apply(new RequestOptions()
                            .signature(new ObjectKey(String.valueOf(cacheProfileFlag)))
                            .centerCrop()
                            .placeholder(placeholder)
                            .error(placeholder)
                            .transform(new CropCircleTransformation(MyApplication.getInstance())))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgProfilePhoto);*/

            if (imagePath != null && !imagePath.isEmpty()) {
                Picasso.with(MyApplication.getInstance())
                        .load(imagePath)
                        .placeholder(placeholder)
                        //.stableKey(String.valueOf(cacheProfileFlag))
                        .error(placeholder)
                        .transform(new CropCircleTransformation())
                        .into(imgProfilePhoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            } else {
                Picasso.with(MyApplication.getInstance())
                        .load(placeholder)
                        .placeholder(placeholder)
                        //.stableKey(String.valueOf(cacheProfileFlag))
                        .error(placeholder)
                        .transform(new CropCircleTransformation())
                        .into(imgProfilePhoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }


        } else {
           /* Glide.with(MyApplication.getInstance()).load(imagePath)
                    .apply(new RequestOptions()
                            .signature(new ObjectKey(String.valueOf(cacheProfileFlag)))
                            .centerCrop()
                            .placeholder(placeholder)
                            .error(placeholder))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgProfilePhoto);*/

            if (imagePath != null && !imagePath.isEmpty()) {
                Picasso.with(MyApplication.getInstance())
                        .load(imagePath)
                        .placeholder(placeholder)
                        //.stableKey(String.valueOf(cacheProfileFlag))
                        .error(placeholder)
                        //.transform(new CropCircleTransformation(MyApplication.getInstance()))
                        .into(imgProfilePhoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            } else {
                Picasso.with(MyApplication.getInstance())
                        .load(placeholder)
                        .placeholder(placeholder)
                        //.stableKey(String.valueOf(cacheProfileFlag))
                        .error(placeholder)
                        //.transform(new CropCircleTransformation(MyApplication.getInstance()))
                        .into(imgProfilePhoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }


        }

    }

    public static void setImage(int resourceImage, int placeholder, ImageView imgProfilePhoto) {

       /* Glide.with(MyApplication.getInstance()).load(resourceImage)
                .apply(new RequestOptions()
                        .placeholder(placeholder)
                        .error(placeholder)
                        .transform(new CropCircleTransformation(MyApplication.getInstance()))
                        .signature(new ObjectKey(String.valueOf(cacheProfileFlag))))
                .into(imgProfilePhoto);*/

        Picasso.with(MyApplication.getInstance())
                .load(resourceImage)
                .placeholder(placeholder)
                //.stableKey(String.valueOf(cacheProfileFlag))
                .error(placeholder)
                .transform(new CropCircleTransformation())
                .into(imgProfilePhoto);
    }

    public static String mytoString(ArrayList<String> theAray, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < theAray.size(); i++) {
            if (i > 0) {
                sb.append(delimiter);
            }
            String item = theAray.get(i);
            sb.append(item);
        }
        return sb.toString();
    }

    public static void slideUP(final View view, Context context) {
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.slid_up);
        view.startAnimation(animation);
        // view.setVisibility(View.GONE);
    }

    public static void slideDown(final View view, Context context) {
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.slid_down);
        view.startAnimation(animation);
        // view.setVisibility(View.GONE);
    }
}
