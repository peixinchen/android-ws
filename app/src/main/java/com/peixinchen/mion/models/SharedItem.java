package com.peixinchen.mion.models;


import java.util.ArrayList;
import java.util.List;

public final class SharedItem {
    public String tagline;
    public List<String> imageUrlList = new ArrayList<>();

    public String toString() {
        return String.format("SharedItem{tagline='%s', imageUrlList=%s}", tagline, imageUrlList);
    }
}
