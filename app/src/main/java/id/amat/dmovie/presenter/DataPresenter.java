package id.amat.dmovie.presenter;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;


import id.amat.dmovie.BuildConfig;
import id.amat.dmovie.R;
import id.amat.dmovie.model.DataItem;
import id.amat.dmovie.room.Movie;
import id.amat.dmovie.view.DataView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataPresenter {

    private DataView view;

    public DataPresenter(DataView view){
        this.view = view;
    }

    public void getDataList(String endPoint, final String type){
        view.showLoading();
        AndroidNetworking.get(endPoint)
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .addQueryParameter("language", String.valueOf(R.string.lang))
                .setTag(Movie.class)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<DataItem> dataItems = new ArrayList<>();
                            JSONArray results = response.getJSONArray("results");
                            for (int i=0; i<results.length(); i++){
                                JSONObject data = results.getJSONObject(i);
                                DataItem dataItem= new DataItem();
                                dataItem.setDataId(data.getInt("id"));
                                if (type.equals("movie")){
                                    dataItem.setDataTitle(data.getString("original_title"));
                                    dataItem.setDataReleaseDate(data.getString("release_date"));
                                    dataItem.setDataAdult(data.getBoolean("adult"));
                                }else if (type.equals("tv")){
                                    dataItem.setDataTitle(data.getString("original_name"));
                                    dataItem.setDataReleaseDate(data.getString("first_air_date"));
                                    dataItem.setDataAdult(false);
                                }

                                ArrayList<Integer> genre = new ArrayList<>();
                                JSONArray resultsGenre = data.getJSONArray("genre_ids");
                                for (int j=0; j<resultsGenre.length(); j++){
                                    genre.add(resultsGenre.getInt(j));
                                }
                                dataItem.setDataGenreId(genre);

                                dataItem.setDataOverview(data.getString("overview"));
                                dataItem.setDataPosterPath(data.getString("poster_path"));
                                dataItems.add(dataItem);
                            }
                            view.hideLoading();
                            view.onSuccess(dataItems);
                        } catch (JSONException e) {
                            e.printStackTrace();
							view.hideLoading();
							view.onFailed("Server Response Error");
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        view.hideLoading();
                        Log.d("ERROR", "onError: ", anError);
                        view.onFailed("Server Response Error");
                    }
                });
    }

    public void getDataSearch (String endPoint, final String type, String query){
        view.showLoading();
        AndroidNetworking.get(endPoint)
                .addPathParameter("type", type)
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .addQueryParameter("language", String.valueOf(R.string.lang))
                .addQueryParameter("query", query)
                .setTag(Movie.class)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<DataItem> dataItems = new ArrayList<>();
                            JSONArray results = response.getJSONArray("results");
                            for (int i=0; i<results.length(); i++){
                                JSONObject data = results.getJSONObject(i);
                                DataItem dataItem= new DataItem();
                                dataItem.setDataId(data.getInt("id"));
                                if (type.equals("movie")){
                                    dataItem.setDataTitle(data.getString("original_title"));
                                    dataItem.setDataReleaseDate(data.getString("release_date"));
                                    dataItem.setDataAdult(data.getBoolean("adult"));
                                }else if (type.equals("tv")){
                                    dataItem.setDataTitle(data.getString("original_name"));
                                    dataItem.setDataReleaseDate(data.getString("first_air_date"));
                                    dataItem.setDataAdult(false);
                                }

                                ArrayList<Integer> genre = new ArrayList<>();
                                JSONArray resultsGenre = data.getJSONArray("genre_ids");
                                for (int j=0; j<resultsGenre.length(); j++){
                                    genre.add(resultsGenre.getInt(j));
                                }
                                dataItem.setDataGenreId(genre);

                                dataItem.setDataOverview(data.getString("overview"));
                                dataItem.setDataPosterPath(data.getString("poster_path"));
                                dataItems.add(dataItem);
                            }
                            view.hideLoading();
                            view.onSuccess(dataItems);
                        } catch (JSONException e) {
                            e.printStackTrace();
							view.hideLoading();
							view.onFailed("Server Response Error");
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        view.hideLoading();
                        Log.d("ERROR", "onError: ", anError);
                        view.onFailed("Server Response Error");
                    }
                });
    }

    public void getReleaseToday (String endPoint, final String type, String date){
        view.showLoading();
        AndroidNetworking.get(endPoint)
                .addPathParameter("type", type)
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .addQueryParameter("language", String.valueOf(R.string.lang))
                .addQueryParameter("primary_release_date.gte", date)
                .addQueryParameter("primary_release_date.lte", date)
                .setTag(Movie.class)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<DataItem> dataItems = new ArrayList<>();
                            JSONArray results = response.getJSONArray("results");
                            for (int i=0; i<results.length(); i++){
                                JSONObject data = results.getJSONObject(i);
                                DataItem dataItem= new DataItem();
                                dataItem.setDataId(data.getInt("id"));
                                if (type.equals("movie")){
                                    dataItem.setDataTitle(data.getString("original_title"));
                                    dataItem.setDataReleaseDate(data.getString("release_date"));
                                    dataItem.setDataAdult(data.getBoolean("adult"));
                                }else if (type.equals("tv")){
                                    dataItem.setDataTitle(data.getString("original_name"));
                                    dataItem.setDataReleaseDate(data.getString("first_air_date"));
                                    dataItem.setDataAdult(false);
                                }

                                ArrayList<Integer> genre = new ArrayList<>();
                                JSONArray resultsGenre = data.getJSONArray("genre_ids");
                                for (int j=0; j<resultsGenre.length(); j++){
                                    genre.add(resultsGenre.getInt(j));
                                }
                                dataItem.setDataGenreId(genre);

                                dataItem.setDataOverview(data.getString("overview"));
                                dataItem.setDataPosterPath(data.getString("poster_path"));
                                dataItems.add(dataItem);
                            }
                            view.hideLoading();
                            view.onSuccess(dataItems);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            view.hideLoading();
                            view.onFailed("Server Response Error");
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        view.hideLoading();
                        Log.d("ERROR", "onError: ", anError);
                        view.onFailed("Server Response Error");
                    }
                });
    }

}
