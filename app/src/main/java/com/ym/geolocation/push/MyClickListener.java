package com.ym.geolocation.push;

import android.util.Log;

import com.google.firebase.inappmessaging.FirebaseInAppMessagingClickListener;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.CampaignMetadata;
import com.google.firebase.inappmessaging.model.InAppMessage;

public class MyClickListener implements FirebaseInAppMessagingClickListener {

    @Override
    public void messageClicked(InAppMessage inAppMessage, Action action) {
        // Determine which URL the user clicked
        String url = action.getActionUrl();
        Log.e("YYYM", "messageClicked: " + url );

        // Get general information about the campaign
        CampaignMetadata metadata = inAppMessage.getCampaignMetadata();
        assert metadata != null;
        Log.e("YYYM", "messageClicked: " + metadata.getCampaignId() );
        // ...
    }

}
