package com.khhs.clinetappsub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        LatLng loc = new LatLng(21.9489697,96.0732459);
        mMap.addMarker(new MarkerOptions().position(loc).title("Khin Htet Htet Hsu"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,25));
    }

    public AboutFragment() {
        // Required empty public constructor
    }


    View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_about, container, false);

        GoogleAdMob adMob = new GoogleAdMob();
        adMob.loadBanner(myView,getContext(),getActivity());SupportMapFragment mapFragment =(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return  myView;
    }
}
