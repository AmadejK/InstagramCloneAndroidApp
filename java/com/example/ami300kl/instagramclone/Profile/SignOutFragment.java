package com.example.ami300kl.instagramclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ami300kl.instagramclone.Login.LoginActivity;
import com.example.ami300kl.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Ami300kl on 29. 12. 2018.
 */

public class SignOutFragment extends Fragment {

    private static final String TAG = "SignOutFragment";
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext;
    private ProgressBar mProgresBar;
    private TextView tvSignOut,tvsigninng_out;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_signout,container,false);
        Log.d(TAG, "onCreateView: Succesfull called onCreate");
        tvSignOut=(TextView)view.findViewById(R.id.txConfirmSignOut);
        mProgresBar=(ProgressBar) view.findViewById(R.id.progressBar);
        Button btnConfirmSignOut=(Button)view.findViewById(R.id.btnConfirmSignOut);
        tvsigninng_out=(TextView)view.findViewById(R.id.tvsigninng_out);

        mProgresBar.setVisibility(View.GONE);
        tvsigninng_out.setVisibility(View.GONE);

        setupFirebaseAuth();

        btnConfirmSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                mProgresBar.setVisibility(View.VISIBLE);
                tvsigninng_out.setVisibility(View.VISIBLE);

                mAuth.signOut();
                getActivity().finish();

            }
        });
        return view;
    }





    //Setup the firebase  auth object
/*if user is Logged Inn than go to home Activity and Call finish/();*/

    private void setupFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                Log.d(TAG, "onAuthStateChanged: setting up authorization check");
                FirebaseUser user =firebaseAuth.getCurrentUser();
                // Calling method to check user @param

                if(user!=null)
                {//user logged inn
                    Log.d(TAG, "onAuthStateChanged: signed_in"+user.getUid());
                }
                else
                {
                    //user signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                    Log.d(TAG, "onAuthStateChanged: navigating back to Login Activity");
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    /*--------------------------------------------------------------*/
    //END OF FIREBASE
    /*--------------------------------------------------------------*/


}
