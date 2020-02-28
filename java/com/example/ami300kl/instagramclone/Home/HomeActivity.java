package com.example.ami300kl.instagramclone.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ami300kl.instagramclone.Login.LoginActivity;
import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Utils.BottomNavViewHelper;
import com.example.ami300kl.instagramclone.Utils.SectionsPagerAdapter;
import com.example.ami300kl.instagramclone.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity {
    private Context mContext = HomeActivity.this;
    private static final int ACTIVITY_NUM = 0;
    private static final String TAG = "Home Activity";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate starting.");
        //firebase setup //
        setupFirebaseAuth();
        //firebase setup //

        initImageLoader();
        SetupBottomNavigationView();
        setupViewPager();


    }

    private void initImageLoader()
    {
        UniversalImageLoader universalImageLoader= new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
    /*
* responsible for adding tabs*/

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new MessagesFragment());
        ViewPager viewpager= (ViewPager)findViewById(R.id.container);
        viewpager.setAdapter(adapter);
        TabLayout tablayout=(TabLayout)findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewpager);
        tablayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tablayout.getTabAt(1).setIcon(R.drawable.ic_action_name);
        tablayout.getTabAt(2).setIcon(R.drawable.ic_arrow);



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
/*--------------------------------------------------------------*/
    //Setup the firebase  auth object
    /*--------------------------------------------------------------*/
/*if user is Logged Inn than go to home Activity and Call finish/();*/
    private void checkCurrentUser(FirebaseUser user)
{
    Log.d(TAG, "checkCurrentUser: Checking if user is logged inn;");
    if(user==null)
    {
        Intent intent=new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
    private void setupFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                Log.d(TAG, "onAuthStateChanged: setting up authorization check");
                FirebaseUser user = mAuth.getCurrentUser();
                // Calling method to check user @param
                checkCurrentUser(user);

                if(user!=null)
                {//user logged inn
                    Log.d(TAG, "onAuthStateChanged: signed_in" + user.getUid());
                }
                else
                {
                    //user signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set the listener for Checking user state logged IN/OUT
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
    }
    @Override
    public void onStop() {
        super.onStop();
        // Set the listener for Checking user state logged IN/OUT
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    /*--------------------------------------------------------------*/
    //END OF FIREBASE
    /*--------------------------------------------------------------*/
}