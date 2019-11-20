package com.example.favoritesiteapp;

public class UrlSites {

    public long mId;
    public String mUrl;
    public long mDate;

    public UrlSites(long id, String name, long date) {
        mId = id;
        mUrl = name;
        mDate = date;
    }

    public UrlSites(UrlSites urlSites) {
        mId = urlSites.mId;
        mUrl = urlSites.mUrl;
        mDate = urlSites.mDate;
    }

}