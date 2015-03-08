package com.elliot.mitchell.hudlapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.elliot.mitchell.hudlapp.R;
import com.elliot.mitchell.hudlapp.adapters.CardResultsAdapter;
import com.elliot.mitchell.hudlapp.classes.Card;
import com.elliot.mitchell.hudlapp.classes.DatabaseHelper;
import com.elliot.mitchell.hudlapp.classes.GoogleItems;
import com.elliot.mitchell.hudlapp.classes.GooglePlusHelper;

import java.util.ArrayList;

public class ResultsActivity extends ActionBarActivity implements CardResultsAdapter.FavoriteActionCallbacks{

    private ListView listView;
    private CardResultsAdapter cardResultsAdapter;
    private GooglePlusHelper oGPH;
    private DatabaseHelper oDB;
    private String sKeyWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //Set home bar as back bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#414D58")));

        //Set view
        listView = (ListView) findViewById(R.id.card_listView);

        //Set db helper
        oDB = new DatabaseHelper(this);

        //Get bundle
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        if(extras!=null){
            oGPH = (GooglePlusHelper)extras.getSerializable("GooglePlusHelper");
            sKeyWord = extras.getString("sKeyWord");
        } else if(savedInstanceState !=null){
            oGPH = (GooglePlusHelper)savedInstanceState.getSerializable("GooglePlusHelper");
            sKeyWord = savedInstanceState.getString("sKeyWord");
        }

        //Set up adapter
        buildAdapter();

        //Set up title bar
        getSupportActionBar().setTitle(getString(R.string.results_for) + " " + sKeyWord);
    }

    private void buildAdapter(){
        //Get favorite ids
        ArrayList<String> arrFavIds = oDB.getFavoritesIDs();


            //Const adapter
            cardResultsAdapter = new CardResultsAdapter(this,null,R.layout.list_item_card,arrFavIds);

            if(oGPH !=null){
                for(int i=0;i<oGPH.arrItems.size(); i++){
                    Card card = new Card(oGPH.arrItems.get(i));
                    cardResultsAdapter.add(card);
                }
                listView.setAdapter(cardResultsAdapter);

            } else {
                Toast.makeText(this,"Search not successful",Toast.LENGTH_LONG).show();
                this.onBackPressed();
            }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sKeyWord",sKeyWord);
        outState.putSerializable("GooglePlusHelper",oGPH);

    }

    @Override
    public void goToLink(String sUrl){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sUrl));
        startActivity(browserIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void addGoogleItemToFavorites(GoogleItems oGI) {
        oDB.insertFavorite(oGI);
    }

    @Override
    public void removeGoogleItemFromFavorites(GoogleItems oGI) {
        oDB.deleteFavorite(oGI.getsId());
    }


}
