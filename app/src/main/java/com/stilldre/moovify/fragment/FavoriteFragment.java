package com.stilldre.moovify.fragment;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stilldre.moovify.R;
import com.stilldre.moovify.adapter.FavoriteAdapter;
import com.stilldre.moovify.database.FavoriteHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.stilldre.moovify.database.DatabaseContract.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    @BindView(R.id.recyclerView_favorite)
    RecyclerView rvMovie;
    @BindView(R.id.fav_swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    private FavoriteHelper favoriteHelper;
    private FavoriteAdapter favoriteAdapter;

    private Context context;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMovies();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadMovies();
            swipeRefreshLayout.setRefreshing(false);
        });
        favoriteHelper = new FavoriteHelper(context);
        favoriteAdapter = new FavoriteAdapter(getActivity());
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void loadMovies() {
        swipeRefreshLayout.setRefreshing(true);
        new LoadFavoriteAsync().execute();
    }

    private class LoadFavoriteAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            swipeRefreshLayout.setRefreshing(false);

            Cursor movie = cursor;
            favoriteAdapter.setFavoriteMovies(cursor);
            favoriteAdapter.notifyDataSetChanged();
            rvMovie.setAdapter(favoriteAdapter);

            int count = 0;
            try {
                count = ((movie.getCount() > 0) ? movie.getCount() : 0);
            } catch (Exception e) {
                Log.w("ERROR", e.getMessage());
            }
            if (count == 0) {
                showToast(getString(R.string.no_fav_found));
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }
}
