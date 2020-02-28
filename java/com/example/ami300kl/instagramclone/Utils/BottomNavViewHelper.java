package com.example.ami300kl.instagramclone.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.ami300kl.instagramclone.Home.HomeActivity;
import com.example.ami300kl.instagramclone.Likes.LikesActivity;
import com.example.ami300kl.instagramclone.Profile.ProfileActivity;
import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Search.SearchActivity;
import com.example.ami300kl.instagramclone.Share.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Ami300kl on 25. 11. 2018.
 */

public class BottomNavViewHelper {
    private static  final String TAG="BottomNavigationViewHel";
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG,"Setting up Bottom Navigation View");

        bottomNavigationViewEx.enableAnimation(false);
    bottomNavigationViewEx.enableItemShiftingMode(false);
    bottomNavigationViewEx.enableShiftingMode(false);
    bottomNavigationViewEx.setTextVisibility(false);

}
public static void enableNavigation(final Context context, BottomNavigationViewEx view)
    {
      view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {

              switch(item.getItemId())
              {
                  case R.id.ic_house :
                      Intent intent1 = new Intent(context, HomeActivity.class); //ACTIVITY_NUM = 0
                      context.startActivity(intent1);
                     break;
                  case R.id.ic_search :
                      Intent intent2 = new Intent(context, SearchActivity.class); //ACTIVITY_NUM = 1
                      context.startActivity(intent2);
                      break;
                  case R.id.ic_cicrcle :
                      Intent intent3 = new Intent(context, ShareActivity.class); //ACTIVITY_NUM = 2
                      context.startActivity(intent3);
                      break;
                  case R.id.ic_alert :
                      Intent intent4 = new Intent(context, LikesActivity.class); //ACTIVITY_NUM = 3
                      context.startActivity(intent4);
                      break;
                  case R.id.ic_android :
                      Intent intent5 = new Intent(context, ProfileActivity.class); //ACTIVITY_NUM = 4
                      context.startActivity(intent5);
                      break;
              }
              return false;
          }
      });
    }
}
