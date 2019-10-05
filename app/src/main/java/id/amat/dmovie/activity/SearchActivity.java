package id.amat.dmovie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.util.ArrayList;

import id.amat.dmovie.R;
import id.amat.dmovie.adapter.DataAdapter;
import id.amat.dmovie.listener.ItemClickSupport;
import id.amat.dmovie.model.DataItem;
import id.amat.dmovie.presenter.DataPresenter;
import id.amat.dmovie.util.HelperUrl;
import id.amat.dmovie.view.DataView;

public class SearchActivity extends AppCompatActivity implements DataView {

    private RecyclerView rvData;
    private ProgressBar progressBar;
    private ArrayList<DataItem> dataList = new ArrayList<>();
    private DataAdapter dataAdapter;
    private String dataType;
    public static final String EXTRA_TYPE = "extra_type";
    private DataPresenter dataPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dataType = getIntent().getStringExtra(EXTRA_TYPE);

        rvData = findViewById(R.id.rv_movie);
        progressBar = findViewById(R.id.progress_bar);
        rvData.setHasFixedSize(true);
        showRecyclerList();

        dataPresenter = new DataPresenter(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchItem.expandActionView();

            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    return false;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    finish();
                    return false;
                }
            });
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
            if (dataType.equals("movie")){
                searchView.setQueryHint(getResources().getString(R.string.search_movie));
            }else if (dataType.equals("tv")){
                searchView.setQueryHint(getResources().getString(R.string.search_tv));
            }

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (!query.isEmpty()){
                        dataPresenter.getDataSearch(HelperUrl.URL_SEARCH, dataType, query);
                    }else {
                        dataList.clear();
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }


        return true;
    }

    private void showRecyclerList(){
        rvData.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        dataAdapter = new DataAdapter(SearchActivity.this, dataList);

        rvData.setAdapter(dataAdapter);
        rvData.setHasFixedSize(true);
        ItemClickSupport.addTo(rvData).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(dataList.get(position));
            }
        });
    }

    private void showSelectedMovie(DataItem movie){
        Intent detail = new Intent(SearchActivity.this, DetailActivity.class);
        detail.putExtra(DetailActivity.EXTRA_DATA, movie);
        detail.putExtra(DetailActivity.EXTRA_TYPE, dataType);
        startActivity(detail);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        String STATE_LIST = "state_list";
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
    public void onSuccess(ArrayList<DataItem> listData) {
        if (!listData.isEmpty()){
            dataList.clear();
            dataList.addAll(listData);
            dataAdapter.refill(dataList);
        }
    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(SearchActivity.this , error, Toast.LENGTH_SHORT).show();
    }
}
