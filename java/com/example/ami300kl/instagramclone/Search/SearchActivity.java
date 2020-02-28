package com.example.ami300kl.instagramclone.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Utils.BottomNavViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Ami300kl on 25. 11. 2018.
 */

public class SearchActivity extends AppCompatActivity{
    private Context mContext = SearchActivity.this;
    private static final int ACTIVITY_NUM=1;

    private static final  String TAG="SearchActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG,"onCreate started");
        SetupBottomNavigationView();
    }
    /*
    * Bottom Nav setup !!!
    * */
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


}
