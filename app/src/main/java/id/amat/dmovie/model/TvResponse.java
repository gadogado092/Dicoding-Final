package id.amat.dmovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TvResponse {

    @SerializedName("results")
    private ArrayList<TvItem> results;

    public ArrayList<TvItem> getResults() {
        return results;
    }
}
