package com.example.ami300kl.instagramclone.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Utils.FirebaseMethods;
import com.example.ami300kl.instagramclone.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ami300kl on 27. 02. 2019.
 */

public class NextActivity extends AppCompatActivity {
    private static final String TAG = "NextActivity";
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mfirebaseMethods;

    //Widgets
    private EditText mCaption;


    //Var
    private static final String mAppend="file:/";
    private int imageCount=0;
    private String imgURL;
    private Intent intent;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        mfirebaseMethods=new FirebaseMethods(NextActivity.this);
        mCaption=(EditText)findViewById(R.id.caption);

        setupFirebaseAuth();


        ImageView backArrow=(ImageView)findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clossing the Activity");
                finish();
            }
        });
        TextView share=(TextView)findViewById(R.id.tvshare);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to final screen");
                //Upload the image !!!
                Toast.makeText(NextActivity.this,"Attempting to upload ",Toast.LENGTH_LONG);
                String caption =mCaption.getText().toString();

                if(intent.hasExtra(getString(R.string.selected_image))){
                    imgURL=intent.getStringExtra(getString(R.string.selected_image));
                    mfirebaseMethods.uploadNewPhoto(getString(R.string.new_photo),caption,imageCount,imgURL,null);

                }else
                if(intent.hasExtra(getString(R.string.selected_bitmap))){
                    bitmap = intent.getParcelableExtra(getString(R.string.selected_bitmap));
                    mfirebaseMethods.uploadNewPhoto(getString(R.string.new_photo),caption,imageCount,null,bitmap);


                }

            }
        });
        setImage();
    }
    private void someMethod()
    {
        /*
        * step 1)
        * create a data model for photos
        * step 2)
        * Add properties to the photoObjects(caption,date,imgURL,photoID,tags,userID)
        * step 3)
        * count the number of photos
        * step 4)
        * Upload photo to firebase storage insert 2 new nodes in the firebase database
        *
        * */

    }
    private void setImage()
{
     intent= getIntent();
    ImageView image =(ImageView)findViewById(R.id.imageShare);
    if(intent.hasExtra(getString(R.string.selected_image))){
        imgURL=intent.getStringExtra(getString(R.string.selected_image));
        UniversalImageLoader.setImage(imgURL,image,null,mAppend);
    }else
        if(intent.hasExtra(getString(R.string.selected_bitmap))){
        bitmap = intent.getParcelableExtra(getString(R.string.selected_bitmap));
            Log.d(TAG, "setImage: got new bitmap");
            image.setImageBitmap(bitmap);
    }



}

        /*--------------------------------------------------------------*/
    //Setup the firebase  auth object
    /*--------------------------------------------------------------*/

    private void setupFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase= FirebaseDatabase.getInstance();
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
             imageCount=mfirebaseMethods.getImageCount(dataSnapshot);

                Log.d(TAG, "onDataChange: image  count"+ imageCount);
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
