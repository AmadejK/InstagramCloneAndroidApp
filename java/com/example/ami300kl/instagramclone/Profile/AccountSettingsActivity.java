package com.example.ami300kl.instagramclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Utils.BottomNavViewHelper;
import com.example.ami300kl.instagramclone.Utils.FirebaseMethods;
import com.example.ami300kl.instagramclone.Utils.SectionsStatePagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * Created by Ami300kl on 20. 12. 2018.
 */

public class AccountSettingsActivity extends AppCompatActivity {

   private static final String TAG ="AccountSettingsActivity";
   private Context mContext;
   private static final int ACTIVITY_NUM=4;
   public SectionsStatePagerAdapter pagerAdapter;
   private ViewPager mViewPager;
   private RelativeLayout mRelativeLayout;


   @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_accountsettings);
       Log.d(TAG, "onCreate started.");
       mViewPager=(ViewPager)findViewById(R.id.container);
       mRelativeLayout=(RelativeLayout)findViewById(R.id.relLayout1);
       mContext = AccountSettingsActivity.this;
       //backarrow

       setupSettingsList();
       SetupBottomNavigationView();
       setupFragments();
       getIncommingActivity();

       ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
       backArrow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });


   }


   private void getIncommingActivity()
   { Log.d(TAG, "getIncommingActivity: Setting Intent ");
       Intent intent = getIntent();

       if(intent.hasExtra(getString(R.string.selected_image))||intent.hasExtra(getString(R.string.selected_bitmap))) {


           //if there is an image url attached as an extra ,than chosen from galery/photo  fragment

           Log.d(TAG, "getIncommingActivity: new incomming img url");
           if (intent.getStringExtra(getString(R.string.return_to_fragment)).equals(getString(R.string.edit_profile_fragment))) {
               if (intent.hasExtra(getString(R.string.selected_image))) {
                   //set the new picture
                   FirebaseMethods firebaseMethods = new FirebaseMethods(AccountSettingsActivity.this);
                   firebaseMethods.uploadNewPhoto(getString(R.string.profile_photo), null, 0,
                           intent.getStringExtra(getString(R.string.selected_image)), null);
               } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
                   //set the new picture
                   FirebaseMethods firebaseMethods = new FirebaseMethods(AccountSettingsActivity.this);
                   firebaseMethods.uploadNewPhoto(getString(R.string.profile_photo), null, 0,
                           null, (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap)));

               }


           }
       }



       Log.d(TAG, "getIncommingActivity: Intent Set ");
       if(intent.hasExtra(getString(R.string.calling_activity))){
           Log.d(TAG, "getIncommingActivity: recived incomming activity from"+getString(R.string.profile_activity));
           setViewPager(pagerAdapter.getFragmentNumber(getString(R.string.edit_profile_fragment)));
       }


   }
       private void setupFragments()
    {
        pagerAdapter=new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddFragment(new EditProfileFragment(),getString(R.string.edit_profile_fragment));
        pagerAdapter.AddFragment(new SignOutFragment(),getString(R.string.sign_out_fragment));
    }


    public void setViewPager(int fragmentNumber)
    {
        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setupViewPager: Navigating to fragment"+ fragmentNumber);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(fragmentNumber);

    }
    private void setupSettingsList()
    {

        Log.d(TAG,"setupSetingsList: initializing Account Settings List");
        ListView listView=(ListView)findViewById(R.id.lvAccountSettings);
        ArrayList<String>options=new ArrayList<>();
        options.add(getString(R.string.edit_profile_fragment));      //fragment 0
        options.add(getString(R.string.sign_out_fragment));          //fragment 1

        ArrayAdapter arrayAdapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_1,options);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: navigating to fragment"+ position);
                setViewPager(position);
            }
        });


    }
    /*
* Bottom Nav setup !!!
* */
    private void SetupBottomNavigationView() {
        Log.d(TAG, "settingBottomNavigationView: Setting up Bottom Navigation View");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.BottomNavViewBar);
        BottomNavViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
