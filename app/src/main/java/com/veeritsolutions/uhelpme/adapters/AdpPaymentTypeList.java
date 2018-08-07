package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.PaymentTypeModel;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ABC on 10/5/2017.
 */

public class AdpPaymentTypeList extends BaseAdapter {
    private Context context;
    private ArrayList<PaymentTypeModel> listPaymentType;
    private String filter = "", strLoading = "";

    public AdpPaymentTypeList(Context context, ArrayList<PaymentTypeModel> listPaymentType, String filter/*, LocationType locationType*/) {
        this.context = context;
        this.listPaymentType = listPaymentType;
        this.filter = filter;
        /*this.locationType = locationType;*/
        this.strLoading = "Loading";
    }

    @Override
    public int getCount() {
        return listPaymentType.size();
    }

    @Override
    public Object getItem(int position) {
        return listPaymentType.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            AdpPaymentTypeList.ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_location, parent, false);
                holder = new AdpPaymentTypeList.ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (AdpPaymentTypeList.ViewHolder) convertView.getTag();
            }
            // holder.footerView.setVisibility(View.GONE);

            String itemName = null;
            PaymentTypeModel paymentTypeModel = listPaymentType.get(position);
            itemName=paymentTypeModel.getPaymentTypeName();
            paymentTypeModel.setPosition(position);
            holder.txtLocationName.setTag(paymentTypeModel);
           // Change color on search
            int startPos = itemName.toLowerCase(Locale.US).indexOf(filter.toLowerCase(Locale.US));
            int endPos = startPos + filter.length();
            if (startPos != -1) // This should always be true, just a sanity check
            {
                ColorStateList searchedTextColour;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    searchedTextColour = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getColor(R.color.colorAccent)});
                } else {
                    searchedTextColour = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getResources().getColor(R.color.colorAccent)});
                }
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, searchedTextColour, null);

                Spannable spannable = new SpannableString(itemName);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.txtLocationName.setText(spannable);
            } else
                holder.txtLocationName.setText(itemName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
    private class ViewHolder {
        TextView txtLocationName/*, txtLoading*/;
        // View footerView;
        LinearLayout linLocation;

        ViewHolder(View convertView) {

            txtLocationName = (TextView) convertView.findViewById(R.id.txtLocationName);
            // txtLoading = (TextView) convertView.findViewById(R.id.txtLoading);
            linLocation = (LinearLayout) convertView.findViewById(R.id.lin_location);
            //  footerView = convertView.findViewById(R.id.footerview);

            //  Set Font Type
            txtLocationName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
            //txtLoading.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        }
    }
}
