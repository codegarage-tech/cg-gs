package com.rc.grocery.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.rc.grocery.R;

/**
 * Created by Rajesh Dabhi on 4/7/2017.
 */

public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.MyViewHolder> {

    private List<String> modelList;
    private String currentDateValues;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_socity_name);
        }
    }

    public TimeListAdapter(List<String> modelList, String currentDateValue) {
        this.modelList = modelList;
        this.currentDateValues = currentDateValue;
    }

    @Override
    public TimeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_socity_rv, parent, false);

        context = parent.getContext();

        return new TimeListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Socity_model mList = modelList.get(position);
        Log.e("currentDateValues",currentDateValues+"");
        if (currentDateValues.equalsIgnoreCase("0")){
            holder.title.setTextColor(Color.RED);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.title.setTextColor(context.getColor(R.color.color_grey_800));
            }

        }
        holder.title.setText(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
