package com.kavin.newsfeed.General;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kavin.newsfeed.Model.User;

public class General {
    public static User currentUser;

    public static final String DELETE = "Delete";
    public static final String UPDATE = "Update";
    public static final String USER_KEY = "User";
    public static final String PASS_KEY = "Password";

    public static final int PICK_IMAGE_REQUEST = 71;



   

    public static  boolean isConnectedtoInternet(Context context)
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo[] info= connectivityManager.getAllNetworkInfo();
            if (info != null)
            {
                for (int i=0;i<info.length;i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
