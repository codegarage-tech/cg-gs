package com.rc.grocery.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.rc.grocery.Model.Category_subcat_model;
import com.rc.grocery.R;
import com.rc.grocery.interfacelistener.OnRecyclerViewItemClickListener;


public class SeeAllSectionAdapter extends RecyclerView.Adapter<SeeAllSectionAdapter.ViewHolder> {
    private ArrayList<Category_subcat_model> categorySubList ;
    Context mContext;
    private OnRecyclerViewItemClickListener listener;

    private int verticalPosition;

    public SeeAllSectionAdapter(ArrayList<Category_subcat_model> categorySubLists, Context context) {
        categorySubList = categorySubLists;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.row_categorywise_sub_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Category_subcat_model data = categorySubList.get(position);
        Log.e( "dataList",data.toString()+"" );

        if (categorySubList != null) {

            holder.tvSubCategoryName.setText( data.getTitle() );
        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onRecyclerViewItemClicked(verticalPosition, position, holder.ivSubCategory);
                }
            });

    }


    @Override
    public int getItemCount() {
        return categorySubList.size();
    }


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener, int verticalPosition) {
        this.listener = listener;
        this.verticalPosition = verticalPosition;


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  tvSubCategoryName;
        ImageView ivSubCategory;

        ViewHolder(View itemView) {
            super(itemView);

            tvSubCategoryName = (TextView) itemView.findViewById( R.id.tv_categorywise_sub_name);


        }
    }

}
