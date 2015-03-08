package com.elliot.mitchell.hudlapp.data;

import android.content.Context;

import com.elliot.mitchell.hudlapp.R;
import com.elliot.mitchell.hudlapp.classes.GooglePlusHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Elliot on 3/5/2015.
 * Data Layer
 */
public class Requests {
    private static String sAPIKey = "AIzaSyBM6MobqU8YUV4yP60RzAIjbuFstokTWsY";
    private static String sURL = "https://www.googleapis.com/plus/v1/activities?query=<<KEYWORDS_HERE>>&maxResults=20&orderBy=best&fields=items(url%2Cpublished%2Cobject(actor%2Cattachments(content%2CdisplayName%2CfullImage%2Curl)%2Cid)%2Ctitle%2Cid)%2Ckind%2Ctitle&key=";
    private static String sReplaceToken = "<<KEYWORDS_HERE>>";
    private static String UTF_8 = "utf-8";
    private Context context;

    public Requests(Context context){
        this.context = context;
    }

    public GooglePlusHelper getGooglePlusItems(String sKeyword){
        GooglePlusHelper oGPH = new GooglePlusHelper();

        try {
            sKeyword = URLEncoder.encode(sKeyword, UTF_8);
        } catch (UnsupportedEncodingException e){
            oGPH.bSuccess = false;
            oGPH.sMessage = e.getMessage();
            return oGPH;
        }

        String sURL = this.sURL.replace(sReplaceToken,sKeyword) + this.sAPIKey;

        try {
            String sReturn = GET(sURL);
            oGPH.ParseJSON(sReturn);
            if(oGPH.arrItems.size() >0){
                oGPH.bSuccess = true;
                oGPH.sMessage = "";
            } else {
                oGPH.bSuccess = false;
                oGPH.sMessage = context.getString(R.string.no_results);
            }
        } catch (Exception e){
            oGPH.bSuccess = false;
            oGPH.sMessage = e.getMessage();
        }

        return oGPH;
    }
    public static String GET(String url) throws Exception{
        InputStream inputStream = null;
        String result = "";


            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);




        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


}
