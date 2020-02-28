package com.example.ami300kl.instagramclone.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ami300kl on 27. 02. 2019.
 */

public class FileSearch {
    public static ArrayList<String> getDirectoryPaths(String directory)
    {
        ArrayList<String>pathArray=new ArrayList<>();
        File file=new File(directory);
        File[]listfiles =file.listFiles();
        for(int i =0;i<listfiles.length;i++)
        {
            if(listfiles[i].isDirectory())
            {
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }

        return  pathArray;
    }
    public static ArrayList<String> getFilePaths(String directory)
    {
        ArrayList<String>pathArray=new ArrayList<>();
        File file=new File(directory);
        File[]listfiles =file.listFiles();
        for(int i =0;i<listfiles.length;i++)
        {
            if(listfiles[i].isFile())
            {
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
}
