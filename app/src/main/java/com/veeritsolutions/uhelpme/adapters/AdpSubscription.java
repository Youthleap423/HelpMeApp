package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.SubscriptionModel;

import java.util.ArrayList;

/**
 * Created by VEER7 on 7/14/2017.
 */

public class AdpSubscription extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<SubscriptionModel> PackagesList;

    public AdpSubscription(Context mContext, ArrayList<SubscriptionModel> PackagesList) {
        this.mContext = mContext;
        this.PackagesList = PackagesList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_subscription, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {

            SubscriptionModel subscriptionModel = PackagesList.get(position);

            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.linPackges.setTag(subscriptionModel);

            myViewHolder.tvPackageName.setText(subscriptionModel.getPackageName());
            myViewHolder.tvCreateOn.setText(subscriptionModel.getPaymentTime());
            myViewHolder.tvMoney.setText("$ " +subscriptionModel.getPaymentAmount());

        }

    }

    @Override
    public int getItemCount() {

        return PackagesList.size();
    }

    public void refreshList(ArrayList<SubscriptionModel> PackagesList) {
        this.PackagesList = PackagesList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPackageName;
        TextView tvCreateOn;
        TextView tvMoney;
        TextView tvPost;
        LinearLayout linPackges;

        MyViewHolder(View itemView) {
            super(itemView);

//            imgCategory = (ImageView) itemView.findViewById(R.id.img_categoryIcon);
            tvPackageName = (TextView) itemView.findViewById(R.id.txv_package1_name);
            tvPackageName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
            tvCreateOn = (TextView) itemView.findViewById(R.id.txv_date);
            tvCreateOn.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
            tvMoney = (TextView) itemView.findViewById(R.id.txv_Monay);
            tvMoney.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
//            tvPackageCredits = (TextView) itemView.findViewById(R.id.txv_post);
            linPackges = (LinearLayout) itemView.findViewById(R.id.lin_subscription);
        }
    }
}
