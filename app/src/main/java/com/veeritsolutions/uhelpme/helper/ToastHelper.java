package com.veeritsolutions.uhelpme.helper;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;


/**
 * Created by ${hitesh} on 12/6/2016.
 */
public class ToastHelper {

    private static ToastHelper ourInstance = new ToastHelper();
    private static Toast mToast;
    private boolean showToast = true;

    private ToastHelper() {
    }

    public static ToastHelper getInstance() {
        return ourInstance;

    }

    public void showMessage(String mMessage) {

        mToast = new Toast(MyApplication.getInstance());
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.BOTTOM, 0, 200);

        View view = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.custom_toast, null, false);
        mToast.setView(view);

        TextView toastMessage = (TextView) view.findViewById(R.id.tv_toast);
        toastMessage.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        toastMessage.setText(mMessage);

        if (showToast)
            mToast.show();
    }

    public void showMessage(String mMessage, int duration) {

        mToast = new Toast(MyApplication.getInstance());
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.NO_GRAVITY, 0, 0);

        View view = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.custom_toast, null, false);
        mToast.setView(view);

        TextView toastMessage = (TextView) view.findViewById(R.id.tv_toast);
        toastMessage.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        toastMessage.setText(mMessage);

        if (showToast)
            mToast.show();
    }

    public void cancel() {

        if (mToast != null) {
            mToast.cancel();

        }
    }
}
