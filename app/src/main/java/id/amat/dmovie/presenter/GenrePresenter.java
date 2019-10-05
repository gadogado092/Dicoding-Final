package id.amat.dmovie.presenter;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import id.amat.dmovie.BuildConfig;
import id.amat.dmovie.R;
import id.amat.dmovie.util.HelperUrl;
import id.amat.dmovie.view.GenreView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GenrePresenter {

    private GenreView view;

    public GenrePresenter(GenreView view){
        this.view = view;
    }

    public void getGenre(final ArrayList<Integer> genre, String type){
        view.showLoading();
        AndroidNetworking.get(HelperUrl.URL_GENRE)
                .addPathParameter("type", type)
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .addQueryParameter("language", String.valueOf(R.string.lang))
                .setTag("callGenre")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject( new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArrayGenre = response.getJSONArray("genres");
                            StringBuilder name= new StringBuilder();
                            loop:
                            for (int i=0;i<genre.size(); i++){
                                String id = genre.get(i).toString();
                                for (int j=0;j<jsonArrayGenre.length(); j++){
                                    JSONObject data = jsonArrayGenre.getJSONObject(j);
                                    if (id.equals(data.getString("id"))){
                                        if (genre.size()==1){
                                            name.append(data.getString("name"));
                                        }else if (i==genre.size()-2){
                                            name.append(data.getString("name"));
                                        }else if (i==genre.size()-1){
                                            name.append(" And ").append(data.getString("name"));
                                        }else  {
                                            name.append(data.getString("name")).append(", ");
                                        }
                                        continue loop;
                                    }
                                }
                            }
                            view.hideLoading();
                            view.onSuccess( String.format("\t\t%s", name.toString()));
                        } catch (JSONException e) {
                            view.hideLoading();
                            e.printStackTrace();
                            view.onFailed("Server Response Error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        view.hideLoading();
                        Log.e("ERROR", "onError: ", anError);
                        view.onFailed("Server Response Error");
                    }
                });
    }

}
