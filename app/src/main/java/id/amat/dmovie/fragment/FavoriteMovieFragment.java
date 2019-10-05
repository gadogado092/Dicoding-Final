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
import id.amat.dmovie.activity.DetailFavoriteMovieActivity;
import id.amat.dmovie.adapter.MovieFavoriteAdapter;
import id.amat.dmovie.listener.ItemClickSupport;
import id.amat.dmovie.room.AppDatabase;
import id.amat.dmovie.room.Movie;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment {

    private RecyclerView rvMovie;
    private TextView txtEmpty;
    private List<Movie> movies;
    private MovieFavoriteAdapter movieFavoriteAdapter;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
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
        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        movies=appDatabase.movieDAO().getMovieFavorite();
        movieFavoriteAdapter.refill(movies);

        if (movies.size()<1){
            txtEmpty.setVisibility(View.VISIBLE);
        }else {
            txtEmpty.setVisibility(View.GONE);
        }
        super.onResume();
    }

    private void showRecyclerList(){
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        movieFavoriteAdapter = new MovieFavoriteAdapter(getContext(), movies);

        rvMovie.setAdapter(movieFavoriteAdapter);
        rvMovie.setHasFixedSize(true);

        ItemClickSupport.addTo(rvMovie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(movies.get(position));
            }
        });
    }

    private void showSelectedMovie(Movie movie){
        Intent detail = new Intent(getActivity(), DetailFavoriteMovieActivity.class);
        detail.putExtra(DetailFavoriteMovieActivity.EXTRA_MOVIE, (Parcelable) movie);
        startActivity(detail);
    }

}
