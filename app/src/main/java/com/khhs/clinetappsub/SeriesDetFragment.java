package com.khhs.clinetappsub;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.khhs.clinetappsub.adapter.EpisdoeAdapter;
import com.khhs.clinetappsub.models.Episode;
import com.khhs.clinetappsub.models.Series;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesDetFragment extends Fragment {

   public Series myModel;
    public SeriesDetFragment() {
        // Required empty public constructor
    }

    View myView;

    ImageView image;
    TextView txtName;
    FirebaseFirestore db;
    CollectionReference ref;
    RecyclerView rcEpList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      myView = inflater.inflate(R.layout.fragment_series_det, container, false);
        GoogleAdMob adMob = new GoogleAdMob();
        adMob.loadBanner(myView,getContext(),getActivity());
      image = myView.findViewById(R.id.image);
      txtName = myView.findViewById(R.id.seriesName);
      rcEpList =  myView.findViewById(R.id.epList);
    if(myModel!=null)
    {
        getActivity().setTitle(myModel.getTitle());
    }
      if(myModel!=null)
      {
          Glide.with(getContext())
                  .load(myModel.getImageUrl())
                  .into(image);
          txtName.setText(myModel.getTitle());
          db= FirebaseFirestore.getInstance();
          ref = db.collection(getString(R.string.episode_ref));
          ref.whereEqualTo("seriesTitle",myModel.getTitle())
                  .orderBy("createdAt")
                  .addSnapshotListener(new EventListener<QuerySnapshot>() {
                      @Override
                      public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                          ArrayList<Episode> episodes = new ArrayList<>();
                          for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                          {
                              episodes.add(snapshot.toObject(Episode.class));
                          }
                          EpisdoeAdapter adapter = new EpisdoeAdapter(episodes,getContext(),getFragmentManager());
                          rcEpList.setAdapter(adapter);
                          rcEpList.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));


                      }
                  });

      }
      return myView;

    }


}
