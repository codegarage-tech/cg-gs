package com.rc.grocery.viewholder;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import com.rc.grocery.Config.BaseURL;
import com.rc.grocery.Model.Category_model;

import com.rc.grocery.R;
import com.rc.grocery.util.AppUtil;
import com.rc.grocery.Fragment.ProductFragment;

/**
 * @author Md. Hozrot Belal
 *         Email: belal.cse.brur@gmail.com
 */
public class CategoryViewHolder extends BaseViewHolder<Category_model> {


    TextView  tvCategoryName;
    ImageView ivCategory;
    LinearLayout contentView;
    Context mContext;

    public CategoryViewHolder(ViewGroup parent, Context context) {
        super( parent, R.layout.row_category_feature );
        mContext = context;

        ivCategory = (ImageView) itemView.findViewById( R.id.iv_category );
        tvCategoryName = (TextView) itemView.findViewById( R.id.tv_category_title);
        contentView = (LinearLayout) itemView.findViewById( R.id.content_view);

    }

    @Override
    public void setData(final Category_model data) {

        Log.e("Category_model",data.toString()+">>");
        tvCategoryName.setText(data.getTitle());
        AppUtil.loadImage(getContext(), ivCategory, BaseURL.IMG_CATEGORY_URL+data.getImage(), true, true);

//
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getid = data.getId();
                String getcat_title = data.getTitle();

                Bundle args = new Bundle();
                ProductFragment fm = new ProductFragment();
                args.putString("cat_id", getid);
                args.putString("cat_title", getcat_title);
                fm.setArguments(args);
                FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        });



    }


}
