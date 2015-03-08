package com.elliot.mitchell.hudlapp.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GPlusFavorites.db";
    public static final String POSTS_TABLE_NAME = "posts";
    public static final String POSTS_COLUMN_PLUS_ID = "id";
    public static final String POSTS_COLUMN_TITLE = "title";
    public static final String POSTS_COLUMN_PUBLISHED = "published";
    public static final String POSTS_COLUMN_PLUS_URL = "plus_url";
    public static final String POSTS_COLUMN_OBJECT_URL = "object_url";
    public static final String POSTS_COLUMN_IMAGE_URL= "image_url";

    private HashMap hp;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table posts " +
                        "(id text, title text,published text, plus_url text,object_url text, image_url text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS posts");
        onCreate(db);
    }

    public boolean insertFavorite  (GoogleItems oGI)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POSTS_COLUMN_PLUS_ID,oGI.getsId());
        contentValues.put(POSTS_COLUMN_TITLE, oGI.getsTitle());
        contentValues.put(POSTS_COLUMN_PUBLISHED,oGI.getsPublished());
        contentValues.put(POSTS_COLUMN_PLUS_URL,oGI.getsPlusURL());
        contentValues.put(POSTS_COLUMN_OBJECT_URL, oGI.getsObjectURL());
        contentValues.put(POSTS_COLUMN_IMAGE_URL,oGI.getsImageURL());


        db.insert("posts", null, contentValues);
        return true;
    }
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from posts where id="+id+"", null );

        return res;
    }

    public ArrayList<String> getFavoritesIDs(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> ids = new ArrayList<String>();
        Cursor res =  db.rawQuery( "select * from posts", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            ids.add(res.getString(res.getColumnIndex(POSTS_COLUMN_PLUS_ID)));
            res.moveToNext();
        }
        return ids;
    }
    public ArrayList<GoogleItems> getFavorites(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<GoogleItems> arrGoogleItems = new ArrayList<GoogleItems>();
        Cursor res =  db.rawQuery( "select * from posts", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            GoogleItems gi = new GoogleItems();
            gi.setsId(res.getString(res.getColumnIndex(POSTS_COLUMN_PLUS_ID)));
            gi.setsTitle(res.getString(res.getColumnIndex(POSTS_COLUMN_TITLE)));
            gi.setsImageURL(res.getString(res.getColumnIndex(POSTS_COLUMN_IMAGE_URL)));
            gi.setsObjectURL(res.getString(res.getColumnIndex(POSTS_COLUMN_OBJECT_URL)));
            gi.setsPublished(res.getString(res.getColumnIndex(POSTS_COLUMN_PUBLISHED)));
            gi.setsPlusURL(res.getString(res.getColumnIndex(POSTS_COLUMN_PLUS_URL)));

            arrGoogleItems.add(gi);
            res.moveToNext();
        }
        return arrGoogleItems;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, POSTS_TABLE_NAME);
        return numRows;
    }


    public Integer deleteFavorite (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(POSTS_TABLE_NAME,
                "id = ? ",
                new String[] { id });
    }

}