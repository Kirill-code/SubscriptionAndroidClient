package com.subscription.android.client;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VisitCardViewHolder extends RecyclerView.ViewHolder {

    public TextView dayOfWeek;
    public TextView date;
    public LinearLayout linearLayout;



    public VisitCardViewHolder(@NonNull View itemView) {
        super(itemView);
        dayOfWeek = itemView.findViewById(R.id.card_title);
        linearLayout=itemView.findViewById(R.id.visitor_stamps);
        date=itemView.findViewById(R.id.card_date);
    }
}
