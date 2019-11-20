package com.example.favoritesiteapp;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UrlViewModel extends AndroidViewModel {

    private FavouritesDBHelper mFavHelper;
    private ArrayList<UrlSites> mFavs;

    public UrlViewModel(Application application) {
        super(application);
        mFavHelper = new FavouritesDBHelper(application);
    }

    public List<UrlSites> getFavs() {
        if (mFavs == null) {
            mFavs = new ArrayList<>();

            addManualList();
            loadFavs();
        }
        ArrayList<UrlSites> clonedFavs = new ArrayList<>(mFavs.size());
        for (int i = 0; i < mFavs.size(); i++) {
            clonedFavs.add(new UrlSites(mFavs.get(i)));
        }
        return clonedFavs;
    }

    public void addManualList() {

        addFav("https://www.google.com", (new Date()).getTime());

    }

    public void loadFavs() {

        mFavs.clear();

        SQLiteDatabase db = mFavHelper.getReadableDatabase();
        Cursor cursor = db.query(DbSettings.DBEntry.TABLE,
                new String[]{
                        DbSettings.DBEntry._ID,
                        DbSettings.DBEntry.COL_FAV_URL,
                        DbSettings.DBEntry.COL_FAV_DATE
                },
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idxId = cursor.getColumnIndex(DbSettings.DBEntry._ID);
            int idxUrl = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_URL);
            int idxDate = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_DATE);
            mFavs.add(new UrlSites(cursor.getLong(idxId), cursor.getString(idxUrl), cursor.getLong(idxDate)));
        }
        cursor.close();
        db.close();
    }

    public UrlSites addFav(String url, long date) {

        SQLiteDatabase db = mFavHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbSettings.DBEntry.COL_FAV_URL, url);
        values.put(DbSettings.DBEntry.COL_FAV_DATE, date);
        long id = db.insertWithOnConflict(DbSettings.DBEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        UrlSites fav = new UrlSites(id, url, date);
        mFavs.add(fav);
        return new UrlSites(fav);
    }

    public void removeFav(long id) {
        SQLiteDatabase db = mFavHelper.getWritableDatabase();
        db.delete(
                DbSettings.DBEntry.TABLE,
                DbSettings.DBEntry._ID + " = ?",
                new String[]{Long.toString(id)}
        );
        db.close();

        int index = -1;
        for (int i = 0; i < mFavs.size(); i++) {
            UrlSites favourites = mFavs.get(i);
            if (favourites.mId == id) {
                index = i;
            }
        }
        if (index != -1) {
            mFavs.remove(index);
        }
    }

}
