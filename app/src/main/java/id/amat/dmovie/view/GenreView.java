package id.amat.dmovie.view;

public interface GenreView {

    void showLoading();

    void hideLoading();

    void onSuccess(String genre);

    void onFailed(String error);
}
