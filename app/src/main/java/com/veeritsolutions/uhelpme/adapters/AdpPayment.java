package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.PaymentModel;

import java.util.ArrayList;

/**
 * Created by VEER7 on 7/13/2017.
 */

public class AdpPayment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PaymentModel> paymnetList;
    private Context context;
    private int[] images = new int[]{R.drawable.img_paypal, R.drawable.img_stripe};

    public AdpPayment(ArrayList<PaymentModel> arrayList, Context context) {
        this.paymnetList = arrayList;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_payments, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            PaymentModel reviewModel = paymnetList.get(position);
            myViewHolder.tvPaymentName.setText(reviewModel.getPaymentTypeName());
            if (reviewModel.getPaymentType() == 0) {
                myViewHolder.imgPayment.setImageResource(images[0]);
            } else {
                myViewHolder.imgPayment.setImageResource(images[1]);
            }
        }
    }

    @Override
    public int getItemCount() {
        return paymnetList.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView tvPaymentName/*, tvPoints, tvMoney, txvOfferDetail, txvUserName, txvTime*/;
        ImageView imgPayment;
        // RatingBar rtbProductRating;


        MyViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view_strip);

            tvPaymentName = (TextView) itemView.findViewById(R.id.tv_paymentName);
            tvPaymentName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            imgPayment = (ImageView) itemView.findViewById(R.id.img_payment);

        }
    }
}

