package com.example.ami300kl.instagramclone.Share;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Utils.BottomNavViewHelper;
import com.example.ami300kl.instagramclone.Utils.Permissions;
import com.example.ami300kl.instagramclone.Utils.SectionsPagerAdapter;
import com.example.ami300kl.instagramclone.Utils.SectionsStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Ami300kl on 25. 11. 2018.
 */

public class ShareActivity extends AppCompatActivity{
    private Context mContext = ShareActivity.this;
    private static final int ACTIVITY_NUM=2;
    private static final int VERIFY_PERMISSION_REQUEST=1;
    private static final  String TAG="ShareActivity";
    private ViewPager mViewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG,"onCreate started");

        if(checkPermissionArray(Permissions.PERMISSIONS))
        {

            setupViewPager();
        }else
            {
                verifyPermissions(Permissions.PERMISSIONS);
            }
        //SetupBottomNavigationView();
    }
    /*
    * 0=GALLERY FRAGMENT
    * 1=PHOTO FRAGMENT
    * */
    public int getCurrentTabNumber()
    {
        return mViewPager.getCurrentItem();
    }
    /*
    * Setup viewvpager for tabs
    * */
    private void setupViewPager()
    {
        SectionsPagerAdapter adapter= new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        mViewPager=(ViewPager)findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        tabLayout.getTabAt(1).setText(getString(R.string.photo));



    }
public int getTask()
{
   // Log.d(TAG, "getTask:task" + getIntent().getFlags());
    return getIntent().getFlags();
}
    /**
     *Verify all the permissions passed
     * @ param permission
     *
     * */
    public void verifyPermissions(String[]permissions)
    {
        Log.d(TAG, "verifyPermissions: ");
        ActivityCompat.requestPermissions(ShareActivity.this,permissions,VERIFY_PERMISSION_REQUEST);
    }
public boolean checkPermissionArray(String[]permissions)
{
    Log.d(TAG, "checkPermissionArray: checking permission array");
    for(int i =0;i<permissions.length;i++)
    {
        String  check= permissions[i];
        if(!checkPermissions(check))
        {
            return false;
        }
    }
    return true;
}
    public boolean checkPermissions(String permission)
    {
        Log.d(TAG, "checkPermissions: checking single permission"+permission);
        int permissionRequest= ActivityCompat.checkSelfPermission(ShareActivity.this,permission);
        if(permissionRequest!=PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "checkPermissions: permission not granted for :"+permission);
            return false;
        }else
            {
                Log.d(TAG, "checkPermissions: permission Granted");
                return true;
            }
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
