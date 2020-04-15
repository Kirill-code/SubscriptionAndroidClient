package com.subscription.android.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){

            @Override
            public String[] getWeekdays() {
                return new String[]{"","Воскресенье","Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
            }

        };
        if (productList != null && position < productList.size()) {
            VisitEntry product = productList.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE"/*,myDateFormatSymbols*/);
            Date d = new Date();
            String textDay = sdf.format(d);
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");

            String formattedDate = currentDate.format(d);
            holder.dayOfWeek.setText(textDay);
            holder.date.setText(formattedDate);

            int count=17;
            int k=0;
            for (int i=0;i<=count/5;i++){
                LinearLayout layout2 = new LinearLayout(holder.linearLayout.getContext());
                layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout2.setOrientation(LinearLayout.HORIZONTAL);
                for(int j=0;j<10;j++) {
                    if(k==count) break;
                    ImageView imageView = new ImageView(layout2.getContext());
                    imageView.setImageResource(R.drawable.user);
                    imageView.setAdjustViewBounds(true);
                    imageView.setMaxWidth(85);
                    imageView.setMaxHeight(85);

                    layout2.addView(imageView);
                    k++;
                }
                holder.linearLayout.addView(layout2);
            }

        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }
}
