package com.rc.grocery.util;

import static com.rc.grocery.BuildConfig.DEBUG;

public class AllConstant {
    //Stripe info
    public static final String STRIPE_PUBLISHABLE_KEY =DEBUG ? "pk_test_Nvm33EZAH0syho7CqCZQWkfC" : "pk_live_QpM8hsQvTgOu12bQRKIZoAv2";
    public static final String STRIPE_SECRET_KEY = DEBUG ? "sk_test_oKFxV6rW7o9C2BYN4bs9nT6k" : "sk_live_E7I3kdXup47DzUHJ5tFXwKrt";

    public static final String TOTAL_VALUE = "TOTAL_VALUE";
    public static final String PRODUCT_BARCODE_ID = "PRODUCT_BARCODE_ID";

    //Asset files
    public static final String ASSET_FILE_ABOUT_US = "about_us.pdf";
    public static final String ASSET_FILE_TERMS_AND_CONDITIONS = "terms_and_conditions.pdf";

    // Support info
    public static final String SUPPORT_CELL = "+1646893385";
    public static final String SUPPORT_EMAIL = "iexpresswholesale1@gmail.com";
}