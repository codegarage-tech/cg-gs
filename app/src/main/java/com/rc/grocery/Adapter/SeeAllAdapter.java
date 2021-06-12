package com.rc.grocery.Adapter;



import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.rc.grocery.Fragment.SubCategoryFragment;
import com.rc.grocery.Model.Category_model;
import com.rc.grocery.R;
import com.rc.grocery.interfacelistener.OnRecyclerViewItemClickListener;
import com.rc.grocery.Fragment.ProductFragment;

public class SeeAllAdapter extends RecyclerView.Adapter<SeeAllAdapter.ViewHolder> implements OnRecyclerViewItemClickListener {

    Context mContext;
    private SeeAllSectionAdapter recyclerViewAdapter;
    public static Typeface typeface;
    private List<Category_model> categoryModelList;


    public SeeAllAdapter(List<Category_model> categoryModelLists, Context context) {
        categoryModelList = categoryModelLists;
        mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.row_categorywise_sub_info, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

       Log.e( "landingInfos", categoryModelList.toString()+ "" );
//        Log.e( "toString", landingInfos.toString() + "" );
        if (categoryModelList != null && categoryModelList.size() > position) {
            if (getItemViewType(position) == position) {
                if (categoryModelList!= null) {
                    holder.tvTitle.setText( categoryModelList.get( position ).getTitle()  );
                    Log.e( "getTitle",   categoryModelList.get( position ).getTitle()+ "" );
                    if (categoryModelList.get( position ).getCategory_sub_datas()!=null) {
                        recyclerViewAdapter = new SeeAllSectionAdapter( categoryModelList.get( position ).getCategory_sub_datas(), mContext );
                        holder.rvMainHorizontal.setAdapter( recyclerViewAdapter );
                        holder.rvMainHorizontal.setNestedScrollingEnabled( false );
                        recyclerViewAdapter.setOnItemClickListener( this, position );
                        holder.rvMainHorizontal.setLayoutManager( new GridLayoutManager( mContext, 2 ));
                        holder.rvMainHorizontal.setHasFixedSize( true );

//                        LinearLayoutManager horizontalManager = new LinearLayoutManager( mContext, LinearLayoutManager.VERTICAL, false );
//                        holder.rvMainHorizontal.setLayoutManager( horizontalManager );
                    }

                }
              }

            }

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onRecyclerViewItemClicked(int verticalPosition, int horizontalPosition, View view) {

        String getId = categoryModelList.get(verticalPosition).getId();
        String getCatTitle = categoryModelList.get(verticalPosition).getTitle();
        String getSubCatTitle = categoryModelList.get(verticalPosition).getCategory_sub_datas().get(horizontalPosition ).getTitle();

        SubCategoryFragment fm = new SubCategoryFragment();
        Bundle args = new Bundle();

        args.putString("cat_id", getId);
        args.putString("cat_title", getCatTitle);
        args.putString("sub_cat_title", getSubCatTitle);
        fm.setArguments(args);
        FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                .addToBackStack(null).commit();
        Log.e( "getId" ,getId+"");
        Log.e( "getCatTitle" ,getCatTitle+"");
        Log.e( "verticalposition" ,verticalPosition+"");
        Log.e( "horizontalPosition" ,horizontalPosition+"");
    }

//
//    @Override
//    public void onRecyclerViewItemClicked(int verticalposition, int horizontalPosition, View view) {
//
//        //Toast.makeText(mContext,mMovies[verticalposition].getMovies().get(horizontalPosition).id + " is clicked", Toast.LENGTH_SHORT).show();
//        Log.e( "verticalposition" ,verticalposition+"");
//        Log.e( "horizontalPosition" ,horizontalPosition+"");
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvSeeAll;
        RecyclerView rvMainHorizontal;
        LinearLayout linCategoryAll;
        ImageView ivCategoryAll;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSeeAll = (TextView) itemView.findViewById(R.id.tv_see_all);
            rvMainHorizontal = (RecyclerView) itemView.findViewById(R.id.rv_main_horizontal);

        }
    }


}