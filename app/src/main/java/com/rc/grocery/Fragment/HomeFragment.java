package com.rc.grocery.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
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

import com.rc.grocery.Adapter.CategorySeeAllAdapter;
import com.rc.grocery.Adapter.HomeCategoryAdapter;
import com.rc.grocery.Adapter.Home_adapter;
import com.rc.grocery.Config.BaseURL;
import com.rc.grocery.Model.Category_model;
import com.rc.grocery.application.AppController;
import com.rc.grocery.activity.MainActivity;
import com.rc.grocery.R;
import com.rc.grocery.util.ConnectivityReceiver;
import com.rc.grocery.util.CustomVolleyJsonRequest;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class HomeFragment extends Fragment {

    private static String TAG = HomeFragment.class.getSimpleName();

    private TextView tvSeeAll;
    private SliderLayout imgSlider;
    private RecyclerView rvCategoryAll,rvSubCategoryAll;
    //private RelativeLayout rl_view_all;
    private CategorySeeAllAdapter categorySeeAllAdapter;
    private List<Category_model> category_modelList = new ArrayList<>();
    private ArrayList<Category_model> categorModelArrayList = new ArrayList<>();
    private Home_adapter adapter;
    private TextView tvSearch;
    private boolean isSubcat = false;
    HomeCategoryAdapter homeCategoryAdapter;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        setHasOptionsMenu(true);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
        ((MainActivity) getActivity()).updateHeader();
        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    ((MainActivity) getActivity()).finish();

                    return true;
                }
                return false;
            }
        });


        tvSeeAll = (TextView) view.findViewById(R.id.tv_see_all);
        tvSearch = (TextView) view.findViewById(R.id.tv_search);
        imgSlider = (SliderLayout) view.findViewById(R.id.home_img_slider);
        rvCategoryAll = (RecyclerView) view.findViewById(R.id.rv_category_all);
        rvSubCategoryAll = (RecyclerView) view.findViewById(R.id.rv_sub_category_all);
        //rl_view_all = (RelativeLayout) view.findViewById(R.id.rl_home_view_allcat);
        rvCategoryAll.setNestedScrollingEnabled(false);
        rvSubCategoryAll.setNestedScrollingEnabled(false);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fm = new SearchFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        });
        // initialize a SliderLayout
//        imgSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        imgSlider.setCustomAnimation(new DescriptionAnimation());
//        imgSlider.setDuration(4000);
        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetSliderRequest();
            makeGetCategoryRequest("");
        }

        tvSeeAll.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SeeAllFragment fm = new SeeAllFragment();
                Bundle args = new Bundle();
                args.putParcelableArrayList("Category_Array", categorModelArrayList );
                fm.setArguments( args );
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace( R.id.contentPanel, fm )
                        .addToBackStack( null ).commit();

//                if (categorModelArrayList != null && categorModelArrayList.size() > 0) {
//
//                    Bundle args = new Bundle();
//                    com.rc.grocery.Fragment fm = new SeeAllFragment();
//                    args.putParcelableArrayList("Category_Array", categorModelArrayList );
//                    fm.setArguments( args );
//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace( R.id.contentPanel, fm )
//                            .addToBackStack( null ).commit();
//                }
            }
        } );

        return view;
    }

    /**
     * Method to make json array request where json response starts wtih {
     */
    private void makeGetSliderRequest() {

        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object

                            // arraylist list variable for store data;
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = (JSONObject) response
                                        .get(i);

                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));

                                listarray.add(url_maps);
                            }

                            for (HashMap<String, String> name : listarray) {
                                TextSliderView textSliderView = new TextSliderView(getActivity());
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name.get("slider_title"))
                                        .descriptionVisibility(View.GONE)
                                        .image(name.get("slider_image"))
                                        .setScaleType( BaseSliderView.ScaleType.Fit);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name.get("slider_title"));

                                imgSlider.addSlider(textSliderView);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(req);

    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetCategoryRequest(String parent_id) {
        category_modelList.clear();
        categorModelArrayList.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_category_req";

        isSubcat = false;

        Map<String, String> params = new HashMap<String, String>();
        if (parent_id != null && parent_id != "") {
            params.put("parent", parent_id);
            isSubcat = true;
        }

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
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

                        category_modelList = gson.fromJson(response.getString("data"), listType);
                        Log.e( "category_modelList", category_modelList.size()+"");
                        if (category_modelList!=null&&category_modelList.size()>0) {
                            homeCategoryAdapter = new HomeCategoryAdapter( category_modelList, getActivity() );
                            rvSubCategoryAll.setAdapter( homeCategoryAdapter );
                            LinearLayoutManager verticalManager = new LinearLayoutManager( getActivity(), LinearLayoutManager.VERTICAL, false );
                            rvSubCategoryAll.setLayoutManager( verticalManager );
                            homeCategoryAdapter.notifyDataSetChanged();
                            categorySeeAllAdapter = new CategorySeeAllAdapter( getActivity() );
                            rvCategoryAll.setLayoutManager( new LinearLayoutManager( getActivity(), LinearLayoutManager.HORIZONTAL, false ) );
                            rvCategoryAll.setAdapter( categorySeeAllAdapter );
                            categorySeeAllAdapter.addAll( category_modelList );
                            categorySeeAllAdapter.notifyDataSetChanged();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

//        MenuItem search = menu.findItem(R.id.action_search);
//        search.setVisible(true);
        MenuItem check = menu.findItem(R.id.action_change_password);
        check.setVisible(false);
        MenuItem barcode = menu.findItem(R.id.action_bar_code);
        barcode.setVisible(true);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        switch (item.getItemId()) {
////
////            case R.id.action_search:
////                com.rc.grocery.Fragment fm = new SearchFragment();
////                FragmentManager fragmentManager = getFragmentManager();
////                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
////                        .addToBackStack(null).commit();
////                return false;
////        }
//        return false;
//    }

}
