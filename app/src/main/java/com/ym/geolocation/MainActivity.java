package com.ym.geolocation;


import static net.daum.mf.map.api.MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.ym.geolocation.Upmoo.UpmooService;
import com.ym.geolocation.Util.ImageUtils;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener {

    Button btn1, btn2, btn3;
    private ActivityResultLauncher<Intent> resultLauncher;
    private static final MapPoint CUSTOM_MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.537229, 127.005515);
    double latitude = 0.0;
    double longitude = 0.0;
    public FirebaseAnalytics mFirebaseAnalytics;
    public FirebaseCrashlytics mFirebaseCrashlytics;
    private static final String[] LOCATION_PERMISSIONS_REQUESTED = new String[] {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };
    ImageUtils imageUtils ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseCrashlytics = FirebaseCrashlytics.getInstance();
        mFirebaseCrashlytics.log("MainActivity_onCreate()..");
        //#1. 레이아웃ID 설정
        setinitView();
        imageUtils = new ImageUtils(getApplicationContext());
        //#2. 업무앱 리스트
        UpmooService upmooService = new UpmooService(this, getApplicationContext());
        upmooService.init();
        //#3. 위치권한요청
        accessFineLocationPermissionContract.launch(LOCATION_PERMISSIONS_REQUESTED);
        //getPoint();
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == 0) {
                    Log.i("디버깅", "디버깅 확인");
                }
            }
        });
    }

    private void setinitView() {
        //#1. 공지사항 마기기능
        TextView tv = findViewById(R.id.tv_margee);
        tv.setSelected(true);

        //#2.이미지 버튼 테스트
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        //GalleryControler galleryControler = new GalleryControler(this ,btn1, btn2, btn3);
        //#1. 첫번째 방법
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callGalleryIntent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R
                        && android.os.ext.SdkExtensions.getExtensionVersion(android.os.Build.VERSION_CODES.R) >= 2) {
                    callGalleryIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                    callGalleryIntent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, 10);
                } else {
                    callGalleryIntent = new Intent(Intent.ACTION_PICK);
                    callGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    callGalleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    callGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                }
                //startActivityForResult(callGalleryIntent, 1000);
                getImageFileToGalleryResultLauncher.launch(callGalleryIntent);
            }
        });
        //#2. 두번쨰 방법 - startActivityForResult deprecated
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //subwebView = findViewById(R.id.subwebview);

                //subwebView.loadUrl("file:///android_asset/www/04_test.html");
                //subwebView.loadUrl("https://m.naver.com");
                Intent i = new Intent();
                i.setClass(MainActivity.this, PopWebViewActivity.class);
                startActivity(i);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //throw new RuntimeException("Test Crash");
                Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
                sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Avocado/glTF/Avocado.gltf"));
                sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox");
                startActivity(sceneViewerIntent);
            }
        });
    }

    ActivityResultLauncher<Intent> getImageFileToGalleryResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        if (data != null) {
                            ClipData clipData = data.getClipData();
                            if (clipData != null) {
                                imageUtils.handleClipData(clipData);
                            } else {
                                Uri uriPath = data.getData();
                                imageUtils.handleSingleUri(uriPath);
                            }
                        }
                        //갤럭시노트10S ==> dat=content://com.android.providers.media.documents/document/image:51480 flg=0x1
                        Log.e("YYYM", "onActivityResult: " + data);
                    }
                }
            });

    //old
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("YYYM", "OLD_onActivityResult: " + resultCode +" , "+ requestCode +" , "+ data);
        switch (requestCode)
        {
            case 1000:
                Log.e("YYYM", "onActivityResult: " +data.getData());
                if (data.getData().equals("") || data.getData() == null)
                    return;
                else
                {Uri uri = data.getData();}
                break;
        }
    }

    /* 카카오앱 상속함수들*/
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getPoint() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // 위치 정보가 변경되었을 때 실행되는 코드
                //Log.e("YYYM", "onLocationChanged: " +location.getLatitude() + ", " + location.getLongitude());
/*                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.e("YYYM", "latitude: " +latitude+ ", longitude:"+longitude);
                    MapViewLoad();
                }*/
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e("YYYM", "status: " + status);
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            accessFineLocationPermissionContract.launch(LOCATION_PERMISSIONS_REQUESTED); //권한요청
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location location =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.e("YYYM", "latitude: " + latitude + ", longitude:" + longitude);
                MapViewLoad();
            }
        }

    }

    //퍼미션 하나 요청할떄
/*        ActivityResultLauncher<String> accessFineLocationPermissionContract
    = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isPermissionAccepted -> {
            if (isPermissionAccepted) {
                Log.e("YYYM", "permission__: " + isPermissionAccepted);
                getPoint();
            } else {
                Log.e("YYYM", "permission_: "+isPermissionAccepted);
            }
        });*/
    //다중퍼미션 요청할떄
    ActivityResultLauncher<String[]> accessFineLocationPermissionContract
            = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isPermissionAccepted -> {
            boolean allPermissionsGranted = true;
                Log.e("YYYM", "permission__: " + isPermissionAccepted);
            for (Map.Entry<String, Boolean> entry : isPermissionAccepted.entrySet()) {
                String permission = entry.getKey();
                Boolean isGranted = entry.getValue();
                Log.e("YYYM", "permission: " + permission +", isGranted:"+isGranted);
                if (!isGranted) { allPermissionsGranted = false; }
            }

            if (allPermissionsGranted) {getPoint();}
            else {
                Log.e("YYYM", "permission_FAIL");
                //필수권한일떄 다시 요청
            }
    });

    public void MapViewLoad() {
        Log.e("YYYM", "MapViewLoad: ");
        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(TrackingModeOnWithoutHeading);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(CUSTOM_MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

// 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
// 줌 레벨 변경
        mapView.setZoomLevel(9, true);
// 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 2, true);
// 줌 인
        mapView.zoomIn(true);
// 줌 아웃
        mapView.zoomOut(true);

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        Log.e("YYYM", "onCurrentLocationUpdate: ");
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
        Log.e("YYYM", "onCurrentLocationDeviceHeadingUpdate: ");
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.e("YYYM", "onCurrentLocationUpdateFailed: ");
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.e("YYYM", "onCurrentLocationUpdateCancelled: ");
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Log.e("YYYM", "onMapViewInitialized: ");
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        Log.e("YYYM", "onMapViewCenterPointMoved: ");
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        Log.e("YYYM", "onMapViewZoomLevelChanged: ");
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        Log.e("YYYM", "onMapViewSingleTapped: ");
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        Log.e("YYYM", "onMapViewDoubleTapped: ");
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        Log.e("YYYM", "onMapViewLongPressed: ");
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        Log.e("YYYM", "onMapViewDragStarted: ");
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Log.e("YYYM", "onMapViewDragEnded: ");
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        //Log.e("YYYM", "onMapViewMoveFinished: ");
    }
}