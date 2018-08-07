package com.veeritsolutions.uhelpme.adapters;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;

/**
 * Created by hitesh on 05-09-2017.
 */

public class AdpViewPager extends PagerAdapter {

    ArrayList<String> picList;

    public AdpViewPager(ArrayList<String> picList) {
        this.picList = picList;
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.list_item_image_viewpager, container, false);

        final String path = picList.get(position);
        final ImageView imgHelpPhoto = (ImageView) view.findViewById(R.id.img_help_ProfilePic);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pg_helpPhoto);
        Utils.setImage(path, android.R.color.white, imgHelpPhoto, progressBar, false);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}