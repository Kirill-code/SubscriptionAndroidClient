package com.subscription.android.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Adapter used to show a simple grid of products.
 */
public class VisitCardRecyclerViewAdapter extends RecyclerView.Adapter<VisitCardViewHolder> {

    private List<VisitEntry> productList;

    public VisitCardRecyclerViewAdapter(List<VisitEntry> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public VisitCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new VisitCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            VisitEntry product = productList.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            holder.productTitle.setText(dayOfTheWeek);
            int count=17;
            int k=0;
            for (int i=0;i<=count/5;i++){
                LinearLayout layout2 = new LinearLayout(holder.linearLayout.getContext());
                layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout2.setOrientation(LinearLayout.HORIZONTAL);
                for(int j=0;j<5;j++) {
                    if(k==count) break;
                    ImageView imageView = new ImageView(layout2.getContext());
                    imageView.setImageResource(R.drawable.user);
                    layout2.addView(imageView);
                    k++;
                }
                holder.linearLayout.addView(layout2);
            }
            /*imageRequester.setImageFromUrl(holder.productImage, product.url);*/
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
