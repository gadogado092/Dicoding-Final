package id.amat.dmovie.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.amat.dmovie.R;
import id.amat.dmovie.activity.DetailFavoriteTvActivity;
import id.amat.dmovie.adapter.TvFavoriteAdapter;
import id.amat.dmovie.listener.ItemClickSupport;
import id.amat.dmovie.room.AppDatabase;
import id.amat.dmovie.room.Tv;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvFragment extends Fragment {

    private RecyclerView rvMovie;
    private TextView txtEmpty;
    private List<Tv> tvs;
    private TvFavoriteAdapter tvFavoriteAdapter;
    public FavoriteTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovie= view.findViewById(R.id.rv_movie);
        txtEmpty = view.findViewById(R.id.textViewEmpty);
        rvMovie.setHasFixedSize(true);
        showRecyclerList();

    }

    @Override
    public void onResume() {
        super.onResume();
        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        tvs=appDatabase.tvDAO().getTvFavorite();
        tvFavoriteAdapter.refill(tvs);
        if (tvs.size()<1){
            txtEmpty.setVisibility(View.VISIBLE);
        }else {
            txtEmpty.setVisibility(View.GONE);
        }
    }

    private void showRecyclerList(){
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        tvFavoriteAdapter = new TvFavoriteAdapter(getContext(), tvs);

        rvMovie.setAdapter(tvFavoriteAdapter);
        rvMovie.setHasFixedSize(true);

        ItemClickSupport.addTo(rvMovie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(tvs.get(position));
            }
        });
    }

    private void showSelectedMovie(Tv tv){
        Intent detail = new Intent(getActivity(), DetailFavoriteTvActivity.class);
        detail.putExtra(DetailFavoriteTvActivity.EXTRA_MOVIE, (Parcelable) tv);
        startActivity(detail);
    }
}
