package com.example.ami300kl.instagramclone.Login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ami300kl.instagramclone.Module.User;
import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Utils.FirebaseMethods;
import com.example.ami300kl.instagramclone.Utils.StringManipulation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by Ami300kl on 17. 01. 2019.
 */

public class RegisterActivity extends AppCompatActivity{
    private static final String TAG = "RegisterActivity";
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext;
    private String email,username,password;
    private EditText mEmail,mUsername,mPassword;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private ProgressBar mProgressBar;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference myRef;
    private String append="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started");
        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);
        initWidgets();
        setupFirebaseAuth();
        init();
    }
    private  void init()
    {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=mEmail.getText().toString();
                username=mUsername.getText().toString();
                password=mPassword.getText().toString();

                loadingPleaseWait.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                if(checkInputs(username,email,password))
                {
                    firebaseMethods.registerNewEmail(email,password,username);
                    loadingPleaseWait.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                }
                else
                    loadingPleaseWait.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private boolean checkInputs(String username,String email,String password)
    {
        Log.d(TAG, "checkInputs: Method Called");

        //if all fields are full
     if(!username.isEmpty()&&!email.isEmpty()&&!password.isEmpty())
     {
         Log.d(TAG, "checkInputs:  inputs are full");
         return true;
     }
     else
         Log.d(TAG, "checkInputs:  inputs are Empty");
        Toast.makeText(mContext,"all fields must be fufilled",Toast.LENGTH_LONG);
        return false;
    }
/*
     Initilizing Activity Widgets
 */
    private void initWidgets()
    {
        Log.d(TAG, "initWidgets: Initiating Widgets");
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        btnRegister=(Button)findViewById(R.id.btn_register);
        loadingPleaseWait=(TextView)findViewById(R.id.txtPleaseWait);
        mEmail=(EditText)findViewById(R.id.input_email);
        mPassword=(EditText)findViewById(R.id.input_password);
        mUsername=(EditText)findViewById(R.id.input_username);
        mContext= RegisterActivity.this;

        loadingPleaseWait.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

    }



    private boolean isStringNull(String string)
    {
        Log.d(TAG, "isStringNull: Checking if fields are empty");
        if(string.equals(""))
        {
            return true;
        }
        else
        {
            return  false;
        }
    }


    /*--------------------------------------------------------------*/
    //START OF FIREBASE
    /*--------------------------------------------------------------*/
    private void checkifUsernameExists(final String username) {
        Log.d(TAG, "checkifUsernameExists: Checking if" + username + "Alredy Exists");

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        Query query=reference.child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot:dataSnapshot.getChildren())
                {
                    if(singleSnapshot.exists())
                    {
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH "+singleSnapshot.getValue(User.class));
                        append=myRef.push().getKey().substring(3,10);
                        Log.d(TAG, "onDataChange: username alredy exists,appending random string to name"+append);
                    }
                }
                String mUsername;
                mUsername = username + append;

                    //add new user account settings to the database
                    firebaseMethods.addNewUser(email,mUsername,"","","");
                    Toast.makeText(mContext,"Signup succesfull : sending Verification Email",Toast.LENGTH_SHORT).show();
                    mAuth.signOut();//Log out

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setupFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mfirebaseDatabase.getReference();

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                Log.d(TAG, "onAuthStateChanged: setting up authorization check");
                final FirebaseUser user =firebaseAuth.getCurrentUser();
                // Calling method to check user @param

                if(user!=null)
                {//user logged inn
                    Log.d(TAG, "onAuthStateChanged: signed_in"+user.getUid());


                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //make sure the username isnt alredy taken
                            checkifUsernameExists(username);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    finish();

                } else
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
