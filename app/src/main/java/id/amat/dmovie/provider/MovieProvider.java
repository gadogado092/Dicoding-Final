package id.amat.dmovie.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import id.amat.dmovie.room.AppDatabase;
import id.amat.dmovie.room.MovieDAO;

public class MovieProvider extends ContentProvider {

    /** The authority of this content provider. */
    public static final String AUTHORITY = "id.amat.dmovie.provider.MovieProvider";

    /** The URI for the Movie table. */
    public static final Uri URI_MOVIE = Uri.parse(
            "content://"+ AUTHORITY + "/"+ AppDatabase.TABEL_MOVIE);

    private static final int CODE_MOVIE_LIST=1;
    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, AppDatabase.TABEL_MOVIE, CODE_MOVIE_LIST);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final int code = MATCHER.match(uri);
        if (code == CODE_MOVIE_LIST){
            final Context context = getContext();
            if (context == null){
                return null;
            }
            MovieDAO movieDAO = AppDatabase.getInstance(context).movieDAO();
            Cursor cursor = null;
            if (code == CODE_MOVIE_LIST){
                cursor = movieDAO.selectAll();
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        }else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
