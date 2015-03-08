package com.elliot.mitchell.hudlapp.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elliot.mitchell.hudlapp.R;
import com.elliot.mitchell.hudlapp.activities.ResultsActivity;
import com.elliot.mitchell.hudlapp.classes.GooglePlusHelper;
import com.elliot.mitchell.hudlapp.classes.Utils;
import com.elliot.mitchell.hudlapp.data.Requests;

/**
 *
 */
public class SearchFragment extends Fragment {

    private EditText etKeywordInput;
    private ImageView ivClear;
    private TextView tvSearch;
    private RelativeLayout rlSearchButton;
    private ProgressBar pbSearch;

    private SearchKeyword asyncSearchKeyword;

    public SearchFragment() {
        // Required empty public constructor
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       gatherViews();
       setOnClickListeners();
       setValues();

        etKeywordInput.clearFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(asyncSearchKeyword != null && !asyncSearchKeyword.isCancelled())
            asyncSearchKeyword.cancel(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(asyncSearchKeyword != null && !asyncSearchKeyword.isCancelled())
            asyncSearchKeyword.cancel(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(asyncSearchKeyword != null && !asyncSearchKeyword.isCancelled())
            asyncSearchKeyword.cancel(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(asyncSearchKeyword != null && !asyncSearchKeyword.isCancelled())
            asyncSearchKeyword.cancel(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    /**
     * Functions
     */

    private void gatherViews(){
        etKeywordInput = (EditText)getView().findViewById(R.id.etKeywordInput);

        tvSearch = (TextView)getView().findViewById(R.id.tvSearch);

        ivClear = (ImageView)getView().findViewById(R.id.ivClear);

        rlSearchButton = (RelativeLayout)getView().findViewById(R.id.rlSearchButton);

        pbSearch = (ProgressBar)getView().findViewById(R.id.pbSearch);
    }

    private void setOnClickListeners(){

        rlSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity(),v);
                searchKeyword();
            }
        });

        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSearch.setText(getString(R.string.search));

                etKeywordInput.setText("");
            }
        });
    }

    private void setValues(){
        tvSearch.setText(getString(R.string.search));
    }

    public String getKeyword(){
        return etKeywordInput.getText().toString().toLowerCase();
    }


    private void searchKeyword() {
        /**
         *Check if input is ok
         */
        String sKeyword = getKeyword();

        if(!Utils.isValidString(sKeyword)){
            Toast.makeText(getActivity(),"Invalid keyword",Toast.LENGTH_LONG).show();
        }else if(!Utils.isAlphaNumeric(sKeyword)){
            Toast.makeText(getActivity(),"Keyword must contain only alpha numerics",Toast.LENGTH_LONG).show();
        } else {
            asyncSearchKeyword = new SearchKeyword();
            asyncSearchKeyword.execute();
        }

    }


    private void sendUserToResultsPage(GooglePlusHelper result){

        //Make intent of results activity
        Intent intent = new Intent(getActivity(),ResultsActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("GooglePlusHelper",result);
        bundle.putString("sKeyWord",getKeyword());
        intent.putExtras(bundle);

        //Start results activity
        startActivity(intent);

        //Clear progress bar
        //Reset button
        pbSearch.setVisibility(View.GONE);
        tvSearch.setText(getString(R.string.search));
        tvSearch.setVisibility(View.VISIBLE);
        rlSearchButton.setEnabled(true);

    }

    /**
     * Async Task to search posts
     */
    private class SearchKeyword extends AsyncTask<String, Void, GooglePlusHelper> {

        @Override
        protected GooglePlusHelper doInBackground(String... strings) {
            GooglePlusHelper oGPH;
            Requests oRequests = new Requests(getActivity());
            oGPH = oRequests.getGooglePlusItems(getKeyword());
            return oGPH;
        }

        @Override
        protected void onPostExecute(GooglePlusHelper result) {
            if(result.bSuccess){
                sendUserToResultsPage(result);
            } else {
                Toast.makeText(getActivity(),result.sMessage,Toast.LENGTH_LONG).show();
                pbSearch.setVisibility(View.GONE);
                tvSearch.setVisibility(View.VISIBLE);
                rlSearchButton.setEnabled(true);
            }
        }

        @Override
        protected void onPreExecute() {
            pbSearch.setVisibility(View.VISIBLE);
            tvSearch.setVisibility(View.GONE);
            rlSearchButton.setEnabled(false);
        }


    }

}
