package com.khhs.clinetappsub.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.khhs.clinetappsub.HomeFragment;
import com.khhs.clinetappsub.R;
import com.khhs.clinetappsub.models.Genre;
import com.khhs.clinetappsub.models.Movie;
import com.khhs.clinetappsub.models.Series;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import project.aamir.sheikh.circletextview.CircleTextView;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyItems> {

    Context context;
    ArrayList<Genre> mArrayList;
    FragmentManager fm;



    public GenreAdapter(ArrayList<Genre> mArrayList,Context context,FragmentManager fm) {

        this.mArrayList = mArrayList;
        this.context = context;
        this.fm = fm;
    }


    @Override
    public MyItems onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent,false);
        MyItems items = new MyItems(v);
        return items;
    }

    @Override
    public void onBindViewHolder(final MyItems holder, final int position) {
        holder.mTextView.setText(mArrayList.get(position).getName());
        holder.mCircleTextView.setCustomText(mArrayList.get(position).getName());
        holder.mCircleTextView.setSolidColor(position);
        holder.mCircleTextView.setTextColor(Color.WHITE);
        holder.mCircleTextView.setCustomTextSize(18);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference Movieref = db.collection("movies");
        final CollectionReference Seriesref= db.collection("series");
        holder.mCircleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mArrayList.get(position).getName().equals("All"))
                {
                    Movieref.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ArrayList<Movie> movies = new ArrayList<Movie>();
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                            {
                                movies.add(snapshot.toObject(Movie.class));

                            }
                            MovieAdapter adapter = new MovieAdapter(movies,context,fm);
                            HomeFragment.rcvMovie.setAdapter(adapter);
                            HomeFragment.rcvMovie.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                            HomeFragment.txtMovie.setText("All Movie ("+movies.size()+")");
                        }
                    });
                    Seriesref.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ArrayList<Series> series = new ArrayList<Series>();
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                            {
                                series.add(snapshot.toObject(Series.class));

                            }
                            SeriesAdapter adapter = new SeriesAdapter(series,context,fm);
                            HomeFragment.rcVSeries.setAdapter(adapter);
                            HomeFragment.rcVSeries.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                            HomeFragment.txtSeries.setText("All Series("+series.size()+")");
                        }
                    });
                }
                else
                {
                    Movieref.whereEqualTo("genreName",mArrayList.get(position).getName())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ArrayList<Movie> movies = new ArrayList<Movie>();
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                            {
                                movies.add(snapshot.toObject(Movie.class));

                            }
                            MovieAdapter adapter = new MovieAdapter(movies,context,fm);
                            HomeFragment.rcvMovie.setAdapter(adapter);
                            HomeFragment.rcvMovie.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                            HomeFragment.txtMovie.setText(mArrayList.get(position).getName()+"("+movies.size()+")");
                        }
                    });
                    Seriesref.whereEqualTo("genreName",mArrayList.get(position).getName())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ArrayList<Series> series = new ArrayList<Series>();
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                            {
                                series.add(snapshot.toObject(Series.class));

                            }
                            SeriesAdapter adapter = new SeriesAdapter(series,context,fm);
                            HomeFragment.rcVSeries.setAdapter(adapter);
                            HomeFragment.rcVSeries.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));

                            HomeFragment.txtSeries.setText(mArrayList.get(position).getName()+"("+series.size()+")");
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class MyItems extends RecyclerView.ViewHolder {
        TextView mTextView;
        CircleTextView mCircleTextView;

        public MyItems(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv);
            mCircleTextView = (CircleTextView) itemView.findViewById(R.id.ctv);

        }
    }
}