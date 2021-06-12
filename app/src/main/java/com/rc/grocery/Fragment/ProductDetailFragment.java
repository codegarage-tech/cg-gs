package com.rc.grocery.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rc.grocery.Config.BaseURL;
import com.rc.grocery.Model.Product_model;
import com.rc.grocery.R;
import com.rc.grocery.activity.MainActivity;
import com.rc.grocery.util.AppUtil;
import com.rc.grocery.util.DatabaseHandler;

import org.parceler.Parcels;

import java.util.HashMap;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ProductDetailFragment extends Fragment {

    private static String TAG = ProductDetailFragment.class.getSimpleName();
    private TextView tvProductDetailName, tvProductDetailBarcode, tvProductDetailPerPrice, tvProductDetail, tvAdd, tvShare, tvContetiy;
    private ImageView ivProductDetailImg, ivMinus, ivPlus;
    private DatabaseHandler dbcart;

    Product_model productModel;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        dbcart = new DatabaseHandler(getActivity());

        String title = getArguments().getString("title");
        ((MainActivity) getActivity()).setTitle(title);
        productModel = Parcels.unwrap(getArguments().getParcelable("product_model"));
        Log.d(TAG, "ProductDetail: title- " + title);
        Log.d(TAG, "ProductDetail: product_model- " + productModel.toString());

        tvProductDetailName = (TextView) view.findViewById(R.id.tv_product_detail_name);
        tvProductDetailBarcode = (TextView) view.findViewById(R.id.tv_product_detail_barcode);
        ivProductDetailImg = (ImageView) view.findViewById(R.id.iv_product_detail_img);
        tvProductDetail = (TextView) view.findViewById(R.id.tv_product_detail);
        tvProductDetailPerPrice = (TextView) view.findViewById(R.id.tv_product_detail_per_price);
        tvAdd = (TextView) view.findViewById(R.id.tv_subcat_add);
        tvShare = (TextView) view.findViewById(R.id.tv_share);
        tvContetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);

        ivMinus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
        ivPlus = (ImageView) view.findViewById(R.id.iv_subcat_plus);

        tvProductDetailName.setText(productModel.getProduct_name());
        tvProductDetailBarcode.setText(productModel.getBarcode());
        tvProductDetailPerPrice.setText(getActivity().getResources().getString(R.string.tv_pro_price) + productModel.getUnit_value() + " " +
                productModel.getUnit() + " " + getActivity().getResources().getString(R.string.currency) + " " + productModel.getPrice());
        AppUtil.loadImage(getActivity(), ivProductDetailImg, BaseURL.IMG_PRODUCT_URL + productModel.getProduct_image(), false, true);
        tvProductDetail.setText(productModel.getProduct_description());
        eventClickListsener();
        return view;
    }


    private void eventClickListsener() {
        if (productModel != null) {

            if (dbcart.isInCart(productModel.getProduct_id())) {
                tvAdd.setText(getActivity().getResources().getString(R.string.tv_pro_update));
                tvContetiy.setText(dbcart.getCartItemQty(productModel.getProduct_id()));

                AppUtil.setCartButtonBackground(getActivity(), tvAdd, false);
            } else {
                tvAdd.setText(getActivity().getResources().getString(R.string.tv_pro_add));
                AppUtil.setCartButtonBackground(getActivity(), tvAdd, true);
            }

            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HashMap<String, String> map = new HashMap<>();

                    map.put("product_id", productModel.getProduct_id());
                    map.put("category_id", productModel.getCategory_id());
                    map.put("product_image", productModel.getProduct_image());
                    map.put("increament", productModel.getIncreament());
                    map.put("product_name", productModel.getProduct_name());

                    map.put("price", productModel.getPrice());
                    map.put("stock", productModel.getIn_stock());
                    map.put("title", productModel.getTitle());
                    map.put("unit", productModel.getUnit());

                    map.put("unit_value", productModel.getUnit_value());

                    if (!tvContetiy.getText().toString().equalsIgnoreCase("0")) {

                        if (dbcart.isInCart(map.get("product_id"))) {
                            dbcart.setCart(map, Float.valueOf(tvContetiy.getText().toString()));
                            tvAdd.setText(getActivity().getResources().getString(R.string.tv_pro_update));
                        } else {
                            dbcart.setCart(map, Float.valueOf(tvContetiy.getText().toString()));
                            tvAdd.setText(getActivity().getResources().getString(R.string.tv_pro_update));
                        }

                        AppUtil.setCartButtonBackground(getActivity(), tvAdd, false);
                    } else {
                        dbcart.removeItemFromCart(map.get("product_id"));
                        tvAdd.setText(getActivity().getResources().getString(R.string.tv_pro_add));

                        AppUtil.setCartButtonBackground(getActivity(), tvAdd, true);
                    }

                    Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                    Double price = Double.parseDouble(productModel.getPrice());

                    ((MainActivity) getActivity()).setCartCounter("" + dbcart.getCartCount());


                }
            });

            tvShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "http://iexpresswholesale.com/");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });

            ivPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qty = Integer.valueOf(tvContetiy.getText().toString());
                    qty = qty + 1;

                    tvContetiy.setText(String.valueOf(qty));

                    AppUtil.setCartButtonBackground(getActivity(), tvAdd, true);
                }
            });

            ivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qty = 0;
                    if (!tvContetiy.getText().toString().equalsIgnoreCase(""))
                        qty = Integer.valueOf(tvContetiy.getText().toString());

                    if (qty > 0) {
                        qty = qty - 1;
                        tvContetiy.setText(String.valueOf(qty));
                    }

                    AppUtil.setCartButtonBackground(getActivity(), tvAdd, true);
                }
            });
        }
    }
}