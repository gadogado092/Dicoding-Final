package id.amat.dmovie.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieFavorite(Movie movie);

    @Delete
    void deleteMovieFavorite(Movie movie);

    @Query("SELECT * FROM tbmovie ")
    List<Movie> getMovieFavorite();

    @Query("SELECT * FROM tbmovie ")
    Cursor selectAll();


}
