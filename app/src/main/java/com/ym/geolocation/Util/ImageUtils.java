package com.ym.geolocation.Util;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ImageUtils {
    private Context mContext;
    public ImageUtils(Context applicationContext) {
        mContext = applicationContext;
    }
/*
* 갤러리----------------------------------------------------------
* */
    public void handleClipData(ClipData clipData) {
        int size = clipData.getItemCount();
        for (int i = 0; i < size; i++) {
            Uri imgUri = clipData.getItemAt(i).getUri();
            handleUri(imgUri);
        }
    }

    public void handleSingleUri(Uri uriPath) { handleUri(uriPath); }

    private void handleUri(Uri imgUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getApplicationContext().getContentResolver(), imgUri);
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, true);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bo);

            String fileName = String.valueOf(System.currentTimeMillis());
            File tempFile = File.createTempFile(fileName, ".jpg", mContext.getExternalFilesDir(Environment.DIRECTORY_DCIM));

            FileOutputStream fo = new FileOutputStream(tempFile);
            fo.write(bo.toByteArray());
            fo.close();

            // 파일 업로드 후 tempFile 삭제
            if (tempFile.delete()) {
                Log.e("YYYM", "임시 파일 삭제 성공");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 쿠쿠라이브러리 갤러리----------------------------------------------------------
     *
     */

    private void kukuImage(Uri imgUri){

        Log.e("YYYM", "kukuImage: " + imgUri);
        //content://media/picker/0/com.android.providers.media.photopicker/media/1000002871
        JSONObject params = new JSONObject();
        try {
            params.put("data", imgUri);
            params.put("imageDataType", "urlImage"); // base64Image , urlImage
            params.put("format", "JPG"); // png , jpg
            params.put("quality", 75);
            params.put("resizeType", "maxPixelResize");
            params.put("width", 3000);
            params.put("height", 3000);

            KukuImageResize kukuImage = new KukuImageResize();
            JSONObject result = kukuImage.imageResize(params);

            Log.e("YYYM", "kukuImage: " + result.getString("imageData"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
