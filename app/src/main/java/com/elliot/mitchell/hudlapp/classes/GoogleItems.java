package com.elliot.mitchell.hudlapp.classes;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Elliot on 3/5/2015.
 * Container Class
 */
public class GoogleItems implements Serializable{
    private static final long serialVersionUID = 4094748562126545746L;
    /**
     * Title of post
     */
    String sTitle;

    /**
     * RFC 3339 Timestamp
     */
    String sPublished;

    /**
     * ID of Post
     */
    String sId;

    /**
     * URL to G+ page
     */
    String sPlusURL;

    /**
     * URL to subject at hand
     */
    String sObjectURL;

    /**
     * Image URL to subject at hand
     */
    String sImageURL;

    /**
     * Bitmap for attachment image
     */
    Bitmap bmImage;

    public Bitmap getBmImage() {
        return bmImage;
    }

    public void setBmImage(Bitmap bmImage) {
        this.bmImage = bmImage;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsTitle() {
        return sTitle;
    }

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }

    public String getsPublished() {
        return sPublished;
    }

    public void setsPublished(String sPublished) {
        this.sPublished = sPublished;
    }

    public String getsPlusURL() {
        return sPlusURL;
    }

    public void setsPlusURL(String sPlusURL) {
        this.sPlusURL = sPlusURL;
    }

    public String getsObjectURL() {
        return sObjectURL;
    }

    public void setsObjectURL(String sObjectURL) {
        this.sObjectURL = sObjectURL;
    }

    public String getsImageURL() {
        return sImageURL;
    }

    public void setsImageURL(String sImageURL) {
        this.sImageURL = sImageURL;
    }
}
