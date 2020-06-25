package com.khhs.clinetappsub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SafeBrowsingResponse;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.InterstitialAd;
import com.khhs.clinetappsub.GoogleAdMob;
import com.khhs.clinetappsub.MainActivity;
import com.khhs.clinetappsub.R;
import com.khhs.clinetappsub.SeriesDetFragment;
import com.khhs.clinetappsub.SeriesFragment;
import com.khhs.clinetappsub.models.Series;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesHolder> {
    ArrayList<Series> series = new ArrayList<>();
    Context context;
    FragmentManager fm;

    private InterstitialAd interstitialAd;

    public SeriesAdapter(ArrayList<Series> Seriess, Context context, FragmentManager fm) {
        this.series = Seriess;
        this.context = context;
        this.fm = fm;
        GoogleAdMob adMob = new GoogleAdMob();
        interstitialAd = adMob.loadInterAds(context);
    }

    @NonNull
    @Override
    public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.seriesitem,parent,false);

        return new SeriesHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesHolder holder, final int position) {
        Glide.with(context)
                .load(series.get(position).getImageUrl())
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesDetFragment detFragment = new SeriesDetFragment();
                detFragment.myModel = series.get(position);
               setFragment(detFragment,position);
               if(interstitialAd.isLoaded())
               {
                   interstitialAd.show();
               }
            }
        });

    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    public class SeriesHolder extends RecyclerView.ViewHolder{
        ImageView image;
        public SeriesHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    public void setFragment(Fragment f,int poistion)
    {

        FragmentTransaction tf = fm.beginTransaction();
        tf.replace(R.id.content_fragment,f);
        tf.commit();
        MainActivity.series= series.get(poistion);

        if(MainActivity.currentFrag.equals(context.getString(R.string.home_frage)))
        {
            MainActivity.prevFrag=context.getString(R.string.home_frage);
        }
       else if(MainActivity.currentFrag.equals(context.getString(R.string.series_frage)))
        {
            MainActivity.prevFrag=context.getString(R.string.series_frage);
        }
       else if(MainActivity.currentFrag.equals(context.getString(R.string.search_frage)))
        {
            MainActivity.prevFrag=(context.getString(R.string.search_frage));
        }
        MainActivity.currentFrag=context.getString(R.string.series_det_frage);

    }

}
