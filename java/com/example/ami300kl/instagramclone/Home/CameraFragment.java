package com.example.ami300kl.instagramclone.Home;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ami300kl.instagramclone.R;

/**
 * Created by Ami300kl on 26. 11. 2018.
 */

public class CameraFragment extends Fragment {
private final String TAG="Home Fragment";

    @Nullable
@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view =inflater.inflate(R.layout.fragment_camera,container,false);
    return view;
    }
}
