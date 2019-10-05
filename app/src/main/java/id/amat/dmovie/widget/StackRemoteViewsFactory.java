package id.amat.dmovie.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import id.amat.dmovie.R;

import java.util.ArrayList;
import java.util.List;

import id.amat.dmovie.room.AppDatabase;
import id.amat.dmovie.room.Movie;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

//    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private List<Movie> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        final long identityToken = Binder.clearCallingIdentity();

        AppDatabase appDatabase = AppDatabase.getInstance(mContext);
        mWidgetItems = appDatabase.movieDAO().getMovieFavorite();

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(final int position) {
        final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (mWidgetItems.size()==0){
            return rv;
        }

//        rv.setTextViewText(R.id.text2, mWidgetItems.get(position).getOriginalTitle());
        Glide.with(mContext)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w500"+mWidgetItems.get(position).getPosterPath())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        rv.setImageViewBitmap(R.id.imageView, resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });


        Bundle extras = new Bundle();
        extras.putInt(ImageBannerWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
