package com.rc.grocery.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.model.Charge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rc.grocery.Config.BaseURL;
import com.rc.grocery.Model.card.StripeCard;
import com.rc.grocery.application.AppController;
import com.rc.grocery.activity.MainActivity;
import com.rc.grocery.R;
import com.rc.grocery.util.ConnectivityReceiver;
import com.rc.grocery.util.CustomVolleyJsonRequest;
import com.rc.grocery.util.DatabaseHandler;
import com.rc.grocery.util.Session_management;

import static com.rc.grocery.util.AllConstant.STRIPE_PUBLISHABLE_KEY;
import static com.rc.grocery.util.AllConstant.STRIPE_SECRET_KEY;
import static com.rc.grocery.util.AllConstant.TOTAL_VALUE;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class PayNowStripeDeliveryFragment extends Fragment {

    private static String TAG = PayNowStripeDeliveryFragment.class.getSimpleName();

    private TextView tv_timeslot, tv_address, tv_item, tv_total;
    private Button btnPayment;

    private String getlocation_id = "";
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private String getaddress = "";
    private String totalValue;
    private int deli_charges;

    private DatabaseHandler db_cart;
    private Session_management sessionManagement;
    private CardMultilineWidget mCardMultilineWidget;
    public ProgressDialog mProgressDialog;

    public PayNowStripeDeliveryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_info_screen, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.pay_now));

        db_cart = new DatabaseHandler(getActivity());
        sessionManagement = new Session_management(getActivity());
        getIntentValue();
        initUI(view);
        actionListener();

        return view;
    }


    private void getIntentValue() {

        if (getArguments()!=null){
            totalValue = getArguments().getString(TOTAL_VALUE);
            getdate = getArguments().getString("getdate");
            gettime = getArguments().getString("time");
            getlocation_id = getArguments().getString("location_id");
            Log.e("totalValue",totalValue+"");
            Log.e("getlocation_id",getlocation_id+"");

        }
    }

    private void initUI(View parentView) {

        btnPayment = parentView.findViewById( R.id.btn_payment );
        mCardMultilineWidget = parentView.findViewById( R.id.card_multiline_widget );
        // retrive data from cart database


    }

    private void actionListener() {
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    doCardValidation();
                } else {
                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });

    }

    private void doCardValidation() {
        if (!mCardMultilineWidget.validateAllFields()) {
            Toast.makeText( getActivity(), getString( R.string.toast_please_input_valid_card_information ), Toast.LENGTH_SHORT ).show();
            return;
        }

        Card card = mCardMultilineWidget.getCard();
        if (card == null) {
            Toast.makeText( getActivity(), getString( R.string.toast_something_went_wrong ), Toast.LENGTH_SHORT ).show();
            return;
        }

        StripeCard stripeCard = new StripeCard( card.getNumber(), card.getLast4(), card.getName(), card.getExpMonth(), card.getExpYear(), card.getCVC(), card.getAddressZip() );
        Log.e( "stripeCard", stripeCard.toString() + "" );
        submitCardInfo( stripeCard );

    }
    /******************
     * Stripe methods *
     ******************/
    private void submitCardInfo(final StripeCard stripeCard) {

        if (stripeCard != null) {
            Card card = new Card(stripeCard.getCardNumber(), stripeCard.getCardExpireMonth(), stripeCard.getCardExpireYear(), stripeCard.getCardCvc());

            if (card.validateCard()) {
                showProgressDialog();

                Stripe stripe = new Stripe(getActivity(), STRIPE_PUBLISHABLE_KEY);
                stripe.createToken(card, new TokenCallback() {
                            public void onSuccess(Token token) {
                                //Token successfully created.
                                //Create a charge or save token to the server and use it later
                                Log.e("totalPrice>>>", token.toString() + "");
                                Log.e("getCard>>>", token.getCard() + "");
                                Log.e("getId>>>", token.getId() + "");
                                Log.e("getBankAccount>>>", token.getBankAccount() + "");
//                                dismissProgressDialog();
                             //   doRequestAddUserPayout(stripeCard);
                               new doCharge(getActivity(), token,  Float.parseFloat(totalValue)).execute();
                            }
                            public void onError(Exception error) {
                                dismissProgressDialog();

                                Toast.makeText(getActivity(), getString(R.string.toast_error_on_creating_token), Toast.LENGTH_LONG).show();
                            }
                        }
                );
            } else {
                Toast.makeText(getActivity(), getString(R.string.toast_invalid_card), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.toast_please_select_card), Toast.LENGTH_SHORT).show();
        }
    }

    private class doCharge extends AsyncTask<String, Long, Charge> {

        private Context mContext;
        private Token mToken;
        private float mAmount = 0.0f;

        public doCharge(Context context, Token token, float amount) {
            mContext = context;
            mToken = token;
            mAmount = amount;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Charge doInBackground(String... params) {
            try {
                int amount = (int) (mAmount * 100);
                Log.d(TAG, "ChargeAmount: " + amount + " cent");
                Log.d(TAG, "ChargeAmount: $" + (int) mAmount);

                Map<String, Object> chargeParams = new HashMap<String, Object>();
                chargeParams.put("amount", amount);
                chargeParams.put("currency", "usd");
                chargeParams.put("description", "Charged $" + (int) mAmount + " from Android");
                chargeParams.put("source", mToken.getId());

                com.stripe.Stripe.apiKey = STRIPE_SECRET_KEY;

                return Charge.create(chargeParams);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Charge charge) {
            if (charge != null && charge.getPaid() && charge.getStatus().equalsIgnoreCase("succeeded")) {
                Log.d(TAG, "ChargeInfo: " + charge.toString() + "\nTransaction_id: " + charge.getId());

                //calling Order
                  attemptOrder();
                //new DoOrderTask(mContext, paramCheckout).execute();
            } else {
                dismissProgressDialog();
                Toast.makeText(getActivity(), getString(R.string.toast_payment_is_not_successful), Toast.LENGTH_LONG).show();
            }
        }
    }



    private void attemptOrder() {

        // retrive data from cart database
        ArrayList<HashMap<String, String>> items = db_cart.getCartAll();
        if (items.size() > 0) {
            JSONArray passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);


                JSONObject jObjP = new JSONObject();

                try {
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("qty", map.get("qty"));
                    jObjP.put("unit_value", map.get("unit_value"));
                    jObjP.put("unit", map.get("unit"));
                    jObjP.put("price", map.get("price"));

                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

            if (ConnectivityReceiver.isConnected()) {

                Log.e(TAG, "from:" + gettime + "\ndate:" + getdate +
                        "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + "\ndata:" + passArray.toString());

                makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, passArray);
            }
        }
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddOrderRequest(String date, String gettime, String userid,
                                     String location, JSONArray passArray) {

        // Tag used to cancel the request
        String tag_json_obj = "json_add_order_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("date", date);
        params.put("time", gettime);
        params.put("user_id", userid);
        params.put("location", location);
        params.put("data", passArray.toString());
        Log.e("params",params+"params");
        showProgressDialog();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    dismissProgressDialog();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("data");

                        db_cart.clearCart();
                        ((MainActivity) getActivity()).setCartCounter("" + db_cart.getCartCount());

                        Bundle args = new Bundle();
                        Fragment fm = new ThanksFragment();
                        args.putString("msg", msg);
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                .addToBackStack(null).commit();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                dismissProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }




    /***************************
     * Progress com.rc.grocery.dialog methods *
     ***************************/
    public ProgressDialog showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getResources().getString(R.string.view_loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }
            });
        }

        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        return mProgressDialog;
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
