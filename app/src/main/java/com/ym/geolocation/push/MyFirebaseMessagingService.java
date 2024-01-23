package com.ym.geolocation.push;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // 메시지 수신 처리
        if (remoteMessage.getNotification() != null) {
            Log.d("YYYM", "Message Notification Body: " + remoteMessage.getNotification().getBody());

            // 알림을 표시하거나, 특정 동작을 수행하는 코드를 여기에 추가
        }

        // 데이터 메시지의 경우
        if (remoteMessage.getData().size() > 0) {
            Log.d("YYYM", "Message data payload: " + remoteMessage.getData());

            // 데이터 메시지 처리 코드를 여기에 추가
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("YYYM", "onNewToken: " + token);
    }


}
