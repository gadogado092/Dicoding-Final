package id.amat.dmovie.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TvDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTvFavorite(Tv tv);

    @Delete
    void deleteTvFavorite(Tv Tv);

    @Query("SELECT * FROM tbtv ")
    List<Tv> getTvFavorite();


}
