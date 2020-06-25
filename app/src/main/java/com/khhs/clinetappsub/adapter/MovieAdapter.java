package com.khhs.clinetappsub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.khhs.clinetappsub.GoogleAdMob;
import com.khhs.clinetappsub.MainActivity;
import com.khhs.clinetappsub.R;
import com.khhs.clinetappsub.VideoDetFragment;
import com.khhs.clinetappsub.models.Movie;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    ArrayList<Movie> movies = new ArrayList<>();
    Context context;

    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    FragmentManager fm;
    public MovieAdapter(ArrayList<Movie> movies, Context context,FragmentManager fm) {
        this.movies = movies;
        this.context = context;
        this.fm = fm;
        GoogleAdMob adMob = new GoogleAdMob();
        interstitialAd = adMob.loadInterAds(context);
        rewardedVideoAd = adMob.loadRewarded(context);
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.movieitem,parent,false);

        return new MovieHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, final int position) {
        Glide.with(context)
                .load(movies.get(position).getImageUrl())
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              VideoDetFragment fragment = new VideoDetFragment();
              fragment.movie = movies.get(position);
              setFragment(fragment);
              if(interstitialAd.isLoaded())
              {
                  interstitialAd.show();
              }

              if(rewardedVideoAd.isLoaded())
              {
                  rewardedVideoAd.show();
              }

              if(MainActivity.currentFrag.equals(context.getString(R.string.home_frage)))
              {
                  MainActivity.prevFrag=context.getString(R.string.home_frage);

              }
              else if(MainActivity.currentFrag.equals(context.getString(R.string.movie_frage)))
              {
                  MainActivity.prevFrag=context.getString(R.string.movie_frage);
              }
              else if(MainActivity.currentFrag.equals(context.getString(R.string.search_frage)))
              {
                  MainActivity.prevFrag=context.getString(R.string.search_frage);
              }
              MainActivity.currentFrag=context.getString(R.string.video_det_frage);


            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder{
        ImageView image;
        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);

        }
    }

    public void setFragment(Fragment f)
    {

        FragmentTransaction tf = fm.beginTransaction();
        tf.replace(R.id.content_fragment,f);
        tf.commit();
    }


}
