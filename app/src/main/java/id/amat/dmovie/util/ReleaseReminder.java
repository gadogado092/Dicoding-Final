package id.amat.dmovie.util;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import id.amat.dmovie.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.amat.dmovie.activity.DetailActivity;
import id.amat.dmovie.activity.MainActivity;
import id.amat.dmovie.model.DataItem;
import id.amat.dmovie.model.MovieItem;
import id.amat.dmovie.presenter.DataPresenter;
import id.amat.dmovie.room.Movie;
import id.amat.dmovie.view.DataView;

public class ReleaseReminder extends BroadcastReceiver implements DataView {

    public static String CHANNEL_ID = "channel_1";
    public static CharSequence CHANNEL_NAME = "RELEASE";
    private final int ID_REPEATING = 10;
    private Context context;
    private String TIME_FORMAT = "HH:mm";
    private NotificationCompat.Builder mBuilder;

    public ReleaseReminder(){

    }

    public ReleaseReminder(Context context){
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Date cal = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String dateToday = dateFormat.format(cal);

        DataPresenter dataPresenter = new DataPresenter(this);
        dataPresenter.getReleaseToday(HelperUrl.URL_RELEASE_TODAY, "movie", dateToday);

    }

    private void releaseReminderNotification(Context context, ArrayList<DataItem> movieList) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_home_black_24dp);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Intent intent;
        PendingIntent pendingIntent;

        int numMovies = 0;
        try {
            numMovies = ((movieList.size() > 0) ? movieList.size() : 0);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        String msg = "";

        if (numMovies == 0) {
            msg = context.getString(R.string.msg_no_release_today);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntent(intent)
                    .getPendingIntent(ID_REPEATING, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.ic_home_black_24dp)
                    .setLargeIcon(largeIcon)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(sound)
                    .setAutoCancel(true);
            if (mNotificationManager != null) {
                mNotificationManager.notify(0, mBuilder.build());
            }
        } else {
            intent = new Intent(context, DetailActivity.class);
            //| Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            for (int i = 0; i < numMovies; i++) {
                msg = movieList.get(i).getDataTitle() + " " + context.getString(R.string.msg_release_today);
                intent.putExtra(DetailActivity.EXTRA_DATA, movieList.get(i));
                intent.putExtra(DetailActivity.EXTRA_TYPE, "movie");
                //pendingIntent = PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                pendingIntent = TaskStackBuilder.create(context)
                        .addNextIntent(intent)
                        .getPendingIntent(i, PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentTitle(context.getString(R.string.app_name))
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.ic_home_black_24dp)
                        .setLargeIcon(largeIcon)
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSound(sound)
                        .setAutoCancel(true);
                if (mNotificationManager != null) {
                    mNotificationManager.notify(i, mBuilder.build());
                }
            }
        }
    }

    public void startReleaseReminder(String time){
        if (isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);

//        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//        }

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void stopReleaseReminder(){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onSuccess(ArrayList<DataItem> listData) {
        releaseReminderNotification(context, listData);
    }

    @Override
    public void onFailed(String error) {

    }

    @Override
    public void onSuccessRefresh(ArrayList<DataItem> listData) {

    }

    @Override
    public void hideLoadingRefresh() {

    }
}
