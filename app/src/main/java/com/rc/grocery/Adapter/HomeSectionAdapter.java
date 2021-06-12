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

import com.rc.grocery.Config.BaseURL;
import com.rc.grocery.Model.Category_subcat_model;
import com.rc.grocery.R;
import com.rc.grocery.interfacelistener.OnRecyclerViewItemClickListener;
import com.rc.grocery.util.AppUtil;


public class HomeSectionAdapter extends RecyclerView.Adapter<HomeSectionAdapter.ViewHolder> {
    private ArrayList<Category_subcat_model> categorySubList ;
    Context mContext;
    private OnRecyclerViewItemClickListener listener;

    private int verticalPosition;

    public HomeSectionAdapter(ArrayList<Category_subcat_model> categorySubLists, Context context) {
        categorySubList = categorySubLists;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.row_sub_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e( "mMovies",categorySubList.toString()+"pos" );
        Log.e( "verticalPosition",verticalPosition+"pos" );

        Category_subcat_model data = categorySubList.get(position);
        Log.e( "dataList",data.toString()+"" );
        Log.e( "getImage",BaseURL.BASE_URL+data.getImage()+"" );

        if (categorySubList != null) {

            holder.tvSubCategoryName.setText( data.getTitle() );
           AppUtil.loadImage(mContext, holder.ivSubCategory, BaseURL.IMG_CATEGORY_URL+data.getImage(), false, true);
//            Glide.with(mContext)
//                    .load(BaseURL.IMG_CATEGORY_URL+data.getImage())
//                    .placeholder(R.mipmap.ic_launcher)
//                    .crossFade()
//                    .diskCacheStrategy( DiskCacheStrategy.ALL)
//                    .dontAnimate()
//                    .into(holder.ivCategory);

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

            ivSubCategory = (ImageView) itemView.findViewById( R.id.iv_sub_cat );
            tvSubCategoryName = (TextView) itemView.findViewById( R.id.tv_sub_cat_name);


        }
    }

}
