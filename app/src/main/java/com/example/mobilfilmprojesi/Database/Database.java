package com.example.mobilfilmprojesi.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME="FilmProjesi.db";
    private static  final int DB_VER=1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);

    }


    public void addToFavorites(String filmId,String userName)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query =String.format("INSERT INTO Favorites(FilmId) VALUES('%s');",filmId);
        db.execSQL(query);
    }


    public void removeFromFavorites(String filmId,String userName)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query =String.format("DELETE FROM Favorites WHERE filmId='%s';",filmId);
        db.execSQL(query);
    }


    public boolean isFavorite(String filmId,String userName)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query =String.format("SELECT * FROM Favorites WHERE filmId='%s' ;",filmId);
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.getCount()<=0)
        {
            cursor.close();
            return false;

        }

        cursor.close();
        return true;
    }












}
