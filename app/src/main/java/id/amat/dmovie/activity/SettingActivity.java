package id.amat.dmovie.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import id.amat.dmovie.R;

import id.amat.dmovie.util.DailyReminder;
import id.amat.dmovie.util.MyPreference;
import id.amat.dmovie.util.ReleaseReminder;

public class SettingActivity extends AppCompatActivity {
    private MyPreference myPreference;
    private DailyReminder dailyReminder;
    private ReleaseReminder releaseReminder;
    private Switch switchReleaseReminder, switchDailyReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        myPreference = new MyPreference(SettingActivity.this);
        dailyReminder = new DailyReminder(SettingActivity.this);
        releaseReminder = new ReleaseReminder(SettingActivity.this);
        switchReleaseReminder = findViewById(R.id.switchReleaseReminder);
        switchDailyReminder = findViewById(R.id.switchDailyReminder);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        switchReleaseReminder.setChecked(myPreference.getReleaseReminder());
        switchReleaseReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                myPreference.setReleaseReminder(b);
                if (b){
                    releaseReminder.startReleaseReminder(getString(R.string.time_release_reminder));
                }else {
                    releaseReminder.stopReleaseReminder();
                }

            }
        });

        switchDailyReminder.setChecked(myPreference.getDailyReminder());
        switchDailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                myPreference.setDailyReminder(b);
                if (b){
                    dailyReminder.startDailyReminder(getString(R.string.time_daily_reminder));
                }else {
                    dailyReminder.stopDailyReminder();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
