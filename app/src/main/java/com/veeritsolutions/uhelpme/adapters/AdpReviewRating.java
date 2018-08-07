package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;

import com.veeritsolutions.uhelpme.models.ReviewModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;

/**
 * Created by Admin on 7/10/2017.
 */

public class AdpReviewRating extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ReviewModel> rewardArrayList;
    private Context context;
   // private ReviewModel reviewModel;

    public AdpReviewRating(ArrayList<ReviewModel> arrayList, Context context) {
        this.rewardArrayList = arrayList;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_other_user_profile, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            ReviewModel reviewModel = rewardArrayList.get(position);
            // myViewHolder.tvPaymentName.setText(reviewModel.);
            myViewHolder.txvOfferTitle.setText(reviewModel.getJobTitle());
            myViewHolder.tvPoints.setText(String.valueOf(reviewModel.getRating()));
            myViewHolder.rtbProductRating.setRating(reviewModel.getRating());
            myViewHolder.tvMoney.setText("$ " + reviewModel.getOfferAmount());
            myViewHolder.txvOfferDetail.setText(reviewModel.getReviewData());

            Utils.setImage(reviewModel.getHelpSeekerProfilePic(), R.drawable.img_user_placeholder, myViewHolder.imgUserProfile);
            myViewHolder.txvUserName.setText(reviewModel.getFirstName() + " " + reviewModel.getLastName());
            myViewHolder.txvTime.setText(reviewModel.getReviewTimeDiff());

        }

    }

    @Override
    public int getItemCount() {
        return rewardArrayList.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView txvOfferTitle, tvPoints, tvMoney, txvOfferDetail, txvUserName, txvTime;
        ImageView imgUserProfile;
        RatingBar rtbProductRating;


        MyViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view_strip);

            txvOfferTitle = (TextView) itemView.findViewById(R.id.txv_offer_title);
            txvOfferTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            txvOfferDetail = (TextView) itemView.findViewById(R.id.txv_Offer_Detail);
            txvOfferTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            tvPoints = (TextView) itemView.findViewById(R.id.tv_points);
            tvPoints.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvMoney = (TextView) itemView.findViewById(R.id.txv_Money);
            tvMoney.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            txvUserName = (TextView) itemView.findViewById(R.id.txv_User_Name);
            txvUserName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            txvTime = (TextView) itemView.findViewById(R.id.txv_Time);
            txvTime.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            imgUserProfile = (ImageView) itemView.findViewById(R.id.img_User_Profile);
            rtbProductRating = (RatingBar) itemView.findViewById(R.id.rtbProductRating);
        }
    }

    public void refreshList(ArrayList<ReviewModel> rewardArrayList){

        this.rewardArrayList = rewardArrayList;
        notifyDataSetChanged();
    }
}

