package com.rc.grocery.util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rc.grocery.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.RequestBuilder;
//import com.bumptech.glide.RequestManager;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AppUtil {

    public static boolean isSimSupport(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static <C> List<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

//
//    public void startActivity(Class<?> clazz) {
//        startActivity(new Intent(MainActivity.this, clazz));
//    }

    //Toolbar
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int toPx(Context context, int value) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (value * density);
    }

    public static void doMarqueeTextView(TextView textView) {
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setMarqueeRepeatLimit(-1);
        textView.setSelected(true);
    }

    public static String getAppVersion(Context context) {
        String appVersion = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }


    public static int getGridSpanCount(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.item_product_width);
        return Math.round(screenWidth / cellWidth);
    }


    public static String formatLocationInfo(String myString) {
        String location = "";
        if (myString != null && myString.trim().length() > 0) {
            location = myString.startsWith(",") ? myString.substring(1).trim().replaceAll(", ,", ",") : myString.replaceAll(", ,", ",");
        }
        return location;
    }

    public static <T> void loadImage(Context context, ImageView imageView, T imageSource, boolean isRoundedImage, boolean isPlaceHolder) {
        try {
            RequestManager requestManager = Glide.with(context);
            RequestBuilder requestBuilder = requestManager.asBitmap();

            //Dynamic loading without caching while update need for each time loading
//            requestOptions.signature(new ObjectKey(System.currentTimeMillis()));
            //If Cache needed
//            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

            //If Cache needed
            RequestOptions requestOptionsCache = new RequestOptions();
            requestOptionsCache.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            requestBuilder.apply(requestOptionsCache);

            //For placeholder
            if (isPlaceHolder) {
                RequestOptions requestOptionsPlaceHolder = new RequestOptions();
                requestOptionsPlaceHolder.placeholder(R.drawable.iexpresslogo);
                requestBuilder.apply(requestOptionsPlaceHolder);
            }

            //For error
            RequestOptions requestOptionsError = new RequestOptions();
            requestOptionsError.error(R.drawable.iexpresslogo);
            requestBuilder.apply(requestOptionsError);

            //For rounded image
            if (isRoundedImage) {
                RequestOptions requestOptionsRounded = new RequestOptions();
                requestOptionsRounded.circleCrop();
                requestOptionsRounded.autoClone();
                requestBuilder.apply(requestOptionsRounded);
            }

            //Generic image source
            T mImageSource = null;
            if (imageSource instanceof String) {
                if (!isNullOrEmpty((String) imageSource)) {
                    mImageSource = imageSource;
                }
            } else if (imageSource instanceof Integer) {
                mImageSource = imageSource;
            }
            requestBuilder.load((mImageSource != null) ? mImageSource : R.mipmap.ic_launcher);

            //Load into image view
            requestBuilder.into(imageView);

//            Glide
//                    .with(context)
//                    .asBitmap()
//                    .load((mImageSource != null) ? mImageSource : R.mipmap.ic_launcher)
//                    .apply(requestOptions)
//                    .into(imageView);

        } catch (Exception e) {
        }
    }


    public static ValueAnimator flashView(final View viewGroup, long time) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                viewGroup.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        animator.setDuration(time);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(-1);
        animator.start();
        return animator;
    }

    public static float formatFloat(float value) {
        float twoDigitsFloat = 0.00f;
        try {
//            DecimalFormat decimalFormat = new DecimalFormat("#.##");
//            twoDigitsFloat = Float.valueOf(decimalFormat.format(value));
            twoDigitsFloat = Float.parseFloat(String.format("%.02f", value));
        } catch (Exception ex) {
            twoDigitsFloat = 0.00f;
            ex.printStackTrace();
        }
        return twoDigitsFloat;
    }

    public static String formatDoubleString(float value) {
        String twoDigitsDouble = "0.00";
        try {
//            DecimalFormat decimalFormat = new DecimalFormat("#.##");
//            twoDigitsFloat = Float.valueOf(decimalFormat.format(value));
            double doubleValue = (double) value;
            twoDigitsDouble = String.format("%.2f", doubleValue);

        } catch (Exception ex) {
            twoDigitsDouble = "0.00";
            ex.printStackTrace();
        }
        return twoDigitsDouble;
    }


    public static int getTotalLastPrice(int lastSale, int secondLastSale) {
        int tempSubTotalPrice = lastSale - secondLastSale;
//        if (promotionalDiscount > 0) {
//            float discount = ((subtotal * promotionalDiscount) / 100);
//            tempSubTotalPrice = subtotal - discount;
//        } else {
//            tempSubTotalPrice = subtotal;
//        }

        return tempSubTotalPrice;
    }


    public static String getLastFourDigits(final String word) {
        if (word.length() == 4) {
            return word;
        } else if (word.length() > 4) {
            return word.substring(word.length() - 4);
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("word has less than 4 characters!");
        }
    }

    public static boolean isNullOrEmpty(String myString) {
        if (myString == null) {
            return true;
        } else {
            return myString.length() == 0 || myString.equalsIgnoreCase("null") || myString.equalsIgnoreCase("");
        }
    }

    public static String optStringNullCheckValue(final String myString) {
        if (myString == null || myString.equalsIgnoreCase("") || myString.equalsIgnoreCase("null"))
            return "";
        else
            return myString;
    }

    public static int compareDates(String inputDate, String outputFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        int dateDifference;
        try {
            Date date1 = sdf.parse(inputDate);
            Date date2 = sdf.parse(outputFormat);
            //CompareTo() Method
            dateDifference = date2.compareTo(date1);

            System.out.println("Date diff : " + dateDifference);    //1
            Log.e("dateDifference", dateDifference + ">>>");

            //   JavaCompareDateExample.printDateCompareInfo(srcDate, destDate, ret);

            if (dateDifference > 0) {
                System.out.println(date1 + " > " + date2);       //prints
                return 1;
            } else if (dateDifference < 0) {
                System.out.println(date1 + " < " + date2); //does not print
                return -1;
            } else if (date1.compareTo(date2) == 0) {
                System.out.println(date1 + " = " + date2);      //does not print
                return 0;
            }

            boolean isDateAfter = date1.after(date2);

            if (isDateAfter)
                System.out.println(date2 + " lies before " + date1);    //true
            else
                System.out.println(date2 + " lies after " + date1);     //false

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

//        //before() Method
//        boolean isDateBefore = date1.before(date2);
//
//        if(isDateBefore)
//            System.out.println(date1 + " lies before " + date2);    //false
//        else
//            System.out.println(date1 + " lies after " + date2);     //true
//
        //after() Method


    }

    public static void setCartButtonBackground(Context context, View view) {
        if (view.getBackground() == null) {
            view.setBackgroundResource(R.drawable.rounded_corner_textview_purple);
        } else if (view.getBackground().getConstantState().equals(context.getResources().getDrawable(R.drawable.rounded_corner_textview_purple).getConstantState())) {
            view.setBackgroundResource(R.drawable.rounded_corner_textview_grey);
        } else if (view.getBackground().getConstantState().equals(context.getResources().getDrawable(R.drawable.rounded_corner_textview_grey).getConstantState())) {
            view.setBackgroundResource(R.drawable.rounded_corner_textview_purple);
        } else {
            view.setBackgroundResource(R.drawable.rounded_corner_textview_purple);
        }
    }

    public static void setCartButtonBackground(Context context, View view, boolean isEditing) {
        if (isEditing) {
            view.setBackgroundResource(R.drawable.rounded_corner_textview_purple);
        } else {
            view.setBackgroundResource(R.drawable.rounded_corner_textview_grey);
        }
    }
}