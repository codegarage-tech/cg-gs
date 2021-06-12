package com.rc.grocery.Adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.security.InvalidParameterException;

import com.rc.grocery.Model.Category_model;
import com.rc.grocery.viewholder.CategoryViewHolder;

/**
 * @author Md. Hozrot Belal
 * Email: belal.cse.brur@gmail.com
 */
public class CategorySeeAllAdapter extends RecyclerArrayAdapter<Category_model> {

    private static final int VIEW_TYPE_REGULAR = 1;
    Context mContext;

    public CategorySeeAllAdapter(Context context) {
        super( context );
        mContext = context;
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new CategoryViewHolder( parent,mContext );
            default:
                throw new InvalidParameterException();
        }
    }
}