package com.khhs.clinetappsub;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.khhs.clinetappsub.adapter.GenreAdapter;
import com.khhs.clinetappsub.adapter.MovieAdapter;
import com.khhs.clinetappsub.adapter.SeriesAdapter;
import com.khhs.clinetappsub.models.Genre;
import com.khhs.clinetappsub.models.Movie;
import com.khhs.clinetappsub.models.Series;
import com.khhs.clinetappsub.models.SlideModel;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static TextView txtGenre,txtMovie,txtSeries;
    public  static RecyclerView rcvGenre,rcvMovie,rcVSeries;
    public HomeFragment() {
        // Required empty public constructor
    }
    public static CarouselView carouselView;



    View myView;
    ArrayList<String> sampleImages=new ArrayList<>();
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            try {
                Glide.with(getContext())
                        .load(sampleImages.get(position))
                        .into(imageView);
            }
            catch (Exception ex)
            {
                Log.e("Exception ",ex.getMessage());
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       myView = inflater.inflate(R.layout.fragment_home, container, false);
        GoogleAdMob adMob = new GoogleAdMob();
        adMob.loadBanner(myView,getContext(),getActivity());

       carouselView = myView.findViewById(R.id.carouselView);
       txtGenre = myView.findViewById(R.id.txtGenre);
       txtMovie = myView.findViewById(R.id.txtMovie);
       txtSeries = myView.findViewById(R.id.txtSeries);
       rcvGenre =  myView.findViewById(R.id.rcvGenre);
        rcvMovie = myView.findViewById(R.id.rcvMovies);
        rcVSeries = myView.findViewById(R.id.rcvSeries);


       carouselView.setPageCount(0);
       carouselView.setImageListener(imageListener);
        FirebaseFirestore db  =FirebaseFirestore.getInstance();
        CollectionReference slideRef = db.collection(getString(R.string.slide_ref));
        CollectionReference genreRef = db.collection(getString(R.string.genre_ref));
        slideRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                ArrayList<SlideModel> slideModels = new ArrayList<>();
                for (DocumentSnapshot s: queryDocumentSnapshots)
                {
                    slideModels.add(s.toObject(SlideModel.class));
                }

                sampleImages.add(slideModels.get(0).s1);
                sampleImages.add(slideModels.get(0).s2);
                sampleImages.add(slideModels.get(0).s3);
                sampleImages.add(slideModels.get(0).s4);
                sampleImages.add(slideModels.get(0).s5);
                carouselView.setPageCount(sampleImages.size());

                carouselView.setImageListener(imageListener);

            }
        });

        genreRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Genre> genres = new ArrayList<>();
                Genre all= new Genre();
                all.setName("All");
                genres.add(all);
                for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    genres.add(snapshot.toObject(Genre.class));
                }
                GenreAdapter adapter = new GenreAdapter(genres,getContext(),getFragmentManager());
                txtGenre.setText("All Genres ("+genres.size()+")");
                rcvGenre.setAdapter(adapter);
                rcvGenre.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
            }
        });

        final CollectionReference movieRef = db.collection(getString(R.string.movie_ref));
        movieRef.orderBy("createdAt", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Movie> movies  = new ArrayList<>();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                {
                   movies.add(snapshot.toObject(Movie.class));
                }
                MovieAdapter adapter = new MovieAdapter(movies,getContext(),getFragmentManager());
                LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                rcvMovie.setAdapter(adapter);
                rcvMovie.setLayoutManager(lm);
                txtMovie.setText("All Movies ( "+movies.size()+")");
            }
        });
        CollectionReference seriesRef= db.collection(getString(R.string.series_ref));
        seriesRef.orderBy("createdAt", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Series> series = new ArrayList<>();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                {
                    series.add(snapshot.toObject(Series.class));
                }
                SeriesAdapter adapter = new SeriesAdapter(series,getContext(),getFragmentManager());
                rcVSeries.setAdapter(adapter);
                LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                rcVSeries.setLayoutManager(lm);
                txtSeries.setText("All Series ("+series.size()+")");
            }
        });
        return  myView;
    }
}
