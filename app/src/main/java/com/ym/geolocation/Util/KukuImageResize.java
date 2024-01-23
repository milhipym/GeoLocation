package com.ym.geolocation.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



public class KukuImageResize {

    public static final String IMAGE_DATA_TYPE_BASE64 = "base64Image";
    public static final String IMAGE_DATA_TYPE_URL = "urlImage";
    public static final String RESIZE_TYPE_FACTOR = "factorResize";
    public static final String RESIZE_TYPE_MIN_PIXEL = "minPixelResize";
    public static final String RESIZE_TYPE_MAX_PIXEL = "maxPixelResize";
    public static final String RETURN_BASE64 = "returnBase64";
    public static final String RETURN_URI = "returnUri";
    public static final String FORMAT_JPG = "jpg";
    public static final String FORMAT_PNG = "png";
    public static final String DEFAULT_FORMAT = "jpg";
    public static final String DEFAULT_IMAGE_DATA_TYPE = IMAGE_DATA_TYPE_BASE64;
    public static final String DEFAULT_RESIZE_TYPE = RESIZE_TYPE_FACTOR;

    public JSONObject imageResize(JSONObject params) {
        try{
            String imageData = params.getString("data");
            String imageDataType = DEFAULT_IMAGE_DATA_TYPE;
            if (params.has("imageDataType")) {
                imageDataType = params.getString("imageDataType");
            }
            String format = DEFAULT_FORMAT;
            if (params.has("format")) {
                format = params.getString("format");
            }

            //--------------------------------------------------------------------------------------
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            float[] sizes = calculateFactors(params, options.outWidth, options.outHeight);
            float reqWidth = options.outWidth * sizes[0];
            float reqHeight = options.outHeight * sizes[1];
            int inSampleSize = calculateInSampleSize(options, (int)reqWidth, (int)reqHeight);

            options = new BitmapFactory.Options();
            options.inSampleSize = inSampleSize;
            Bitmap bmp = getBitmap(imageData, imageDataType, options);


            sizes = calculateFactors(params, options.outWidth, options.outHeight);
            bmp = getResizedBitmap(bmp, sizes[0], sizes[1]);


            int quality = params.getInt("quality");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (format.equals(FORMAT_PNG)) {
                bmp.compress(Bitmap.CompressFormat.PNG, quality, baos);
            } else {
                bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            }
            byte[] b = baos.toByteArray();
            String returnString = Base64.encodeToString(b, Base64.NO_WRAP);
            // return object

            Log.d("PLUGIN -###------------", returnString);

            JSONObject res = new JSONObject();
            res.put("imageData", returnString);
            res.put("width", bmp.getWidth());
            res.put("height", bmp.getHeight());

            return res;

        } catch (JSONException e) {
            Log.d("ERROR", e.getMessage());
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());

        } catch (URISyntaxException e) {
            Log.d("ERROR", e.getMessage());

        }

        return null;
    }
    public Bitmap getBitmap(String imageData, String imageDataType, BitmapFactory.Options options) throws IOException, URISyntaxException {
        Bitmap bmp;
        if (imageDataType.equals(IMAGE_DATA_TYPE_BASE64)) {
            String imageData2 =  imageData.substring(imageData.indexOf(",")  + 1);
            byte[] blob = Base64.decode(imageData2, Base64.DEFAULT);
            bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
        } else {
            URI uri = new URI(imageData);
            File imageFile = new File(uri);
            bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        }
        return bmp;
    }


    private Bitmap getResizedBitmap(Bitmap bm, float widthFactor, float heightFactor) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(widthFactor, heightFactor);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private float[] calculateFactors(JSONObject params, int width, int height) throws JSONException {
        float widthFactor;
        float heightFactor;
        String resizeType = params.getString("resizeType");
        float desiredWidth = (float)params.getDouble("width");
        float desiredHeight = (float)params.getDouble("height");

        if (resizeType.equals(RESIZE_TYPE_MIN_PIXEL)) {
            widthFactor = desiredWidth / (float)width;
            heightFactor = desiredHeight / (float)height;
            if (widthFactor > heightFactor && widthFactor <= 1.0) {
                heightFactor = widthFactor;
            } else if (heightFactor <= 1.0) {
                widthFactor = heightFactor;
            } else {
                widthFactor = 1.0f;
                heightFactor = 1.0f;
            }
        } else if (resizeType.equals(RESIZE_TYPE_MAX_PIXEL)) {
            widthFactor = desiredWidth / (float)width;
            heightFactor = desiredHeight / (float)height;
            if (widthFactor == 0.0) {
                widthFactor = heightFactor;
            } else if (heightFactor == 0.0) {
                heightFactor = widthFactor;
            } else if (widthFactor > heightFactor) {
                widthFactor = heightFactor; // scale to fit height
            } else {
                heightFactor = widthFactor; // scale to fit width
            }
        } else {
            widthFactor = desiredWidth;
            heightFactor = desiredHeight;
        }

        float[] sizes = {widthFactor, heightFactor};
        return sizes;
    }

}
