package com.rc.grocery.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rc.grocery.Adapter.Product_adapter;
import com.rc.grocery.Config.BaseURL;
import com.rc.grocery.Model.Product_model;
import com.rc.grocery.application.AppController;
import com.rc.grocery.activity.MainActivity;
import com.rc.grocery.R;
import com.rc.grocery.util.ConnectivityReceiver;
import com.rc.grocery.util.CustomVolleyJsonRequest;

import static com.rc.grocery.util.AllConstant.PRODUCT_BARCODE_ID;

/**
 * Created by Rajesh Dabhi on 14/7/2017.
 */

public class BarcodeSearchFragment extends Fragment {

    private static String TAG = BarcodeSearchFragment.class.getSimpleName();

    String productBarcodeID = "";
    private EditText et_search;
    private Button btn_search;
    private RecyclerView rv_search;

    private List<Product_model> product_modelList = new ArrayList<>();
    private Product_adapter adapter_product;

    public BarcodeSearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_barcode_search, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.scan));
        getIntentValue();
        initUI(view);
        netWorkCheck();

        return view;
    }



    private void initUI(View view) {
        rv_search = (RecyclerView) view.findViewById(R.id.rv_search);
        rv_search.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getIntentValue() {

        if (getArguments()!=null){
             productBarcodeID = getArguments().getString(PRODUCT_BARCODE_ID);
            Log.e("productBarcodeID",productBarcodeID+"");

        }
    }
    private void netWorkCheck() {
        if(ConnectivityReceiver.isConnected()){
            makeGetProductRequest(productBarcodeID);
        }else{
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetProductRequest(String searchProductID) {

        // Tag used to cancel the request
        String tag_json_obj = "json_product_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("barcode", searchProductID);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_BARCODE_SEARCH, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();

                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        Log.e("product_modelList",product_modelList.toString()+"");
                        adapter_product = new Product_adapter(product_modelList, getActivity());
                        rv_search.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();

                        if(getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

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
