package id.amat.dmovie.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import id.amat.dmovie.R;
import id.amat.dmovie.base.BaseAppCompatActivity;
import id.amat.dmovie.fragment.FavoriteFragment;
import id.amat.dmovie.fragment.MovieFragment;

import id.amat.dmovie.fragment.TvFragment;
//import id.amat.dmovie.util.AlarmReceiver;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends BaseAppCompatActivity {

    private Fragment pageContent = new MovieFragment();
    private String title = "";
    private String type = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    type = "movie";
                    title="D "+getResources().getString(R.string.title_movie);
                    setTitle(title);
                    pageContent = new MovieFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, pageContent, pageContent.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_tv_show:
                    type = "tv";
                    title="D "+getResources().getString(R.string.title_tv_show);
                    setTitle(title);
                    pageContent = new TvFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, pageContent, pageContent.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_favorite:
                    title="D "+getResources().getString(R.string.title_favorite);
                    setTitle(title);
                    pageContent = new FavoriteFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, pageContent, pageContent.getClass().getSimpleName())
                            .commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null){
            navView.setSelectedItemId(R.id.navigation_movie);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, pageContent, pageContent.getClass().getSimpleName());
            setTitle(title);
        }else {
            pageContent = getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, pageContent, pageContent.getClass().getSimpleName());
            title = savedInstanceState.getString(KEY_TITLE);
            type = savedInstanceState.getString(KEY_TYPE);
            setTitle(title);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search){
            Intent search = new Intent(MainActivity.this, SearchActivity.class);
            search.putExtra(SearchActivity.EXTRA_TYPE, type);
            startActivity(search);

        }
        else if (item.getItemId() == R.id.language) {
            String Language = getResources().getString(R.string.language);
            if (Language.equals("Bahasa")) {
                setLocale("In");
            } else {
                setLocale("eng");
            }

            return true;
        }
        else if (item.getItemId() == R.id.setting){
            Intent detail = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(detail);
            return true;
        }else if (item.getItemId() == R.id.theme){
            Toast.makeText(getApplicationContext(), "THEME CLICK", Toast.LENGTH_SHORT).show();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            recreate();
        }
        return true;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, KEY_FRAGMENT, pageContent );
        outState.putString(KEY_TITLE, title);
        outState.putString(KEY_TYPE, type);
        super.onSaveInstanceState(outState);
    }

    public void setLocale(String lang) {
        Locale myLocale;
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        recreate();
    }

}
