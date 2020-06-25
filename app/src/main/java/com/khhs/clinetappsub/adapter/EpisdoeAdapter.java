package com.khhs.clinetappsub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.khhs.clinetappsub.GoogleAdMob;
import com.khhs.clinetappsub.MainActivity;
import com.khhs.clinetappsub.R;
import com.khhs.clinetappsub.VideoDetFragment;
import com.khhs.clinetappsub.models.Episode;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class EpisdoeAdapter  extends RecyclerView.Adapter<EpisdoeAdapter.EpisodeHolder> {
    ArrayList<Episode> episodes = new ArrayList<>();
    Context context;

    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    FragmentManager fm;
    public EpisdoeAdapter(ArrayList<Episode> episodes, Context context, FragmentManager fm) {
        this.episodes = episodes;
        this.context = context;
        this.fm = fm;
        GoogleAdMob adMob = new GoogleAdMob();
        interstitialAd = adMob.loadInterAds(context);
        rewardedVideoAd = adMob.loadRewarded(context);
    }

    @NonNull
    @Override
    public EpisodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_item,parent,false);
        return new EpisodeHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeHolder holder, final int position) {

        holder.txtSr.setText(position+1+"");
        holder.txtName.setText(episodes.get(position).getName());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                VideoDetFragment fragment = new VideoDetFragment();
                fragment.episode = episodes.get(position);
                setFragment(fragment);
                if(interstitialAd.isLoaded())
                {
                    interstitialAd.show();
                }
                if(rewardedVideoAd.isLoaded())
                {
                    rewardedVideoAd.show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder
    {
        TextView txtSr,txtName;
        ImageView play;
        public EpisodeHolder(@NonNull View itemView) {
            super(itemView);
            txtSr = itemView.findViewById(R.id.sr);
            txtName = itemView.findViewById(R.id.name);
            play = itemView.findViewById(R.id.playbtn);
        }
    }

    public void setFragment(Fragment f)
    {

        FragmentTransaction tf = fm.beginTransaction();
        tf.replace(R.id.content_fragment,f);
        tf.commit();
        MainActivity.mediumFrag=MainActivity.prevFrag;
        MainActivity.prevFrag=context.getString(R.string.series_det_frage);
        MainActivity.currentFrag=context.getString(R.string.video_det_frage);
    }


}
