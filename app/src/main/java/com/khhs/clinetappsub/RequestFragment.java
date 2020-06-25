package com.khhs.clinetappsub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.khhs.clinetappsub.models.RequestModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    EditText edtname,image;
    Button save,cancel;
    View myView;
    public RequestFragment() {
        // Required empty public constructor
    }


    FirebaseFirestore db;
    CollectionReference ref;
    private InterstitialAd mInterstitalAd;
    private RewardedVideoAd rewardedVideoAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       myView =inflater.inflate(R.layout.fragment_request, container, false);
        GoogleAdMob adMob = new GoogleAdMob();
        adMob.loadBanner(myView,getContext(),getActivity());
        mInterstitalAd = adMob.loadInterAds(getContext());
        rewardedVideoAd =adMob.loadRewarded(getContext());
       edtname = myView.findViewById(R.id.name);
        image  = myView.findViewById(R.id.imagelink);
        save = myView.findViewById(R.id.save);
        db= FirebaseFirestore.getInstance();
        ref = db.collection(getString(R.string.request_ref));
        cancel = myView.findViewById(R.id.cancel);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtname.getText().toString().trim().equals(""))
                {
                    Toasty.error(getContext(),"Please Fill Data",Toasty.LENGTH_LONG).show();
                }
                else
                {
                    if(mInterstitalAd.isLoaded())
                    {
                        mInterstitalAd.show();
                    }
                    if(rewardedVideoAd.isLoaded())
                    {
                        rewardedVideoAd.show();
                    }
                    RequestModel model = new RequestModel();
                    model.setTitle(edtname.getText().toString().trim());
                    model.setImageLink(image.getText().toString().trim());
                    ref.add(model);
                    edtname.setText("");
                    image.setText("");
                    Toasty.success(getContext(),"Save OK",Toasty.LENGTH_LONG).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtname.setText("");
                image.setText("");
            }
        });
        return  myView;
    }
}
