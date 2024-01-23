package com.ym.geolocation.wakelock;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

import com.ym.geolocation.MainActivity;

public class WakeLock {

    Context mContext;
    private MainActivity mActivity;


    public WakeLock(Context applicationContext, MainActivity activity) {
        mContext = applicationContext;
        mActivity = activity;
    }

    public void requestKeepScreen(){
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void requestOffScreen(){
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
