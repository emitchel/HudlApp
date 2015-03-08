package com.elliot.mitchell.hudlapp.classes;

/**
 * Created by Elliot on 3/7/2015.
 */

public class Card {
    private String sTitle;
    private String sGooglePlusURL;
    private GoogleItems oGoogleItem;
    public boolean bLoadedImageAlready = false;

    public Card( GoogleItems oGoogleItems) {
        this.oGoogleItem = oGoogleItems;
        this.sTitle = this.oGoogleItem.sTitle;
        this.sGooglePlusURL = this.oGoogleItem.sPlusURL;
    }

    public String getTitle() {
        return sTitle;
    }

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }

    public GoogleItems getoGoogleItem() {
        return oGoogleItem;
    }

    public void setoGoogleItem(GoogleItems oGoogleItem) {
        this.oGoogleItem = oGoogleItem;
    }

    public String getsGooglePlusURL() {
        return sGooglePlusURL;
    }

    public void setsGooglePlusURL(String sGooglePlusURL) {
        this.sGooglePlusURL = sGooglePlusURL;
    }
}