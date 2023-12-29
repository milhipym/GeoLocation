package com.ym.geolocation.Upmoo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ym.geolocation.R;

import java.util.List;

public class CardUpmooAdapter extends RecyclerView.Adapter<CardUpmooAdapter.CardViewHolder> {

    private List<CardUpmooItem> cardItemList;

    public CardUpmooAdapter(List<CardUpmooItem> cardItemList) {
        this.cardItemList = cardItemList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardUpmooItem cardItem = cardItemList.get(position);
        holder.imgUpmooIcon.setImageResource(cardItem.getImgUpmooIcon());
        holder.txtUpmooTitle.setText(cardItem.getTxtUpmooTitle());
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUpmooIcon;
        TextView txtUpmooTitle;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUpmooIcon = itemView.findViewById(R.id.img_upmoo_icon);
            txtUpmooTitle = itemView.findViewById(R.id.txt_upmoo_title);
        }
    }
}