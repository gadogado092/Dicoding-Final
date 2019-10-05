package id.amat.dmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TvItem implements Parcelable {

    @SerializedName("id")
    private int tvId;

    public int getTvId() {
        return tvId;
    }

    public void setTvId(int tvId) {
        this.tvId = tvId;
    }

    @SerializedName("original_name")
    private String originalName;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("overview")
    private String overview;

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ArrayList<Integer> getGenreId() {
        return genreId;
    }

    public void setGenreId(ArrayList<Integer> genreId) {
        this.genreId = genreId;
    }

    @SerializedName("genre_ids")
    private ArrayList<Integer> genreId;

    public TvItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tvId);
        dest.writeString(this.originalName);
        dest.writeString(this.posterPath);
        dest.writeString(this.firstAirDate);
        dest.writeString(this.overview);
        dest.writeList(this.genreId);
    }

    protected TvItem(Parcel in) {
        this.tvId = in.readInt();
        this.originalName = in.readString();
        this.posterPath = in.readString();
        this.firstAirDate = in.readString();
        this.overview = in.readString();
        this.genreId = new ArrayList<Integer>();
        in.readList(this.genreId, Integer.class.getClassLoader());
    }

    public static final Creator<TvItem> CREATOR = new Creator<TvItem>() {
        @Override
        public TvItem createFromParcel(Parcel source) {
            return new TvItem(source);
        }

        @Override
        public TvItem[] newArray(int size) {
            return new TvItem[size];
        }
    };
}
