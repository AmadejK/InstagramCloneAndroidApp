package com.example.ami300kl.instagramclone.Utils;

import android.os.Environment;

/**
 * Created by Ami300kl on 27. 02. 2019.
 */

public class FilePaths {
    //"storage/emulated/0"
    public String ROOT_DIR= Environment.getExternalStorageDirectory().getPath();
    public String PICTURES=ROOT_DIR+"/Pictures";
    public String CAMERA=ROOT_DIR+"/DCIM/camera";
    public String FIREBASE_IMAGE_STORAGE="photos/users/";
}
