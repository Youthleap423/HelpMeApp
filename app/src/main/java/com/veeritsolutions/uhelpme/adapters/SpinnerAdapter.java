package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;

import java.util.List;

/**
 * Created by aas2 on 03-06-2016.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> spinnerList;
    private int resourceLayout;

    public SpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource,objects);
        this.resourceLayout = resource;
        this.context = context;
        spinnerList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);

    }

    private View getCustomView(int position, ViewGroup parent) {

        View convertView = LayoutInflater.from(context).inflate(resourceLayout, parent, false);

        TextView spinnerItem = (TextView) convertView.findViewById(R.id.spinnerItem);
        spinnerItem.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        if (position == 0) {
            // spinnerItem.setBackgroundColor(Color.parseColor("#b0a3fc"));
            spinnerItem.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.colorHint, null));
        } else {
            //spinnerItem.setBackgroundColor(Color.parseColor("#faebd7"));
            spinnerItem.setTextColor(Color.BLACK);
        }

        spinnerItem.setText(spinnerList.get(position));

        return convertView;

    }
}
