package com.app.projectfinal.db;

import static com.app.projectfinal.utils.Constant.TABLE_NAME;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CardDAO {
    @Insert
    public void insert(Card... cards);
    @Update
    public void update(Card... items);
    @Delete
    public void delete(Card item);
    @Query("SELECT * FROM "+ TABLE_NAME)
    public List<Card> getCards();



//    @Query("SELECT * FROM items WHERE id = :id")
//    public Card getItemById(String id);

//    https://viblo.asia/p/su-dung-room-database-trong-android-naQZRD0q5vx
}
