package com.example.ami300kl.instagramclone.Utils;

/**
 * Created by Ami300kl on 18. 01. 2019.
 */

public class StringManipulation {
    public  static String expandUsername(String username)
    {
        return username.replace("."," ");
    }
    public static  String condenseUsername(String username)
    {
        return username.replace(" ",".");
    }
    public static String getTags(String string){
        if(string.indexOf("#") > 0) {
            StringBuilder sb= new StringBuilder();
            char[]charArray=string.toCharArray();
            boolean foundWord=false;
            for(char c : charArray)
            {
                if(c=='#')
                {
                    foundWord=true;
                    sb.append(c);
                }else {
                        if(foundWord=true)
                        {
                            sb.append(c);
                        }
                    }
                if( c == ' '){
                    foundWord=false;
                }
            }
            String s =sb.toString().replace(" ","").replace("#",",#");
            return s.substring(1,s.length());
        }
        return string;
    }

}
