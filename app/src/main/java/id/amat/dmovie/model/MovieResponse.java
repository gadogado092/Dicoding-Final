package id.amat.dmovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieResponse {

    @SerializedName("results")
    private ArrayList<MovieItem> results;

    public ArrayList<MovieItem> getResults() {
        return results;
    }
}
