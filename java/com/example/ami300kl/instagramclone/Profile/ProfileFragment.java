package com.example.ami300kl.instagramclone.Profile;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ami300kl.instagramclone.Login.LoginActivity;
import com.example.ami300kl.instagramclone.Module.User;
import com.example.ami300kl.instagramclone.Module.UserAccountSettings;
import com.example.ami300kl.instagramclone.Module.UserSettings;
import com.example.ami300kl.instagramclone.Photo;
import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Utils.BottomNavViewHelper;
import com.example.ami300kl.instagramclone.Utils.FirebaseMethods;
import com.example.ami300kl.instagramclone.Utils.GridImageAdapter;
import com.example.ami300kl.instagramclone.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ami300kl on 23. 01. 2019.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private TextView mPosts,mFollowers,mFollowing,mDisplayName,mUsername,mWebsite,mDescription;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mfirebaseMethods;

    //Variables
    private final int NUM_GRID_COLUMNG=3;
    private static final int ACTIVITY_NUM=4;
    private Context mContext;

    @Nullable
    @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_profile,container,false);

        mPosts=(TextView)view.findViewById(R.id.tvPosts);
        mFollowers=(TextView)view.findViewById(R.id.tvFollower);
        mFollowing=(TextView)view.findViewById(R.id.tvFolowing);
        mDisplayName=(TextView)view.findViewById(R.id.display_name);
        mUsername=(TextView)view.findViewById(R.id.profileName);
        mWebsite=(TextView)view.findViewById(R.id.website);
        mDescription=(TextView)view.findViewById(R.id.description);
        mProgressBar=(ProgressBar)view.findViewById(R.id.profileProgresBar);
        gridView=(GridView)view.findViewById(R.id.gridView);
        toolbar=(Toolbar)view.findViewById(R.id.profileTolBar);
        profileMenu=(ImageView)view.findViewById(R.id.profileMenu);
        bottomNavigationView =(BottomNavigationViewEx)view.findViewById(R.id.BottomNavViewBar);
        mProfilePhoto=(CircleImageView)view.findViewById(R.id.profile_photo);
        mContext = getActivity();
        mfirebaseMethods = new FirebaseMethods(getActivity());



        Log.d(TAG, "onCreateView: started;"+ getActivity());


        setupBottomNavigationView();
        setupToolbar();

        setupFirebaseAuth();
        setupGridView();

        TextView editProfile =(TextView)view.findViewById(R.id.textEditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to "+ mContext.getString(R.string.edit_profile_fragment));
                Intent intent= new Intent(mContext, AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity),getString(R.string.profile_activity));
                startActivity(intent);
            }
        });

        return view ;
    }
    private void setupGridView() {
        Log.d(TAG, "setupGridView: setting up img grid");
        final ArrayList<Photo>photos=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        Query query=reference
                .child(getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot:dataSnapshot.getChildren())
                {
                    photos.add(singleSnapshot.getValue(Photo.class));
                }
                //setup image grid
                int gridWidth=getResources().getDisplayMetrics().widthPixels;
                int imageWidth=gridWidth/NUM_GRID_COLUMNG;
                gridView.setColumnWidth(imageWidth);

                ArrayList<String>imgURLs=new ArrayList<>();
                for(int i=0;i<photos.size();i++)
                {
                    imgURLs.add(photos.get(i).getImage_path());
                }
                GridImageAdapter adapter = new GridImageAdapter(getActivity(),R.layout.layout_grid_imageview,"",imgURLs);
                gridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled");
            }
        });
    }

    private void setProfileWidgets(UserSettings userSettings)
    {
        Log.d(TAG, "setProfileWidgets: setting widgets withdata retrieving from firebase database"+userSettings.toString());
        Log.d(TAG, "setProfileWidgets: setting widgets withdata retrieving from firebase database"+userSettings.getSettings().getUsername());

        User user = userSettings.getUser();
        UserAccountSettings  settings = userSettings.getSettings();
        UniversalImageLoader.setImage(settings.getProfile_photo(),mProfilePhoto,null,"");
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mPosts.setText(String.valueOf(settings.getPosts()));
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));

        mProgressBar.setVisibility(View.GONE);
    }


    private void setupToolbar()
    {
        Log.d(TAG, "setupToolbar: setting up toolbar");

        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick: navigating to account settings");
                Intent intent= new Intent(mContext,AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }



    private void setupBottomNavigationView()
    {
        Log.d(TAG,"settingBottomNavigationView: Setting up Bottom Navigation View");
        BottomNavViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavViewHelper.enableNavigation(mContext, bottomNavigationView);
        Menu menu= bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    /*--------------------------------------------------------------*/
    //Setup the firebase  auth object
    /*--------------------------------------------------------------*/

    private void setupFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                Log.d(TAG, "onAuthStateChanged: setting up authorization check");
                FirebaseUser user = mAuth.getCurrentUser();


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


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retrive user data !!! and inflate Profile Fragment

               setProfileWidgets(mfirebaseMethods.getUserSettings(dataSnapshot));

                //retrive images for the user in question
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set the listener for Checking user state logged IN/OUT
        mAuth.addAuthStateListener(mAuthListener);
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
