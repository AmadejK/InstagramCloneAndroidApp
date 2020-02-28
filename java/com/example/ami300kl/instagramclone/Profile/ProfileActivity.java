package com.example.ami300kl.instagramclone.Profile;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Utils.BottomNavViewHelper;
import com.example.ami300kl.instagramclone.Utils.GridImageAdapter;
import com.example.ami300kl.instagramclone.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * Created by Ami300kl on 25. 11. 2018.
 */

public class ProfileActivity extends AppCompatActivity{
    private Context mContext = ProfileActivity.this;
    private ProgressBar mprogressbar;
    private ImageView profilePhoto;
    private static final int NUM_GRID_COLUMNS =3;
    private static final int ACTIVITY_NUM=4;
    private static final  String TAG="ProfileActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG,"onCreate started");
        init();
        /*
        setActivityWidgets();
        SetupBottomNavigationView();
        setupToolbar();
        setProfileImage();


        tempGridSetup();
        */
    }
    private void init()
    {
        Log.d(TAG, "init: inflating" + getString(R.string.profile_fragment));
        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }
    /*
    private void tempGridSetup()
    {
        ArrayList<String>imgURLs=new ArrayList<>();
        imgURLs.add("http://3.bp.blogspot.com/-yhJCC3i06U4/UTi7NApnmEI/AAAAAAAAATU/B7e9_9fr89I/s1600/Logo_Android.jpg");
        imgURLs.add("http://3.bp.blogspot.com/-yhJCC3i06U4/UTi7NApnmEI/AAAAAAAAATU/B7e9_9fr89I/s1600/Logo_Android.jpg");
        imgURLs.add("http://3.bp.blogspot.com/-yhJCC3i06U4/UTi7NApnmEI/AAAAAAAAATU/B7e9_9fr89I/s1600/Logo_Android.jpg");


        setupImageGrid(imgURLs);

    }
    private void setupImageGrid(ArrayList<String> imgURLs)
    {
        GridView gridView=(GridView)findViewById(R.id.gridView);
     //Setting up the adapter

        int gridWidth=getResources().getDisplayMetrics().widthPixels;
        int imageWidth=gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter=new GridImageAdapter(mContext,R.layout.layout_grid_imageview,"",imgURLs);
        gridView.setAdapter(adapter);
    }
    private void setProfileImage()
    {
        Log.d(TAG, "setProfileImage: setting up the profile Image");
        String imgURL="images.idgesg.net/images/article/2018/07/apple-6-color-logo-100763179-large.jpg";
        UniversalImageLoader.setImage(imgURL,profilePhoto,mprogressbar,"https://");

    };
    private void setActivityWidgets()
    {
        mprogressbar=(ProgressBar)findViewById(R.id.profileProgresBar);
        mprogressbar.setVisibility(View.GONE);
        profilePhoto=(ImageView)findViewById(R.id.profile_photo);

    }
    //Responsible for setting up toolbar
    private void setupToolbar()
    {
        Log.d(TAG, "setupToolbar: setting up toolbar");

        Toolbar toolbar=(Toolbar)findViewById(R.id.profileTolBar);
        setSupportActionBar(toolbar);
        ImageView profileMenu =(ImageView)findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick: navigating to account settings");
                Intent intent= new Intent(mContext,AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
    /*
   * Bottom Nav setup !!!
   * */
    /*
    * Bottom Nav setup !!!
    *
    private void SetupBottomNavigationView()
    {
        Log.d(TAG,"settingBottomNavigationView: Setting up Bottom Navigation View");
        BottomNavigationViewEx bottomNavigationViewEx =(BottomNavigationViewEx)findViewById(R.id.BottomNavViewBar);
        BottomNavViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavViewHelper.enableNavigation(mContext,bottomNavigationViewEx);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

*/
}
