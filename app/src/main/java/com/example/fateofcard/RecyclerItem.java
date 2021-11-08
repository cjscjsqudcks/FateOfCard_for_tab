package com.example.fateofcard;

import android.graphics.drawable.Drawable;

public class RecyclerItem {
    private Drawable iconD, selected;
    private String cardno;

    public void setSelected(Drawable select) {
        selected = select;
    }

    public void setIcon(Drawable icon) {
        iconD = icon;
    }

    public void setCard(String cardn) {
        cardno = cardn;
    }

    public Drawable getSelected() {
        return this.selected;
    }

    public Drawable getIconD() {
        return this.iconD;
    }

    public String getCardno() {
        return this.cardno;
    }
}
