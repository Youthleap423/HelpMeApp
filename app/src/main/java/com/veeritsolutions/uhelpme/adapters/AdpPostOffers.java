package com.veeritsolutions.uhelpme.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;

/**
 * Created by ${hitesh} on 7/7/2017.
 */

public class AdpPostOffers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AllHelpOfferModel> list;
    //private Context context;
    protected boolean showLoader;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;


    public AdpPostOffers(Context context, ArrayList<AllHelpOfferModel> list, boolean showLoader) {
        this.list = list;
        //this.context = context;
        this.showLoader = showLoader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_item, parent, false);
            return new VHFooter(itemView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_post_offers, parent, false);
            return new MyViewHolder(itemView);
        }
        throw new RuntimeException("there is no type matches" + viewType + "\n make sure you are using the correct type");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VHFooter) {
            final VHFooter myViewHolder1 = (VHFooter) holder;

            if (showLoader) {
                myViewHolder1.footerView.setVisibility(View.VISIBLE);
            } else {
                myViewHolder1.footerView.setVisibility(View.GONE);
            }
        } else if (holder instanceof MyViewHolder) {

            MyViewHolder myViewHolder = (MyViewHolder) holder;
            // StateListDrawable stateListDrawable = (StateListDrawable) myViewHolder.view.getBackground();
            // stateListDrawable.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.colorViolet, null), PorterDuff.Mode.SRC_ATOP);
            //bgShape1.setColor(ResourcesCompat.getColor(context.getResources(), R.color.colorViolet, null));

            AllHelpOfferModel model = list.get(position);
            myViewHolder.linContatcs.setTag(model);
            myViewHolder.tvAcceptOffer.setTag(model);
            myViewHolder.imgMapView.setTag(model);
            myViewHolder.tvRejectOffer.setTag(model);

            myViewHolder.tvOfferTitle.setText(model.getFirstName() + " " + model.getLastName());
            myViewHolder.tvAmount.setText("$ " + model.getOfferAmount());

            Utils.setImage(model.getProfilePic(), R.drawable.img_user_placeholder, myViewHolder.imgOfferDp);

            if (model.getIsHire() == 0) {
                myViewHolder.tvAcceptOffer.setText("Accept");
                myViewHolder.tvAcceptOffer.setVisibility(View.VISIBLE);
                myViewHolder.tvRejectOffer.setVisibility(View.VISIBLE);
                myViewHolder.imgMapView.setVisibility(View.GONE);
            } else if (model.getIsHire() == Constants.JOB_AWARDED) {
                myViewHolder.tvAcceptOffer.setText("Awarded");
                myViewHolder.imgMapView.setVisibility(View.VISIBLE);
                myViewHolder.tvRejectOffer.setVisibility(View.GONE);
            } else if (model.getIsHire() == Constants.JOB_FINISHED) {
                myViewHolder.tvAcceptOffer.setText("Finished");
                myViewHolder.tvAcceptOffer.setVisibility(View.VISIBLE);
                myViewHolder.imgMapView.setVisibility(View.GONE);
                myViewHolder.tvRejectOffer.setVisibility(View.GONE);
            } else if (model.getIsHire() == Constants.JOB_REJECTED) {
                //myViewHolder.tvAcceptOffer.setVisibility(View.GONE);
                myViewHolder.tvRejectOffer.setVisibility(View.GONE);
                myViewHolder.imgMapView.setVisibility(View.GONE);
                myViewHolder.tvAcceptOffer.setText("Rejected");
            } else if (model.getIsHire() == Constants.JOB_CANCELLED) {
                myViewHolder.tvRejectOffer.setVisibility(View.GONE);
                myViewHolder.imgMapView.setVisibility(View.GONE);
                myViewHolder.tvAcceptOffer.setText("Cancelled");
            }
            // myViewHolder.btnMoreDetails.setTag(model);
        }
    }

    @Override
    public int getItemViewType(int position) {

        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;

    }

    @Override
    public int getItemCount() {

        // If no items are present, there's no need for loader
        if (list == null || list.size() == 0) {
            return 0;
        }

        return list.size() + 1;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        // View view;
        ImageView imgOfferDp, imgMapView;
        TextView tvOfferTitle, tvAmount, tvAcceptOffer, tvRejectOffer;
        //  Button btnMoreDetails, btnDecline;
        LinearLayout linContatcs;

        MyViewHolder(View itemView) {
            super(itemView);

            // view = itemView.findViewById(R.id.view_strip);
            imgOfferDp = (ImageView) itemView.findViewById(R.id.img_profilePhoto);
            imgMapView = (ImageView) itemView.findViewById(R.id.img_mapIcon);

            tvOfferTitle = (TextView) itemView.findViewById(R.id.tv_userName);
            tvOfferTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            tvAmount.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

            tvAcceptOffer = (TextView) itemView.findViewById(R.id.tv_acceptOffer);
            tvAcceptOffer.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            tvRejectOffer = (TextView) itemView.findViewById(R.id.tv_rejectOffer);
            tvRejectOffer.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            linContatcs = (LinearLayout) itemView.findViewById(R.id.lin_contacts);

        }
    }

    private class VHFooter extends RecyclerView.ViewHolder {
        CardView footerView;

        VHFooter(View itemView) {
            super(itemView);
            this.footerView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

    public void showLoading(boolean status) {
        showLoader = status;
    }

    public void refreshList(ArrayList<AllHelpOfferModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
