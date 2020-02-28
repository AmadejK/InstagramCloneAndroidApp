package com.example.ami300kl.instagramclone.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.ami300kl.instagramclone.Home.HomeActivity;
import com.example.ami300kl.instagramclone.Module.User;
import com.example.ami300kl.instagramclone.Module.UserAccountSettings;
import com.example.ami300kl.instagramclone.Module.UserSettings;
import com.example.ami300kl.instagramclone.Photo;
import com.example.ami300kl.instagramclone.Profile.AccountSettingsActivity;
import com.example.ami300kl.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Created by Ami300kl on 18. 01. 2019.
 */

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext;
    private String userID;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;

    private double mPhotoUploadProgress=0;

    public FirebaseMethods(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDataBase.getReference();
        mStorageReference= FirebaseStorage.getInstance().getReference();

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    public void uploadNewPhoto(String photoType, final String caption, int count, String imgURL,Bitmap bm)
    {
        Log.d(TAG, "uploadNewPhoto: attempting to upload a new photo");
        //new photo

        FilePaths filePaths = new FilePaths();

        if(photoType.equals(mContext.getString(R.string.new_photo)))
        {
            Log.d(TAG, "uploadNewPhoto: new photo uploading on its way");
            String user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference=mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (count + 1));

            //convert imgURL to bitmap
            if(bm==null)
            {
                bm =ImageManager.getBitmap(imgURL);
            }
            byte[]bytes=ImageManager.getBytesFromBitmap(bm,100);

            UploadTask uploadTask=null;
            uploadTask=storageReference.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Uri firebaseURL = taskSnapshot.getDownloadUrl();
        Toast.makeText(mContext,"photo upload succes",Toast.LENGTH_SHORT).show();
        //add the photo to photo nodo and user photos node
        addPhotoToDatabase(caption,firebaseURL.toString());
        //navigate to main feed so the user can see the photo
        Intent intent=new Intent(mContext, HomeActivity.class);
        mContext.startActivity(intent);

    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "onFailure: photo upload failed");
        Toast.makeText(mContext,"photo upload failed",Toast.LENGTH_SHORT).show();

    }
}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

        double progress =(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

    if(progress-15>mPhotoUploadProgress)
    {
        Toast.makeText(mContext,"photo upload progress"+String.format("%.0f",progress),Toast.LENGTH_SHORT).show();
        mPhotoUploadProgress = progress;
    }
        Log.d(TAG, "onProgress: upload progress"+progress+"%done");
    }
});

        }
        else if(photoType.equals(mContext.getString(R.string.profile_photo)))
        {
            Log.d(TAG, "uploadNewPhoto: uploading new profile photo");

            String user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference=mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/profile_photo");

            //convert imgURL to bitmap
            if(bm==null)
            {
                bm =ImageManager.getBitmap(imgURL);
            }

            byte[]bytes=ImageManager.getBytesFromBitmap(bm,100);

            UploadTask uploadTask=null;
            uploadTask=storageReference.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri firebaseURL = taskSnapshot.getDownloadUrl();
                    Toast.makeText(mContext,"photo upload succes",Toast.LENGTH_SHORT).show();

                    //insert into user account settings
                    setProfilePhoto(firebaseURL.toString());
                    ((AccountSettingsActivity)mContext).setViewPager(
                            ((AccountSettingsActivity)mContext).pagerAdapter.getFragmentNumber(mContext.getString(R.string.edit_profile_fragment))
                    );


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: photo upload failed");
                    Toast.makeText(mContext,"photo upload failed",Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress =(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                    if(progress-15>mPhotoUploadProgress)
                    {
                        Toast.makeText(mContext,"photo upload progress"+String.format("%.0f",progress),Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }
                    Log.d(TAG, "onProgress: upload progress"+progress+"%done");
                }
            });

        }

        //profile photo
    }
    private  void setProfilePhoto(String url)
    {
        Log.d(TAG, "setProfilePhoto: setting new profile image");
        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mContext.getString(R.string.profile_photo))
        .setValue(url);
    }
    private String getTimeStamp()
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/pacific"));
        return sdf.format(new Date());
    }
    private void addPhotoToDatabase(String caption,String url)
    {
        Log.d(TAG, "addPhotoToDatabase: adding photos to database");
        String tags=StringManipulation.getTags(caption);


        String newPhotoKey=myRef.child(mContext.getString(R.string.dbname_photos)).push().getKey();
        Photo photo =new Photo();
        photo.setCaption(caption);
        photo.setDate_created(getTimeStamp());
        photo.setImage_path(url);
        photo.setTags(tags);
        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setPhoto_id(newPhotoKey);

        //insert into database
        myRef.child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(newPhotoKey).setValue(photo);

        myRef.child(mContext.getString(R.string.dbname_photos))
                .child(newPhotoKey).setValue(photo);

    }
    public int getImageCount(DataSnapshot dataSnapshot)
{
    int count=0;
    for(DataSnapshot ds:dataSnapshot
            .child(mContext.getString(R.string.dbname_user_photos))
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .getChildren()) {
        count++;

    }
    return count;
}
    public  void updateUserAccountSetings(String displayname, String website ,String description, long phonenumber)
    {

        Log.d(TAG, "updateUserAccountSetings: updating user acc settings");
        String PhoneNumber = Objects.toString(phonenumber, null);
        if(displayname!=null)
        {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_display_name))
                    .setValue(displayname);

        }
        if(website!=null)
        {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_website))
                    .setValue(website);
        }
        if(description!=null)
        {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_description))
                    .setValue(description);
        }
        if(PhoneNumber!=null)
        {
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_phone_number))
                    .setValue(phonenumber);
        }







    }

public void updateUsername(String username)
{
    Log.d(TAG, "updateUsername: updating username to "+ username);
    myRef.child(mContext.getString(R.string.dbname_users))
            .child(userID)
            .child(mContext.getString(R.string.field_username))
            .setValue(username);
    myRef.child(mContext.getString(R.string.dbname_user_account_settings))
            .child(userID)
            .child(mContext.getString(R.string.field_username))
            .setValue(username);
}
/*
    public boolean checkIfUsernameExists(String username, DataSnapshot dataSnapshot) {
        Log.d(TAG, "checkIfUsernameExists: checking if" + username + "alredy exists");

        User user = new User();
        for (DataSnapshot ds : dataSnapshot.child(userID).getChildren()) {
            Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);
            user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG, "checkIfUsernameExists: " + user.getUsername());

            if (StringManipulation.expandUsername(user.getUsername()).equals(username)) {
                Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH " + user.getUsername());

                return true;
            }
        }
        return false;
    }

*/
    /*
    Register a new Email and password to Firebase
    @param email
    @param username
    @param password

     */
    public void registerNewEmail(final String email, String password, final String username) {
        Log.d(TAG, "registerNewEmail: ");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, send Verification Email
                            sendVerificationEmail();
                            //Automaticly signs inn user
                            //Log Out on  Register user!!!!

                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete:Authstate Changed" + userID);
                        } else if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failed", task.getException());
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                Toast.makeText(mContext, "couldn't send verification email", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }
    }

    /*
     *
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String description, String website, String profile_photo) {
        Log.d(TAG, "addNewUser: Adding user into database");
        //User(String user_id, String phone_number, String username, String email)
        User user = new User(userID, 1, StringManipulation.condenseUsername(username), email);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(
                description,
                username,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username),
                website);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);

    }

    public  UserSettings  getUserSettings(DataSnapshot dataSnapshot) {
        Log.d(TAG, "UserAccountSettings: Retriving user data settings from firebase ");
        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            //User Account Settings Node !!!
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                try {
                    Log.d(TAG, "UserAccountSettings: daatasnapshot: " + ds);
                    settings
                            .setDisplay_name(ds
                                    .child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                            );

                    settings
                            .setUsername(ds
                                    .child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                            );
                    settings
                            .setWebsite(ds
                                    .child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                            );
                    settings
                            .setDescription(ds
                                    .child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                            );
                    settings
                            .setProfile_photo(ds
                                    .child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                            );
                    settings
                            .setPosts(ds
                                    .child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                            );
                    settings
                            .setFollowing(ds
                                    .child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                            );
                    settings
                            .setFollowers(ds
                                    .child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                            );

                } catch (NullPointerException e) {
                    Log.d(TAG, "UserAccountSettings: inputs have failed ");
                }

            }
            else if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                try{
                    user
                            .setUsername(ds
                                    .child(userID)
                                    .getValue(User.class)
                                    .getUsername()
                            );
                    user
                            .setEmail(ds
                                    .child(userID)
                                    .getValue(User.class)
                                    .getEmail()
                            );
                    user
                            .setPhone_number(ds
                                    .child(userID)
                                    .getValue(User.class)
                                    .getPhone_number()
                            );
                    user
                            .setUser_id(ds
                                    .child(userID)
                                    .getValue(User.class)
                                    .getUser_id()
                            );
                }catch (NullPointerException e) {
                    Log.d(TAG, "UserAccountSettings: inputs have failed ");
                }

            }
        }
return new UserSettings(user,settings);
    }
}
