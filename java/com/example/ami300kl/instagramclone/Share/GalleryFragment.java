package com.example.ami300kl.instagramclone.Share;

import android.app.Fragment;
import android.content.Intent;
import android.drm.DrmInfoRequest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ami300kl.instagramclone.Profile.AccountSettingsActivity;
import com.example.ami300kl.instagramclone.R;
import com.example.ami300kl.instagramclone.Utils.FilePaths;
import com.example.ami300kl.instagramclone.Utils.FileSearch;
import com.example.ami300kl.instagramclone.Utils.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ami300kl on 20. 02. 2019.
 */

public class GalleryFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "GalleryFragment";

    private static final int NUM_GRID_COLUMNS=3;
    //widgets
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    //var
    private ArrayList<String> directories;
    private static final String mAppend="file:/";
    private String mSelectedImage="" ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_gallery,container,false);
        galleryImage=(ImageView)view.findViewById(R.id.galleryImageView);
        gridView=(GridView)view.findViewById(R.id.gridView);
        directorySpinner=(Spinner)view.findViewById(R.id.spinner);
        mProgressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(view.GONE);
        directories=new ArrayList<>();
        Log.d(TAG, "onCreateView: started.");

        ImageView shareclose=(ImageView)view.findViewById(R.id.ivCloseShare);
        shareclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clossing the gallery tab");
                getActivity().finish();
            }
        });
        TextView nextScreen=(TextView)view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to final screen");
        if(isRootTask())
        {
            Intent intent = new Intent(getActivity(),NextActivity.class);
            intent.putExtra(getString(R.string.selected_image),mSelectedImage);
            startActivity(intent);

        }else
            {
                Intent intent = new Intent(getActivity(),AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.selected_image),mSelectedImage);
                intent.putExtra(getString(R.string.return_to_fragment),getString(R.string.edit_profile_fragment));
                startActivity(intent);
                getActivity().finish();
            }


            }
        });
        init();

        return view;
    }

    private boolean isRootTask() {
        if(((ShareActivity)getActivity()).getTask()==0)
        {
            return true;
        }else{
            return false;
        }

    }
    private void init()
    {
        FilePaths filePaths=new FilePaths();

        if(FileSearch.getDirectoryPaths(filePaths.PICTURES)!=null)
        {
            directories=FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }

        if(FileSearch.getDirectoryPaths(filePaths.CAMERA)!=null)
        {
            directories=FileSearch.getDirectoryPaths(filePaths.CAMERA);
        }



        ArrayList<String>directoryNames = new ArrayList<>();
        for(int i=0;i<directories.size();i++)
        {
             int index = directories.get(i).lastIndexOf("/");
             String string = directories.get(i).substring(index);
             directoryNames.add(string);
        }

        directories.add(filePaths.CAMERA);
        directories.add(filePaths.PICTURES);



        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,directories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d(TAG, "Selected: "+directories.get(position));
                //setup oure image grid for the directories chosen
                setupGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setupGridView(String selectedDirectory)
    {
        Log.d(TAG, "setupGridView: ");
        final ArrayList<String>imgURLs=FileSearch.getFilePaths(selectedDirectory);
        int gridWidth=getResources().getDisplayMetrics().widthPixels;
        int imageWidth =gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        //use the grid image adapt the images

        GridImageAdapter adapter=new GridImageAdapter(getActivity(),R.layout.layout_grid_imageview,mAppend,imgURLs);
        gridView.setAdapter(adapter);
        //SET MAIN image
        setImage(imgURLs.get(0),galleryImage,mAppend);
        mSelectedImage =imgURLs.get(0);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                setImage(imgURLs.get(position),galleryImage,mAppend);
                mSelectedImage=imgURLs.get(position);

            }
        });

    }
    private void setImage(String imgURL,ImageView image,String append)
    {
        Log.d(TAG, "setImage: setting image");
        ImageLoader imageLoader=ImageLoader.getInstance();
        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(view.VISIBLE);

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(view.INVISIBLE);

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(view.INVISIBLE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(view.INVISIBLE);

            }
        });
    }
}
