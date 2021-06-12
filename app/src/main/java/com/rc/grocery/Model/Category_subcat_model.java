package com.rc.grocery.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class Category_subcat_model implements Parcelable{

    String id;
    String title;
    String slug;
    String parent;
    String leval;
    String description;
    String image;
    String status;


    String Count;
    String PCount;

    protected Category_subcat_model(Parcel in) {
        id = in.readString();
        title = in.readString();
        slug = in.readString();
        parent = in.readString();
        leval = in.readString();
        description = in.readString();
        image = in.readString();
        status = in.readString();
        Count = in.readString();
        PCount = in.readString();
    }

    public static final Creator<Category_subcat_model> CREATOR = new Creator<Category_subcat_model>() {
        @Override
        public Category_subcat_model createFromParcel(Parcel in) {
            return new Category_subcat_model( in );
        }

        @Override
        public Category_subcat_model[] newArray(int size) {
            return new Category_subcat_model[size];
        }
    };

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getSlug(){
        return slug;
    }

    public String getParent(){
        return parent;
    }

    public String getLeval(){
        return leval;
    }

    public String getDescription(){
        return description;
    }

    public String getImage(){
        return image;
    }

    public String getStatus(){
        return status;
    }




    public String getCount(){
        return Count;
    }

    public String getPCount(){
        return PCount;
    }

    @Override
    public String toString() {
        return "Category_subcat_model{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", parent='" + parent + '\'' +
                ", leval='" + leval + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", Count='" + Count + '\'' +
                ", PCount='" + PCount + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( id );
        parcel.writeString( title );
        parcel.writeString( slug );
        parcel.writeString( parent );
        parcel.writeString( leval );
        parcel.writeString( description );
        parcel.writeString( image );
        parcel.writeString( status );
        parcel.writeString( Count );
        parcel.writeString( PCount );
    }
}
