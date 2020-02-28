package com.example.ami300kl.instagramclone.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toolbar;

import com.example.ami300kl.instagramclone.Home.HomeActivity;
import com.example.ami300kl.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/*
 * Created by Ami300kl on 17. 01. 2019.
 */
public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "LoginActivity";
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext;
    private ProgressBar mProgresBar;
    private EditText mEmail,mPassword;
    private TextView mPleaseWait;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgresBar=(ProgressBar)findViewById(R.id.progressBar);
        mPleaseWait=(TextView)findViewById(R.id.pleaseWait);
        mEmail=(EditText)findViewById(R.id.input_email);
        mPassword=(EditText)findViewById(R.id.input_password);
        mContext=LoginActivity.this;

        mProgresBar.setVisibility(View.GONE);
        mPleaseWait.setVisibility(View.GONE);

        Log.d(TAG, "onCreate: Created");


        setupFirebaseAuth();
        init();
    }

    /*--------------------------------------------------------------*/
    //Setup the firebase  auth object
    /*--------------------------------------------------------------*/


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
    //initiate Login Button btnLogin

    private void init()
    {
        Button btnLogin=(Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d(TAG, "onClick: attempting to log in");
              //  mProgresBar.setVisibility(View.VISIBLE);
              //  mPleaseWait.setVisibility(View.VISIBLE);
           String email=mEmail.getText().toString();
           String password=mPassword.getText().toString();


           //false means NOT NULL
           if(!isStringNull(email)&&!isStringNull(password))
           {
               mProgresBar.setVisibility(View.VISIBLE);
               mPleaseWait.setVisibility(View.VISIBLE);

               mAuth.signInWithEmailAndPassword(email, password)
                       // Can not use mContext Linked things dont work here !!!!
                       .addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               Log.d(TAG, "onComplete: OnComplete ");
                               FirebaseUser user=mAuth.getCurrentUser();



                               if (!task.isSuccessful()) {
                                   // Sign in success, update UI with the signed-in user's information
                                   Log.d(TAG, "signInWithEmail:failed",task.getException());

                                   //FirebaseUser user = mAuth.getCurrentUser();
                                   Toast.makeText(mContext, R.string.auth_failed,Toast.LENGTH_SHORT).show();
                                   mProgresBar.setVisibility(View.GONE);
                                   mPleaseWait.setVisibility(View.GONE);

                               }
                               else {
                                   // If sign in fails, display a message to the user.
                                   Log.d(TAG, "signInWithEmail : succesfull Login ");

                                   try{
                                       if(user.isEmailVerified())
                                       {
                                           Log.d(TAG, "isUserVerified: User is Verified ");
                                           Intent intent = new Intent(mContext,HomeActivity.class);
                                           startActivity(intent);

                                       }else
                                       {
                                           Toast.makeText(mContext,"email isnt verified \n check youre email inbox",Toast.LENGTH_LONG);
                                           mProgresBar.setVisibility(view.GONE);
                                           mPleaseWait.setVisibility(View.GONE);
                                           mAuth.signOut();
                                       }
                                   }
                                   catch(NullPointerException e){
                                       Log.e(TAG, "onComplete:Null Pointer exception"+ e.getMessage());
                                   }


                               }

                               // ...
                           }
                       });
           }
           //if checked string is null
           else
               {
                   Toast.makeText(mContext, "Please insert username and password", Toast.LENGTH_SHORT).show();
               }

            }
        });

        TextView linkSignup=(TextView)findViewById(R.id.txtlinkSignup);
        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        /*if User is logged inn thannavigate to HomeActivity*/
        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
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
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // Calling method to check user @param

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
