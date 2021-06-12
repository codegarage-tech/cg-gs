package com.rc.grocery.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import com.rc.grocery.Adapter.SeeAllAdapter;
import com.rc.grocery.Config.BaseURL;
import com.rc.grocery.Model.Category_model;
import com.rc.grocery.application.AppController;
import com.rc.grocery.activity.MainActivity;
import com.rc.grocery.R;

import com.rc.grocery.util.ConnectivityReceiver;
import com.rc.grocery.util.CustomVolleyJsonRequest;
import com.rc.grocery.util.Session_management;

/**
 * Created by Rajesh Dabhi on 28/6/2017.
 */

public class SeeAllFragment extends Fragment implements View.OnClickListener {

    private static String TAG = SeeAllFragment.class.getSimpleName();

    private RecyclerView rvCategoryWiseProduct;
    private List<Category_model> categorModelArrayList= new ArrayList<>(  );

    private SeeAllAdapter seeAllAdapter;
    private boolean isSubcat = false;


    private Session_management sessionManagement;
    public ProgressDialog mProgressDialog;

    public SeeAllFragment() {
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
        View view = inflater.inflate(R.layout.fragment_categorywise_product, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.txt_products));
        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Fragment fm = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });
      //  getIntentValue();
        initUI(view);


        return view;
    }

    private void getIntentValue() {
        if (getActivity().getIntent()!=null) {
            Bundle bundle = getActivity().getIntent().getExtras();
            categorModelArrayList = bundle.getParcelableArrayList( "Category_Array" );
        }
    }

    private void initUI(View view) {

        rvCategoryWiseProduct = (RecyclerView) view.findViewById(R.id.rv_categorywise_product);
        rvCategoryWiseProduct.setNestedScrollingEnabled(false);
        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetCategoryRequest("");
        }
    }


    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetCategoryRequest(String parent_id) {
        categorModelArrayList.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_category_req";

        isSubcat = false;
        showProgressDialog();

        Map<String, String> params = new HashMap<String, String>();
        if (parent_id != null && parent_id != "") {
            params.put("parent", parent_id);
            isSubcat = true;
        }

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest( Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Category_model>>() {
                        }.getType();
                        dismissProgressDialog();
                        categorModelArrayList = gson.fromJson(response.getString("data"), listType);
                        Log.e( "category_modelList", categorModelArrayList.size()+"");
                        if (categorModelArrayList != null && categorModelArrayList.size() > 0) {

                            seeAllAdapter = new SeeAllAdapter( categorModelArrayList, getActivity() );
                            rvCategoryWiseProduct.setAdapter(seeAllAdapter);
                            LinearLayoutManager verticalManager = new LinearLayoutManager( getActivity(), LinearLayoutManager.VERTICAL, false );
                            rvCategoryWiseProduct.setLayoutManager( verticalManager );
                            seeAllAdapter.notifyDataSetChanged();
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
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_pro_edit) {
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem cart = menu.findItem(R.id.action_cart);
        cart.setVisible(false);
        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return false;
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
