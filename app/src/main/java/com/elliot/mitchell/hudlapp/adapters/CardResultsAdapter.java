package com.elliot.mitchell.hudlapp.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elliot.mitchell.hudlapp.R;
import com.elliot.mitchell.hudlapp.classes.Card;
import com.elliot.mitchell.hudlapp.classes.GoogleItems;
import com.elliot.mitchell.hudlapp.classes.ImageCache;
import com.elliot.mitchell.hudlapp.classes.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CardResultsAdapter extends ArrayAdapter<Card> {
    private static final String TAG = "CardArrayAdapter";
    private List<Card> cardList = new ArrayList<Card>();
    public ImageCache imageCache;
    private List<String> arrFavIds;
    private FavoriteActionCallbacks mFavoriteActionCallbacks;
    Context context ;
    Fragment fragment;



    static class CardViewHolder {
        TextView tvTitle;
        ImageView ivGooglePlusLink;
        ImageView ivAddToFavorites;
        ImageView ivObjectImage;
        FrameLayout imageProgressBar;
    }

    public CardResultsAdapter(Context context,Fragment frag, int textViewResourceId, ArrayList<String> arrFavIds) {
        super(context, textViewResourceId);
        this.context = context;
        this.fragment = frag;
        this.arrFavIds = arrFavIds;

        imageCache = new ImageCache(context);

        try {
            if(this.fragment !=null) {
                this.mFavoriteActionCallbacks = ((FavoriteActionCallbacks) this.fragment);
            } else {
                this.mFavoriteActionCallbacks = ((FavoriteActionCallbacks) this.context);
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement AdapterCallback.");
        }
    }

    public static interface FavoriteActionCallbacks{
        void addGoogleItemToFavorites(GoogleItems oGI);
        void removeGoogleItemFromFavorites(GoogleItems oGI);
        void goToLink(String sURL);
    }

    @Override
    public void add(Card object) {
        cardList.add(object);
        super.add(object);
    }

    public void setCardList(ArrayList<Card> arrCards){
        this.cardList.clear();
        this.cardList.addAll(arrCards);
        this.notifyDataSetChanged();


    }

    public void addGoogleItemToFavorites(GoogleItems gi){
        mFavoriteActionCallbacks.addGoogleItemToFavorites(gi);
        this.arrFavIds.add(gi.getsId());
    }

    public boolean isItemFavorites(String id){
        return this.arrFavIds.contains(id);
    }

    public void removeGoogleItemFromFavorites(GoogleItems gi){
        mFavoriteActionCallbacks.removeGoogleItemFromFavorites(gi);
        this.arrFavIds.remove(this.arrFavIds.indexOf(gi.getsId()));
    }


    public ArrayList<Card> getCardList(){
        return (ArrayList)this.cardList;
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.tvTitle = (TextView) row.findViewById(R.id.tvCardTitle);
            viewHolder.ivGooglePlusLink = (ImageView) row.findViewById(R.id.ivGPlusLink);
            viewHolder.ivAddToFavorites = (ImageView) row.findViewById(R.id.ivAddToFavorites);
            viewHolder.ivObjectImage = (ImageView) row.findViewById(R.id.ivObjectImage);
            viewHolder.imageProgressBar = (FrameLayout) row.findViewById(R.id.imageProgressBar);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        final Card card = getItem(position);

        viewHolder.ivObjectImage.setTag(getItem(position));
        viewHolder.imageProgressBar.setTag(viewHolder.ivObjectImage);

        final GoogleItems oGI = card.getoGoogleItem();
        String sImgURL = oGI.getsImageURL();

            if (Utils.isValidString(sImgURL)) {
                if(imageCache.getBitmapFromMemCache(sImgURL)!=null){
                    viewHolder.imageProgressBar.setVisibility(View.GONE);
                    viewHolder.ivObjectImage.setVisibility(View.VISIBLE);
                    viewHolder.ivObjectImage.setImageBitmap(imageCache.getBitmapFromMemCache(sImgURL));
                } else {
                    viewHolder.imageProgressBar.setVisibility(View.VISIBLE);
                    viewHolder.ivObjectImage.setVisibility(View.GONE);
                    new DownloadImageTask(context,imageCache, viewHolder.ivObjectImage, viewHolder.imageProgressBar).execute();
                }

            } else {
                viewHolder.imageProgressBar.setVisibility(View.GONE);
                viewHolder.ivObjectImage.setVisibility(View.VISIBLE);
                viewHolder.ivObjectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.gplus_logo));

            }

        if(!card.getTitle().trim().equals("")) {
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(card.getTitle());
        }else {
            viewHolder.tvTitle.setVisibility(View.GONE);
        }

        if(isItemFavorites(oGI.getsId())){
            //In favorites, make selected
            viewHolder.ivAddToFavorites.setBackgroundColor(context.getResources().getColor(R.color.hudl_orange));
        } else {
            //Not in favorites, make normal
            viewHolder.ivAddToFavorites.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        viewHolder.ivAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isItemFavorites(oGI.getsId())){
                    //In favorites, remove from favorites
                    removeGoogleItemFromFavorites(oGI);

                    //Set background to normal, no longer in favorites
                    viewHolder.ivAddToFavorites.setBackgroundColor(context.getResources().getColor(R.color.white));

                } else {
                    //Not in favorites, add to favorites
                    addGoogleItemToFavorites(oGI);

                    //Set background to select, in favorites
                    viewHolder.ivAddToFavorites.setBackgroundColor(context.getResources().getColor(R.color.hudl_orange));
                }

            }
        });

        viewHolder.ivGooglePlusLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFavoriteActionCallbacks.goToLink(card.getsGooglePlusURL());
            }
        });

        viewHolder.ivAddToFavorites.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,"Add to Favorites",Toast.LENGTH_LONG);
                return false;
            }
        });

        viewHolder.ivGooglePlusLink.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,"View in Google+",Toast.LENGTH_LONG);
                return false;
            }
        });
        return row;
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private Context context;
        private ImageCache imgCache;
        private ImageView ivImage;
        private FrameLayout progressBar;

        //Tag references
        private Card card;
        private ImageView ivTag;
        public DownloadImageTask(Context context,ImageCache imgCache, ImageView ivImage, FrameLayout progressBar) {
            this.context = context;
            this.ivImage = ivImage;
            this.progressBar = progressBar;
            this.imgCache=imgCache;

            /**
             * this should always this.iv.getTag
             */
            this.card = (Card)this.ivImage.getTag();

            /**
             * This should equal this.iv
             */
            this.ivTag = (ImageView)this.progressBar.getTag();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = this.card.getoGoogleItem().getsImageURL();
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if(!this.ivImage.getTag().equals(this.card)){
                //do nothing
                return;
            }

            if(result !=null && this.ivImage !=null && this.progressBar !=null){
                this.progressBar.setVisibility(View.GONE);
                this.ivImage.setVisibility(View.VISIBLE);
                this.ivImage.setImageBitmap(result);

                //Add to cache
                imgCache.addBitmapToMemoryCache(this.card.getoGoogleItem().getsImageURL(),result);
            } else {
                this.progressBar.setVisibility(View.GONE);
                this.ivImage.setVisibility(View.VISIBLE);
                this.ivImage.setImageDrawable(this.context.getResources().getDrawable(R.drawable.gplus_logo));
            }
        }
    }


}