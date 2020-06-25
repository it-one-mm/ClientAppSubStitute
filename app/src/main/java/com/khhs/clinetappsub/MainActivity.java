package com.khhs.clinetappsub;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.grpc.android.BuildConfig;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SafeBrowsingResponse;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.khhs.clinetappsub.models.Series;

public class MainActivity extends AppCompatActivity {


    public static String mediumFrag="";
    public static Series series;
   public static String currentFrag="";
   public static String prevFrag="";
    DrawerLayout drawerLayout;
    NavigationView navMenu;
    public static Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

       drawerLayout = findViewById(R.id.drawerlayout);
        navMenu = findViewById(R.id.navMenu);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,toolbar,
                R.string.open_drawer,
                R.string.close_drawer);
        toggle.syncState();


        View headerView = navMenu.getHeaderView(0);
        TextView txtVersion,txtAppName;
        txtVersion = headerView.findViewById(R.id.appVersion);

        txtAppName = headerView.findViewById(R.id.appName);
        txtAppName.setText(getString(R.string.app_name));
        txtVersion.setText("Version "+BuildConfig.VERSION_NAME);




      navMenu.getMenu().getItem(0).setChecked(true);
      navMenu.setCheckedItem(0);

        setTitle("Home");
        setFragment(new HomeFragment());
        currentFrag=getString(R.string.home_frage);
        prevFrag=getString(R.string.home_frage);

        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {



                   switch (menuItem.getItemId())
                   {
                       case R.id.home_menu:
                           setFragment(new HomeFragment());
                           setTitle("Home");
                           currentFrag=getString(R.string.home_frage);
                           prevFrag =getString(R.string.home_frage);
                           break;
                       case R.id.movie_menu:
                           setFragment(new MovieFragment());
                           setTitle("Movie");
                           prevFrag=getString(R.string.home_frage);
                           currentFrag=getString(R.string.movie_frage);
                           break;
                       case R.id.series_menu:
                           setFragment(new SeriesFragment());
                           setTitle(getString(R.string.series_frage));
                           prevFrag=getString(R.string.home_frage);
                           currentFrag=getString(R.string.series_frage);
                           break;
                       case R.id.share_menu:
                           Intent shareIntent = new Intent();

                           shareIntent.setAction(Intent.ACTION_SEND);

                           shareIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+ com.khhs.clinetappsub.BuildConfig.APPLICATION_ID);


                           /* shareIntent.putExtra(Intent.EXTRA_TEXT,"https://www.mediafire.com/file/2p2k8wtb1vvamii/cartoon_cafev2.apk/file");*/
                           shareIntent.setType("text/plain");
                           startActivity(shareIntent);
                           break;
                       case R.id.search_menu:
                           setTitle(getString(R.string.search_frage));
                           setFragment(new SearchFragment());
                           prevFrag = getString(R.string.home_frage);
                           currentFrag = getString(R.string.search_frage);
                           break;
                        case   R.id.request_menu:
                       setFragment(new RequestFragment());
                           setTitle(getString(R.string.request_frage));
                           prevFrag = getString(R.string.home_frage);
                           currentFrag = getString(R.string.request_frage);
                           break;
                       case R.id.about_menu:
                           setFragment(new AboutFragment());
                           setTitle(getString(R.string.about_frage));
                           prevFrag = getString(R.string.home_frage);
                           currentFrag = getString(R.string.about_frage);
                           break;


                   }
                drawerLayout.closeDrawer(Gravity.LEFT);

                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT))
        {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else
        {

            if(prevFrag.equals(getString(R.string.home_frage))
                    &&
                    currentFrag.equals(getString(R.string.home_frage)))
                {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exit!");
                builder.setMessage("Are You Sure To Exit");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();;
                        finish();;
                    }

                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
            else if(
                    currentFrag.equals(getString(R.string.movie_frage))
                    || currentFrag.equals(getString(R.string.series_frage))
                    || currentFrag.equals(getString(R.string.request_frage))
                    || currentFrag.equals(getString(R.string.search_frage))
                    || currentFrag.equals(getString(R.string.about_frage))
            )
            {
                setFragment(new HomeFragment());
                setTitle(getString(R.string.home_frage));
                currentFrag=getString(R.string.home_frage);
                prevFrag=getString(R.string.home_frage);
            }

            else if(currentFrag.equals(getString(R.string.video_det_frage))
            && prevFrag.equals(getString(R.string.series_det_frage)))
            {
                SeriesDetFragment detFragment = new SeriesDetFragment();
                detFragment.myModel=MainActivity.series;
                series=null;
                setFragment(detFragment);
                setTitle(getString(R.string.series_frage));
                currentFrag=getString(R.string.series_det_frage);
                prevFrag=MainActivity.mediumFrag;
            }
            else if(currentFrag.equals(getString(R.string.video_det_frage))
            && prevFrag.equals(getString(R.string.home_frage)))
            {
                setFragment(new HomeFragment());
                setTitle(getString(R.string.home_frage));
                currentFrag=getString(R.string.home_frage);
                prevFrag=getString(R.string.home_frage);
            }
            else if(currentFrag.equals(getString(R.string.video_det_frage))
                    && prevFrag.equals(getString(R.string.movie_frage)))
            {
                setFragment(new MovieFragment());
                setTitle(getString(R.string.movie_frage));
                currentFrag=getString(R.string.movie_frage);
                prevFrag=getString(R.string.video_det_frage);
            }
            else if(currentFrag.equals(getString(R.string.video_det_frage))
                    && prevFrag.equals(getString(R.string.search_frage)))
            {
                setFragment(new SearchFragment());
                setTitle(getString(R.string.search_frage));
                currentFrag=getString(R.string.search_frage);
                prevFrag=getString(R.string.video_det_frage);
            }
            else if(currentFrag.equals(getString(R.string.series_det_frage))
            && prevFrag.equals(getString(R.string.home_frage)))
            {
                setFragment(new HomeFragment());
                setTitle(getString(R.string.home_frage));
                currentFrag=getString(R.string.home_frage);
                prevFrag=getString(R.string.home_frage);

            }
            else if(currentFrag.equals(getString(R.string.series_det_frage))
                    && prevFrag.equals(getString(R.string.series_frage)))
            {
                setFragment(new SeriesFragment());
                setTitle(getString(R.string.series_frage));
                currentFrag=getString(R.string.series_frage);
                prevFrag=getString(R.string.series_det_frage);

            } else if(currentFrag.equals(getString(R.string.series_det_frage))
                    && prevFrag.equals(getString(R.string.search_frage)))
            {
                setFragment(new SearchFragment());
                setTitle(getString(R.string.search_frage));
                currentFrag=getString(R.string.search_frage);
                prevFrag=getString(R.string.series_det_frage);

            }
        }

        if(VideoDetFragment.player!=null)
        {
            VideoDetFragment.player.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(VideoDetFragment.player!=null)
        {
            VideoDetFragment.player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(VideoDetFragment.player!=null)
        {
            VideoDetFragment.player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(VideoDetFragment.player!=null)
        {
            VideoDetFragment.player.setPlayWhenReady(true);
        }
    }


    public void setFragment(Fragment f)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tf = fm.beginTransaction();
        tf.replace(R.id.content_fragment,f);
        tf.commit();
    }



}
