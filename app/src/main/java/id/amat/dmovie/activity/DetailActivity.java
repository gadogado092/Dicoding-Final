package id.amat.dmovie.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


import id.amat.dmovie.R;
import id.amat.dmovie.widget.ImageBannerWidget;
import id.amat.dmovie.model.DataItem;
import id.amat.dmovie.presenter.GenrePresenter;
import id.amat.dmovie.room.AppDatabase;
import id.amat.dmovie.room.Movie;
import id.amat.dmovie.room.Tv;
import id.amat.dmovie.view.GenreView;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements GenreView {
    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_TYPE = "extra_type";
    private static final String STATE_GENRE = "state_genre";
    public  ProgressBar progressBarGenre;
    private  TextView tvGenre;
    private Boolean status = false;
    private DataItem dataItem;
    private String dataType;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        appDatabase = AppDatabase.getInstance(DetailActivity.this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView tvDateRelease = findViewById(R.id.txt_date_release);
        TextView tvOverview = findViewById(R.id.txt_overview);
        tvGenre = findViewById(R.id.txt_genre);
        ImageView imgPoster = findViewById(R.id.img_poster);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBarGenre = findViewById(R.id.progressBarGenre);

        dataItem = getIntent().getParcelableExtra(EXTRA_DATA);
        dataType = getIntent().getStringExtra(EXTRA_TYPE);

        setTitle(dataItem.getDataTitle());
        tvDateRelease.setText(dataItem.getDataReleaseDate());
        tvOverview.setText(String.format("\t\t%s", dataItem.getDataOverview()));

        if (savedInstanceState != null) {
            progressBarGenre.setVisibility(View.GONE);
            String result = savedInstanceState.getString(STATE_GENRE);
            tvGenre.setText(result);
        }else {
            GenrePresenter genrePresenter = new GenrePresenter(this);
            genrePresenter.getGenre(dataItem.getDataGenreId(), "movie");
        }


        Glide.with(DetailActivity.this)
                .load("https://image.tmdb.org/t/p/w500"+dataItem.getDataPosterPath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(imgPoster);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (dataType.equals("movie")){
            List<Movie> listMovie = appDatabase.movieDAO().getMovieFavorite();
            int id=dataItem.getDataId();
            for (int i=0; i<listMovie.size(); i++){
                if (id==listMovie.get(i).getMovieId()){
                    status=true;
                    break;
                }
            }
        }else if (dataType.equals("tv")){
            List<Tv> listTv = appDatabase.tvDAO().getTvFavorite();
            int id=dataItem.getDataId();
            for (int i=0; i<listTv.size(); i++){
                if (id==listTv.get(i).getTvId()){
                    status=true;
                    break;
                }
            }
        }


        if (status) {
            //in production you'd probably be better off keeping a reference to the item
            menu.findItem(R.id.favorite)
                    .setIcon(R.drawable.ic_favorite_white_24dp);
        } else {
            menu.findItem(R.id.favorite)
                    .setIcon(R.drawable.ic_favorite_border_white_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
           // Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.favorite){
            if (status){
                status = false;
                if (dataType.equals("movie")){
                    Movie movie = new Movie();
                    movie.setMovieId(dataItem.getDataId());
                    movie.setOriginalTitle(dataItem.getDataTitle());
                    movie.setPosterPath(dataItem.getDataPosterPath());
                    movie.setReleaseDate(dataItem.getDataReleaseDate());
                    movie.setOverview(dataItem.getDataOverview());
                    movie.setAdult(dataItem.getDataAdult());
                    movie.setGenre(tvGenre.getText().toString());
                    appDatabase.movieDAO().deleteMovieFavorite(movie);
                    updateWidget();
                }else if (dataType.equals("tv")){
                    Tv tv = new Tv();
                    tv.setTvId(dataItem.getDataId());
                    tv.setOriginalTitle(dataItem.getDataTitle());
                    tv.setPosterPath(dataItem.getDataPosterPath());
                    tv.setReleaseDate(dataItem.getDataReleaseDate());
                    tv.setOverview(dataItem.getDataOverview());
                    tv.setAdult(dataItem.getDataAdult());
                    tv.setGenre(tvGenre.getText().toString());
                    appDatabase.tvDAO().deleteTvFavorite(tv);
                }

                Toast.makeText(DetailActivity.this, getString(R.string.delete_from_favorite), Toast.LENGTH_SHORT).show();
            }else {
                status = true;
                if (dataType.equals("movie")) {
                    Movie movie = new Movie();
                    movie.setMovieId(dataItem.getDataId());
                    movie.setOriginalTitle(dataItem.getDataTitle());
                    movie.setPosterPath(dataItem.getDataPosterPath());
                    movie.setReleaseDate(dataItem.getDataReleaseDate());
                    movie.setOverview(dataItem.getDataOverview());
                    movie.setAdult(dataItem.getDataAdult());
                    movie.setGenre(tvGenre.getText().toString());
                    appDatabase.movieDAO().insertMovieFavorite(movie);

                   updateWidget();
                }else if (dataType.equals("tv")){
                    Tv tv = new Tv();
                    tv.setTvId(dataItem.getDataId());
                    tv.setOriginalTitle(dataItem.getDataTitle());
                    tv.setPosterPath(dataItem.getDataPosterPath());
                    tv.setReleaseDate(dataItem.getDataReleaseDate());
                    tv.setOverview(dataItem.getDataOverview());
                    tv.setAdult(dataItem.getDataAdult());
                    tv.setGenre(tvGenre.getText().toString());
                    appDatabase.tvDAO().insertTvFavorite(tv);
                }

                Toast.makeText(DetailActivity.this, getString(R.string.add_to_favorite), Toast.LENGTH_SHORT).show();
            }
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_GENRE, tvGenre.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showLoading() {
        progressBarGenre.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBarGenre.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String genre) {
        tvGenre.setText(genre);
    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(DetailActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    private void updateWidget(){
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, ImageBannerWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
    }
}
