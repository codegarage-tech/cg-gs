package com.rc.grocery.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rc.grocery.Config.BaseURL;
import com.rc.grocery.application.AppController;
import com.rc.grocery.activity.MainActivity;
import com.rc.grocery.R;
import com.rc.grocery.util.ConnectivityReceiver;
import com.rc.grocery.util.CustomVolleyJsonRequest;
import com.rc.grocery.util.DatabaseHandler;
import com.rc.grocery.util.Session_management;

import static com.rc.grocery.util.AllConstant.TOTAL_VALUE;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class DeliveryPaymentDetailFragment extends Fragment {

    private static String TAG = DeliveryPaymentDetailFragment.class.getSimpleName();

    private TextView tv_timeslot, tv_address, tv_item, tv_total;
    private Button btn_order, btnPayNow;

    private String getlocation_id = "";
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private String location_id = "";
    private String getaddress = "";
    private String deli_charges = "";
    // private int deli_charges;
    private double total = 0.0;

    private DatabaseHandler db_cart;
    private Session_management sessionManagement;

    public DeliveryPaymentDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.payment_detail));

        db_cart = new DatabaseHandler(getActivity());
        sessionManagement = new Session_management(getActivity());

        tv_timeslot = (TextView) view.findViewById(R.id.textTimeSlot);
        tv_address = (TextView) view.findViewById(R.id.txtAddress);
        //tv_item = (TextView) view.findViewById(R.id.textItems);
        //tv_total = (TextView) view.findViewById(R.id.textPrice);
        tv_total = (TextView) view.findViewById(R.id.txtTotal);

        btn_order = (Button) view.findViewById(R.id.btn_pay_later);
        btnPayNow = (Button) view.findViewById(R.id.btn_pay_now);

        getdate = getArguments().getString("getdate");
        gettime = getArguments().getString("time");
        getlocation_id = getArguments().getString("location_id");
        deli_charges = getArguments().getString("deli_charges");
        getaddress = getArguments().getString("address");
        Log.d("getlocation>>id", getlocation_id + "");
        Log.d("getaddress>>", getaddress + "");

        tv_timeslot.setText(getdate + " " + gettime);
        tv_address.setText(getaddress);


        //tv_total.setText("" + db_cart.getTotalAmount());
        //tv_item.setText("" + db_cart.getCartCount());
        if (Integer.parseInt(db_cart.getTotalAmount()) > 250) {
            total = Double.parseDouble(db_cart.getTotalAmount());
            tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
                    getResources().getString(R.string.amount) + db_cart.getTotalAmount() + "\n" +
                    getResources().getString(R.string.delivery_charge) + "0.0" + "\n" +
                    getResources().getString(R.string.total_amount) +
//                    db_cart.getTotalAmount() + " + " + "0" +" = " + Double.parseDouble(db_cart.getTotalAmount()) + " " + getResources().getString(R.string.currency));
                    Double.parseDouble(db_cart.getTotalAmount()) + " " + getResources().getString(R.string.currency));

        } else {
            total = Double.parseDouble(db_cart.getTotalAmount()) + Integer.parseInt(deli_charges);

            tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
                    getResources().getString(R.string.amount) + db_cart.getTotalAmount() + "\n" +
                    getResources().getString(R.string.delivery_charge) + deli_charges + "\n" +
                    getResources().getString(R.string.total_amount) +
//                    db_cart.getTotalAmount() + " + " + deli_charges + " = " + total + " " + getResources().getString(R.string.currency));
                    total + " " + getResources().getString(R.string.currency));
        }


        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    attemptOrder();
                } else {
                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("total", total + "??");
                if (total >= 50) {
                    String totalValue = String.valueOf(total);
                    Bundle args = new Bundle();
                    Fragment fm = new PayNowStripeDeliveryFragment();
                    args.putString(TOTAL_VALUE, totalValue);
                    args.putString("getdate", getdate);
                    args.putString("time", gettime);
                    args.putString("location_id", getlocation_id);
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                } else {

                    Toast.makeText(getActivity(), getResources().getString(R.string.minimum_order) + " : " + total, Toast.LENGTH_SHORT).show();

                }

            }
        });

        return view;
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

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
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
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
