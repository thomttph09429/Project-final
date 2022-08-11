package com.app.projectfinal.db;


import static com.app.projectfinal.utils.Constant.TABLE_DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Cart.class}, version =1,exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {
    private static CartDatabase instance;
    public abstract CartDAO cartDAO();

    public static synchronized CartDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),CartDatabase.class, TABLE_DB).allowMainThreadQueries().build();
        }
        return instance;

    }
}
