package com.khhs.clinetappsub;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.khhs.clinetappsub.adapter.MovieAdapter;
import com.khhs.clinetappsub.adapter.SeriesAdapter;
import com.khhs.clinetappsub.models.Movie;
import com.khhs.clinetappsub.models.Series;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }


    TextView txtMovie,txtSeries;
    EditText search;
    RecyclerView rcvMovies,rcvSeries;
    View myView;
    FirebaseFirestore db;
    CollectionReference movieRef,seriesRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         myView = inflater.inflate(R.layout.fragment_search, container, false);
        GoogleAdMob adMob = new GoogleAdMob();
        adMob.loadBanner(myView,getContext(),getActivity());
         search = myView.findViewById(R.id.search);
        txtMovie = myView.findViewById(R.id.txtMovie);
        txtSeries = myView.findViewById(R.id.txtSeries);
        rcvMovies = myView.findViewById(R.id.rcvMovies);
        rcvSeries  = myView.findViewById(R.id.rcvSeries);
        db= FirebaseFirestore.getInstance();
        movieRef =  db.collection(getString(R.string.movie_ref));
        seriesRef  = db.collection(getString(R.string.series_ref));
        loadData();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(search.getText().toString().trim().equals(""))
                {

                    loadData();
                }
                else
                {
                    movieRef
                            .orderBy("title")
                            .startAt(search.getText().toString().trim())
                            .endAt(search.getText().toString().trim()+"\uf8ff")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    ArrayList<Movie> movies  = new ArrayList<>();
                                    for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                    {
                                        movies.add(snapshot.toObject(Movie.class));
                                    }
                                    MovieAdapter adapter = new MovieAdapter(movies,getContext(),getFragmentManager());
                                    LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                                    rcvMovies.setAdapter(adapter);
                                    rcvMovies.setLayoutManager(lm);
                                    txtMovie.setText(search.getText().toString()+" Movies ("+movies.size()+")");
                                }
                            });
                    seriesRef.orderBy("title")
                            .startAt(search.getText().toString().trim())
                            .endAt(search.getText().toString().trim()+"\uf8ff")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    ArrayList<Series> series = new ArrayList<>();
                                    for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                    {
                                        series.add(snapshot.toObject(Series.class));
                                    }
                                    SeriesAdapter adapter = new SeriesAdapter(series,getContext(),getFragmentManager());
                                    rcvSeries.setAdapter(adapter);
                                    LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                                    rcvSeries.setLayoutManager(lm);
                                    txtSeries.setText(search.getText().toString().trim()+" Series ("+series.size()+")");
                                }
                            });

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
         return  myView;

    }

    public void loadData()
    {
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
                rcvMovies.setAdapter(adapter);
                rcvMovies.setLayoutManager(lm);
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
                rcvSeries.setAdapter(adapter);
                LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                rcvSeries.setLayoutManager(lm);
                txtSeries.setText("All Series ("+series.size()+")");
            }
        });
    }

}
