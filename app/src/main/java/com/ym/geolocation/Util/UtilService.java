package com.ym.geolocation.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilService {




    public String getHashKey(Activity mActivity, Context mContext) {
        /* 키 해시 얻기*/
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                if (packageInfo == null
                        || packageInfo.signingInfo == null) {
                    return null;
                }
                if(packageInfo.signingInfo.hasMultipleSigners()){
                    return ""; //signatureDigest(packageInfo.signingInfo.getApkContentsSigners());
                }
                else{
                    return "";//signatureDigest(packageInfo.signingInfo.getSigningCertificateHistory());
                }
            }
            else {
                @SuppressLint("PackageManagerGetSignatures")
                PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_SIGNATURES);
                if (packageInfo == null
                        || packageInfo.signatures == null
                        || packageInfo.signatures.length == 0
                        || packageInfo.signatures[0] == null) {
                    return null;
                }
                return ""; //signatureDigest(packageInfo.signatures);
            }
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
