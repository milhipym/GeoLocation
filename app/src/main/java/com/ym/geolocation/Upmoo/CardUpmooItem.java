package com.ym.geolocation.Upmoo;

public class CardUpmooItem
{
        private int img_upmoo_icon;
        private String txt_upmoo_title;

        public CardUpmooItem(int img_upmoo_icon, String txt_upmoo_title) {
            this.img_upmoo_icon = img_upmoo_icon;
            this.txt_upmoo_title = txt_upmoo_title;
        }

        public int getImgUpmooIcon() {
            return img_upmoo_icon;
        }

        public String getTxtUpmooTitle() {
            return txt_upmoo_title;
        }
}
