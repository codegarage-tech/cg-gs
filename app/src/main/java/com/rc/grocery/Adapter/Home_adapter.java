package com.rc.grocery.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.rc.grocery.Config.BaseURL;
import com.rc.grocery.Model.Category_model;
import com.rc.grocery.R;
import com.rc.grocery.util.AppUtil;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class Home_adapter extends RecyclerView.Adapter<Home_adapter.MyViewHolder> {

    private List<Category_model> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_title);
            image = (ImageView) view.findViewById(R.id.iv_home_img);
        }
    }

    public Home_adapter(List<Category_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Home_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home_rv, parent, false);

        context = parent.getContext();

        return new Home_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Home_adapter.MyViewHolder holder, int position) {
        Category_model mList = modelList.get(position);

        AppUtil.loadImage(context,holder.image, BaseURL.IMG_CATEGORY_URL+mList.getImage(), false, true);

//        Glide.with(context)
//                .load(BaseURL.IMG_CATEGORY_URL+mList.getImage())
//                .placeholder(R.mipmap.ic_launcher)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontAnimate()
//                .into(holder.image);

        holder.title.setText(mList.getTitle());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

