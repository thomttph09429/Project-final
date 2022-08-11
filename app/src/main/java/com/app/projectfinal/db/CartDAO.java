package com.app.projectfinal.db;

import static com.app.projectfinal.utils.Constant.TABLE_NAME;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Cart... carts);

    @Update
    public void update(Cart... items);

    @Delete
    public void delete(Cart item);

    @Query("SELECT * FROM " + TABLE_NAME)
    public List<Cart> getCards();


    // check if primary key is exist or not
    @Query("SELECT EXISTS(SELECT * FROM CARD WHERE idProduct = :id)")
    boolean containsPrimaryKey(String id);


}
