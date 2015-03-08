package com.elliot.mitchell.hudlapp.classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Elliot on 3/5/2015.
 */
public class GooglePlusHelper implements Serializable {
    private static final long serialVersionUID = 754399653230090422L;
    public boolean bSuccess;
    public String sMessage;
    public ArrayList<GoogleItems> arrItems;

    public GooglePlusHelper(){
        this.arrItems = new ArrayList<GoogleItems>();
        this.bSuccess = false;
        this.sMessage = "";
    }

    public void ParseJSON(String sJSON){
        GooglePlusAPIConstants o = new GooglePlusAPIConstants();
        try {
            JSONObject jo = new JSONObject(sJSON);
            JSONArray jr = jo.getJSONArray(o.items);
            for(int i=0;i<jr.length();i++){
                JSONObject item = jr.getJSONObject(i);
                GoogleItems oGI = new GoogleItems();
                oGI.sTitle = item.getString(o.title);
                oGI.sPublished = item.getString(o.published);
                oGI.sId = item.getString(o.id);
                oGI.sPlusURL = item.getString(o.url);


                if(item.has(o.object) && !item.isNull(o.object)) {
                    JSONObject object = item.getJSONObject(o.object);


                    if (object.has(o.attachments) && !object.isNull(o.attachments)) {
                        JSONArray attachments = object.getJSONArray(o.attachments);
                        if(attachments.length() > 0) {
                            //Only grab the first attachment
                            JSONObject firstAttachment = attachments.getJSONObject(0);

                            if(firstAttachment.has(o.url) && !firstAttachment.isNull(o.url))
                                oGI.sObjectURL = firstAttachment.getString(o.url);

                            if(firstAttachment.has(o.fullImage) && !firstAttachment.isNull(o.fullImage)) {
                                JSONObject fullImage = firstAttachment.getJSONObject(o.fullImage);
                                oGI.sImageURL = fullImage.getString(o.url);
                            }
                        }
                    }
                }

                //Don't add listings without a title and image
                if(!(oGI.getsTitle().equals("") && oGI.getsImageURL().equals("")))
                    this.arrItems.add(oGI);
            }
        } catch(Exception e){
            this.bSuccess = false;
            this.sMessage = e.getMessage();
        }

    }

    private static class GooglePlusAPIConstants {
        /**
         * https://developers.google.com/+/api/latest/activities/search
         */
        public static String object = "object";
        public static String title = "title";
        public static String items = "items";
        public static String published = "published";
        public static String id = "id";
        public static String url = "url";
        public static String attachments = "attachments";
        public static String fullImage = "fullImage";
    }
}
