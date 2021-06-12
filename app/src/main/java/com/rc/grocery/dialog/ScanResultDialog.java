package com.rc.grocery.dialog;

import android.app.Activity;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ScanResultDialog extends BaseAlertDialog {

    private String mMessage = "";
    private OnClickListener mOnClickListener;

    public ScanResultDialog(Activity activity, String message, OnClickListener onClickListener) {
        super(activity);
        mMessage = message;
        mOnClickListener = onClickListener;
    }

    @Override
    public Builder initView() {
        Builder builder = prepareView("               Scan Result", "Product id: " + mMessage, "Show Product", "Rescan", "", mOnClickListener);

        return builder;
    }
}