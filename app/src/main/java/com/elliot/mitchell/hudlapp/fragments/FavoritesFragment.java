package com.elliot.mitchell.hudlapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.elliot.mitchell.hudlapp.R;
import com.elliot.mitchell.hudlapp.adapters.CardResultsAdapter;
import com.elliot.mitchell.hudlapp.classes.Card;
import com.elliot.mitchell.hudlapp.classes.DatabaseHelper;
import com.elliot.mitchell.hudlapp.classes.GoogleItems;
import com.elliot.mitchell.hudlapp.classes.GooglePlusHelper;

import java.util.ArrayList;

/**
 *
 */
public class FavoritesFragment extends Fragment implements CardResultsAdapter.FavoriteActionCallbacks{

    public static String _Title;
    private ListView listView;
    private CardResultsAdapter cardResultsAdapter;
    private GooglePlusHelper oGPH;
    private DatabaseHelper oDB;

    public FavoritesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set view
        listView = (ListView) getView().findViewById(R.id.card_listView);

        //Set db helper
        oDB = new DatabaseHelper(getActivity());

        buildAdapter(oDB.getFavorites());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.favorites_fragment, container, false);
    }

    private void buildAdapter(ArrayList<GoogleItems> arrGIs){
        if(arrGIs.size()>0){
            //Get favorite ids
            ArrayList<String> arrFavIds = oDB.getFavoritesIDs();
            if(arrGIs != null){
                //Const adapter
                cardResultsAdapter = new CardResultsAdapter(getActivity(),this,R.layout.list_item_card,arrFavIds);

                for(int i=0;i<arrGIs.size(); i++){
                    Card card = new Card(arrGIs.get(i));
                    cardResultsAdapter.add(card);
                }
                listView.setAdapter(cardResultsAdapter);

            } else {
                Toast.makeText(getActivity(),"Unable to get Favorites",Toast.LENGTH_LONG).show();
            }
        } else {

        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void goToLink(String sUrl){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sUrl));
        startActivity(browserIntent);
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
