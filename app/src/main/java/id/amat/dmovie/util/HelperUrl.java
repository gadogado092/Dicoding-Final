package id.amat.dmovie.util;


import id.amat.dmovie.BuildConfig;

public class HelperUrl {

    public static final String URL_MOVIE = BuildConfig.BASE_URL + "discover/movie/";
    public static final String URL_TV = BuildConfig.BASE_URL + "discover/tv/";
    public static final String URL_GENRE = BuildConfig.BASE_URL+"genre/{type}/list";
    public static final String URL_SEARCH = BuildConfig.BASE_URL+"search/{type}/";
    public static final String URL_RELEASE_TODAY = BuildConfig.BASE_URL+"discover/{type}/";
}
