package com.example.ami300kl.instagramclone.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Ami300kl on 28. 02. 2019.
 */

public class ImageManager {
    private static final String TAG = "ImageManager";

    public static Bitmap getBitmap(String imgURL)
    {
        File imageFile=new File(imgURL);
        FileInputStream fis=null;
        Bitmap bitmap=null;
        try {
            fis=new FileInputStream(imageFile);
            bitmap= BitmapFactory.decodeStream(fis);
        }
        catch(FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException: "+e.getMessage());
        }finally {
            try {
                fis.close();}
            catch(IOException e ) {
                Log.d(TAG, "IOException: "+e.getMessage());
            }
        }
return bitmap;
    }
    public static byte[] getBytesFromBitmap(Bitmap bm,int quality)
    {
        ByteArrayOutputStream stream= new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,quality,stream);
        return stream.toByteArray();
    }
}
