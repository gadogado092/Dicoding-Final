package id.amat.dmovie.activity;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import id.amat.dmovie.R;
import id.amat.dmovie.room.AppDatabase;
import id.amat.dmovie.room.Tv;

import java.util.List;

public class DetailFavoriteTvActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
    public  ProgressBar progressBarGenre;
    private  TextView tvGenre;
    private Boolean status = false;
    private Tv tvItem;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        appDatabase = AppDatabase.getInstance(DetailFavoriteTvActivity.this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView tvDateRelease = findViewById(R.id.txt_date_release);
        TextView tvOverview = findViewById(R.id.txt_overview);
        tvGenre = findViewById(R.id.txt_genre);
        ImageView imgPoster = findViewById(R.id.img_poster);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBarGenre = findViewById(R.id.progressBarGenre);

        tvItem = getIntent().getParcelableExtra(EXTRA_MOVIE);
        setTitle(tvItem.getOriginalTitle());
        tvDateRelease.setText(tvItem.getReleaseDate());
        tvOverview.setText(String.format("\t\t%s", tvItem.getOverview()));
        tvGenre.setText(tvItem.getGenre());

        progressBarGenre.setVisibility(View.GONE);


        Glide.with(DetailFavoriteTvActivity.this)
                .load("https://image.tmdb.org/t/p/w500"+tvItem.getPosterPath())
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

        List<Tv> listTv = appDatabase.tvDAO().getTvFavorite();
        int id=tvItem.getTvId();
        for (int i=0; i<listTv.size(); i++){
            if (id==listTv.get(i).getTvId()){
                status=true;
                break;
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
            Intent intent = new Intent(DetailFavoriteTvActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.favorite){
            if (status){
                status = false;
                Tv tv = new Tv();
                tv.setTvId(tvItem.getTvId());
                tv.setOriginalTitle(tvItem.getOriginalTitle());
                tv.setPosterPath(tvItem.getPosterPath());
                tv.setReleaseDate(tvItem.getReleaseDate());
                tv.setOverview(tvItem.getOverview());
                tv.setAdult(tvItem.getAdult());
                tv.setGenre(tvGenre.getText().toString());

                appDatabase.tvDAO().deleteTvFavorite(tv);
                Toast.makeText(DetailFavoriteTvActivity.this, getString(R.string.delete_from_favorite), Toast.LENGTH_SHORT).show();
            }else {
                status = true;
                Tv tv = new Tv();
                tv.setTvId(tvItem.getTvId());
                tv.setOriginalTitle(tvItem.getOriginalTitle());
                tv.setPosterPath(tvItem.getPosterPath());
                tv.setReleaseDate(tvItem.getReleaseDate());
                tv.setOverview(tvItem.getOverview());
                tv.setAdult(tvItem.getAdult());
                tv.setGenre(tvGenre.getText().toString());

                appDatabase.tvDAO().insertTvFavorite(tv);
                Toast.makeText(DetailFavoriteTvActivity.this, getString(R.string.add_to_favorite), Toast.LENGTH_SHORT).show();
            }
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

}
