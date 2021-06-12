package com.rc.grocery.Model;

import org.parceler.Parcel;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */
@Parcel
public class Product_model {

    String product_id;
    String product_name;
    String product_description;
    String product_image;
    String category_id;
    String in_stock;
    String price;
    String unit_value;
    String unit;
    String increament;
    String barcode;
    String stock;
    String title;

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getIn_stock() {
        return in_stock;
    }

    public String getPrice() {
        return price;
    }

    public String getUnit_value() {
        return unit_value;
    }

    public String getUnit() {
        return unit;
    }


    public String getIncreament() {
        return increament;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getStock() {
        return stock;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Product_model{" +
                "product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_description='" + product_description + '\'' +
                ", product_image='" + product_image + '\'' +
                ", category_id='" + category_id + '\'' +
                ", in_stock='" + in_stock + '\'' +
                ", price='" + price + '\'' +
                ", unit_value='" + unit_value + '\'' +
                ", unit='" + unit + '\'' +
                ", increament='" + increament + '\'' +
                ", barcode='" + barcode + '\'' +
                ", stock='" + stock + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
