package id.amat.dmovie.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import id.amat.dmovie.activity.DetailActivity;
import id.amat.dmovie.R;
import id.amat.dmovie.adapter.DataAdapter;
import id.amat.dmovie.model.DataItem;
import id.amat.dmovie.listener.ItemClickSupport;
import id.amat.dmovie.presenter.DataPresenter;
import id.amat.dmovie.util.HelperUrl;
import id.amat.dmovie.view.DataView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements DataView {

    private RecyclerView rvMovie;
    private ProgressBar progressBar;
    private ArrayList<DataItem> dataList = new ArrayList<>();
    private DataAdapter dataAdapter;
    private String type;
    private final String STATE_LIST = "state_list";

    public MovieFragment() {
        this.type = "movie";
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovie= view.findViewById(R.id.rv_movie);
        progressBar= view.findViewById(R.id.progress_bar);
        rvMovie.setHasFixedSize(true);

        showRecyclerList();

        if (savedInstanceState == null){
            DataPresenter dataPresenter = new DataPresenter(this);
            if (type.equals("movie")){
                dataPresenter.getDataList(HelperUrl.URL_MOVIE, type);
            }else if (type.equals("tv")){
                dataPresenter.getDataList(HelperUrl.URL_TV, type);
            }

        }else {
            ArrayList<DataItem> stateList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            if (stateList != null) {
                dataList.addAll(stateList);
            }
        }
    }
    private void showSelectedMovie(DataItem movie){
        Intent detail = new Intent(getActivity(), DetailActivity.class);
        detail.putExtra(DetailActivity.EXTRA_DATA, movie);
        detail.putExtra(DetailActivity.EXTRA_TYPE, type);
        startActivity(detail);
    }

    private void showRecyclerList(){
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        dataAdapter = new DataAdapter(getContext(), dataList);

        rvMovie.setAdapter(dataAdapter);
        rvMovie.setHasFixedSize(true);
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(dataList.get(position));
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(STATE_LIST, dataList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(ArrayList<DataItem> listMovie) {
        if (!listMovie.isEmpty()){
            dataList.clear();
            dataList.addAll(listMovie);
            dataAdapter.refill(dataList);
        }

    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(getContext() , error, Toast.LENGTH_SHORT).show();
    }
}
