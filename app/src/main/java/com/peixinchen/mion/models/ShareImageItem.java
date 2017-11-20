package com.peixinchen.mion.models;

public final class ShareImageItem {
    public boolean isChecked;
    public String imageUrl;

    public ShareImageItem(boolean isChecked, String imageUrl) {
        this.isChecked = isChecked;
        this.imageUrl = imageUrl;
    }

    public String toString() {
        return String.format("ShareImageItem{isChecked=%s, imageUrl='%s'}", isChecked, imageUrl);
    }
}
