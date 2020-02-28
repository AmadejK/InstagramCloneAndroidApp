package com.example.ami300kl.instagramclone.Profile;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ami300kl.instagramclone.Home.HomeActivity;
import com.example.ami300kl.instagramclone.Module.User;
import com.example.ami300kl.instagramclone.Module.UserAccountSettings;
import com.example.ami300kl.instagramclone.Module.UserSettings;
import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Share.ShareActivity;
import com.example.ami300kl.instagramclone.Utils.FirebaseMethods;
import com.example.ami300kl.instagramclone.Utils.UniversalImageLoader;

import com.example.ami300kl.instagramclone.dialogs.ConfirmPasswordDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ami300kl on 29. 12. 2018.
 */

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mfirebaseMethods;
    private String userID;

    //EditProfile Widgets
    private EditText mDisplayName,mUsername,mWebsite,mDescription,mEmail,mPhoneNumber;
    private TextView mChangeProfilePhoto;
    private CircleImageView mProfilePhoto;
    //Variables
    private UserSettings mUserSettings;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable final Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_editprofile,container,false);

        mProfilePhoto =(CircleImageView)view.findViewById(R.id.profile_photo);
        mDisplayName=(EditText) view.findViewById(R.id.display_name);
        mUsername=(EditText)view.findViewById(R.id.username);
        mWebsite=(EditText)view.findViewById(R.id.website);
        mDescription=(EditText)view.findViewById(R.id.description);
        mEmail=(EditText)view.findViewById(R.id.email);
        mPhoneNumber=(EditText)view.findViewById(R.id.phoneNumber);
        mChangeProfilePhoto=(TextView)view.findViewById(R.id.changeProfilePhoto);

        mfirebaseMethods= new FirebaseMethods(getActivity());
        Log.d(TAG, "onCreateView: Succesfull called onCreate");
       //Initiating image loader setting up for set ProfileImage
        //setting up profile image
       // setProfileImage();
        setupFirebaseAuth();


        //backbutton
        ImageView backArrow=(ImageView)view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigatinmg back to ProfileActivity ");
                getActivity().finish();


            }
        });
        ImageView checkmark=(ImageView)view.findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                saveProfileSettings();
            }
        });

        return view;
    }


    //save profile data ! with pressing a button
    // Check the username and make sure it is unique

    private void checkifUsernameExists(final String username) {
        Log.d(TAG, "checkifUsernameExists: Checking if" + username + "Alredy Exists");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        Query query=reference.child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {//add username
                    Toast.makeText(getActivity(),"saved username",Toast.LENGTH_LONG).show();
                    mfirebaseMethods.updateUsername(username);
                    Log.d(TAG, "checkIfUsernameExists: Data Passed");


                }
                for(DataSnapshot singleSnapshot:dataSnapshot.getChildren())
                {
                    if(singleSnapshot.exists())
                    {
                        Toast.makeText(getActivity(),"That username Alredy EXIST",Toast.LENGTH_LONG).show();
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH "+singleSnapshot.getValue(User.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void saveProfileSettings()
    {
    final String displayName=mDisplayName.getText().toString();
    final String username = mUsername.getText().toString();
    final String website=mWebsite.getText().toString();
    final String description=mDescription.getText().toString();
    final String email=mEmail.getText().toString();
    final long phoneNumber=Long.parseLong(mPhoneNumber.getText().toString());




            Log.d(TAG, "onDataChange:Current Username ");
            //if username is the current username than no changes have been made
            // case 1: user did not change USERNAME

            //case 2 : Check if Username is Unique
            if(!mUserSettings.getUser().getUsername().equals(username))
            {
                checkifUsernameExists(username);

            }
            //user made a change to the Email
            else if(!mUserSettings.getUser().getEmail().equals(email)) {
                // 1.)Reauthenticate the user Email
                ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));

                // 2.)Check if Email alredy registered

                //         .fetchProvidersForEmail(String email)
                // 3.)change  the email.
                //         .submit  the new email to the database authentification
            }


        if(!mUserSettings.getSettings().getDisplay_name().equals(displayName))
        {//update displayName
            mfirebaseMethods.updateUserAccountSetings(displayName,null,null,0);
        }
        if(!mUserSettings.getSettings().getWebsite().equals(website))
        {//update website
            mfirebaseMethods.updateUserAccountSetings(null,website,null,0);

        }
        if(!mUserSettings.getSettings().getDescription().equals(description))
        {//update description
            mfirebaseMethods.updateUserAccountSetings(null,null,description,0);

        }
        if(!mUserSettings.getSettings().getProfile_photo().equals(phoneNumber))
        {//update phoneNumber
            mfirebaseMethods.updateUserAccountSetings(null,null,null,phoneNumber);
        }



    }
    private void setProfileWidgets(UserSettings userSettings)
    {
      //  Log.d(TAG, "setProfileWidgets: setting widgets withdata retrieving from firebase database"+userSettings.toString());
      //  Log.d(TAG, "setProfileWidgets: setting widgets withdata retrieving from firebase database"+userSettings.getSettings().getUsername());

        mUserSettings=userSettings;
        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();
        UniversalImageLoader.setImage(settings.getProfile_photo(),mProfilePhoto,null,"");
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mEmail.setText(userSettings.getUser().getEmail());
        mPhoneNumber.setText(String.valueOf(userSettings.getUser().getPhone_number()));

        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: changing profile photo");
                Intent intent=new Intent(getActivity(), ShareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//268345456
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

    }
/*
private  void setProfileImage()
{
    Log.d(TAG, "setProfileImage: setting profile image");
    String imgURL="https://images.idgesg.net/images/article/2018/07/apple-6-color-logo-100763179-large.jpg";
    UniversalImageLoader.setImage(imgURL,mProfilePhoto,null,"");
}
*/

    /*--------------------------------------------------------------*/
    //Setup the firebase  auth object
    /*--------------------------------------------------------------*/

    private void setupFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();
        userID=mAuth.getCurrentUser().getUid();
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
                //retrive user data !!!

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
