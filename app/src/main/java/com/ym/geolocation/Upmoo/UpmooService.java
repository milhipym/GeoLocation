package com.ym.geolocation.Upmoo;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ym.geolocation.R;

import java.util.ArrayList;
import java.util.List;

public class UpmooService {

    private Activity mActivity;
    private Context mContext;
    private RecyclerView recyclerView;
    private CardUpmooAdapter cardAdapter;
    private List<CardUpmooItem> cardItemList;
    public UpmooService(Activity activity, Context applicationContext) {
        mActivity = activity;
        mContext = applicationContext;
    }

    public void init() {
        recyclerView = mActivity.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));

        cardItemList = new ArrayList<>();
        cardItemList.add(new CardUpmooItem(R.drawable.portal_icon, "MobilePortal"));
        cardItemList.add(new CardUpmooItem(R.drawable.portal_icon, "DBins"));
        cardItemList.add(new CardUpmooItem(R.drawable.portal_icon, "사고조사"));
        cardItemList.add(new CardUpmooItem(R.drawable.portal_icon, "스마트총무지원"));
        cardItemList.add(new CardUpmooItem(R.drawable.portal_icon, "PromyTalk"));
        cardItemList.add(new CardUpmooItem(R.drawable.portal_icon, "모바일CSI"));

        cardAdapter = new CardUpmooAdapter(cardItemList);
        recyclerView.setAdapter(cardAdapter);
    }

}
