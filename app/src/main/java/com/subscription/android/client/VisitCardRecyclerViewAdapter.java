package com.subscription.android.client;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.subscription.android.client.model.DTO.VisitsDTO;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Adapter used to show a simple grid of visits.
 */
public class VisitCardRecyclerViewAdapter extends RecyclerView.Adapter<VisitCardViewHolder> {

    private List<VisitsDTO> visitsList;
    private static final String TAG = VisitCardRecyclerViewAdapter.class.getName();

    public VisitCardRecyclerViewAdapter(List<VisitsDTO> visitsList) {
        this.visitsList = visitsList;
    }

    @NonNull
    @Override
    public VisitCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new VisitCardViewHolder(layoutView);
    }



    @Override
    public void onBindViewHolder(@NonNull VisitCardViewHolder holder, int position) {
        DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols() {
            @Override
            public String[] getWeekdays() {
                return new String[] {"", "Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
            }
        };
        if (visitsList != null && position < visitsList.size()) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE",myDateFormatSymbols);
            Date textDate= new Date();
            try{
                textDate=new SimpleDateFormat("yyyy-MM-dd").parse(visitsList.get(position).getDate());

            }catch (ParseException ex){
                Log.e(TAG, ex.getMessage());
            }

            String textDay = sdf.format(textDate);

            holder.dayOfWeek.setText(textDay);
            holder.date.setText(visitsList.get(position).getDate());
            holder.numberOfVisits.setText(Double.toString(visitsList.get(position).getVisits_count()).substring(0,1));
/* Used for Image grid of visits
            int count=17;
            int k=0;
            for (int i=0;i<=count/5;i++){
                LinearLayout layout2 = new LinearLayout(holder.linearLayout.getContext());
                layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout2.setOrientation(LinearLayout.HORIZONTAL);
                ImageView imageView = new ImageView(layout2.getContext());
                imageView.setImageResource(R.drawable.user);
                imageView.setAdjustViewBounds(true);
                imageView.setMaxWidth(85);
                imageView.setMaxHeight(85);

                layout2.addView(imageView);
                for(int j=0;j<10;j++) {
                    if(k==count) break;

                    k++;
                }
                holder.linearLayout.addView(layout2);
            }*/

        }
    }


    @Override
    public int getItemCount() {
        return visitsList.size();
    }
}
