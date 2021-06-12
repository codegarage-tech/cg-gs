package com.rc.grocery.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rc.grocery.Config.BaseURL;
import com.rc.grocery.Model.Socity_model;
import com.rc.grocery.application.AppController;
import com.rc.grocery.activity.MainActivity;
import com.rc.grocery.R;
import com.rc.grocery.util.ConnectivityReceiver;
import com.rc.grocery.util.CustomVolleyJsonArrayRequest;
import com.rc.grocery.util.CustomVolleyJsonRequest;
import com.rc.grocery.util.Session_management;

/**
 * Created by Rajesh Dabhi on 6/7/2017.
 */

public class AddDeliveryAddressFragment extends Fragment implements View.OnClickListener {

    private static String TAG = AddDeliveryAddressFragment.class.getSimpleName();

    private EditText et_phone, et_name, et_pin, et_house;
    private Button btn_update;
    private TextView tv_phone, tv_name, tv_pin, tv_house, tv_socity, btn_socity;
    private String getsocity = "";

    private Session_management sessionManagement;

    private boolean isEdit = false;

    private String getlocation_id;
    private List<Socity_model> socity_modelList = new ArrayList<>();
    public ProgressDialog mProgressDialog;

    public AddDeliveryAddressFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_delivery_address, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.add_delivery_address));

        sessionManagement = new Session_management(getActivity());

        et_phone = (EditText) view.findViewById(R.id.et_add_adres_phone);
        et_name = (EditText) view.findViewById(R.id.et_add_adres_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_add_adres_phone);
        tv_name = (TextView) view.findViewById(R.id.tv_add_adres_name);
        tv_pin = (TextView) view.findViewById(R.id.tv_add_adres_pin);
        et_pin = (EditText) view.findViewById(R.id.et_add_adres_pin);
        et_house = (EditText) view.findViewById(R.id.et_add_adres_home);
        tv_house = (TextView) view.findViewById(R.id.tv_add_adres_home);
        //   tv_socity = (TextView) view.findViewById(R.id.tv_add_adres_socity);
        btn_update = (Button) view.findViewById(R.id.btn_add_adres_edit);
        //  btn_socity = (TextView) view.findViewById(R.id.btn_add_adres_socity);

        String getsocity_name = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);
        String getsocity_id = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetSocityRequest();
        }
        Bundle args = getArguments();

        if (args != null) {
            getlocation_id = getArguments().getString("location_id");
            String get_name = getArguments().getString("name");
            String get_phone = getArguments().getString("mobile");
            String get_pine = getArguments().getString("pincode");
            String get_socity_id = getArguments().getString("socity_id");
            String get_socity_name = getArguments().getString("socity_name");
            String get_house = getArguments().getString("house");

            if (TextUtils.isEmpty(get_name) && get_name == null) {
                isEdit = false;
            } else {
                isEdit = true;

                Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();

                et_name.setText(get_name);
                et_phone.setText(get_phone);
                et_pin.setText(get_pine);
                et_house.setText(get_house);
                //   btn_socity.setText(get_socity_name);

                sessionManagement.updateSocity(get_socity_name, get_socity_id);
            }
        }

        et_pin.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.d(TAG, "et_pin>>onTextChanged: " + charSequence.toString());
            }

            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                Log.d(TAG, "et_pin>>beforeTextChanged: " + charSequence.toString());
            }

            private String mPreviousText = "";
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "et_pin>>afterTextChanged: " + editable.toString());
                if (editable.length() == 0) {
                    //Do nothing
                    mPreviousText = "";
                } else if (editable.length() > 0 && editable.length() <= 5) {
                    if ((editable.length() >= 2 && editable.toString().startsWith("10")) || (editable.length() == 1 && editable.toString().startsWith("1"))) {
                        mPreviousText = editable.toString();
                    } else {
                        editable.replace(0, editable.length(), mPreviousText).toString();
                        Toast.makeText(getActivity(), getString(R.string.valid_pincode_show_mgs), Toast.LENGTH_SHORT).show();
                    }
                } else if (editable.length() > 5) {
                    editable.replace(0, editable.length(), mPreviousText).toString();
                    Toast.makeText(getActivity(), getString(R.string.valid_pincode_show_mgs), Toast.LENGTH_SHORT).show();
                }
            }
        });

//
//        if (!TextUtils.isEmpty(getsocity_name)) {
//
//            btn_socity.setText(getsocity_name);
//            sessionManagement.updateSocity(getsocity_name, getsocity_id);
//        }

        btn_update.setOnClickListener(this);
        //    btn_socity.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_add_adres_edit) {
            attemptEditProfile();
        }

//        else if (id == R.id.btn_add_adres_socity) {
//
//            /*String getpincode = et_pin.getText().toString();
//
//            if (!TextUtils.isEmpty(getpincode)) {*/
//
//                Bundle args = new Bundle();
//                com.rc.grocery.Fragment fm = new SocietyFragment();
//                //args.putString("pincode", getpincode);
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//            /*} else {
//                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
//            }*/
//
//        }
    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.receiver_mobile_number));
        tv_pin.setText(getResources().getString(R.string.zip_code));
        tv_name.setText(getResources().getString(R.string.receiver_name_req));
        tv_house.setText(getResources().getString(R.string.tv_reg_house));
        //tv_socity.setText(getResources().getString(R.string.tv_reg_socity));

        tv_name.setTextColor(getResources().getColor(R.color.color_grey_800));
        tv_phone.setTextColor(getResources().getColor(R.color.color_grey_800));
        tv_pin.setTextColor(getResources().getColor(R.color.color_grey_800));
        tv_house.setTextColor(getResources().getColor(R.color.color_grey_800));
        // tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));

        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getpin = et_pin.getText().toString();
        String gethouse = et_house.getText().toString();
        String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpin)) {
            tv_pin.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_pin;
            cancel = true;
        }
//        else if (!(et_pin.getText().toString().startsWith("10"))) {
//            tv_pin.setTextColor(getResources().getColor(R.color.colorPrimary));
//            focusView = et_pin;
//            cancel = true;
//            Toast.makeText(getActivity(), getString(R.string.valid_pincode_show_mgs), Toast.LENGTH_SHORT).show();
//        }

        if (TextUtils.isEmpty(gethouse)) {
            tv_house.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_house;
            cancel = true;
        }

//        if (TextUtils.isEmpty(getsocity) && getsocity == null) {
//            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
//            focusView = btn_socity;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
                String getSocietyID = getSocietyID(getpin, socity_modelList);

                Log.e("getpin", getpin + ">>");
                Log.e("getSocietyValue", getSocietyID + "");
                Log.e("socity_modelList", socity_modelList.toString() + "");
                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    if (isEdit) {
                        makeEditAddressRequest(getlocation_id, getpin, getSocietyID, gethouse, getname, getphone);
                    } else {
                        makeAddAddressRequest(user_id, getpin, getSocietyID, gethouse, getname, getphone);
                    }
                }
            }
        }
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddAddressRequest(String user_id, String pincode, String socity_id,
                                       String house_no, String receiver_name, String receiver_mobile) {

        // Tag used to cancel the request
        String tag_json_obj = "json_add_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("pincode", pincode);
        params.put("socity_id", socity_id);
        params.put("house_no", house_no);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);
        Log.e("AddAddressparams", params + ">>>");
        showProgressDialog();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    dismissProgressDialog();
                    Boolean status = response.getBoolean("responce");

                    if (status && response.getJSONObject("data") != null) {
                        ((MainActivity) getActivity()).onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.valid_pincode), Toast.LENGTH_SHORT).show();
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

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeEditAddressRequest(String location_id, String pincode, String socity_id,
                                        String house_no, String receiver_name, String receiver_mobile) {

        // Tag used to cancel the request
        String tag_json_obj = "json_edit_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("location_id", location_id);
        params.put("pincode", pincode);
        params.put("socity_id", socity_id);
        params.put("house_no", house_no);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);
        Log.e("params", params + ">>>");
        showProgressDialog();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.EDIT_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    dismissProgressDialog();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("data");
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                        ((MainActivity) getActivity()).onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.valid_pincode), Toast.LENGTH_SHORT).show();

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

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetSocityRequest() {

        // Tag used to cancel the request
        String tag_json_obj = "json_socity_req";

        /*Map<String, String> params = new HashMap<String, String>();
        params.put("pincode", pincode);*/

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.GET,
                BaseURL.GET_SOCITY_URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Socity_model>>() {
                }.getType();

                socity_modelList = gson.fromJson(response.toString(), listType);
                Log.e("socity_modelList", socity_modelList.toString() + ">>>");
//                adapter = new Socity_adapter(socity_modelList);
//                rv_socity.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                if (socity_modelList.isEmpty()) {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if (getActivity() != null) {

                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public String getSocietyID(String pinCode, List<Socity_model> societyModelList) {
        {
            String societyID = "";
            Log.e("pinCode>>>", pinCode + ">>>");

            for (Socity_model socityModel : societyModelList) {
                if (socityModel.getPincode().equalsIgnoreCase(pinCode)) {
                    Log.e("socityModel", socityModel.getSocity_id() + ">>>");

                    //searchMatches.
                    return socityModel.getSocity_id();
                }
            }
            return "";
        }
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
