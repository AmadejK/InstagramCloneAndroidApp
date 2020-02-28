package com.example.ami300kl.instagramclone.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.ami300kl.instagramclone.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by Ami300kl on 16. 01. 2019.
 */

public class GridImageAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String mAppend;
    private ArrayList<String>imgURLs;
    public GridImageAdapter(Context context,int layoutResource,String mAppend, ArrayList<String> imgURLs) {

    super(context,layoutResource,imgURLs);
        this.mContext = mContext;
        this.mAppend = mAppend;
        this.imgURLs = imgURLs;
        this.layoutResource=layoutResource;
        mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
private static class ViewHolder
{
SquareImageView image;
ProgressBar mProgressbar;
}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
/*
* ViewHolder buildpattern(Similar to recycleView)
* */
        final ViewHolder holder;
        if(convertView==null)
        {
            convertView= mInflater.inflate(layoutResource,parent,false);
            holder=new ViewHolder();
            holder.mProgressbar=(ProgressBar)convertView.findViewById(R.id.gridImageProgressbar);
            holder.image=(SquareImageView)convertView.findViewById(R.id.gridImageView);

            convertView.setTag(holder);
        }
        else
            {
                holder=(ViewHolder) convertView.getTag();
            }

            String imgURL=getItem(position);
            ImageLoader imageLoader= ImageLoader.getInstance();
            imageLoader.displayImage(mAppend+imgURL,holder.image,new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                    if(holder.mProgressbar!=null)
                    {
                        holder.mProgressbar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if(holder.mProgressbar!=null)
                    {
                        holder.mProgressbar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if(holder.mProgressbar!=null)
                    {
                        holder.mProgressbar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    if(holder.mProgressbar!=null)
                    {
                        holder.mProgressbar.setVisibility(View.GONE);
                    }
                }
            });


        return convertView;
    }
}
