package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.ProductRedeem;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;

/**
 * Created by vaishali on 7/14/2017.
 */

public class AdpProductRedeem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ProductRedeem> ProductRedeemList;
    private boolean isHistory;
    private View.OnClickListener onClickListener;

    public AdpProductRedeem(Context mContext, ArrayList<ProductRedeem> ProductRedeemList, boolean isHistory, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.ProductRedeemList = ProductRedeemList;
        this.isHistory = isHistory;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_redeem_history, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return ProductRedeemList.size();
    }

    public void refreshList(ArrayList<ProductRedeem> ProductRedeemList) {
        this.ProductRedeemList = ProductRedeemList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {

            ProductRedeem productRedeem = ProductRedeemList.get(position);

            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.linRedeem.setTag(productRedeem);

            myViewHolder.tvOfferTitle.setText(productRedeem.getProductName());
            if (isHistory) {
                myViewHolder.tvPoints.setText(String.valueOf(productRedeem.getRedeemPoint() + " Points"));
            } else {
                myViewHolder.tvPoints.setText(String.valueOf(productRedeem.getPoint() + " Points"));
            }

            myViewHolder.tvDate.setText(productRedeem.getCreatedOn());
            Utils.setImage(productRedeem.getProductImage(), R.color.colorHint, myViewHolder.imgItem, 50, 50, true);
            myViewHolder.linRedeem.setOnClickListener(onClickListener);
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvOfferTitle;
        TextView tvPoints;
        TextView tvDate;
        LinearLayout linRedeem;
        ImageView imgItem;

        MyViewHolder(View itemView) {
            super(itemView);

            imgItem = (ImageView) itemView.findViewById(R.id.img_item);

            tvOfferTitle = (TextView) itemView.findViewById(R.id.txv_offer_title);
            tvOfferTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            tvPoints = (TextView) itemView.findViewById(R.id.txv_poits);
            tvPoints.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            tvDate = (TextView) itemView.findViewById(R.id.txv_date);
            tvDate.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            linRedeem = (LinearLayout) itemView.findViewById(R.id.lin_Redeem);
        }
    }


}
