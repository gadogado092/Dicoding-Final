package id.amat.dmovie.view;

import id.amat.dmovie.model.DataItem;

import java.util.ArrayList;

public interface DataView {

    void showLoading();

    void hideLoading();

    void onSuccess(ArrayList<DataItem> listData);

    void onFailed(String error);
}
