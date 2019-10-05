package id.amat.dmovie.model;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;

public class DataItem implements Parcelable {

    private int dataId;

    private String dataTitle;

    private String dataPosterPath;

    private String dataReleaseDate;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public String getDataPosterPath() {
        return dataPosterPath;
    }

    public void setDataPosterPath(String dataPosterPath) {
        this.dataPosterPath = dataPosterPath;
    }

    public String getDataReleaseDate() {
        return dataReleaseDate;
    }

    public void setDataReleaseDate(String dataReleaseDate) {
        this.dataReleaseDate = dataReleaseDate;
    }

    public String getDataOverview() {
        return dataOverview;
    }

    public void setDataOverview(String dataOverview) {
        this.dataOverview = dataOverview;
    }

    public ArrayList<Integer> getDataGenreId() {
        return dataGenreId;
    }

    public void setDataGenreId(ArrayList<Integer> dataGenreId) {
        this.dataGenreId = dataGenreId;
    }

    public Boolean getDataAdult() {
        return dataAdult;
    }

    public void setDataAdult(Boolean dataAdult) {
        this.dataAdult = dataAdult;
    }

    private String dataOverview;

    private ArrayList<Integer> dataGenreId;

    private Boolean dataAdult;

    public DataItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dataId);
        dest.writeString(this.dataTitle);
        dest.writeString(this.dataPosterPath);
        dest.writeString(this.dataReleaseDate);
        dest.writeString(this.dataOverview);
        dest.writeList(this.dataGenreId);
        dest.writeValue(this.dataAdult);
    }

    protected DataItem(Parcel in) {
        this.dataId = in.readInt();
        this.dataTitle = in.readString();
        this.dataPosterPath = in.readString();
        this.dataReleaseDate = in.readString();
        this.dataOverview = in.readString();
        this.dataGenreId = new ArrayList<Integer>();
        in.readList(this.dataGenreId, Integer.class.getClassLoader());
        this.dataAdult = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel source) {
            return new DataItem(source);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };
}
