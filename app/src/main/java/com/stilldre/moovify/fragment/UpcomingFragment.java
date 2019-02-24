package com.stilldre.moovify.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stilldre.moovify.R;
import com.stilldre.moovify.activity.DetailActivity;
import com.stilldre.moovify.adapter.MovieAdapter;
import com.stilldre.moovify.model.Movie;
import com.stilldre.moovify.repository.MovieRepository;
import com.stilldre.moovify.service.network.callback.OnGetMoviesCallback;
import com.stilldre.moovify.service.click.OnMovieClickCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {

    @BindView(R.id.recyclerView_upcoming)
    RecyclerView rvMovie;
    @BindView(R.id.upcoming_swiper)
    SwipeRefreshLayout swiper;

    private MovieAdapter movieAdapter;
    private MovieRepository movieRepository;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        getMovies();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        ButterKnife.bind(this, view);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));

        swiper.setRefreshing(true);

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMovies();
            }
        });

        movieRepository = MovieRepository.getInstance();
        return view;
    }

    private void getMovies() {
        movieRepository.getUpcomingMovie(new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                movieAdapter = new MovieAdapter(movies, callback);
                rvMovie.setAdapter(movieAdapter);
                swiper.setRefreshing(false);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void showError(){
        Toast.makeText(getActivity(),R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
    }

    OnMovieClickCallback callback = new OnMovieClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("movie", movie);
            startActivity(intent);
        }
    };
}