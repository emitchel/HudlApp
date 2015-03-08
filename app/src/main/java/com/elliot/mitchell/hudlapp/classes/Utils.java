package com.elliot.mitchell.hudlapp.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.elliot.mitchell.hudlapp.R;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Elliot on 3/3/2015.
 */
public class Utils {

    public static boolean isValidString(String s){

        if(s==null){
            return false;
        } else if(s.trim().equals("null")){
            return false;
        } else if(s.trim().equals("")){
            return false;
        } else {
            return true;
        }
    }

    public static boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9\\s]*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }

    public static void hideKeyboard(Context context,View view){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void CopyStream(InputStream is, OutputStream os){
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }



}
